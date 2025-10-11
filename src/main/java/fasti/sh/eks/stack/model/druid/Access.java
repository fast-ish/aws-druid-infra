package fasti.sh.eks.stack.model.druid;

import fasti.sh.model.aws.eks.ServiceAccountConf;

public record Access(ServiceAccountConf serviceAccount) {}
