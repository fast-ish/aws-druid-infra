package fasti.sh.eks.stack.model.druid;

import fasti.sh.model.aws.msk.Msk;

public record Ingestion(Msk kafka) {}
