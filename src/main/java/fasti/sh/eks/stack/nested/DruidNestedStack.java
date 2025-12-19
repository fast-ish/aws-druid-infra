package fasti.sh.eks.stack.nested;

import fasti.sh.eks.stack.model.DruidConf;
import fasti.sh.eks.stack.model.druid.Access;
import fasti.sh.eks.stack.model.druid.Ingestion;
import fasti.sh.execute.aws.ecr.DockerImageConstruct;
import fasti.sh.execute.aws.eks.PodIdentityConstruct;
import fasti.sh.execute.util.TemplateUtils;
import fasti.sh.model.main.Common;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.NestedStackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.eks.HelmChart;
import software.amazon.awscdk.services.eks.ICluster;
import software.amazon.awscdk.services.s3.assets.Asset;
import software.amazon.awscdk.services.s3.assets.AssetProps;
import software.constructs.Construct;

/**
 * Nested stack for Apache Druid Helm chart deployment.
 *
 * <p>
 * Deploys the Druid cluster via Helm after setup resources are provisioned. Dependencies are managed through {@link DruidSetupNestedStack}
 * which creates databases and storage. Pod Identities for Druid pods and MSK clients are created in this stack.
 */
@Getter
public class DruidNestedStack extends NestedStack {
  private final DruidSetupNestedStack setupStack;
  private final PodIdentityConstruct druidPodIdentity;
  private final List<PodIdentityConstruct> mskClientPodIdentities;
  private final DockerImageConstruct dockerImage;
  private final HelmChart chart;

  public DruidNestedStack(Construct scope, Common common, DruidConf conf, Vpc vpc, ICluster cluster, NestedStackProps props) {
    super(scope, "druid", props);

    this.setupStack = new DruidSetupNestedStack(this, common, conf, vpc, cluster);

    // Parse configurations
    var replace = Map.<String, Object>of("deployment:eks:druid:release", conf.chart().release());
    var accessConf = TemplateUtils.parseAs(scope, conf.access(), replace, Access.class);
    var ingestionConf = TemplateUtils.parseAs(scope, conf.ingestion(), replace, Ingestion.class);

    // Create Pod Identity for Druid pods
    this.druidPodIdentity = new PodIdentityConstruct(this, common, accessConf.podIdentity(), cluster);

    // Create Pod Identities for MSK clients
    this.mskClientPodIdentities = ingestionConf
      .kafka()
      .clients()
      .stream()
      .map(client -> new PodIdentityConstruct(this, common, client.podIdentity(), cluster))
      .toList();

    this.dockerImage = new DockerImageConstruct(this, common, conf.dockerImage());

    // Prepare template mappings for Helm values
    var templateMappings = new HashMap<String, Object>();
    templateMappings.put("deployment:eks:druid:release", conf.chart().release());
    templateMappings.put("image:uri", this.dockerImage.imageUri());
    templateMappings.put("druid.role.arn", this.druidPodIdentity.roleConstruct().role().getRoleArn());

    var values = TemplateUtils.parseAsMap(scope, conf.chart().values(), templateMappings);

    this.chart = HelmChart.Builder
      .create(this, conf.chart().name())
      .cluster(cluster)
      .createNamespace(true)
      .chartAsset(new Asset(this, conf.chart().name() + "-asset", AssetProps.builder().path(conf.asset()).build()))
      .namespace(conf.chart().namespace())
      .release(conf.chart().release())
      .values(values)
      .build();

    this.chart().getNode().addDependency(this.setupStack());
  }
}
