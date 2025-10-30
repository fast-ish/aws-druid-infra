# AI Context: aws-druid-infra

> **Purpose**: This document helps AI assistants quickly understand the aws-druid-infra codebase architecture, patterns, and conventions.

## What is aws-druid-infra?

A **production-ready Apache Druid deployment on AWS EKS** built with AWS CDK that provides:
- Complete EKS cluster with managed and custom addons
- Multi-AZ VPC with public/private subnets
- Apache Druid cluster with AWS resource integration
- RDS database for Druid metadata storage
- S3 buckets for deep storage and multi-stage query ingestion
- MSK (Managed Streaming for Apache Kafka) for real-time ingestion
- Advanced autoscaling with Karpenter
- Comprehensive observability with Grafana Cloud
- Security-focused with Pod Identity, KMS encryption, and RBAC

**Key Technologies**: Java 21, AWS CDK 2.219.0, cdk-common library, Kubernetes 1.33, Apache Druid, Karpenter, Grafana

## Architecture Overview

### Nested Stack Pattern

```
DeploymentStack (main)
├── VpcNestedStack          # VPC, subnets, NAT gateways
├── EksNestedStack          # EKS cluster, node groups, addons
├── DruidSetupNestedStack   # RDS, S3, MSK resources for Druid
└── DruidNestedStack        # Druid Helm chart deployment
```

**Infrastructure Flow**:
1. VPC stack creates VPC with multiple AZs (public/private subnets)
2. EKS stack creates cluster in the VPC with managed addons
3. Druid Setup stack creates supporting AWS resources (RDS, S3, MSK)
4. Druid stack deploys Helm chart integrated with AWS resources
5. Node groups provisioned with Bottlerocket AMI

### Project Structure

```
aws-druid-infra/
├── src/
│   ├── main/
│   │   ├── java/fasti/sh/eks/
│   │   │   ├── Launch.java                      # CDK App entry point
│   │   │   └── stack/
│   │   │       ├── DeploymentConf.java          # Configuration record
│   │   │       ├── DeploymentStack.java         # Main orchestration stack
│   │   │       ├── model/                       # Configuration models
│   │   │       │   ├── SystemConf.java
│   │   │       │   ├── DruidConf.java
│   │   │       │   └── druid/                   # Druid-specific models
│   │   │       │       ├── Storage.java         # S3 bucket configs
│   │   │       │       ├── Ingestion.java       # MSK configs
│   │   │       │       ├── Access.java          # IAM policy configs
│   │   │       │       ├── Secrets.java         # Secrets manager configs
│   │   │       │       └── Common.java          # Druid common config
│   │   │       └── nested/
│   │   │           ├── DruidSetupNestedStack.java  # AWS resources for Druid
│   │   │           └── DruidNestedStack.java       # Druid Helm deployment
│   │   └── resources/production/v1/             # Configuration templates
│   │       ├── conf.mustache                    # Main deployment config
│   │       ├── synthesizer.mustache             # CDK synthesizer config
│   │       ├── eks/                             # EKS cluster configs
│   │       │   ├── addons.mustache              # Managed & Helm addons
│   │       │   ├── node-groups.mustache
│   │       │   ├── tenancy.mustache
│   │       │   ├── observability.mustache
│   │       │   ├── sqs.mustache
│   │       │   └── rbac.mustache
│   │       ├── druid/
│   │       │   ├── values.mustache              # Druid Helm values
│   │       │   └── setup/                       # Druid AWS resources
│   │       │       ├── storage.mustache         # S3 bucket configs
│   │       │       ├── ingestion.mustache       # MSK cluster config
│   │       │       ├── access.mustache          # IAM policies
│   │       │       └── secrets.mustache         # Secrets manager
│   │       ├── helm/                            # Helm chart values
│   │       │   ├── karpenter.mustache
│   │       │   ├── cert-manager.mustache
│   │       │   ├── aws-load-balancer.mustache
│   │       │   ├── aws-secrets-store.mustache
│   │       │   ├── alloy-operator.mustache
│   │       │   └── grafana.mustache
│   │       └── policy/                          # IAM policy templates
│   │           ├── karpenter.mustache
│   │           ├── aws-load-balancer-controller.mustache
│   │           ├── druid-bucket-access.mustache
│   │           ├── bucket-access.mustache
│   │           ├── msk-cluster-access.mustache
│   │           └── secret-access.mustache
│   └── test/
│       └── java/fasti/sh/eks/
│           └── (test files - to be added)
│
├── .github/
│   ├── dependabot.yml                           # Dependency update automation
│   ├── workflows/                               # GitHub Actions CI/CD
│   └── AI_CONTEXT.md                            # This file
│
├── docs/                                        # Documentation (optional)
├── cdk.json                                     # CDK app configuration
├── cdk.context.template.json                    # Context template for deployment
└── pom.xml                                     # Maven build configuration
```

