package fasti.sh.eks.stack.nested;

import com.fasterxml.jackson.core.type.TypeReference;
import fasti.sh.eks.stack.model.DruidConf;
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
import software.constructs.Construct;

@Getter
public class DruidNestedStack extends NestedStack {
  private final DruidSetupNestedStack setupStack;
  private final HelmChart chart;

  @SneakyThrows
  public DruidNestedStack(Construct scope, Common common, DruidConf conf, Vpc vpc, ICluster cluster, NestedStackProps props) {
    super(scope, "druid", props);

    this.setupStack = new DruidSetupNestedStack(this, common, conf, vpc, cluster);

    var replace = Map.<String, Object>of("deployment:eks:druid:release", conf.chart().release());
    var yaml = Template.parse(scope, conf.chart().values(), replace);
    var values = Mapper.get().readValue(yaml, new TypeReference<Map<String, Object>>() {});

    this.chart = HelmChart.Builder
      .create(this, conf.chart().name())
      .cluster(cluster)
      .createNamespace(true)
      .chart(conf.chart().name())
      .namespace(conf.chart().namespace())
      .repository(conf.chart().repository())
      .release(conf.chart().release())
      .version(conf.chart().version())
      .values(values)
      .build();

    this.chart().getNode().addDependency(this.setupStack());
  }
}
