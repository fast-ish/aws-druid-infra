package main

import (
	"context"
	"fmt"
	"net/http"
	"os"
	"strings"
	"time"

	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/apis/meta/v1/unstructured"
	"k8s.io/apimachinery/pkg/runtime/schema"
	"k8s.io/client-go/dynamic"
	"k8s.io/client-go/kubernetes"
	"k8s.io/client-go/tools/clientcmd"
)

const (
	colorReset  = "\033[0m"
	colorRed    = "\033[31m"
	colorGreen  = "\033[32m"
	colorYellow = "\033[33m"
	colorBlue   = "\033[34m"
	colorCyan   = "\033[36m"
)

type TestResult struct {
	Name    string
	Passed  bool
	Warning bool
	Message string
}

type TestSuite struct {
	clientset     *kubernetes.Clientset
	dynamicClient dynamic.Interface
	results       []TestResult
	passed        int
	failed        int
	warnings      int
}

func main() {
	printBanner()

	suite, err := NewTestSuite()
	if err != nil {
		fmt.Printf("%s✗ Failed to initialize: %v%s\n", colorRed, err, colorReset)
		os.Exit(1)
	}

	suite.RunAll()
	suite.PrintSummary()

	if suite.failed > 0 {
		os.Exit(1)
	}
}

func printBanner() {
	fmt.Printf("%s", colorCyan)
	fmt.Println("╔═══════════════════════════════════════════════════════════════╗")
	fmt.Println("║         DRUID SMOKE TEST - Infrastructure Validation          ║")
	fmt.Println("╚═══════════════════════════════════════════════════════════════╝")
	fmt.Printf("%s\n", colorReset)
}

func NewTestSuite() (*TestSuite, error) {
	loadingRules := clientcmd.NewDefaultClientConfigLoadingRules()
	configOverrides := &clientcmd.ConfigOverrides{}
	kubeConfig := clientcmd.NewNonInteractiveDeferredLoadingClientConfig(loadingRules, configOverrides)

	config, err := kubeConfig.ClientConfig()
	if err != nil {
		return nil, fmt.Errorf("failed to load kubeconfig: %w", err)
	}

	clientset, err := kubernetes.NewForConfig(config)
	if err != nil {
		return nil, fmt.Errorf("failed to create clientset: %w", err)
	}

	dynamicClient, err := dynamic.NewForConfig(config)
	if err != nil {
		return nil, fmt.Errorf("failed to create dynamic client: %w", err)
	}

	return &TestSuite{
		clientset:     clientset,
		dynamicClient: dynamicClient,
	}, nil
}

func (s *TestSuite) RunAll() {
	s.testEKSCluster()
	s.testCoreAddons()
	s.testSecurity()
	s.testNetworking()
	s.testDruid()
	s.testObservability()
}

func (s *TestSuite) printHeader(title string) {
	fmt.Printf("\n%s━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━%s\n", colorBlue, colorReset)
	fmt.Printf("%s  %s%s\n", colorBlue, title, colorReset)
	fmt.Printf("%s━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━%s\n", colorBlue, colorReset)
}

func (s *TestSuite) printSection(title string) {
	fmt.Printf("\n%s▶ %s%s\n", colorYellow, title, colorReset)
}

func (s *TestSuite) pass(name string) {
	fmt.Printf("  %s✓%s %s\n", colorGreen, colorReset, name)
	s.passed++
	s.results = append(s.results, TestResult{Name: name, Passed: true})
}

func (s *TestSuite) fail(name string) {
	fmt.Printf("  %s✗%s %s\n", colorRed, colorReset, name)
	s.failed++
	s.results = append(s.results, TestResult{Name: name, Passed: false})
}

func (s *TestSuite) warn(name string) {
	fmt.Printf("  %s⚠%s %s\n", colorYellow, colorReset, name)
	s.warnings++
	s.results = append(s.results, TestResult{Name: name, Warning: true})
}

// -----------------------------------------------------------------------------
// EKS Cluster Tests
// -----------------------------------------------------------------------------