## Core Concepts

### 1. Configuration-Driven Infrastructure

All infrastructure is defined through YAML configuration files processed by Mustache templates:

```yaml
# resources/production/v1/druid/values.yaml
cluster:
  name: "{{hosted:name}}-druid"
metadata:
  storage: postgres
  connector: postgresql
  host: "{{druid:metadata:endpoint}}"
  port: 5432
deepStorage:
  type: s3
  bucket: "{{druid:storage:deep:bucket}}"
```

**Template Variables**:
- `{{host:*}}` - AWS account/region context (e.g., `host:account`, `host:region`)
- `{{hosted:*}}` - Application context (e.g., `hosted:name`, `hosted:environment`)
- Service-specific: `{{druid:*}}`, `{{eks:*}}`, `{{vpc:*}}`, etc.

### 2. cdk-common Dependency

This project uses the `cdk-common` library which provides:
- High-level AWS constructs (`VpcConstruct`, `EksConstruct`, etc.)
- Template processing (`Template.java`, `Mapper.java`)
- Common models (`Common`, `NetworkConf`, `KubernetesConf`)
- Naming conventions (`Format.java`)

**All nested stacks use cdk-common constructs internally.**

### 3. Deployment Pattern

```
CDK Context → Template Processing → YAML/JSON → Java Records → CDK Constructs → AWS Resources
```

**Key Classes**:
- `Launch.java` - Entry point, loads configuration and creates stacks
- `DeploymentConf` - Record holding all configuration paths
- `DeploymentStack` - Orchestrates nested stack creation
- `DruidSetupNestedStack` - Creates RDS, S3, MSK for Druid
- `DruidNestedStack` - Deploys Druid Helm chart
- `Template.parse()` - Processes Mustache templates with context
- `Mapper.get()` - Jackson YAML/JSON parser

### 4. Nested Stack Architecture

Each nested stack:
- Extends `software.amazon.awscdk.NestedStack`
- Uses cdk-common constructs for resource creation
- Receives `Common` metadata and specific configuration
- Returns resources via getter methods

Example pattern:
```java
public class DruidSetupNestedStack extends NestedStack {
  @Getter
  private final DatabaseInstance database;
  @Getter
  private final Bucket deepStorageBucket;
  @Getter
  private final Cluster mskCluster;

  public DruidSetupNestedStack(Construct scope, Common common,
                               DruidConf conf, Vpc vpc, NestedStackProps props) {
    super(scope, Format.id("druid-setup", common.id()), props);

    // Create RDS, S3, MSK using cdk-common constructs
    this.database = createDatabase(common, conf, vpc);
    this.deepStorageBucket = createBucket(common, conf);
    this.mskCluster = createMskCluster(common, conf, vpc);
  }
}
```

## Key Components

### 1. VPC Stack

**VPC Configuration**:
- CIDR: 10.0.0.0/16 (configurable)
- Multiple Availability Zones for high availability
- Public subnets: /24 per AZ (NAT Gateways, Load Balancers)
- Private subnets: /24 per AZ (EKS worker nodes, RDS, MSK)
- NAT Gateways for redundancy

**Pattern**: Uses `VpcConstruct` from cdk-common with configuration loaded from `resources/production/v1/vpc/`

### 2. EKS Stack

**Cluster Features**:
- Kubernetes version: 1.33
- Endpoint access: Public + Private (hybrid mode)
- Logging: All log types enabled
- Authentication: IAM + RBAC integration
- Access entries: Administrator and user roles

**Node Groups**:
- AMI: Bottlerocket (security-focused container OS)
- Instance type: m5a.large (configurable)
- Capacity: 2-6 nodes (min-max-desired)
- Deployment: Private subnets only
- Management: SSM for secure access

### 3. AWS Managed Addons

Installed automatically with proper configuration:

