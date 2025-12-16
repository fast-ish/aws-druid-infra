'use client';

import { motion } from 'framer-motion';
import React, { useState } from 'react';
import {
  DruidIcon,
  CoordinatorIcon,
  BrokerIcon,
  HistoricalIcon,
  OverlordIcon,
  RouterIcon,
  MiddleManagerIcon,
  VPCIcon,
  EKSIcon,
  S3Icon,
  RDSIcon,
  MSKIcon,
  IAMIcon,
  HelmIcon,
  CDKIcon,
  KubernetesIcon,
  KarpenterIcon,
  SecretsManagerIcon,
  ALBIcon,
  CloudWatchIcon,
  GrafanaIcon,
  CertManagerIcon,
  PostgreSQLIcon,
  ExternalSecretsIcon,
  TLSIcon,
  MetricsServerIcon,
  ExternalDNSIcon,
  ReloaderIcon,
  GoldilocksIcon,
  VeleroIcon,
  KyvernoIcon,
  NodeTerminationHandlerIcon,
} from '../icons/aws-icons';
import { ServiceCard, Layer, DataFlowArrow } from '../ui/service-card';

type ViewMode = 'infrastructure' | 'druid-cluster' | 'data-flow' | 'deployment';

export const DruidArchitecture: React.FC = () => {
  const [activeView, setActiveView] = useState<ViewMode>('infrastructure');

  const views: { id: ViewMode; label: string; description: string }[] = [
    { id: 'infrastructure', label: 'Infrastructure', description: 'AWS Cloud Resources' },
    { id: 'druid-cluster', label: 'Druid Cluster', description: 'Apache Druid Components' },
    { id: 'data-flow', label: 'Data Flow', description: 'Query & Ingestion Paths' },
    { id: 'deployment', label: 'Deployment', description: 'CDK & Helm Pipeline' },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-950 via-slate-900 to-slate-950">
      {/* Header */}
      <motion.header
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="border-b border-slate-800/50 bg-slate-900/50 backdrop-blur-xl sticky top-0 z-50"
      >
        <div className="max-w-[1800px] mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className="flex items-center gap-3">
                <motion.div
                  animate={{ rotate: [0, 360] }}
                  transition={{ duration: 20, repeat: Infinity, ease: 'linear' }}
                >
                  <DruidIcon size={40} />
                </motion.div>
                <div>
                  <h1 className="text-xl font-bold text-white">Apache Druid on AWS</h1>
                  <p className="text-slate-400 text-sm">Production Analytics Infrastructure</p>
                </div>
              </div>
            </div>

            {/* View Switcher */}
            <div className="flex items-center gap-2 bg-slate-800/50 rounded-xl p-1">
              {views.map((view) => (
                <button
                  key={view.id}
                  onClick={() => setActiveView(view.id)}
                  className={`px-4 py-2 rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer
                    ${activeView === view.id
                      ? 'bg-teal-600 text-white shadow-lg shadow-teal-500/25'
                      : 'text-slate-400 hover:text-white hover:bg-slate-700/50'
                    }`}
                >
                  {view.label}
                </button>
              ))}
            </div>

            {/* Legend */}
            <div className="flex items-center gap-4 text-xs">
              <div className="flex items-center gap-2 cursor-help" title="Services that are running">
                <span className="w-3 h-3 rounded-full bg-green-500"></span>
                <span className="text-slate-400">Active</span>
              </div>
              <div className="flex items-center gap-2 cursor-help" title="Data flow direction">
                <span className="w-3 h-3 rounded-full bg-teal-500"></span>
                <span className="text-slate-400">Data Flow</span>
              </div>
              <div className="flex items-center gap-2 cursor-help" title="AWS managed services">
                <span className="w-3 h-3 rounded-full bg-orange-500"></span>
                <span className="text-slate-400">AWS Service</span>
              </div>
            </div>
          </div>
        </div>
      </motion.header>

      {/* Main Content */}
      <main className="max-w-[1800px] mx-auto px-6 py-8">
        {/* View Description */}
        <motion.div
          key={activeView}
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-8 text-center"
        >
          <h2 className="text-2xl font-bold text-white mb-2">
            {views.find(v => v.id === activeView)?.label} View
          </h2>
          <p className="text-slate-400">
            {views.find(v => v.id === activeView)?.description}
          </p>
        </motion.div>

        {/* Architecture Diagram */}
        {activeView === 'infrastructure' && <InfrastructureView />}
        {activeView === 'druid-cluster' && <DruidClusterView />}
        {activeView === 'data-flow' && <DataFlowView />}
        {activeView === 'deployment' && <DeploymentView />}
      </main>

      {/* Footer Stats */}
      <motion.footer
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 1 }}
        className="border-t border-slate-800/50 bg-slate-900/30 backdrop-blur-sm"
      >
        <div className="max-w-[1800px] mx-auto px-6 py-6">
          <div className="grid grid-cols-6 gap-6">
            {[
              { label: 'Druid Version', value: '35.0.0', icon: <DruidIcon size={24} /> },
              { label: 'CDK Stacks', value: '6', icon: <CDKIcon size={24} /> },
              { label: 'S3 Buckets', value: '3', icon: <S3Icon size={24} /> },
              { label: 'EKS Addons', value: '8+', icon: <KubernetesIcon size={24} /> },
              { label: 'Security Layers', value: '5', icon: <TLSIcon size={24} /> },
              { label: 'Availability Zones', value: '3', icon: <VPCIcon size={24} /> },
            ].map((stat, idx) => (
              <motion.div
                key={stat.label}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 1.2 + idx * 0.1 }}
                className="text-center cursor-pointer hover:bg-slate-800/30 rounded-lg p-2 transition-colors"
              >
                <div className="flex items-center justify-center gap-2 mb-2">
                  {stat.icon}
                  <span className="text-2xl font-bold text-white">{stat.value}</span>
                </div>
                <span className="text-slate-500 text-sm">{stat.label}</span>
              </motion.div>
            ))}
          </div>
        </div>
      </motion.footer>
    </div>
  );
};

