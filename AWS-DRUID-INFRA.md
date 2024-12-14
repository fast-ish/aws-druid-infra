# aws-druid-infra

Apache Druid analytics platform on AWS EKS. Java 21, AWS CDK 2.219.0.

**Depends on**: `cdk-common` (install first with `mvn install -DskipTests`)

## Architecture

```
DruidStack (main)
  ├── NetworkNestedStack        # VPC, subnets, NAT
  ├── EksNestedStack            # EKS cluster, managed addons, node groups
  ├── CoreAddonsNestedStack     # cert-manager, karpenter, external-dns, etc.
  ├── ObservabilityAddonsStack  # grafana k8s-monitoring
  └── DruidNestedStack          # Druid Helm chart + RDS + S3 + MSK
```

**No Backstage, no Argo** - EKS + Druid infrastructure only.

## Directory Layout

```
src/main/java/fasti/sh/eks/
  Launch.java                 # CDK app entry point
  stack/
    DruidStack.java           # Main orchestration stack
    DruidReleaseConf.java     # Configuration record
    model/                    # Druid-specific config models
      DruidConf.java
      SystemConf.java

src/main/resources/production/v1/
  conf.mustache               # Main config
  eks/addons.mustache         # EKS addon configs
  druid/
    values.mustache           # Druid Helm values
    setup/*.mustache          # Storage, ingestion, access configs
  helm/*.mustache             # Addon Helm values
  policy/*.mustache           # IAM policies
```

## Druid Components

| Component | Purpose |
|-----------|---------|
| RDS PostgreSQL | Metadata storage |
| S3 Deep Storage | Segment storage |
| S3 Ingestion Bucket | Multi-stage query temp storage |
| MSK (Kafka) | Real-time ingestion |

## Key Addons

| Addon | Purpose |
|-------|---------|
| cert-manager | TLS certificates |
| karpenter | Node autoscaling with NodePools |
| external-dns | Route53 DNS records |
| external-secrets | Secrets Manager operator |
| grafana k8s-monitoring | Observability |

## Commands

```bash
mvn compile -q                 # Compile
mvn spotless:apply             # Format code
cdk synth                      # Synthesize CloudFormation
cdk deploy                     # Deploy to AWS
```

## Key Files

- `stack/DruidStack.java` - Main stack orchestration
- `stack/model/DruidConf.java` - Druid configuration record
- `resources/production/v1/druid/values.mustache` - Druid Helm values
- `resources/production/v1/druid/setup/*.mustache` - AWS resources for Druid
- `resources/production/v1/eks/addons.mustache` - EKS addon config

## Configuration

Edit `cdk.context.json`:
- AWS account/region
- Domain name
- Druid release version
- Administrator IAM roles
- Grafana Cloud secret ARN

## Druid Access

```bash
# Port-forward to Druid router
kubectl port-forward -n druid svc/druid-router 8888:8888

# Access console
open http://localhost:8888
```

## Don't

- Deploy without installing cdk-common first
- Add Argo or Backstage here - use aws-idp-infra
- Forget RDS/S3/MSK when modifying Druid setup
