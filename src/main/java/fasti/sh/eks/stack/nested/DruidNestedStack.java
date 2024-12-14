package fasti.sh.eks.stack.nested;

import com.fasterxml.jackson.core.type.TypeReference;
import fasti.sh.eks.stack.model.DruidConf;
import fasti.sh.execute.aws.ecr.DockerImageConstruct;
import fasti.sh.execute.serialization.Mapper;
import fasti.sh.execute.serialization.Template;
import fasti.sh.model.main.Common;
import java.util.Map;
import lombok.Getter;
import lombok.SneakyThrows;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.NestedStackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.eks.HelmChart;
import software.amazon.awscdk.services.eks.ICluster;
import software.amazon.awscdk.services.s3.assets.Asset;
import software.amazon.awscdk.services.s3.assets.AssetProps;
import software.constructs.Construct;

@Getter
public class DruidNestedStack extends NestedStack {
  private final DruidSetupNestedStack setupStack;
  private final DockerImageConstruct dockerImage;
  private final HelmChart chart;

  @SneakyThrows
  public DruidNestedStack(Construct scope, Common common, DruidConf conf, Vpc vpc, ICluster cluster, NestedStackProps props) {
    super(scope, "druid", props);

    this.setupStack = new DruidSetupNestedStack(this, common, conf, vpc, cluster);
    this.dockerImage = new DockerImageConstruct(this, common, conf.dockerImage());

    var replace = Map.<String, Object>of(
      "deployment:eks:druid:release", conf.chart().release(),
      "image:uri", this.dockerImage.imageUri());
    var yaml = Template.parse(scope, conf.chart().values(), replace);
    var values = Mapper.get().readValue(yaml, new TypeReference<Map<String, Object>>() {});

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