const InfrastructureView: React.FC = () => {
  return (
    <div className="space-y-6">
      {/* AWS Cloud Container */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        className="relative border-2 border-dashed border-orange-500/30 rounded-3xl p-8 bg-gradient-to-br from-orange-950/10 to-slate-950"
      >
        <div className="absolute -top-4 left-8 bg-slate-950 px-4 py-1 rounded-full border border-orange-500/30 cursor-pointer hover:border-orange-500/60 hover:bg-slate-900 transition-all">
          <span className="text-orange-400 text-sm font-semibold flex items-center gap-2">
            <span className="w-2 h-2 rounded-full bg-orange-500 animate-pulse"></span>
            AWS Cloud
          </span>
        </div>

        {/* VPC Layer */}
        <Layer
          title="VPC Network"
          subtitle="10.0.0.0/16 - Multi-AZ - Private & Public Subnets"
          color="from-purple-900/20 to-slate-900/50"
          delay={0.1}
          className="mb-6"
        >
          <div className="grid grid-cols-3 gap-4">
            {['us-west-2a', 'us-west-2b', 'us-west-2c'].map((az, idx) => (
              <motion.div
                key={az}
                initial={{ opacity: 0, scale: 0.9 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ delay: 0.2 + idx * 0.1 }}
                className="bg-slate-800/30 rounded-xl p-4 border border-slate-700/30 cursor-pointer hover:border-purple-500/50 hover:bg-slate-800/50 transition-all"
              >
                <div className="text-xs text-slate-500 mb-3">Availability Zone: {az}</div>
                <div className="space-y-2">
                  <div className="flex items-center gap-2 text-xs cursor-help" title="NAT Gateway attached">
                    <span className="w-2 h-2 rounded-full bg-green-500"></span>
                    <span className="text-slate-400">Public Subnet (10.0.{idx}.0/24)</span>
                  </div>
                  <div className="flex items-center gap-2 text-xs cursor-help" title="Private workloads">
                    <span className="w-2 h-2 rounded-full bg-teal-500"></span>
                    <span className="text-slate-400">Private Subnet (10.0.{idx + 10}.0/24)</span>
                  </div>
                </div>
              </motion.div>
            ))}
          </div>

          <div className="flex justify-center gap-8 mt-6">
            <ServiceCard
              icon={<VPCIcon size={36} />}
              title="NAT Gateway"
              description="Egress for private subnets"
              details={['High availability', 'Auto-scaling bandwidth', '2 NAT Gateways']}
              delay={0.4}
              size="sm"
              status="active"
              metrics={[
                { label: 'Gateways', value: '2' },
                { label: 'Elastic IPs', value: '2' },
              ]}
              tags={['networking', 'egress', 'ha']}
              docsUrl="https://docs.aws.amazon.com/vpc/latest/userguide/vpc-nat-gateway.html"
            />
            <ServiceCard
              icon={<ALBIcon size={36} />}
              title="Internet Gateway"
              description="Public internet access"
              details={['Ingress routing', 'Redundant by default']}
              delay={0.5}
              size="sm"
              status="active"
              tags={['networking', 'ingress']}
              docsUrl="https://docs.aws.amazon.com/vpc/latest/userguide/VPC_Internet_Gateway.html"
            />
          </div>
        </Layer>

        {/* EKS Layer */}
        <Layer
          title="Amazon EKS Cluster"
          subtitle="Kubernetes 1.28+ - ZooKeeper-less Druid"
          color="from-orange-900/20 to-slate-900/50"
          delay={0.3}
          className="mb-6"
        >
          <div className="grid grid-cols-4 gap-4 mb-6">
            <ServiceCard
              icon={<EKSIcon size={36} />}
              title="Control Plane"
              description="Managed Kubernetes API"
              details={['Multi-AZ deployment', 'OIDC provider', 'Encrypted etcd', 'Pod Identity']}
              delay={0.4}
              status="active"
              metrics={[
                { label: 'Version', value: '1.28+' },
                { label: 'Uptime', value: '99.95%' },
              ]}
              tags={['kubernetes', 'managed', 'control-plane']}
              docsUrl="https://docs.aws.amazon.com/eks/latest/userguide/clusters.html"
            />
            <ServiceCard
              icon={<KarpenterIcon size={36} />}
              title="Karpenter"
              description="Node auto-provisioning"
              details={['Spot instance support', 'Right-sizing', 'Multi-instance types', 'Bottlerocket AMI']}
              delay={0.5}
              status="active"
              metrics={[
                { label: 'Node Pools', value: '6' },
                { label: 'Spot Enabled', value: 'Yes' },
              ]}
              tags={['autoscaling', 'spot', 'cost-optimization']}
              docsUrl="https://karpenter.sh/docs/"
            />
            <ServiceCard
              icon={<ALBIcon size={36} />}
              title="AWS LB Controller"
              description="Ingress management"
              details={['ALB/NLB support', 'TLS termination', 'Target group binding']}
              delay={0.6}
              status="active"
              metrics={[
                { label: 'Ingresses', value: '1' },
                { label: 'Services', value: '6' },
              ]}
              tags={['ingress', 'load-balancer', 'tls']}
              docsUrl="https://kubernetes-sigs.github.io/aws-load-balancer-controller/"
            />
            <ServiceCard
              icon={<ExternalSecretsIcon size={36} />}
              title="External Secrets"
              description="Secret synchronization"
              details={['AWS Secrets Manager', 'Auto-refresh (1h)', 'TLS certs sync']}
              delay={0.7}
              status="active"
              metrics={[
                { label: 'Secrets Synced', value: '4' },
                { label: 'Refresh', value: '1h' },
              ]}
              tags={['security', 'secrets', 'sync']}
              docsUrl="https://external-secrets.io/latest/"
            />
          </div>

          {/* Additional EKS Addons */}
          <div className="bg-slate-800/20 rounded-xl p-4 border border-slate-700/20">
            <h4 className="text-sm font-semibold text-white mb-3">EKS Addons</h4>
            <div className="grid grid-cols-4 gap-3 mb-3">
              {[
                { name: 'cert-manager', desc: 'TLS certificate management', icon: <CertManagerIcon size={24} /> },
                { name: 'Metrics Server', desc: 'Resource metrics API', icon: <MetricsServerIcon size={24} /> },
                { name: 'External DNS', desc: 'Route53 automation', icon: <ExternalDNSIcon size={24} /> },
                { name: 'Reloader', desc: 'ConfigMap/Secret reload', icon: <ReloaderIcon size={24} /> },
              ].map((addon, idx) => (
                <motion.div
                  key={addon.name}
                  initial={{ opacity: 0, x: -10 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: 0.8 + idx * 0.1 }}
                  className="bg-slate-900/50 rounded-lg p-3 border border-slate-700/30 cursor-pointer hover:border-teal-500/50 hover:bg-slate-900/70 transition-all flex items-center gap-2"
                  title={addon.desc}
                >
                  {addon.icon}
                  <div>
                    <div className="text-sm font-medium text-white">{addon.name}</div>
                    <div className="text-[10px] text-slate-500">{addon.desc}</div>
                  </div>
                </motion.div>
              ))}
            </div>
            <div className="grid grid-cols-4 gap-3">
              {[
                { name: 'Goldilocks', desc: 'Resource recommendations', icon: <GoldilocksIcon size={24} /> },
                { name: 'Kyverno', desc: 'Policy enforcement', icon: <KyvernoIcon size={24} /> },
                { name: 'Node Termination', desc: 'Graceful node shutdown', icon: <NodeTerminationHandlerIcon size={24} /> },
                { name: 'Velero', desc: 'Backup & disaster recovery', icon: <VeleroIcon size={24} /> },
              ].map((addon, idx) => (
                <motion.div
                  key={addon.name}
                  initial={{ opacity: 0, x: -10 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: 1.2 + idx * 0.1 }}
                  className="bg-slate-900/50 rounded-lg p-3 border border-slate-700/30 cursor-pointer hover:border-teal-500/50 hover:bg-slate-900/70 transition-all flex items-center gap-2"
                  title={addon.desc}
                >
                  {addon.icon}
                  <div>
                    <div className="text-sm font-medium text-white">{addon.name}</div>
                    <div className="text-[10px] text-slate-500">{addon.desc}</div>
                  </div>
                </motion.div>
              ))}
            </div>
          </div>
        </Layer>

        {/* Data Services Layer */}
        <Layer
          title="Data Services"
          subtitle="Storage - Metadata - Streaming"
          color="from-teal-900/20 to-slate-900/50"
          delay={0.5}
        >
          <div className="grid grid-cols-4 gap-6">
            <ServiceCard
              icon={<S3Icon size={40} />}
              title="S3 Deep Storage"
              description="Druid segments storage"
              details={[
                'druid/segments/',
                'Lifecycle: 1 day TTL',
                'KMS encryption',
                'Versioning enabled',
              ]}
              delay={0.6}
              size="lg"
              status="active"
              metrics={[
                { label: 'Bucket', value: 'deepstorage' },
                { label: 'Storage Class', value: 'Standard' },
              ]}
              tags={['storage', 'segments', 'encrypted']}
              docsUrl="https://druid.apache.org/docs/latest/dependencies/deep-storage/"
            />
            <ServiceCard
              icon={<S3Icon size={40} />}
              title="S3 Index Logs"
              description="Task indexing logs"
              details={[
                'druid/indexing-logs/',
                '1-day auto-cleanup',
                'Task execution history',
              ]}
              delay={0.7}
              size="lg"
              status="active"
              metrics={[
                { label: 'Bucket', value: 'indexlogs' },
                { label: 'TTL', value: '1 day' },
              ]}
              tags={['logs', 'indexing', 'cleanup']}
            />
            <ServiceCard
              icon={<PostgreSQLIcon size={40} />}
              title="Aurora PostgreSQL"
              description="Druid metadata store"
              details={[
                'Aurora PostgreSQL 17.6',
                'Performance Insights',
                'Multi-AZ deployment',
                'Encrypted storage',
              ]}
              delay={0.8}
              size="lg"
              status="active"
              metrics={[
                { label: 'Engine', value: 'Aurora' },
                { label: 'Version', value: '17.6' },
              ]}
              tags={['database', 'metadata', 'aurora']}
              docsUrl="https://druid.apache.org/docs/latest/dependencies/metadata-storage/"
            />
            <ServiceCard
              icon={<MSKIcon size={40} />}
              title="MSK Serverless"
              description="Kafka streaming ingestion"
              details={[
                'Serverless cluster',
                'IAM authentication',
                'Real-time data ingestion',
                'Supervisor integration',
              ]}
              delay={0.9}
              size="lg"
              status="active"
              metrics={[
                { label: 'Type', value: 'Serverless' },
                { label: 'Auth', value: 'IAM' },
              ]}
              tags={['streaming', 'kafka', 'ingestion']}
              docsUrl="https://druid.apache.org/docs/latest/ingestion/kafka-ingestion/"
            />
          </div>
        </Layer>

        {/* Observability Layer */}
        <Layer
          title="Observability"
          subtitle="Monitoring - Logging - Metrics"
          color="from-pink-900/20 to-slate-900/50"
          delay={0.7}
          className="mt-6"
        >
          <div className="grid grid-cols-3 gap-4">
            <ServiceCard
              icon={<GrafanaIcon size={36} />}
              title="Grafana Alloy"
              description="Telemetry collector"
              details={['OTLP ingestion', 'Prometheus scraping', 'Log forwarding']}
              delay={0.8}
              status="active"
              metrics={[
                { label: 'Samples/sec', value: '5K+' },
                { label: 'Targets', value: '12' },
              ]}
              tags={['otel', 'prometheus', 'collector']}
              docsUrl="https://grafana.com/docs/alloy/latest/"
            />
            <ServiceCard
              icon={<CloudWatchIcon size={36} />}
              title="CloudWatch"
              description="AWS native monitoring"
              details={['Container Insights', 'Log Groups', 'Custom metrics']}
              delay={0.9}
              status="active"
              metrics={[
                { label: 'Dashboards', value: '2' },
                { label: 'Alarms', value: '8' },
              ]}
              tags={['monitoring', 'aws', 'logs']}
              docsUrl="https://docs.aws.amazon.com/cloudwatch/"
            />
            <ServiceCard
              icon={<DruidIcon size={36} />}
              title="Prometheus Emitter"
              description="Druid metrics export"
              details={['JVM metrics', 'Query metrics', 'Ingestion stats', 'Port 9000']}
              delay={1.0}
              status="active"
              metrics={[
                { label: 'Port', value: '9000' },
                { label: 'Interval', value: '1m' },
              ]}
              tags={['prometheus', 'metrics', 'druid']}
            />
          </div>
        </Layer>
      </motion.div>
    </div>
  );
};

const DruidClusterView: React.FC = () => {
  return (
    <div className="space-y-6">
      {/* Druid Cluster Container */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        className="relative border-2 border-dashed border-teal-500/30 rounded-3xl p-8 bg-gradient-to-br from-teal-950/10 to-slate-950"
      >
        <div className="absolute -top-4 left-8 bg-slate-950 px-4 py-1 rounded-full border border-teal-500/30 cursor-pointer hover:border-teal-500/60 hover:bg-slate-900 transition-all">
          <span className="text-teal-400 text-sm font-semibold flex items-center gap-2">
            <span className="w-2 h-2 rounded-full bg-teal-500 animate-pulse"></span>
            Apache Druid Cluster (v35.0.0)
          </span>
        </div>

        <div className="absolute -top-4 right-8 bg-slate-950 px-4 py-1 rounded-full border border-cyan-500/30">
          <span className="text-cyan-400 text-xs font-medium">ZooKeeper-less (K8s Discovery)</span>
        </div>

        {/* Master Nodes */}
        <Layer
          title="Master Nodes"
          subtitle="Cluster coordination and task management"
          color="from-purple-900/20 to-slate-900/50"
          delay={0.1}
          className="mb-6"
        >
          <div className="grid grid-cols-2 gap-6">
            <ServiceCard
              icon={<CoordinatorIcon size={40} />}
              title="Coordinator"
              description="Segment management"
              details={[
                'Segment availability',
                'Load balancing (cost)',
                'Tier management',
                'Compaction scheduling',
              ]}
              delay={0.2}
              size="lg"
              status="active"
              metrics={[
                { label: 'Replicas', value: '1' },
                { label: 'Heap', value: '6-12GB' },
                { label: 'Instance', value: 'm6i/m5' },
              ]}
              tags={['master', 'coordinator', 'statefulset']}
              docsUrl="https://druid.apache.org/docs/latest/design/coordinator/"
            />
            <ServiceCard
              icon={<OverlordIcon size={40} />}
              title="Overlord"
              description="Task orchestration"
              details={[
                'K8s task runner',
                'Ingestion management',
                'Supervisor control',
                '10 concurrent tasks',
              ]}
              delay={0.3}
              size="lg"
              status="active"
              metrics={[
                { label: 'Replicas', value: '1' },
                { label: 'Heap', value: '9GB' },
                { label: 'Capacity', value: '10 tasks' },
              ]}
              tags={['master', 'overlord', 'tasks']}
              docsUrl="https://druid.apache.org/docs/latest/design/overlord/"
            />
          </div>
        </Layer>

        {/* Query Nodes */}
        <Layer
          title="Query Nodes"
          subtitle="Query routing and execution"
          color="from-blue-900/20 to-slate-900/50"
          delay={0.3}
          className="mb-6"
        >
          <div className="grid grid-cols-2 gap-6">
            <ServiceCard
              icon={<BrokerIcon size={40} />}
              title="Broker"
              description="Query coordinator"
              details={[
                'SQL/Avatica support',
                '38 query threads',
                '500MB process buffers',
                'Result caching',
              ]}
              delay={0.4}
              size="lg"
              status="active"
              metrics={[
                { label: 'Replicas', value: '1' },
                { label: 'Heap', value: '8GB' },
                { label: 'Direct Memory', value: '8GB' },
              ]}
              tags={['query', 'broker', 'deployment']}
              docsUrl="https://druid.apache.org/docs/latest/design/broker/"
            />
            <ServiceCard
              icon={<RouterIcon size={40} />}
              title="Router"
              description="API gateway"
              details={[
                'Request routing',
                'Port 9088 (external)',
                'Management proxy',
                'Web console access',
              ]}
              delay={0.5}
              size="lg"
              status="active"
              metrics={[
                { label: 'Replicas', value: '1' },
                { label: 'Heap', value: '512MB' },
                { label: 'Port', value: '9088' },
              ]}
              tags={['query', 'router', 'gateway']}
              docsUrl="https://druid.apache.org/docs/latest/design/router/"
            />
          </div>
        </Layer>

        {/* Data Nodes */}
        <Layer
          title="Data Nodes"
          subtitle="Data storage and processing"
          color="from-green-900/20 to-slate-900/50"
          delay={0.5}
          className="mb-6"
        >
          <div className="grid grid-cols-2 gap-6">
            <ServiceCard
              icon={<HistoricalIcon size={40} />}
              title="Historical"
              description="Segment storage & queries"
              details={[
                '120GB segment cache',
                '12 processing threads',
                'Memory-mapped segments',
                'Tiered storage support',
              ]}
              delay={0.6}
              size="lg"
              status="active"
              metrics={[
                { label: 'Replicas', value: '1' },
                { label: 'Heap', value: '8GB' },
                { label: 'Direct Memory', value: '40GB' },
                { label: 'Cache', value: '120GB' },
              ]}
              tags={['data', 'historical', 'statefulset']}
              docsUrl="https://druid.apache.org/docs/latest/design/historical/"
            />
            <ServiceCard
              icon={<MiddleManagerIcon size={40} />}
              title="MiddleManager Tasks"
              description="K8s job execution"
              details={[
                'Kubernetes task runner',
                '48GB heap per task',
                'Dynamic pod creation',
                'Task templates',
              ]}
              delay={0.7}
              size="lg"
              status="active"
              metrics={[
                { label: 'Type', value: 'K8s Jobs' },
                { label: 'Heap', value: '48GB' },
                { label: 'Direct Memory', value: '8GB' },
              ]}
              tags={['data', 'tasks', 'kubernetes']}
              docsUrl="https://druid.apache.org/docs/latest/design/indexer/"
            />
          </div>
        </Layer>

        {/* Node Pools Configuration */}
        <div className="bg-slate-800/20 rounded-xl p-4 border border-slate-700/20">
          <h4 className="text-sm font-semibold text-white mb-4">Karpenter Node Pools</h4>
          <div className="grid grid-cols-5 gap-3">
            {[
              { name: 'Coordinator', types: 'm6i, m5, m5a', arch: 'amd64', spot: true },
              { name: 'Broker', types: 'm6i, m5, m5n, c6i', arch: 'amd64', spot: true },
              { name: 'Router', types: 't3a, t3, m5', arch: 'amd64', spot: true },
              { name: 'Historical', types: 'i4i, i3, r6i, r5', arch: 'amd64', spot: false },
              { name: 'Tasks', types: 'i4i, i3, c6i, c5, m6i', arch: 'amd64', spot: true },
            ].map((pool, idx) => (
              <motion.div
                key={pool.name}
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.8 + idx * 0.1 }}
                className="bg-slate-900/50 rounded-lg p-3 border border-slate-700/30 cursor-pointer hover:border-teal-500/50 hover:bg-slate-900/70 transition-all"
              >
                <div className="text-sm font-medium text-white mb-1">{pool.name}</div>
                <div className="text-[10px] text-slate-500 mb-2">{pool.types}</div>
                <div className="flex items-center gap-2">
                  <span className={`text-[9px] px-1.5 py-0.5 rounded ${pool.spot ? 'bg-green-500/20 text-green-400' : 'bg-blue-500/20 text-blue-400'}`}>
                    {pool.spot ? 'Spot' : 'On-Demand'}
                  </span>
                  <span className="text-[9px] text-slate-600">{pool.arch}</span>
                </div>
              </motion.div>
            ))}
          </div>
        </div>
      </motion.div>

      {/* Security Configuration */}
      <Layer
        title="Security Configuration"
        subtitle="TLS - Authentication - Authorization"
        color="from-red-900/20 to-slate-900/50"
        delay={0.7}
      >
        <div className="grid grid-cols-4 gap-4">
          <ServiceCard
            icon={<TLSIcon size={36} />}
            title="mTLS Enabled"
            description="Inter-component encryption"
            details={['PKCS12 keystores', 'Auto cert rotation', 'Port 8281-8290', 'cert-manager issued']}
            delay={0.8}
            status="active"
            metrics={[
              { label: 'Protocol', value: 'TLS 1.3' },
              { label: 'Refresh', value: '180s' },
            ]}
            tags={['security', 'tls', 'encryption']}
          />
          <ServiceCard
            icon={<IAMIcon size={36} />}
            title="Basic Auth"
            description="User authentication"
            details={['Admin user', 'druid_system user', 'Secrets Manager creds', 'Authenticator chain']}
            delay={0.9}
            status="active"
            metrics={[
              { label: 'Users', value: '2' },
              { label: 'Escalator', value: 'Enabled' },
            ]}
            tags={['auth', 'basic', 'users']}
          />
          <ServiceCard
            icon={<SecretsManagerIcon size={36} />}
            title="Secrets Manager"
            description="Credential storage"
            details={['Admin credentials', 'System credentials', 'RDS credentials', 'TLS certificates']}
            delay={1.0}
            status="active"
            metrics={[
              { label: 'Secrets', value: '4' },
              { label: 'Rotation', value: 'Manual' },
            ]}
            tags={['secrets', 'aws', 'credentials']}
          />
          <ServiceCard
            icon={<IAMIcon size={36} />}
            title="Pod Identity"
            description="IRSA configuration"
            details={['S3 bucket access', 'MSK cluster access', 'Secrets Manager', 'Fine-grained IAM']}
            delay={1.1}
            status="active"
            metrics={[
              { label: 'Policies', value: '3' },
              { label: 'Type', value: 'IRSA' },
            ]}
            tags={['iam', 'irsa', 'pod-identity']}
          />
        </div>
      </Layer>
    </div>
  );
};