func (s *TestSuite) testEKSCluster() {
	s.printHeader("EKS CLUSTER HEALTH")
	ctx := context.Background()

	s.printSection("Cluster Connectivity")
	version, err := s.clientset.Discovery().ServerVersion()
	if err != nil {
		s.fail("Cluster connectivity")
	} else {
		s.pass(fmt.Sprintf("Cluster connectivity (Kubernetes %s)", version.GitVersion))
	}

	s.printSection("Node Health")
	nodes, err := s.clientset.CoreV1().Nodes().List(ctx, metav1.ListOptions{})
	if err != nil {
		s.fail("List nodes")
	} else {
		ready := 0
		notReady := 0
		for _, node := range nodes.Items {
			for _, cond := range node.Status.Conditions {
				if cond.Type == "Ready" {
					if cond.Status == "True" {
						ready++
					} else {
						notReady++
					}
				}
			}
		}
		s.pass(fmt.Sprintf("Nodes ready: %d", ready))
		if notReady > 0 {
			s.warn(fmt.Sprintf("Nodes not ready: %d", notReady))
		}
	}

	s.printSection("System Pods")
	s.checkPodsRunning(ctx, "kube-system", "k8s-app=kube-dns", "CoreDNS")
	s.checkPodsRunning(ctx, "kube-system", "k8s-app=kube-proxy", "kube-proxy")
	s.checkPodsRunning(ctx, "kube-system", "k8s-app=aws-node", "AWS VPC CNI")

	s.printSection("Karpenter")
	s.checkPodsRunning(ctx, "kube-system", "app.kubernetes.io/name=karpenter", "Karpenter controller")
	s.checkCRDExists(ctx, "nodepools.karpenter.sh", "NodePool CRD")
	s.checkCRDExists(ctx, "ec2nodeclasses.karpenter.k8s.aws", "EC2NodeClass CRD")

	// Check NodePools
	nodePoolGVR := schema.GroupVersionResource{Group: "karpenter.sh", Version: "v1", Resource: "nodepools"}
	nodePools, err := s.dynamicClient.Resource(nodePoolGVR).List(ctx, metav1.ListOptions{})
	if err == nil && len(nodePools.Items) > 0 {
		s.pass(fmt.Sprintf("NodePools configured: %d", len(nodePools.Items)))
	} else {
		s.warn("No NodePools configured")
	}
}

// -----------------------------------------------------------------------------
// Core Addons Tests
// -----------------------------------------------------------------------------

func (s *TestSuite) testCoreAddons() {
	s.printHeader("CORE ADDONS")
	ctx := context.Background()

	s.printSection("CRDs Installed")
	crds := []struct{ name, display string }{
		{"externalsecrets.external-secrets.io", "External Secrets"},
		{"clustersecretstores.external-secrets.io", "ClusterSecretStore"},
		{"certificates.cert-manager.io", "Cert Manager Certificates"},
		{"clusterissuers.cert-manager.io", "Cert Manager ClusterIssuers"},
		{"clusterpolicies.kyverno.io", "Kyverno ClusterPolicies"},
		{"nodepools.karpenter.sh", "Karpenter NodePools"},
	}
	for _, crd := range crds {
		s.checkCRDExists(ctx, crd.name, crd.display)
	}

	s.printSection("Addon Deployments")
	addons := []struct{ ns, label, name string }{
		{"cert-manager", "app.kubernetes.io/name=cert-manager", "Cert Manager"},
		{"external-secrets", "app.kubernetes.io/name=external-secrets", "External Secrets Operator"},
		{"kyverno", "app.kubernetes.io/component=admission-controller", "Kyverno Admission Controller"},
		{"aws-load-balancer", "app.kubernetes.io/name=aws-load-balancer-controller", "AWS Load Balancer Controller"},
		{"external-dns", "app.kubernetes.io/name=external-dns", "External DNS"},
		{"reloader", "app.kubernetes.io/name=reloader", "Reloader"},
		{"kube-system", "app.kubernetes.io/name=metrics-server", "Metrics Server"},
	}
	for _, addon := range addons {
		s.checkPodsRunning(ctx, addon.ns, addon.label, addon.name)
	}
}

// -----------------------------------------------------------------------------
// Security Tests
// -----------------------------------------------------------------------------

