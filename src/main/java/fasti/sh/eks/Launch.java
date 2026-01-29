package fasti.sh.eks;

import static fasti.sh.execute.serialization.Format.describe;
import static fasti.sh.execute.serialization.Format.name;

import com.fasterxml.jackson.core.type.TypeReference;
import fasti.sh.eks.stack.DruidReleaseConf;
import fasti.sh.eks.stack.DruidStack;
import fasti.sh.execute.util.ContextUtils;
import fasti.sh.execute.util.TemplateUtils;
import fasti.sh.model.main.Common;
import fasti.sh.model.main.Release;
import java.util.Map;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

/**
 * CDK application entry point for Druid on EKS infrastructure deployment.
 *
 * <p>
 * Deploys a complete Apache Druid cluster on Amazon EKS with supporting infrastructure including VPC, managed addons, observability, and
 * Druid-specific resources (RDS, S3, MSK).
 */
public class Launch {

  public static void main(final String[] args) {
    var app = new App();

    var conf = get(app);

    new DruidStack(
      app, conf.release(),
      StackProps
        .builder()
        .stackName(name("druid", conf.release().common().id()))
        .env(
          Environment
            .builder()
            .account(conf.release().common().account())
            .region(conf.release().common().region())
            .build())
        .description(
          describe(
            conf.platform(),
            String
              .format(
                "Druid cluster release [%s/%s] - Apache Druid on EKS",
                conf.release().common().name(),
                conf.release().common().alias())))
        .tags(Common.Maps.from(conf.platform().tags(), conf.release().common().tags()))
        .build());

    app.synth();
  }

  private static Release<DruidReleaseConf> get(App app) {
    var mappings = Map
      .<String, Object>ofEntries(
        Map
          .entry(
            "deployment:eks:druid:release",
            app.getNode().getContext("deployment:eks:druid:release").toString()),
        Map.entry("deployment:tags", ContextUtils.parseTags(app, "deployment:tags")));
    var type = new TypeReference<Release<DruidReleaseConf>>() {};
    return TemplateUtils.parseAs(app, "conf.mustache", mappings, type);
  }
}