const DataFlowView: React.FC = () => {
  return (
    <div className="space-y-8">
      {/* Query Flow */}
      <Layer
        title="Query Flow"
        subtitle="SQL query execution path"
        color="from-blue-900/20 to-slate-900/50"
      >
        <div className="relative">
          <div className="flex items-center justify-between gap-4">
            <motion.div
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: 0.2 }}
              className="flex flex-col items-center cursor-pointer hover:scale-105 transition-transform"
            >
              <div className="w-16 h-16 rounded-full bg-slate-800 border-2 border-blue-500 flex items-center justify-center mb-2">
                <span className="text-2xl">👤</span>
              </div>
              <span className="text-xs text-slate-400">Client</span>
              <span className="text-[10px] text-slate-600 mt-1">SQL Query</span>
            </motion.div>

            <DataFlowArrow direction="right" label="1. Request" />

            <ServiceCard
              icon={<RouterIcon size={32} />}
              title="Router"
              description="API Gateway"
              details={['Port 9088', 'TLS termination', 'Request routing']}
              delay={0.3}
              size="sm"
              status="active"
              tags={['gateway', 'routing']}
            />

            <DataFlowArrow direction="right" label="2. Route" />

            <ServiceCard
              icon={<BrokerIcon size={32} />}
              title="Broker"
              description="Query coordinator"
              details={['Query planning', 'Scatter/gather', 'Result merging']}
              delay={0.4}
              size="sm"
              status="active"
              tags={['query', 'coordinator']}
            />

            <DataFlowArrow direction="right" label="3. Scatter" />

            <ServiceCard
              icon={<HistoricalIcon size={32} />}
              title="Historical"
              description="Segment scan"
              details={['Local cache', 'Deep storage', 'Parallel scan']}
              delay={0.5}
              size="sm"
              status="active"
              tags={['data', 'scan']}
            />

            <DataFlowArrow direction="right" label="4. Gather" />

            <motion.div
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: 0.6 }}
              className="flex flex-col items-center cursor-pointer hover:scale-105 transition-transform"
            >
              <div className="w-16 h-16 rounded-full bg-slate-800 border-2 border-green-500 flex items-center justify-center mb-2">
                <span className="text-2xl">📊</span>
              </div>
              <span className="text-xs text-slate-400">Results</span>
              <span className="text-[10px] text-slate-600 mt-1">JSON/CSV</span>
            </motion.div>
          </div>

          {/* S3 Deep Storage */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.7 }}
            className="mt-8 flex justify-center"
          >
            <div className="flex items-center gap-8 bg-slate-800/30 rounded-xl p-4 border border-slate-700/20">
              <ServiceCard
                icon={<S3Icon size={32} />}
                title="S3 Deep Storage"
                description="Segment retrieval"
                details={['Cache miss fallback', 'druid/segments/', 'Lazy loading']}
                delay={0.8}
                size="sm"
                status="active"
                tags={['storage', 's3']}
              />
              <div className="text-slate-500 text-xs cursor-help" title="On cache miss">↑ Fetch segments</div>
            </div>
          </motion.div>
        </div>
      </Layer>

      {/* Real-time Ingestion Flow */}
      <Layer
        title="Real-time Ingestion Flow"
        subtitle="Kafka streaming data pipeline"
        color="from-green-900/20 to-slate-900/50"
        delay={0.3}
      >
        <div className="flex items-center justify-between gap-4">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.4 }}
            className="flex flex-col items-center cursor-pointer hover:scale-105 transition-transform"
          >
            <div className="w-16 h-16 rounded-full bg-slate-800 border-2 border-orange-500 flex items-center justify-center mb-2">
              <span className="text-2xl">📡</span>
            </div>
            <span className="text-xs text-slate-400">Data Source</span>
            <span className="text-[10px] text-slate-600 mt-1">Events</span>
          </motion.div>

          <DataFlowArrow direction="right" label="1. Produce" />

          <ServiceCard
            icon={<MSKIcon size={32} />}
            title="MSK Kafka"
            description="Message broker"
            details={['Serverless', 'IAM auth', 'Topic partitions']}
            delay={0.5}
            size="sm"
            status="active"
            tags={['kafka', 'streaming']}
          />

          <DataFlowArrow direction="right" label="2. Consume" />

          <ServiceCard
            icon={<OverlordIcon size={32} />}
            title="Overlord"
            description="Supervisor control"
            details={['Kafka supervisor', 'Task assignment', 'Offset tracking']}
            delay={0.6}
            size="sm"
            status="active"
            tags={['supervisor', 'control']}
          />

          <DataFlowArrow direction="right" label="3. Assign" />

          <ServiceCard
            icon={<MiddleManagerIcon size={32} />}
            title="K8s Tasks"
            description="Ingestion workers"
            details={['Real-time indexing', 'Segment creation', 'Handoff']}
            delay={0.7}
            size="sm"
            status="active"
            tags={['indexing', 'tasks']}
          />

          <DataFlowArrow direction="right" label="4. Publish" />

          <ServiceCard
            icon={<S3Icon size={32} />}
            title="Deep Storage"
            description="Segment persistence"
            details={['S3 upload', 'Metadata update', 'Compaction ready']}
            delay={0.8}
            size="sm"
            status="active"
            tags={['storage', 'persist']}
          />

          <DataFlowArrow direction="right" label="5. Load" />

          <ServiceCard
            icon={<HistoricalIcon size={32} />}
            title="Historical"
            description="Segment serving"
            details={['Segment load', 'Cache population', 'Query ready']}
            delay={0.9}
            size="sm"
            status="active"
            tags={['load', 'serve']}
          />
        </div>

        {/* Coordinator & Metadata */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 1.0 }}
          className="mt-8 flex justify-center gap-8"
        >
          <div className="flex items-center gap-4 bg-slate-800/30 rounded-xl p-4 border border-slate-700/20">
            <ServiceCard
              icon={<CoordinatorIcon size={32} />}
              title="Coordinator"
              description="Segment assignment"
              details={['Load balancing', 'Tier rules', 'Replication']}
              delay={1.1}
              size="sm"
              status="active"
              tags={['coordination', 'assignment']}
            />
            <div className="text-slate-500 text-xs">↔</div>
            <ServiceCard
              icon={<PostgreSQLIcon size={32} />}
              title="Metadata Store"
              description="Segment registry"
              details={['Segment metadata', 'Rules storage', 'Config']}
              delay={1.2}
              size="sm"
              status="active"
              tags={['metadata', 'postgresql']}
            />
          </div>
        </motion.div>
      </Layer>

      {/* Batch Ingestion Flow */}
      <Layer
        title="Batch Ingestion Flow"
        subtitle="S3/File-based data loading"
        color="from-purple-900/20 to-slate-900/50"
        delay={0.5}
      >
        <div className="flex items-center justify-between gap-4">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.6 }}
            className="flex flex-col items-center cursor-pointer hover:scale-105 transition-transform"
          >
            <div className="w-16 h-16 rounded-full bg-slate-800 border-2 border-purple-500 flex items-center justify-center mb-2">
              <span className="text-2xl">📁</span>
            </div>
            <span className="text-xs text-slate-400">Source Files</span>
            <span className="text-[10px] text-slate-600 mt-1">S3/HDFS</span>
          </motion.div>

          <DataFlowArrow direction="right" label="1. Spec Submit" />

          <ServiceCard
            icon={<OverlordIcon size={32} />}
            title="Overlord"
            description="Job orchestration"
            details={['Task creation', 'Parallelism', 'Progress tracking']}
            delay={0.7}
            size="sm"
            status="active"
            tags={['batch', 'orchestration']}
          />

          <DataFlowArrow direction="right" label="2. K8s Jobs" />

          <ServiceCard
            icon={<MiddleManagerIcon size={32} />}
            title="Index Tasks"
            description="Parallel processing"
            details={['MSQ engine', 'Partition pruning', 'Segment generation']}
            delay={0.8}
            size="sm"
            status="active"
            tags={['indexing', 'msq']}
          />

          <DataFlowArrow direction="right" label="3. Segments" />

          <ServiceCard
            icon={<S3Icon size={32} />}
            title="MSQ Storage"
            description="Intermediate results"
            details={['Shuffle data', 'Sort merge', 'Final output']}
            delay={0.9}
            size="sm"
            status="active"
            tags={['msq', 'intermediate']}
          />

          <DataFlowArrow direction="right" label="4. Publish" />

          <ServiceCard
            icon={<S3Icon size={32} />}
            title="Deep Storage"
            description="Final segments"
            details={['Segment upload', 'Metadata commit', 'Availability']}
            delay={1.0}
            size="sm"
            status="active"
            tags={['storage', 'final']}
          />
        </div>
      </Layer>
    </div>
  );
};