func (s *TestSuite) testSecurity() {
	s.printHeader("SECURITY CONFIGURATION")
	ctx := context.Background()

	s.printSection("Secrets Management")
	// Check ClusterSecretStore
	gvr := schema.GroupVersionResource{Group: "external-secrets.io", Version: "v1", Resource: "clustersecretstores"}
	_, err := s.dynamicClient.Resource(gvr).Get(ctx, "aws-secrets-manager", metav1.GetOptions{})
	if err != nil {
		gvr = schema.GroupVersionResource{Group: "external-secrets.io", Version: "v1beta1", Resource: "clustersecretstores"}
		_, err = s.dynamicClient.Resource(gvr).Get(ctx, "aws-secrets-manager", metav1.GetOptions{})
	}
	if err != nil {
		s.fail("ClusterSecretStore 'aws-secrets-manager'")
	} else {
		s.pass("ClusterSecretStore 'aws-secrets-manager'")
	}

	// Check ExternalSecrets sync status
	esGVR := schema.GroupVersionResource{Group: "external-secrets.io", Version: "v1beta1", Resource: "externalsecrets"}
	esList, err := s.dynamicClient.Resource(esGVR).Namespace("").List(ctx, metav1.ListOptions{})
	if err == nil {
		synced := 0
		total := len(esList.Items)
		for _, es := range esList.Items {
			conditions, found, _ := unstructured.NestedSlice(es.Object, "status", "conditions")
			if found {
				for _, c := range conditions {
					cond := c.(map[string]interface{})
					if cond["type"] == "Ready" && cond["status"] == "True" {
						synced++
						break
					}
				}
			}
		}
		if total > 0 {
			if synced == total {
				s.pass(fmt.Sprintf("ExternalSecrets synced: %d/%d", synced, total))
			} else {
				s.warn(fmt.Sprintf("ExternalSecrets synced: %d/%d", synced, total))
			}
		}
	}

	s.printSection("Kyverno Policies")
	policyGVR := schema.GroupVersionResource{Group: "kyverno.io", Version: "v1", Resource: "clusterpolicies"}
	policies, err := s.dynamicClient.Resource(policyGVR).List(ctx, metav1.ListOptions{})
	if err == nil {
		s.pass(fmt.Sprintf("Kyverno policies: %d", len(policies.Items)))
	}
}

// -----------------------------------------------------------------------------
// Networking Tests
// -----------------------------------------------------------------------------

func (s *TestSuite) testNetworking() {
	s.printHeader("NETWORKING")
	ctx := context.Background()

	s.printSection("AWS Load Balancer Controller")
	s.checkPodsRunning(ctx, "aws-load-balancer", "app.kubernetes.io/name=aws-load-balancer-controller", "AWS LB Controller")

	s.printSection("External DNS")
	s.checkPodsRunning(ctx, "external-dns", "app.kubernetes.io/name=external-dns", "External DNS")

	s.printSection("Ingresses")
	ingresses, err := s.clientset.NetworkingV1().Ingresses("").List(ctx, metav1.ListOptions{})
	if err == nil {
		withLB := 0
		for _, ing := range ingresses.Items {
			if len(ing.Status.LoadBalancer.Ingress) > 0 {
				withLB++
			}
		}
		s.pass(fmt.Sprintf("Ingresses: %d total, %d with LoadBalancer", len(ingresses.Items), withLB))
	}
}

// -----------------------------------------------------------------------------
// Druid Tests
// -----------------------------------------------------------------------------