| Addon | Version | Purpose | Configuration |
|-------|---------|---------|--------------|
| VPC CNI | v1.20.4-eksbuild.1 | Pod networking | IAM role with service account |
| EBS CSI Driver | v1.51.1-eksbuild.1 | Persistent storage | KMS encryption, custom storage class |
| CoreDNS | v1.12.4-eksbuild.1 | Cluster DNS | Default configuration |
| Kube Proxy | v1.34.0-eksbuild.4 | Network proxy | Default configuration |
| Pod Identity Agent | v1.3.9-eksbuild.3 | IRSA support | Enables IAM roles for pods |
| Container Insights | v5.0.0-eksbuild.1 | Monitoring | CloudWatch integration |

**Pattern**: Configured via YAML files in `resources/production/v1/eks/addons.mustache`

### 4. Helm Chart Addons

Installed via CDK Helm chart construct:

| Chart | Version | Namespace | Purpose |
|-------|---------|-----------|---------|
| cert-manager | v1.19.1 | cert-manager | TLS certificate automation |
| CSI Secrets Store | 2.1.1 | aws-secrets-store | Secrets/Parameter Store integration |
| Karpenter | 1.8.1 | kube-system | Advanced node autoscaling |
| AWS LB Controller | 1.14.1 | aws-load-balancer | ALB/NLB management |
| Alloy Operator | 0.3.9 | monitoring | Grafana Alloy operator (CRDs) |
| K8s Monitoring | 3.5.1 | monitoring | Grafana Cloud observability |

**Pattern**: Values configured in `resources/production/v1/helm/{chart-name}.mustache`

### 5. Druid Setup Stack

Creates AWS resources required by Apache Druid:

**RDS Database** (Metadata Storage):
- Engine: PostgreSQL
- Purpose: Stores Druid metadata (segment info, task info, etc.)
- High availability: Multi-AZ deployment option
- Security: VPC security groups, encryption at rest

**S3 Buckets**:
- **Deep Storage Bucket**: Permanent storage for Druid segments
- **Ingestion Bucket**: Temporary storage for multi-stage query processing
- Versioning, lifecycle policies, encryption enabled

**MSK Cluster** (Real-time Ingestion):
- Apache Kafka managed service
- Purpose: Real-time data ingestion for Druid
- Configuration: Multi-broker, multi-AZ
- Security: IAM authentication, encryption in transit

**IAM Policies**:
- Druid pods access S3 buckets via IRSA
- Druid pods access RDS via security groups
- Druid pods access MSK via IAM authentication

### 6. Druid Helm Deployment

**Druid Components**:
- **Coordinator**: Manages segment placement and balance
- **Overlord**: Manages task assignment and execution
- **Broker**: Handles query routing and merging
- **Router**: Routes queries to brokers (optional)
- **Historical**: Serves historical segments from deep storage
- **MiddleManager**: Executes ingestion tasks

**Integration with AWS Resources**:
```yaml
metadata:
  storage: postgres
  connector: postgresql
  host: "{{druid:metadata:endpoint}}"  # RDS endpoint

deepStorage:
  type: s3
  bucket: "{{druid:storage:deep:bucket}}"  # S3 bucket name

ingestion:
  kafka:
    bootstrapServers: "{{druid:ingestion:msk:brokers}}"  # MSK brokers
```

### 7. Observability Stack

**Grafana Cloud Integration**:
- Metrics: Prometheus-compatible metrics collection
- Logs: Structured JSON logs via Fluent Bit
- Traces: OpenTelemetry/X-Ray distributed tracing
- Dashboards: Pre-built Kubernetes and Druid dashboards

**CloudWatch Container Insights**:
- Container-level metrics
- Application performance data
- Enhanced observability metrics

## Configuration Records

All configuration is defined as Java records (immutable):

```java
public record DeploymentConf(
  String vpc,              // Path to VPC config
  String eks,              // Path to EKS config
  String nodeGroup,        // Path to node group config
  String druidStorage,     // Path to Druid storage config
  String druidIngestion,   // Path to Druid ingestion config
  String druidAccess,      // Path to Druid access config
  String druidSecrets,     // Path to Druid secrets config
  String druidValues       // Path to Druid Helm values
  // ... addon paths
) {}
```

**Record Pattern**:
- All fields are `String` paths to template files
- Loaded via `Template.load()` method
- Validated in constructors
- Immutable by design