const DeploymentView: React.FC = () => {
  return (
    <div className="space-y-6">
      {/* CDK Stack Hierarchy */}
      <Layer
        title="CDK Stack Architecture"
        subtitle="Infrastructure as Code - Java CDK"
        color="from-yellow-900/20 to-slate-900/50"
      >
        <div className="relative">
          {/* Main Stack */}
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            className="bg-slate-800/50 rounded-xl p-6 border border-yellow-500/30 mb-6"
          >
            <div className="flex items-center gap-3 mb-4">
              <CDKIcon size={40} />
              <div>
                <h3 className="text-white font-bold">DruidStack</h3>
                <p className="text-slate-400 text-xs">Main CDK Stack - Orchestrates all nested stacks</p>
              </div>
            </div>

            {/* Nested Stacks Grid */}
            <div className="grid grid-cols-3 gap-4">
              {[
                { name: 'NetworkNestedStack', desc: 'VPC, Subnets, NAT', icon: <VPCIcon size={28} />, dep: 'None' },
                { name: 'EksNestedStack', desc: 'EKS Cluster', icon: <EKSIcon size={28} />, dep: 'Network' },
                { name: 'AddonsNestedStack', desc: 'Core K8s Addons', icon: <HelmIcon size={28} />, dep: 'EKS' },
                { name: 'ObservabilityAddonsStack', desc: 'Grafana Alloy', icon: <GrafanaIcon size={28} />, dep: 'EKS' },
                { name: 'DruidSetupNestedStack', desc: 'RDS, S3, MSK, IAM', icon: <PostgreSQLIcon size={28} />, dep: 'EKS + Addons' },
                { name: 'DruidNestedStack', desc: 'Helm Chart Deploy', icon: <DruidIcon size={28} />, dep: 'Setup' },
              ].map((stack, idx) => (
                <motion.div
                  key={stack.name}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: 0.3 + idx * 0.05 }}
                  className="bg-slate-900/70 rounded-lg p-3 border border-slate-700/50 hover:border-yellow-500/50 transition-colors cursor-pointer group"
                >
                  <div className="flex items-center gap-2 mb-2">
                    {stack.icon}
                    <span className="text-xs font-medium text-white truncate group-hover:text-yellow-400 transition-colors">{stack.name.replace('NestedStack', '')}</span>
                  </div>
                  <p className="text-[10px] text-slate-500">{stack.desc}</p>
                  <div className="mt-2 flex items-center gap-1">
                    <span className="text-[9px] text-slate-600">Depends:</span>
                    <span className="text-[9px] text-teal-400">{stack.dep}</span>
                  </div>
                </motion.div>
              ))}
            </div>
          </motion.div>

          {/* Deployment Order */}
          <div className="bg-slate-800/30 rounded-xl p-4 border border-slate-700/20">
            <h4 className="text-sm font-semibold text-white mb-3">Deployment Order</h4>
            <div className="flex items-center justify-between text-xs">
              {['Network', 'EKS', 'Addons', 'Observability', 'Druid Setup', 'Druid'].map((step, idx) => (
                <React.Fragment key={step}>
                  <motion.div
                    initial={{ opacity: 0, scale: 0.8 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ delay: 0.8 + idx * 0.1 }}
                    className="flex flex-col items-center cursor-pointer hover:scale-110 transition-transform"
                    title={`Step ${idx + 1}: Deploy ${step}`}
                  >
                    <span className="w-8 h-8 rounded-full bg-teal-600 flex items-center justify-center text-white font-bold mb-1 hover:bg-teal-500 transition-colors">
                      {idx + 1}
                    </span>
                    <span className="text-slate-400 text-center">{step}</span>
                  </motion.div>
                  {idx < 5 && (
                    <motion.div
                      initial={{ scaleX: 0 }}
                      animate={{ scaleX: 1 }}
                      transition={{ delay: 0.9 + idx * 0.1 }}
                      className="flex-1 h-0.5 bg-gradient-to-r from-teal-500 to-teal-400"
                    />
                  )}
                </React.Fragment>
              ))}
            </div>
          </div>
        </div>
      </Layer>

      {/* Helm Chart Deployment */}
      <Layer
        title="Helm Chart Deployment"
        subtitle="Druid Helm Chart v35.0.0"
        color="from-blue-900/20 to-slate-900/50"
        delay={0.3}
      >
        <div className="flex items-center justify-between gap-6">
          <ServiceCard
            icon={<CDKIcon size={36} />}
            title="CDK Values"
            description="Mustache templating"
            details={['Dynamic configuration', 'Context injection', 'Environment-specific']}
            delay={0.4}
            status="active"
            metrics={[
              { label: 'Templates', value: '15+' },
              { label: 'Lines', value: '733' },
            ]}
            tags={['cdk', 'mustache', 'config']}
          />

          <DataFlowArrow direction="right" label="Generate" />

          <ServiceCard
            icon={<HelmIcon size={36} />}
            title="Helm Values"
            description="values.yaml"
            details={['Component configs', 'Resource limits', 'Node selectors']}
            delay={0.5}
            status="active"
            tags={['helm', 'values', 'yaml']}
          />

          <DataFlowArrow direction="right" label="Template" />

          <ServiceCard
            icon={<KubernetesIcon size={36} />}
            title="K8s Resources"
            description="Manifests"
            details={['StatefulSets', 'Deployments', 'ConfigMaps', 'Services']}
            delay={0.6}
            status="active"
            metrics={[
              { label: 'Resources', value: '20+' },
              { label: 'Components', value: '6' },
            ]}
            tags={['kubernetes', 'manifests']}
          />

          <DataFlowArrow direction="right" label="Apply" />

          <ServiceCard
            icon={<EKSIcon size={36} />}
            title="EKS Cluster"
            description="Running pods"
            details={['Rolling updates', 'Health checks', 'Auto-scaling']}
            delay={0.7}
            status="active"
            metrics={[
              { label: 'Pods', value: '6+' },
              { label: 'Services', value: '6' },
            ]}
            tags={['eks', 'deployment', 'running']}
          />
        </div>

        {/* Helm Chart Components */}
        <div className="mt-6 bg-slate-800/20 rounded-xl p-4 border border-slate-700/20">
          <h4 className="text-sm font-semibold text-white mb-3">Chart Components</h4>
          <div className="grid grid-cols-6 gap-3">
            {[
              { name: 'coordinator', type: 'StatefulSet', replicas: 1 },
              { name: 'broker', type: 'Deployment', replicas: 1 },
              { name: 'historical', type: 'StatefulSet', replicas: 1 },
              { name: 'overlord', type: 'StatefulSet', replicas: 1 },
              { name: 'router', type: 'Deployment', replicas: 1 },
              { name: 'task', type: 'PodTemplate', replicas: 'N/A' },
            ].map((comp, idx) => (
              <motion.div
                key={comp.name}
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.8 + idx * 0.05 }}
                className="bg-slate-900/50 rounded-lg p-3 border border-slate-700/30 cursor-pointer hover:border-blue-500/50 transition-all"
              >
                <div className="text-sm font-medium text-white capitalize">{comp.name}</div>
                <div className="text-[10px] text-slate-500">{comp.type}</div>
                <div className="text-[10px] text-teal-400 mt-1">Replicas: {comp.replicas}</div>
              </motion.div>
            ))}
          </div>
        </div>
      </Layer>

      {/* Druid Extensions */}
      <Layer
        title="Druid Extensions"
        subtitle="Loaded extensions and capabilities"
        color="from-teal-900/20 to-slate-900/50"
        delay={0.5}
      >
        <div className="grid grid-cols-4 gap-4">
          {[
            { name: 'druid-s3-extensions', desc: 'S3 deep storage support', category: 'Storage' },
            { name: 'druid-kafka-indexing-service', desc: 'Kafka streaming ingestion', category: 'Ingestion' },
            { name: 'druid-kubernetes-extensions', desc: 'K8s service discovery', category: 'Discovery' },
            { name: 'druid-kubernetes-overlord-extensions', desc: 'K8s task runner', category: 'Tasks' },
            { name: 'postgresql-metadata-storage', desc: 'PostgreSQL metadata', category: 'Metadata' },
            { name: 'druid-aws-rds-extensions', desc: 'RDS IAM auth', category: 'AWS' },
            { name: 'druid-basic-security', desc: 'Basic auth & authz', category: 'Security' },
            { name: 'prometheus-emitter', desc: 'Prometheus metrics', category: 'Monitoring' },
          ].map((ext, idx) => (
            <motion.div
              key={ext.name}
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: 0.6 + idx * 0.05 }}
              className="bg-slate-800/50 rounded-lg p-3 border border-slate-700/30 cursor-pointer hover:border-teal-500/50 hover:bg-slate-800/70 transition-all"
            >
              <div className="flex items-center justify-between mb-1">
                <span className="text-[9px] px-1.5 py-0.5 rounded bg-teal-500/20 text-teal-400">{ext.category}</span>
              </div>
              <div className="text-xs font-medium text-white">{ext.name}</div>
              <div className="text-[10px] text-slate-500 mt-1">{ext.desc}</div>
            </motion.div>
          ))}
        </div>
      </Layer>

      {/* Docker Image */}
      <Layer
        title="Custom Docker Image"
        subtitle="Apache Druid 35.0.0 with AWS extensions"
        color="from-cyan-900/20 to-slate-900/50"
        delay={0.7}
      >
        <div className="flex items-center justify-between gap-6">
          <ServiceCard
            icon={<DruidIcon size={36} />}
            title="Base Image"
            description="apache/druid:35.0.0"
            details={['Official Apache image', 'Java 21 runtime', 'All core extensions']}
            delay={0.8}
            status="active"
            tags={['base', 'apache', 'druid']}
          />

          <DataFlowArrow direction="right" label="+ AWS" />

          <ServiceCard
            icon={<MSKIcon size={36} />}
            title="MSK IAM Auth"
            description="aws-msk-iam-auth-2.3.2"
            details={['IAM authentication', 'Kafka extensions', 'Security integration']}
            delay={0.9}
            status="active"
            tags={['msk', 'iam', 'kafka']}
          />

          <DataFlowArrow direction="right" label="Build" />

          <ServiceCard
            icon={<EKSIcon size={36} />}
            title="ECR Repository"
            description="CDK DockerImageAsset"
            details={['Auto push to ECR', 'Asset hash versioning', 'Region deployment']}
            delay={1.0}
            status="active"
            metrics={[
              { label: 'Registry', value: 'ECR' },
              { label: 'Auto-push', value: 'Yes' },
            ]}
            tags={['ecr', 'docker', 'cdk']}
          />
        </div>
      </Layer>
    </div>
  );
};

export default DruidArchitecture;