func (s *TestSuite) testDruid() {
	s.printHeader("APACHE DRUID")
	ctx := context.Background()

	// Find Druid namespace (may vary by deployment)
	druidNamespace := s.findDruidNamespace(ctx)
	if druidNamespace == "" {
		s.fail("Druid namespace not found")
		return
	}
	s.pass(fmt.Sprintf("Druid namespace: %s", druidNamespace))

	s.printSection("Druid Components")

	// Coordinator (StatefulSet)
	s.checkStatefulSetReady(ctx, druidNamespace, "coordinator", "Druid Coordinator")

	// Overlord (StatefulSet)
	s.checkStatefulSetReady(ctx, druidNamespace, "overlord", "Druid Overlord")

	// Historical (StatefulSet)
	s.checkStatefulSetReady(ctx, druidNamespace, "historical", "Druid Historical")

	// Broker (Deployment)
	s.checkDeploymentReadyByPrefix(ctx, druidNamespace, "broker", "Druid Broker")

	// Router (Deployment)
	s.checkDeploymentReadyByPrefix(ctx, druidNamespace, "router", "Druid Router")

	// MiddleManager / Indexer (may be dynamic via Karpenter)
	s.printSection("Druid Data Ingestion")
	pods, err := s.clientset.CoreV1().Pods(druidNamespace).List(ctx, metav1.ListOptions{})
	if err == nil {
		mmCount := 0
		indexerCount := 0
		for _, pod := range pods.Items {
			if strings.Contains(pod.Name, "middlemanager") || strings.Contains(pod.Name, "middle-manager") {
				if pod.Status.Phase == "Running" {
					mmCount++
				}
			}
			if strings.Contains(pod.Name, "indexer") {
				if pod.Status.Phase == "Running" {
					indexerCount++
				}
			}
		}
		if mmCount > 0 {
			s.pass(fmt.Sprintf("MiddleManagers running: %d", mmCount))
		}
		if indexerCount > 0 {
			s.pass(fmt.Sprintf("Indexers running: %d", indexerCount))
		}
		if mmCount == 0 && indexerCount == 0 {
			s.warn("No MiddleManagers or Indexers running (may scale to zero)")
		}
	}

	s.printSection("Druid Services")
	services, err := s.clientset.CoreV1().Services(druidNamespace).List(ctx, metav1.ListOptions{})
	if err == nil {
		druidServices := 0
		for _, svc := range services.Items {
			if strings.Contains(svc.Name, "druid") || strings.Contains(svc.Name, "broker") ||
				strings.Contains(svc.Name, "coordinator") || strings.Contains(svc.Name, "router") ||
				strings.Contains(svc.Name, "overlord") || strings.Contains(svc.Name, "historical") {
				druidServices++
			}
		}
		s.pass(fmt.Sprintf("Druid services: %d", druidServices))
	}

	s.printSection("Druid Configuration")
	// Check ConfigMaps
	cms, err := s.clientset.CoreV1().ConfigMaps(druidNamespace).List(ctx, metav1.ListOptions{})
	if err == nil {
		druidCMs := 0
		for _, cm := range cms.Items {
			if strings.Contains(cm.Name, "druid") {
				druidCMs++
			}
		}
		s.pass(fmt.Sprintf("Druid ConfigMaps: %d", druidCMs))
	}

	s.printSection("Druid Secrets")
	// Check for metadata store credentials
	secrets, err := s.clientset.CoreV1().Secrets(druidNamespace).List(ctx, metav1.ListOptions{})
	if err == nil {
		metadataSecret := false
		adminSecret := false
		for _, secret := range secrets.Items {
			if strings.Contains(secret.Name, "metadata") {
				metadataSecret = true
			}
			if strings.Contains(secret.Name, "admin") {
				adminSecret = true
			}
		}
		if metadataSecret {
			s.pass("Metadata store credentials exist")
		} else {
			s.warn("Metadata store credentials not found")
		}
		if adminSecret {
			s.pass("Admin credentials exist")
		}
	}

	s.printSection("Druid Health")
	// Try to reach router health endpoint
	routerSvc, err := s.clientset.CoreV1().Services(druidNamespace).Get(ctx, "router", metav1.GetOptions{})
	if err == nil {
		// Check if there's an ingress
		ingresses, _ := s.clientset.NetworkingV1().Ingresses(druidNamespace).List(ctx, metav1.ListOptions{})
		for _, ing := range ingresses.Items {
			if len(ing.Spec.Rules) > 0 {
				host := ing.Spec.Rules[0].Host
				if host != "" && strings.Contains(host, "druid") {
					s.pass(fmt.Sprintf("Druid ingress: %s", host))

					// Try health check
					client := &http.Client{Timeout: 5 * time.Second}
					resp, err := client.Get(fmt.Sprintf("https://%s/status/health", host))
					if err == nil {
						resp.Body.Close()
						if resp.StatusCode == 200 {
							s.pass("Druid Router health: OK")
						} else {
							s.warn(fmt.Sprintf("Druid Router health: HTTP %d", resp.StatusCode))
						}
					} else {
						s.warn("Druid Router not reachable externally")
					}
					break
				}
			}
		}
	} else {
		_ = routerSvc // Unused but checked
		s.warn("Router service not found")
	}

	s.printSection("ZooKeeper")
	// Check for ZooKeeper (may be in same namespace or separate)
	zkNamespaces := []string{druidNamespace, "zookeeper", "kafka"}
	zkFound := false
	for _, ns := range zkNamespaces {
		pods, err := s.clientset.CoreV1().Pods(ns).List(ctx, metav1.ListOptions{
			LabelSelector: "app=zookeeper",
		})
		if err == nil && len(pods.Items) > 0 {
			running := 0
			for _, pod := range pods.Items {
				if pod.Status.Phase == "Running" {
					running++
				}
			}
			if running > 0 {
				s.pass(fmt.Sprintf("ZooKeeper pods running: %d (namespace: %s)", running, ns))
				zkFound = true
				break
			}
		}
		// Try alternative label
		pods, err = s.clientset.CoreV1().Pods(ns).List(ctx, metav1.ListOptions{
			LabelSelector: "app.kubernetes.io/name=zookeeper",
		})
		if err == nil && len(pods.Items) > 0 {
			running := 0
			for _, pod := range pods.Items {
				if pod.Status.Phase == "Running" {
					running++
				}
			}
			if running > 0 {
				s.pass(fmt.Sprintf("ZooKeeper pods running: %d (namespace: %s)", running, ns))
				zkFound = true
				break
			}
		}
	}
	if !zkFound {
		s.warn("ZooKeeper pods not found (may be external)")
	}

	s.printSection("Druid Karpenter NodePools")
	nodePoolGVR := schema.GroupVersionResource{Group: "karpenter.sh", Version: "v1", Resource: "nodepools"}
	nodePools, err := s.dynamicClient.Resource(nodePoolGVR).List(ctx, metav1.ListOptions{})
	if err == nil {
		druidPools := 0
		for _, np := range nodePools.Items {
			name := np.GetName()
			if strings.Contains(name, "druid") || strings.Contains(name, "broker") ||
				strings.Contains(name, "historical") || strings.Contains(name, "coordinator") ||
				strings.Contains(name, "overlord") || strings.Contains(name, "router") {
				druidPools++
			}
		}
		if druidPools > 0 {
			s.pass(fmt.Sprintf("Druid NodePools: %d", druidPools))
		} else {
			s.warn("No Druid-specific NodePools found")
		}
	}
}