## Testing Strategy

### Configuration Tests (497 tests)

High-value tests validating configuration loading and parsing:
- `DeploymentConfTest`: 40 tests for all config paths
- `DruidConfTest`: 89 tests for Druid configuration models
- `SystemConfTest`: 16 tests for system configuration
- Model tests for all Druid components:
  - `CommonTest`: 84 tests for common Druid configuration
  - `IngestionTest`: 67 tests for MSK ingestion config
  - `AccessTest`: 64 tests for IAM access policies
  - `StorageTest`: 44 tests for S3 storage config
  - `SecretsTest`: 59 tests for secrets manager config
- Tests YAML parsing, Mustache processing, record creation
- Validates required fields, optional fields, defaults
- Tests error handling for missing/invalid configs

### Launch Tests (34 tests)

Tests for CDK app initialization:
- `LaunchTest`: 34 tests for app entry point
- Template processing with context variables
- Configuration record creation
- Synthesizer configuration

**Total Tests**: 497 tests, all passing ✅

### What We Don't Test

Integration/CDK tests were removed:
- Stack instantiation tests (brittle, low value)
- Mock-heavy construct tests (maintenance burden)
- CDK synth tests (covered by deployment)

**Rationale**: Focus on configuration correctness; actual AWS resource creation validated during deployment.

## Code Conventions

### Naming Conventions

**Stack Names**: `{Service}NestedStack` (e.g., `DruidNestedStack`, `DruidSetupNestedStack`)
**Config Records**: `{Service}Conf` (e.g., `DruidConf`, `SystemConf`)
**Resource IDs**: Use `Format.id()` from cdk-common
**Resource Names**: Use `Format.name()` from cdk-common

### Common Pattern

Every construct receives `Common` metadata:
```java
public record Common(
  String id, account, region, organization,
  String name, alias, environment, version, domain,
  Map<String, String> tags
)
```

Usage:
```java
var stack = new DruidSetupNestedStack(this, common, druidConf, vpc, props);
// common used for naming, tagging, context
```

### Logging Pattern

```java
@Slf4j
public class DeploymentStack extends Stack {
  public DeploymentStack(...) {
    log.debug("DeploymentStack [common: {} conf: {}]", common, conf);
  }
}
```

### Template Loading Pattern

```java
// From CDK context
var conf = Template.parse(app, "conf.mustache", contextVars);

// From resource file
var druidConf = Template.load(DruidConf.class,
    "production/v1/druid/setup/storage.yaml", ctx());
```

## Common Tasks

### Adding a New EKS Addon

1. Add addon configuration to `resources/production/v1/eks/addons.mustache`
2. Update `DeploymentConf` if needed
3. Configure in `EksConstruct` (for AWS addons) or add Helm chart in `EksNestedStack`
4. Add tests in `DeploymentConfTest`

### Modifying Druid Configuration

1. Edit `resources/production/v1/druid/values.mustache` for Helm chart values
2. Edit `resources/production/v1/druid/setup/*` for AWS resource configs
3. Update corresponding Java records if needed
4. Test with `cdk synth` and deploy

### Adding RBAC Access

1. Update `cdk.context.json` with new IAM role ARN:
```json
{
  "hosted:eks:administrators": [
    {
      "username": "new-admin",
      "role": "arn:aws:iam::ACCOUNT:role/ROLE-NAME",
      "email": "admin@example.com"
    }
  ]
}
```
2. Configuration automatically creates access entries

### Customizing Druid Resources

**RDS Database**:
- Edit `resources/production/v1/druid/setup/storage.mustache`
- Adjust instance type, storage, backup retention

**S3 Buckets**:
- Edit `resources/production/v1/druid/setup/storage.mustache`
- Configure lifecycle policies, versioning

**MSK Cluster**:
- Edit `resources/production/v1/druid/setup/ingestion.mustache`
- Adjust broker count, instance type, Kafka version

## Deployment Workflow

### 1. Prerequisites

```bash
# Install tools
java 21+, maven, aws-cli, cdk-cli, gh-cli

# Bootstrap CDK
cdk bootstrap aws://ACCOUNT-ID/REGION

# Build cdk-common dependency
mvn -f cdk-common/pom.xml clean install
```

### 2. Configure

```bash
# Copy template
cp cdk.context.template.json cdk.context.json

# Edit with your settings:
# - AWS account and region
# - Domain name
# - Administrator/user IAM roles
# - Environment and version
# - Grafana Cloud credentials
```

