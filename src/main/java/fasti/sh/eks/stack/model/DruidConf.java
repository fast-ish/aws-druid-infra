package fasti.sh.eks.stack.model;

import fasti.sh.model.aws.eks.HelmChart;

public record DruidConf(
  String access,
  String secrets,
  String storage,
  String ingestion,
  String asset,
  HelmChart chart
) {}