func (s *TestSuite) findDruidNamespace(ctx context.Context) string {
	// Common Druid namespace patterns
	candidates := []string{"druid", "apache-druid"}

	// First check exact matches
	for _, ns := range candidates {
		_, err := s.clientset.CoreV1().Namespaces().Get(ctx, ns, metav1.GetOptions{})
		if err == nil {
			return ns
		}
	}

	// Then search for namespaces containing "druid"
	namespaces, err := s.clientset.CoreV1().Namespaces().List(ctx, metav1.ListOptions{})
	if err == nil {
		for _, ns := range namespaces.Items {
			if strings.Contains(strings.ToLower(ns.Name), "druid") {
				return ns.Name
			}
		}
	}

	return ""
}

// -----------------------------------------------------------------------------
// Observability Tests
// -----------------------------------------------------------------------------

func (s *TestSuite) testObservability() {
	s.printHeader("OBSERVABILITY")
	ctx := context.Background()

	s.printSection("Metrics Server")
	s.checkPodsRunning(ctx, "kube-system", "app.kubernetes.io/name=metrics-server", "Metrics Server")

	_, err := s.clientset.Discovery().ServerResourcesForGroupVersion("metrics.k8s.io/v1beta1")
	if err == nil {
		s.pass("Metrics API available")
	} else {
		s.warn("Metrics API not available")
	}

	s.printSection("Container Insights")
	s.checkPodsRunning(ctx, "amazon-cloudwatch", "app.kubernetes.io/name=cloudwatch-agent", "CloudWatch Agent")

	s.printSection("Grafana Monitoring")
	pods, err := s.clientset.CoreV1().Pods("monitoring").List(ctx, metav1.ListOptions{})
	if err == nil && len(pods.Items) > 0 {
		running := 0
		for _, pod := range pods.Items {
			if pod.Status.Phase == "Running" {
				running++
			}
		}
		if running > 0 {
			s.pass(fmt.Sprintf("Monitoring pods: %d running", running))
		} else {
			s.warn("Monitoring pods: none running")
		}
	} else {
		s.warn("Monitoring namespace/pods not found")
	}
}

