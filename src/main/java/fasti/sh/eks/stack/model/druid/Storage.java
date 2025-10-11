package fasti.sh.eks.stack.model.druid;

import fasti.sh.model.aws.rds.Rds;
import fasti.sh.model.aws.s3.S3Bucket;

public record Storage(
  Rds metadata,
  S3Bucket deepStorage,
  S3Bucket indexLogs,
  S3Bucket multiStageQuery
) {}
