package fasti.sh.eks.stack.model.druid;

import fasti.sh.model.aws.secretsmanager.SecretCredentials;

public record Secrets(
  SecretCredentials admin,
  SecretCredentials system
) {}