// -----------------------------------------------------------------------------
// Helper Functions
// -----------------------------------------------------------------------------

func (s *TestSuite) checkPodsRunning(ctx context.Context, namespace, labelSelector, name string) {
	pods, err := s.clientset.CoreV1().Pods(namespace).List(ctx, metav1.ListOptions{
		LabelSelector: labelSelector,
	})
	if err != nil {
		s.fail(fmt.Sprintf("%s (error listing)", name))
		return
	}
	running := 0
	for _, pod := range pods.Items {
		if pod.Status.Phase == "Running" {
			running++
		}
	}
	if running > 0 {
		s.pass(fmt.Sprintf("%s: %d running", name, running))
	} else {
		s.fail(fmt.Sprintf("%s: no pods running", name))
	}
}

func (s *TestSuite) checkStatefulSetReady(ctx context.Context, namespace, prefix, name string) {
	stsList, err := s.clientset.AppsV1().StatefulSets(namespace).List(ctx, metav1.ListOptions{})
	if err != nil {
		s.fail(fmt.Sprintf("%s (error listing)", name))
		return
	}
	for _, sts := range stsList.Items {
		if strings.Contains(sts.Name, prefix) {
			if sts.Status.ReadyReplicas == *sts.Spec.Replicas && *sts.Spec.Replicas > 0 {
				s.pass(fmt.Sprintf("%s (%s): %d/%d ready", name, sts.Name, sts.Status.ReadyReplicas, *sts.Spec.Replicas))
			} else {
				s.fail(fmt.Sprintf("%s (%s): %d/%d ready", name, sts.Name, sts.Status.ReadyReplicas, *sts.Spec.Replicas))
			}
			return
		}
	}
	s.warn(fmt.Sprintf("%s: not found", name))
}

func (s *TestSuite) checkDeploymentReadyByPrefix(ctx context.Context, namespace, prefix, name string) {
	depList, err := s.clientset.AppsV1().Deployments(namespace).List(ctx, metav1.ListOptions{})
	if err != nil {
		s.fail(fmt.Sprintf("%s (error listing)", name))
		return
	}
	for _, dep := range depList.Items {
		if strings.Contains(dep.Name, prefix) {
			if dep.Status.ReadyReplicas == *dep.Spec.Replicas && *dep.Spec.Replicas > 0 {
				s.pass(fmt.Sprintf("%s (%s): %d/%d ready", name, dep.Name, dep.Status.ReadyReplicas, *dep.Spec.Replicas))
			} else {
				s.fail(fmt.Sprintf("%s (%s): %d/%d ready", name, dep.Name, dep.Status.ReadyReplicas, *dep.Spec.Replicas))
			}
			return
		}
	}
	s.warn(fmt.Sprintf("%s: not found", name))
}

func (s *TestSuite) checkCRDExists(ctx context.Context, name, display string) {
	gvr := schema.GroupVersionResource{Group: "apiextensions.k8s.io", Version: "v1", Resource: "customresourcedefinitions"}
	_, err := s.dynamicClient.Resource(gvr).Get(ctx, name, metav1.GetOptions{})
	if err != nil {
		s.fail(display)
	} else {
		s.pass(display)
	}
}

func (s *TestSuite) PrintSummary() {
	s.printHeader("TEST SUMMARY")

	total := s.passed + s.failed + s.warnings
	fmt.Printf("\n  %s✓ Passed:%s   %d\n", colorGreen, colorReset, s.passed)
	fmt.Printf("  %s✗ Failed:%s   %d\n", colorRed, colorReset, s.failed)
	fmt.Printf("  %s⚠ Warnings:%s %d\n", colorYellow, colorReset, s.warnings)
	fmt.Printf("  ─────────────────\n")
	fmt.Printf("  Total:     %d\n", total)

	if s.failed == 0 {
		fmt.Printf("\n%s✓ All critical checks passed!%s\n\n", colorGreen, colorReset)
	} else {
		fmt.Printf("\n%s✗ Some checks failed. Review output above.%s\n\n", colorRed, colorReset)
	}
}
