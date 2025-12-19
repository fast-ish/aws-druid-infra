package fasti.sh.eks.stack.model.druid;

import fasti.sh.model.aws.eks.PodIdentity;

public record Access(PodIdentity podIdentity) {}
