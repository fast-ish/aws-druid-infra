# aws-druid-infra

<div align="center">

*fastish aws cdk application written in java that provisions an apache druid deployment on an amazon eks (elastic kubernetes
service) cluster with managed addons, custom helm charts, observability integration, and node groups.*

[![license: mit](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![java](https://img.shields.io/badge/Java-21%2B-blue.svg)](https://www.oracle.com/java/)
[![aws cdk](https://img.shields.io/badge/AWS%20CDK-latest-orange.svg)](https://aws.amazon.com/cdk/)
[![vpc](https://img.shields.io/badge/Amazon-VPC-ff9900.svg)](https://aws.amazon.com/vpc/)
[![eks](https://img.shields.io/badge/Amazon-EKS-ff9900.svg)](https://aws.amazon.com/eks/)
[![apache druid](https://img.shields.io/badge/Apache-Druid-008080.svg)](https://druid.apache.org/)
[![opentelemetry](https://img.shields.io/badge/OpenTelemetry-Enabled-blueviolet.svg)](https://opentelemetry.io/)
[![grafana](https://img.shields.io/badge/Grafana-Observability-F46800.svg)](https://grafana.com/)

</div>

## overview

+ eks cluster with rbac configuration
+ aws managed eks addons (vpc cni, ebs csi driver, coredns, kube proxy, pod identity agent, cloudwatch container
  insights)
+ helm chart-based addons (cert-manager, aws load balancer controller, karpenter, csi secrets store)
+ grafana cloud observability integration
+ managed node groups with bottlerocket ami's
+ sqs queue for node interruption handling
+ apache druid helm chart deployment integrated with aws resources:
    + rds database for metadata storage
    + s3 bucket for deep storage
    + s3 bucket for multi-stage query ingestion
    + kafka (msk) for real-time ingestion

## architecture

the druid infrastructure uses a layered architecture with nested stacks:

```
DeploymentStack (main)
├── VpcNestedStack          (network setup)
├── EksNestedStack          (cluster + managed addons)
├── DruidSetupNestedStack   (rds, s3, msk resources) [depends on vpc]
└── DruidNestedStack        (helm chart deployment)  [depends on eks, setup]
```

**dependency chain**:
1. vpc is created first (foundation)
2. eks cluster is provisioned (independent of druid setup)
3. druid setup creates supporting resources (rds, s3, msk) that depend on vpc
4. druid helm chart is deployed after both eks and setup are ready

## prerequisites

+ [java 21+](https://sdkman.io/)
+ [maven](https://maven.apache.org/download.cgi)
+ [aws cli](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
+ [aws cdk cli](https://docs.aws.amazon.com/cdk/v2/guide/getting-started.html)
+ [github cli](https://cli.github.com/)
+ [grafana cloud account](https://grafana.com/products/cloud/)
+ prepare aws environment by running `cdk bootstrap` with the appropriate aws account and region:

  ```bash
  cdk bootstrap aws://<account-id>/<region>
  ```

    + replace `<account-id>` with your aws account id and `<region>` with your desired aws region (e.g., `us-west-2`).
    + this command sets up the necessary resources for deploying cdk applications, such as an S3 bucket for storing
      assets and a CloudFormation execution role
    + for more information, see the aws cdk documentation:
        + https://docs.aws.amazon.com/cdk/v2/guide/bootstrapping.html
        + https://docs.aws.amazon.com/cdk/v2/guide/ref-cli-cmd-bootstrap.html

## deployment

### step 1: clone repositories

```bash
gh repo clone fast-ish/cdk-common
gh repo clone fast-ish/aws-druid-infra
```

### step 2: build projects

```bash
mvn -f cdk-common/pom.xml clean install
mvn -f aws-druid-infra/pom.xml clean install
```

### step 3: prepare apache druid artifacts (optional)

#### docker image

create an ecr repository and push the druid docker image:

```bash
aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com

aws ecr create-repository \
  --repository-name fasti.sh/v1/docker/druid \
  --region <region> \
  --image-scanning-configuration scanOnPush=true

docker buildx build --provenance=false --platform linux/amd64 -f Dockerfile.druid \
  -t <account-id>.dkr.ecr.<region>.amazonaws.com/fasti.sh/v1/docker/druid:$(date +'%Y%m%d') \
  -t <account-id>.dkr.ecr.<region>.amazonaws.com/fasti.sh/v1/docker/druid:v1 \
  -t <account-id>.dkr.ecr.<region>.amazonaws.com/fasti.sh/v1/docker/druid:latest \
  --push .
```

#### helm chart

create an ecr repository and push the druid helm chart:

```bash
aws ecr get-login-password --region <region> | helm registry login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com

aws ecr create-repository \
  --repository-name fasti.sh/v1/helm/druid \
  --region <region> \
  --image-scanning-configuration scanOnPush=true \
  --encryption-configuration encryptionType=AES256

helm package ./helm/chart/druid
helm push druid-<version>.tgz oci://<account-id>.dkr.ecr.<region>.amazonaws.com/fasti.sh/v1/helm
```

#### update artifact references

update the docker image reference in `aws-druid-infra/src/main/resources/prototype/v1/druid/values.mustache`:

| parameter          | description                            | example                                                                 |
|--------------------|----------------------------------------|-------------------------------------------------------------------------|
| `image.repository` | ecr repository for druid docker image  | `000000000000.dkr.ecr.us-west-2.amazonaws.com/fasti.sh/v1/docker/druid` |
| `image.tag`        | tag of the druid docker image          | `v1`, `latest`, or date tag like `20231001`                             |
| `image.pullPolicy` | pull policy for the druid docker image | `IfNotPresent`                                                          |

update the helm chart reference in `aws-druid-infra/src/main/resources/prototype/v1/conf.mustache`:

| parameter          | description                         | example                                                               |
|--------------------|-------------------------------------|-----------------------------------------------------------------------|
| `chart.repository` | ecr repository for druid helm chart | `oci://000000000000.dkr.ecr.us-west-2.amazonaws.com/fasti.sh/v1/helm` |
| `chart.name`       | name of the druid helm chart        | `druid`                                                               |
| `chart.version`    | version of the druid helm chart     | `34.0.0`                                                              |

### step 4: configure deployment

create `aws-druid-infra/cdk.context.json` from `aws-druid-infra/cdk.context.template.json`:

**required configuration parameters**:

| parameter      | description                      | example        |
|----------------|----------------------------------|----------------|
| `:account`     | aws account id (12-digit number) | `123456789012` |
| `:region`      | aws region for deployment        | `us-west-2`    |
| `:domain`      | registered domain name for ses   | `fasti.sh`     |
| `:environment` | environment name (do not change) | `prototype`    |
| `:version`     | resource version identifier      | `v1`           |

**notes**:
+ `:environment` and `:version` map to resource files at `aws-druid-infra/src/main/resources/prototype/v1`
+ these values are used to locate configuration templates for the deployment

#### grafana cloud configuration

configure grafana cloud integration in `cdk.context.json`:

```json
{
  "hosted:eks:grafana:instanceId":"000000",
  "hosted:eks:grafana:key": "glc_xyz",
  "hosted:eks:grafana:lokiHost": "https://logs-prod-000.grafana.net",
  "hosted:eks:grafana:lokiUsername": "000000",
  "hosted:eks:grafana:prometheusHost": "https://prometheus-prod-000-prod-us-west-0.grafana.net",
  "hosted:eks:grafana:prometheusUsername":"0000000",
  "hosted:eks:grafana:tempoHost": "https://tempo-prod-000-prod-us-west-0.grafana.net/tempo",
  "hosted:eks:grafana:tempoUsername": "000000",
  "hosted:eks:grafana:pyroscopeHost": "https://profiles-prod-000.grafana.net:443"
}
```

**grafana cloud setup guide**:

1. **create grafana cloud account**:
    - sign up at https://grafana.com/
    - create a new stack
    - navigate to your stack settings

2. **retrieve configuration values**:

| parameter            | location                            | description                                                                                      |
|----------------------|-------------------------------------|--------------------------------------------------------------------------------------------------|
| `instanceId`         | stack details page                  | unique identifier for your grafana instance                                                      |
| `key`                | api keys section                    | api key with metrics, logs, traces, profiles, alerts, and rules permissions (starts with `glc_`) |
| `lokiHost`           | logs > data sources > loki          | endpoint url for sending logs                                                                    |
| `lokiUsername`       | logs > data sources > loki          | account identifier for loki                                                                      |
| `prometheusHost`     | metrics > data sources > prometheus | endpoint url for sending metrics                                                                 |
| `prometheusUsername` | metrics > data sources > prometheus | account identifier for prometheus                                                                |
| `tempoHost`          | traces > data sources > tempo       | endpoint url for sending traces                                                                  |
| `tempoUsername`      | traces > data sources > tempo       | account identifier for tempo                                                                     |
| `pyroscopeHost`      | profiles > connect a data source    | endpoint url for continuous profiling                                                            |

**required api key permissions**:
- `metrics`: read and write access (for prometheus metrics ingestion)
- `logs`: read and write access (for loki log ingestion)
- `traces`: read and write access (for tempo trace ingestion)
- `profiles`: read and write access (for pyroscope profiling data ingestion)
- `alerts`: read and write access (for alerting configuration)
- `rules`: read and write access (for recording and alerting rules)

note: while grafana recommends using separate keys with minimal permissions for security, this deployment requires write access to multiple services for the k8s-monitoring helm chart to function properly.

these values are used by the grafana kubernetes monitoring helm chart (k8s-monitoring) to configure the grafana agent properly for sending metrics, logs, and traces to your grafana cloud instance.

#### cluster access configuration

configure iam role mappings in `cdk.context.json`:

```json
{
  "hosted:eks:administrators": [
    {
      "username": "administrator",
      "role": "arn:aws:iam::000000000000:role/AWSReservedSSO_AdministratorAccess_abc",
      "email": "admin@example.com"
    }
  ],
  "hosted:eks:users": [
    {
      "username": "user",
      "role": "arn:aws:iam::000000000000:role/AWSReservedSSO_DeveloperAccess_abc",
      "email": "user@example.com"
    }
  ]
}
```

| parameter        | description                                                                |
|------------------|----------------------------------------------------------------------------|
| `administrators` | iam roles that will have full admin access to the cluster                  |
| `users`          | iam roles that will have read-only access to the cluster                   |
| `username`       | identifier for the user in kubernetes rbac                                 |
| `role`           | aws iam role arn (typically from aws sso) mapped to kubernetes permissions |
| `email`          | for identification and traceability purposes                               |

### step 5: deploy infrastructure

```bash
cd aws-druid-infra

# preview changes
cdk synth

# deploy all stacks
cdk deploy
```

**what gets deployed**:
+ 1 main cloudformation stack
+ 4 nested cloudformation stacks (vpc, eks, druid setup, druid)
+ vpc with 2 azs, public/private subnets
+ eks cluster with managed addons
+ rds database for druid metadata
+ s3 buckets for deep storage and ingestion
+ msk cluster for real-time ingestion
+ druid helm chart with all components

### step 6: access cluster

```bash
aws eks update-kubeconfig --name {{hosted:id}}-eks --region <region>

kubectl get nodes
kubectl get pods -A
```

## license

[mit license](LICENSE)

for your convenience, you can find the full mit license text at

+ [https://opensource.org/license/mit/](https://opensource.org/license/mit/) (official osi website)
+ [https://choosealicense.com/licenses/mit/](https://choosealicense.com/licenses/mit/) (choose a license website)
