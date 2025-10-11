package fasti.sh.eks.stack;

import fasti.sh.eks.stack.model.DruidConf;
import fasti.sh.model.aws.eks.KubernetesConf;
import fasti.sh.model.aws.vpc.NetworkConf;
import fasti.sh.model.main.Common;

public record DeploymentConf(
  Common common,
  NetworkConf vpc,
  KubernetesConf eks,
  DruidConf druid
) {}
