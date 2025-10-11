package fasti.sh.eks.stack.model;

import fasti.sh.model.aws.eks.KubernetesConf;
import fasti.sh.model.aws.vpc.NetworkConf;
import fasti.sh.model.main.Common;

public record SystemConf(
  Common common,
  NetworkConf vpc,
  KubernetesConf eks,
  DruidConf druid
) {}