### 3. Build & Deploy

```bash
# Build project
mvn clean install

# Synthesize CloudFormation
cdk synth

# Deploy to AWS
cdk deploy
```

### 4. Post-Deployment

```bash
# Update kubeconfig
aws eks update-kubeconfig --name CLUSTER-NAME --region REGION

# Verify cluster
kubectl get nodes
kubectl get pods -A

# Check Druid pods
kubectl get pods -n druid

# Access Druid console (if ingress configured)
# Or use port-forward to access locally
kubectl port-forward -n druid svc/druid-router 8888:8888
```

## Dependencies

### Core Dependencies
- **AWS CDK**: 2.219.0 (cdk-common dependency)
- **Java**: 21+
- **cdk-common**: 1.0.0-SNAPSHOT (local dependency)
- **Jackson**: 2.20.0 (YAML/JSON processing)
- **Lombok**: 1.18.42 (annotations)

### Test Dependencies
- **JUnit 5**: 5.14.0 (to be added)
- **Mockito**: 5.20.0 (to be added)

### Maven Plugins (Latest Versions)
- **compiler**: 3.14.0
- **exec**: 3.6.2
- **surefire**: 3.5.4
- **spotless**: 3.0.0
- **spotbugs**: 4.9.8.1
- **pmd**: 3.28.0
- **jacoco**: 0.8.14
- **checkstyle**: 3.6.0
- **dependency-check**: 12.1.8
- **versions**: 2.19.1

## Troubleshooting

### CDK Synth Fails

```bash
# Clean and rebuild
mvn clean install
rm -rf cdk.out
cdk synth
```

### Template Processing Errors

- Check `cdk.context.json` has all required variables
- Verify template paths match `resources/` structure
- Ensure Mustache syntax is correct: `{{variable}}`

### EKS Cluster Access Issues

- Verify IAM role ARNs in `cdk.context.json`
- Check access entries created in cluster
- Update kubeconfig with correct cluster name/region
- Verify AWS credentials have necessary permissions

### Druid Deployment Issues

- Check RDS connection from Druid pods
- Verify S3 bucket permissions
- Check MSK connectivity and IAM authentication
- Review Druid logs: `kubectl logs -n druid -l app=druid`

### Druid Not Starting

- Verify metadata database is accessible
- Check S3 deep storage bucket exists and is accessible
- Verify MSK cluster is running and accessible
- Check Druid configuration in Helm values

## Key Differences from Other Projects

| Aspect | cdk-common | aws-eks-infra | aws-druid-infra |
|--------|------------|---------------|-----------------|
| Purpose | Library of reusable constructs | EKS cluster infrastructure | Druid on EKS infrastructure |
| Structure | Flat construct library | Nested stacks (Network, EKS) | Nested stacks (Network, EKS, Druid Setup, Druid) |
| Complexity | Medium (library) | High (Kubernetes) | Very High (Kubernetes + Druid + AWS services) |
| Testing | Construct + model tests | Model + launch tests | Model + launch tests (to be added) |
| Deployment | N/A (library) | Full EKS cluster | Full EKS cluster + Druid + RDS + S3 + MSK |
| Key Feature | Reusable constructs | Production EKS | Production Druid analytics platform |

## Resources

- [README.md](../README.md) - Overview and quickstart
- [ADDONS.md](../ADDONS.md) - Addon reference (if exists)
- [cdk-common](https://github.com/fast-ish/cdk-common) - Dependency library
- [Apache Druid Documentation](https://druid.apache.org/docs/latest/design/index.html)

## Version Info

- **Java**: 21+
- **AWS CDK**: 2.219.0
- **Kubernetes**: 1.33
- **Maven**: 3.8+
- **Package**: `fasti.sh.eks`
- **Current Version**: 1.0.0-SNAPSHOT

## Dependency Update Strategy

### Dependabot Configuration

Dependabot groups updates into three categories:
1. **maven-plugins**: All Maven plugins grouped together
2. **test-dependencies**: JUnit and Mockito updates grouped
3. **production-dependencies**: Jackson and other production deps grouped

This minimizes PR noise while keeping dependencies current.

---

**Last Updated**: 2025-10-29
**Test Status**: 497/497 passing ✅
**Build Status**: All dependencies up to date
