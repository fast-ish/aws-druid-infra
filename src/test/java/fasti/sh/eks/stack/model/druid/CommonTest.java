package fasti.sh.eks.stack.model.druid;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests for Common record.
 */
public class CommonTest {

  @Test
  public void testCommonRecordStructure() {
    // Test that Common is a valid record with the expected structure
    assertNotNull(Common.class);

    // Verify record components exist
    var recordComponents = Common.class.getRecordComponents();
    assertNotNull(recordComponents);
    assertEquals(5, recordComponents.length, "Common should have 5 components");

    // Verify component names
    assertEquals("env", recordComponents[0].getName());
    assertEquals("jvmConf", recordComponents[1].getName());
    assertEquals("log4jConf", recordComponents[2].getName());
    assertEquals("metricConf", recordComponents[3].getName());
    assertEquals("runtimeProperties", recordComponents[4].getName());
  }

  @Test
  public void testCommonWithNullValues() {
    // Test that Common can be instantiated with null values
    var common = new Common(null, null, null, null, null);

    assertNotNull(common);
    assertEquals(null, common.env());
    assertEquals(null, common.jvmConf());
    assertEquals(null, common.log4jConf());
    assertEquals(null, common.metricConf());
    assertEquals(null, common.runtimeProperties());
  }

  @Test
  public void testCommonAccessorMethods() {
    // Test that Common properly stores and retrieves String values
    var common = new Common(
        "production",
        "jvm.config",
        "log4j.properties",
        "metrics.yaml",
        "runtime.properties"
    );

    assertNotNull(common);
    assertEquals("production", common.env());
    assertEquals("jvm.config", common.jvmConf());
    assertEquals("log4j.properties", common.log4jConf());
    assertEquals("metrics.yaml", common.metricConf());
    assertEquals("runtime.properties", common.runtimeProperties());
  }

  @Test
  public void testCommonComponentTypes() {
    // Verify that all component types are String
    var recordComponents = Common.class.getRecordComponents();

    assertEquals(String.class, recordComponents[0].getType(), "env should be of type String");
    assertEquals(String.class, recordComponents[1].getType(), "jvmConf should be of type String");
    assertEquals(String.class, recordComponents[2].getType(), "log4jConf should be of type String");
    assertEquals(String.class, recordComponents[3].getType(), "metricConf should be of type String");
    assertEquals(String.class, recordComponents[4].getType(), "runtimeProperties should be of type String");
  }

  @Test
  public void testCommonWithMixedNullAndValidValues() {
    // Test that Common can handle a mix of null and valid values
    var common = new Common("development", null, "log4j.xml", null, "runtime.conf");

    assertNotNull(common);
    assertEquals("development", common.env());
    assertEquals(null, common.jvmConf());
    assertEquals("log4j.xml", common.log4jConf());
    assertEquals(null, common.metricConf());
    assertEquals("runtime.conf", common.runtimeProperties());
  }

  @Test
  public void testSerializationDeserialization() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new Common("prod", "jvm.conf", "log4j.xml", "metrics.yaml", "runtime.props");

    // Serialize to YAML string
    String yaml = mapper.writeValueAsString(original);
    assertNotNull(yaml);
    assertTrue(yaml.contains("prod"));
    assertTrue(yaml.contains("jvm.conf"));

    // Deserialize back to object
    var deserialized = mapper.readValue(yaml, Common.class);
    assertNotNull(deserialized);
    assertEquals(original, deserialized);
  }

  @Test
  public void testEqualityAndHashCode() {
    var common1 = new Common("env1", "jvm1", "log1", "metric1", "runtime1");
    var common2 = new Common("env1", "jvm1", "log1", "metric1", "runtime1");
    var common3 = new Common("env2", "jvm1", "log1", "metric1", "runtime1");

    // Test equality
    assertEquals(common1, common2);
    assertNotEquals(common1, common3);

    // Test hashCode consistency
    assertEquals(common1.hashCode(), common2.hashCode());
    assertNotEquals(common1.hashCode(), common3.hashCode());
  }

  @Test
  public void testToString() {
    var common = new Common("production", "jvm.conf", "log4j.xml", "metrics.yaml", "runtime.props");
    String str = common.toString();

    assertNotNull(str);
    assertTrue(str.contains("Common"));
    assertTrue(str.contains("production"));
    assertTrue(str.contains("jvm.conf"));
  }

  @Test
  public void testRecordImmutability() {
    var common = new Common("env", "jvm", "log", "metric", "runtime");

    // Records are immutable - accessor methods should always return same values
    assertEquals(common.env(), common.env());
    assertEquals(common.jvmConf(), common.jvmConf());
    assertEquals(common.log4jConf(), common.log4jConf());
    assertEquals(common.metricConf(), common.metricConf());
    assertEquals(common.runtimeProperties(), common.runtimeProperties());
  }

  @Test
  public void testWithEmptyStrings() {
    var common = new Common("", "", "", "", "");

    assertNotNull(common);
    assertEquals("", common.env());
    assertEquals("", common.jvmConf());
    assertEquals("", common.log4jConf());
    assertEquals("", common.metricConf());
    assertEquals("", common.runtimeProperties());
  }

  @Test
  public void testWithSpecialCharacters() {
    var common = new Common(
      "env-prod_v2.1",
      "config/jvm-server.conf",
      "log4j2.xml",
      "metrics@v1.yaml",
      "runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.env().contains("-"));
    assertTrue(common.env().contains("_"));
    assertTrue(common.env().contains("."));
    assertTrue(common.jvmConf().contains("/"));
  }

  @Test
  public void testWithVeryLongValues() {
    String longEnv = "a".repeat(1000);
    String longPath = "path/".repeat(200) + "file.conf";
    var common = new Common(longEnv, longPath, null, null, null);

    assertNotNull(common);
    assertEquals(1000, common.env().length());
    assertTrue(common.jvmConf().length() > 1000);
  }

  @Test
  public void testWithMultipleEnvironmentNames() {
    String[] envs = {"dev", "staging", "production", "test", "qa", "prod-us-east-1"};

    for (String env : envs) {
      var common = new Common(env, null, null, null, null);
      assertNotNull(common);
      assertEquals(env, common.env());
    }
  }

  @Test
  public void testLoadFromYamlFile() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var inputStream = getClass().getClassLoader().getResourceAsStream("common-test.yaml");
    assertNotNull(inputStream, "common-test.yaml should exist in test resources");

    var common = mapper.readValue(inputStream, Common.class);

    assertNotNull(common);
    assertEquals("production", common.env());
    assertEquals("config/jvm.properties", common.jvmConf());
    assertEquals("config/log4j2.xml", common.log4jConf());
    assertEquals("config/metrics.yaml", common.metricConf());
    assertEquals("config/runtime.properties", common.runtimeProperties());
  }

  @Test
  public void testYamlRoundTrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var inputStream = getClass().getClassLoader().getResourceAsStream("common-test.yaml");
    var loaded = mapper.readValue(inputStream, Common.class);

    // Serialize back to YAML
    String yaml = mapper.writeValueAsString(loaded);

    // Deserialize again
    var reloaded = mapper.readValue(yaml, Common.class);

    // Should be equal
    assertEquals(loaded, reloaded);
  }

  @Test
  public void testComplexEnvironmentConfigurations() {
    // Test various complex environment configurations
    String[][] complexEnvs = {
      {"prod-us-east-1", "jvm/prod-us-east-1.conf", "log4j2-prod.xml", "metrics-datadog.yaml", "runtime-optimized.properties"},
      {"staging-eu-west-1", "jvm/staging-eu.conf", "log4j2-staging.xml", "metrics-prometheus.yaml", "runtime-staging.properties"},
      {"dev-ap-south-1", "jvm/dev-ap.conf", "log4j2-dev.xml", "metrics-cloudwatch.yaml", "runtime-dev.properties"}
    };

    for (String[] config : complexEnvs) {
      var common = new Common(config[0], config[1], config[2], config[3], config[4]);
      assertNotNull(common);
      assertEquals(config[0], common.env());
      assertEquals(config[1], common.jvmConf());
      assertEquals(config[2], common.log4jConf());
      assertEquals(config[3], common.metricConf());
      assertEquals(config[4], common.runtimeProperties());
    }
  }

  @Test
  public void testInvalidPathScenarios() {
    // Test with paths that might be considered invalid but should still be stored
    var common1 = new Common("env", "../../../etc/passwd", null, null, null);
    var common2 = new Common("env", "path/with spaces/config.conf", null, null, null);
    var common3 = new Common("env", "path/with\ttabs", null, null, null);
    var common4 = new Common("env", "", null, null, null);

    assertNotNull(common1);
    assertTrue(common1.jvmConf().contains("../"));

    assertNotNull(common2);
    assertTrue(common2.jvmConf().contains(" "));

    assertNotNull(common3);
    assertNotNull(common3.jvmConf());

    assertNotNull(common4);
    assertEquals("", common4.jvmConf());
  }

  @Test
  public void testConfigurationFilePatternValidation() {
    // Test various configuration file patterns
    var common1 = new Common("prod", "jvm.conf", "log4j.xml", "metrics.yml", "runtime.properties");
    var common2 = new Common("prod", "jvm.config", "log4j.properties", "metrics.yaml", "runtime.props");
    var common3 = new Common("prod", "jvm-options", "log4j2.xml", "metrics.json", "runtime.ini");

    assertNotNull(common1);
    assertTrue(common1.jvmConf().endsWith(".conf"));
    assertTrue(common1.log4jConf().endsWith(".xml"));
    assertTrue(common1.metricConf().endsWith(".yml"));

    assertNotNull(common2);
    assertTrue(common2.jvmConf().endsWith(".config"));
    assertTrue(common2.log4jConf().endsWith(".properties"));

    assertNotNull(common3);
    assertTrue(common3.log4jConf().endsWith(".xml"));
    assertTrue(common3.metricConf().endsWith(".json"));
    assertTrue(common3.runtimeProperties().endsWith(".ini"));
  }

  @Test
  public void testEnvironmentWithComplexNaming() {
    // Test environments with complex naming conventions
    String[] complexEnvNames = {
      "production-v2.1.0",
      "staging_us_east_1",
      "dev-feature-xyz",
      "qa.regression.suite",
      "perf-test-2024-01",
      "dr-failover-site"
    };

    for (String envName : complexEnvNames) {
      var common = new Common(envName, null, null, null, null);
      assertNotNull(common);
      assertEquals(envName, common.env());
    }
  }

  @Test
  public void testSerializationWithAllConfigurationsPopulated() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new Common(
      "production-v1",
      "config/jvm/server-optimized.conf",
      "config/logging/log4j2-async.xml",
      "config/metrics/prometheus-detailed.yaml",
      "config/runtime/druid-production.properties"
    );

    String yaml = mapper.writeValueAsString(original);
    assertNotNull(yaml);
    assertTrue(yaml.contains("production-v1"));
    assertTrue(yaml.contains("server-optimized"));
    assertTrue(yaml.contains("async"));
    assertTrue(yaml.contains("prometheus"));
    assertTrue(yaml.contains("druid-production"));

    var deserialized = mapper.readValue(yaml, Common.class);
    assertEquals(original, deserialized);
  }

  @Test
  public void testAbsoluteAndRelativePaths() {
    var common1 = new Common("prod", "/etc/druid/jvm.conf", "/var/log/druid/log4j.xml", null, null);
    var common2 = new Common("prod", "./config/jvm.conf", "../logs/log4j.xml", null, null);
    var common3 = new Common("prod", "~/druid/jvm.conf", "~/.druid/log4j.xml", null, null);

    assertNotNull(common1);
    assertTrue(common1.jvmConf().startsWith("/"));
    assertTrue(common1.log4jConf().startsWith("/"));

    assertNotNull(common2);
    assertTrue(common2.jvmConf().startsWith("./"));
    assertTrue(common2.log4jConf().startsWith("../"));

    assertNotNull(common3);
    assertTrue(common3.jvmConf().startsWith("~/"));
  }

  @Test
  public void testConfigurationWithURLs() {
    var common = new Common(
      "cloud-prod",
      "s3://config-bucket/jvm/production.conf",
      "https://config-server.example.com/log4j2.xml",
      "consul://metrics/druid/config.yaml",
      "vault://secrets/druid/runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.jvmConf().startsWith("s3://"));
    assertTrue(common.log4jConf().startsWith("https://"));
    assertTrue(common.metricConf().startsWith("consul://"));
    assertTrue(common.runtimeProperties().startsWith("vault://"));
  }

  @Test
  public void testDeserializationWithMissingFields() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // YAML with only env and jvmConf
    String yaml1 = """
      env: minimal-config
      jvmConf: config/jvm.conf
      """;

    var common1 = mapper.readValue(yaml1, Common.class);
    assertNotNull(common1);
    assertEquals("minimal-config", common1.env());
    assertEquals("config/jvm.conf", common1.jvmConf());
    assertNull(common1.log4jConf());
    assertNull(common1.metricConf());
    assertNull(common1.runtimeProperties());

    // YAML with only runtime properties
    String yaml2 = """
      runtimeProperties: config/runtime.properties
      """;

    var common2 = mapper.readValue(yaml2, Common.class);
    assertNotNull(common2);
    assertNull(common2.env());
    assertEquals("config/runtime.properties", common2.runtimeProperties());
  }

  @Test
  public void testConfigurationWithSpecialCharactersInPaths() {
    var common = new Common(
      "prod@v2.1",
      "config/jvm-server#1.conf",
      "log4j[production].xml",
      "metrics{datacenter:us-east}.yaml",
      "runtime(optimized).properties"
    );

    assertNotNull(common);
    assertTrue(common.env().contains("@"));
    assertTrue(common.jvmConf().contains("#"));
    assertTrue(common.log4jConf().contains("["));
    assertTrue(common.metricConf().contains("{"));
    assertTrue(common.runtimeProperties().contains("("));
  }

  @Test
  public void testHashCodeAndEqualityWithComplexConfigurations() {
    var common1 = new Common(
      "production",
      "config/jvm/server.conf",
      "config/log4j2.xml",
      "config/metrics.yaml",
      "config/runtime.properties"
    );

    var common2 = new Common(
      "production",
      "config/jvm/server.conf",
      "config/log4j2.xml",
      "config/metrics.yaml",
      "config/runtime.properties"
    );

    var common3 = new Common(
      "staging",
      "config/jvm/server.conf",
      "config/log4j2.xml",
      "config/metrics.yaml",
      "config/runtime.properties"
    );

    assertEquals(common1, common2);
    assertNotEquals(common1, common3);
    assertEquals(common1.hashCode(), common2.hashCode());
    assertNotEquals(common1.hashCode(), common3.hashCode());
  }

  @Test
  public void testConfigurationWithVersionedPaths() {
    var common = new Common(
      "production-v3.0.0",
      "config/v3/jvm-tuned.conf",
      "config/v3/log4j2-async-v2.xml",
      "config/v3/metrics-prometheus-v1.5.yaml",
      "config/v3/runtime-2024.01.properties"
    );

    assertNotNull(common);
    assertTrue(common.env().contains("v3.0.0"));
    assertTrue(common.jvmConf().contains("/v3/"));
    assertTrue(common.log4jConf().contains("v2.xml"));
    assertTrue(common.metricConf().contains("v1.5"));
    assertTrue(common.runtimeProperties().contains("2024.01"));
  }

  @Test
  public void testConfigurationFileExtensionVariations() {
    // Test different file extensions that might be used
    String[][] configurations = {
      {"yml", "yaml", "properties", "conf", "config"},
      {"xml", "json", "toml", "ini", "cfg"},
      {"prop", "props", "settings", "env", "txt"}
    };

    int counter = 0;
    for (String[] exts : configurations) {
      var common = new Common(
        "env" + counter++,
        "jvm." + exts[0],
        "log4j." + exts[1],
        "metrics." + exts[2],
        "runtime." + exts[3]
      );
      assertNotNull(common);
    }
  }

  // ==================== Additional Comprehensive Tests (15+) ====================

  @Test
  public void testJvmFlagsCombinations() {
    String[] jvmFlags = {
      "-Xms4g -Xmx8g -XX:+UseG1GC",
      "-Xms8g -Xmx16g -XX:+UseZGC",
      "-Xms16g -Xmx32g -XX:+UseShenandoahGC",
      "-Xms2g -Xmx4g -XX:+UseParallelGC",
      "-Xms1g -Xmx2g -XX:MaxGCPauseMillis=200",
      "-XX:+UseStringDeduplication -XX:MaxMetaspaceSize=512m",
      "-XX:+AlwaysPreTouch -XX:+DisableExplicitGC",
      "-Xss512k -XX:ReservedCodeCacheSize=240m"
    };

    for (String flags : jvmFlags) {
      var common = new Common(
        "production",
        "jvm.conf?" + flags,
        "log4j2.xml",
        "metrics.yaml",
        "runtime.properties"
      );

      assertNotNull(common);
      assertTrue(common.jvmConf().contains("jvm.conf"));
    }
  }

  @Test
  public void testLog4j2AllAppenderTypes() {
    String[] appenderConfigs = {
      "log4j2-console.xml",
      "log4j2-rolling-file.xml",
      "log4j2-async-file.xml",
      "log4j2-syslog.xml",
      "log4j2-kafka.xml",
      "log4j2-fluentd.xml",
      "log4j2-jdbc.xml",
      "log4j2-elasticsearch.xml",
      "log4j2-cloudwatch.xml",
      "log4j2-splunk.xml"
    };

    for (String appender : appenderConfigs) {
      var common = new Common(
        "production",
        "jvm.conf",
        "config/" + appender,
        "metrics.yaml",
        "runtime.properties"
      );

      assertNotNull(common);
      assertTrue(common.log4jConf().contains(appender));
    }
  }

  @Test
  public void testMetricExporterConfigurations() {
    String[] exporters = {
      "prometheus-metrics.yaml",
      "statsd-metrics.yaml",
      "datadog-metrics.yaml",
      "cloudwatch-metrics.yaml",
      "graphite-metrics.yaml",
      "influxdb-metrics.yaml",
      "jmx-metrics.yaml",
      "newrelic-metrics.yaml",
      "dynatrace-metrics.yaml"
    };

    for (String exporter : exporters) {
      var common = new Common(
        "production",
        "jvm.conf",
        "log4j2.xml",
        "config/metrics/" + exporter,
        "runtime.properties"
      );

      assertNotNull(common);
      assertTrue(common.metricConf().contains(exporter));
    }
  }

  @Test
  public void testStressTestWithExtremelyLargePropertyFiles() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Create configuration with very large property paths
    for (int i = 0; i < 100; i++) {
      String largePath = "config/" + "nested/".repeat(50) + "file-" + i + ".properties";
      var common = new Common(
        "stress-test-env-" + i,
        "jvm-" + i + ".conf",
        "log4j2-" + i + ".xml",
        "metrics-" + i + ".yaml",
        largePath
      );

      assertNotNull(common);
      assertTrue(common.runtimeProperties().length() > 100);

      // Verify serialization
      String yaml = mapper.writeValueAsString(common);
      var deserialized = mapper.readValue(yaml, Common.class);
      assertEquals(common, deserialized);
    }
  }

  @Test
  public void testPropertyFilePrecedence() {
    // Test different property file precedence patterns
    String[] precedenceOrder = {
      "runtime-default.properties",
      "runtime-dev.properties",
      "runtime-staging.properties",
      "runtime-production.properties",
      "runtime-override.properties",
      "runtime-local.properties"
    };

    for (int i = 0; i < precedenceOrder.length; i++) {
      var common = new Common(
        "env-" + i,
        "jvm.conf",
        "log4j2.xml",
        "metrics.yaml",
        precedenceOrder[i]
      );

      assertNotNull(common);
      assertEquals(precedenceOrder[i], common.runtimeProperties());
    }
  }

  @Test
  public void testSystemPropertyOverrides() {
    var common = new Common(
      "production",
      "jvm.conf?override.heap.size=16g",
      "log4j2.xml?override.level=DEBUG",
      "metrics.yaml?override.interval=30s",
      "runtime.properties?override.port=8888"
    );

    assertNotNull(common);
    assertTrue(common.jvmConf().contains("override.heap.size=16g"));
    assertTrue(common.log4jConf().contains("override.level=DEBUG"));
    assertTrue(common.metricConf().contains("override.interval=30s"));
    assertTrue(common.runtimeProperties().contains("override.port=8888"));
  }

  @Test
  public void testEnvironmentVariableSubstitution() {
    var common = new Common(
      "${DRUID_ENV}",
      "${CONFIG_PATH}/jvm.conf",
      "${CONFIG_PATH}/log4j2.xml",
      "${CONFIG_PATH}/metrics.yaml",
      "${CONFIG_PATH}/runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.env().contains("${DRUID_ENV}"));
    assertTrue(common.jvmConf().contains("${CONFIG_PATH}"));
    assertTrue(common.log4jConf().contains("${CONFIG_PATH}"));
    assertTrue(common.metricConf().contains("${CONFIG_PATH}"));
    assertTrue(common.runtimeProperties().contains("${CONFIG_PATH}"));
  }

  @Test
  public void testConcurrentPropertyLoading() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var threads = new java.util.concurrent.CountDownLatch(20);
    var errors = new java.util.concurrent.CopyOnWriteArrayList<Exception>();

    for (int i = 0; i < 20; i++) {
      final int index = i;
      new Thread(() -> {
        try {
          for (int j = 0; j < 50; j++) {
            var common = new Common(
              "env-" + index + "-" + j,
              "jvm-" + index + "-" + j + ".conf",
              "log4j2-" + index + "-" + j + ".xml",
              "metrics-" + index + "-" + j + ".yaml",
              "runtime-" + index + "-" + j + ".properties"
            );

            String yaml = mapper.writeValueAsString(common);
            var loaded = mapper.readValue(yaml, Common.class);
            assertEquals(common, loaded);
          }
        } catch (Exception e) {
          errors.add(e);
        } finally {
          threads.countDown();
        }
      }).start();
    }

    threads.await(30, java.util.concurrent.TimeUnit.SECONDS);
    assertTrue(errors.isEmpty(), "Concurrent property loading should not produce errors");
  }

  @Test
  public void testHotReloadScenarios() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Simulate hot-reload by creating multiple versions
    for (int version = 1; version <= 10; version++) {
      var common = new Common(
        "production",
        "jvm-v" + version + ".conf",
        "log4j2-v" + version + ".xml",
        "metrics-v" + version + ".yaml",
        "runtime-v" + version + ".properties"
      );

      assertNotNull(common);
      assertTrue(common.jvmConf().contains("-v" + version));

      // Simulate reload by serializing and deserializing
      String yaml = mapper.writeValueAsString(common);
      var reloaded = mapper.readValue(yaml, Common.class);
      assertEquals(common, reloaded);
    }
  }

  @Test
  public void testPropertyEncryptionConfigurations() {
    String[] encryptionSchemes = {
      "encrypted://aes256/config/jvm.conf",
      "vault://kv/secret/druid/jvm-config",
      "kms://us-east-1/alias/druid-config",
      "jasypt:ENC(encryptedvalue)",
      "encrypted:base64:encoded-config"
    };

    for (String scheme : encryptionSchemes) {
      var common = new Common(
        "production",
        scheme,
        "log4j2.xml",
        "metrics.yaml",
        "runtime.properties"
      );

      assertNotNull(common);
      assertTrue(common.jvmConf().length() > 0);
    }
  }

  @Test
  public void testComplexJvmTuningParameters() {
    String[] tuningConfigs = {
      "jvm-coordinator-tuned.conf",
      "jvm-overlord-tuned.conf",
      "jvm-broker-tuned.conf",
      "jvm-historical-tuned.conf",
      "jvm-middlemanager-tuned.conf",
      "jvm-indexer-tuned.conf",
      "jvm-router-tuned.conf"
    };

    for (String config : tuningConfigs) {
      var common = new Common(
        "production",
        "config/" + config,
        "log4j2.xml",
        "metrics.yaml",
        "runtime.properties"
      );

      assertNotNull(common);
      assertTrue(common.jvmConf().contains(config));
    }
  }

  @Test
  public void testLog4j2AsyncAppenderConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yamlWithAsyncConfig = """
      env: production
      jvmConf: config/jvm-async.conf
      log4jConf: config/log4j2-async-console-file-kafka.xml
      metricConf: config/metrics-async.yaml
      runtimeProperties: config/runtime-async.properties
    """;

    var common = mapper.readValue(yamlWithAsyncConfig, Common.class);
    assertNotNull(common);
    assertTrue(common.log4jConf().contains("async"));
    assertTrue(common.runtimeProperties().contains("async"));
  }

  @Test
  public void testMetricsWithMultipleExporters() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yamlWithMultiExporters = """
      env: production
      jvmConf: config/jvm.conf
      log4jConf: config/log4j2.xml
      metricConf: config/metrics-prometheus-statsd-cloudwatch.yaml
      runtimeProperties: config/runtime.properties
    """;

    var common = mapper.readValue(yamlWithMultiExporters, Common.class);
    assertNotNull(common);
    assertTrue(common.metricConf().contains("prometheus"));
    assertTrue(common.metricConf().contains("statsd"));
    assertTrue(common.metricConf().contains("cloudwatch"));
  }

  @Test
  public void testConfigurationWithS3Paths() {
    var common = new Common(
      "production",
      "s3://druid-config-bucket/prod/jvm.conf",
      "s3://druid-config-bucket/prod/log4j2.xml",
      "s3://druid-config-bucket/prod/metrics.yaml",
      "s3://druid-config-bucket/prod/runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.jvmConf().startsWith("s3://"));
    assertTrue(common.log4jConf().startsWith("s3://"));
    assertTrue(common.metricConf().startsWith("s3://"));
    assertTrue(common.runtimeProperties().startsWith("s3://"));
  }

  @Test
  public void testConfigurationWithConsulPaths() {
    var common = new Common(
      "production",
      "consul://kv/druid/jvm-config",
      "consul://kv/druid/log4j-config",
      "consul://kv/druid/metrics-config",
      "consul://kv/druid/runtime-config"
    );

    assertNotNull(common);
    assertTrue(common.jvmConf().startsWith("consul://"));
    assertTrue(common.log4jConf().startsWith("consul://"));
    assertTrue(common.metricConf().startsWith("consul://"));
    assertTrue(common.runtimeProperties().startsWith("consul://"));
  }

  @Test
  public void testGCLoggingConfigurations() {
    String[] gcConfigs = {
      "jvm-gc-g1.conf",
      "jvm-gc-zgc.conf",
      "jvm-gc-shenandoah.conf",
      "jvm-gc-parallel.conf",
      "jvm-gc-serial.conf",
      "jvm-gc-cms.conf"
    };

    for (String gcConfig : gcConfigs) {
      var common = new Common(
        "production",
        "config/" + gcConfig,
        "log4j2.xml",
        "metrics.yaml",
        "runtime.properties"
      );

      assertNotNull(common);
      assertTrue(common.jvmConf().contains("gc"));
    }
  }

  @Test
  public void testConfigurationWithProfileSpecificSettings() {
    String[] profiles = {
      "default", "development", "testing", "staging",
      "production", "performance", "debug", "minimal"
    };

    for (String profile : profiles) {
      var common = new Common(
        profile,
        "config/" + profile + "/jvm.conf",
        "config/" + profile + "/log4j2.xml",
        "config/" + profile + "/metrics.yaml",
        "config/" + profile + "/runtime.properties"
      );

      assertNotNull(common);
      assertEquals(profile, common.env());
      assertTrue(common.jvmConf().contains(profile));
    }
  }

  @Test
  public void testMemoryAndCPUTuningConfigurations() {
    var common = new Common(
      "production",
      "jvm.conf?heap.min=8g&heap.max=32g&cpu.cores=16",
      "log4j2.xml",
      "metrics.yaml?cpu.monitoring=true",
      "runtime.properties?processing.threads=16"
    );

    assertNotNull(common);
    assertTrue(common.jvmConf().contains("heap.min=8g"));
    assertTrue(common.jvmConf().contains("heap.max=32g"));
    assertTrue(common.jvmConf().contains("cpu.cores=16"));
    assertTrue(common.metricConf().contains("cpu.monitoring=true"));
    assertTrue(common.runtimeProperties().contains("processing.threads=16"));
  }

  @Test
  public void testSerializationPerformanceWithLargeDataset() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    long startTime = System.currentTimeMillis();

    for (int i = 0; i < 2000; i++) {
      var common = new Common(
        "production-cluster-" + i,
        "s3://config-bucket/jvm/node-" + i + ".conf",
        "s3://config-bucket/log4j2/node-" + i + ".xml",
        "consul://kv/metrics/node-" + i + ".yaml",
        "vault://secret/runtime/node-" + i + ".properties"
      );

      String yaml = mapper.writeValueAsString(common);
      var deserialized = mapper.readValue(yaml, Common.class);
      assertEquals(common, deserialized);
    }

    long duration = System.currentTimeMillis() - startTime;
    assertTrue(duration < 15000, "2000 serialization/deserialization cycles should complete in under 15 seconds");
  }

  // ==================== Extended Comprehensive Tests (40+ Additional) ====================

  @Test
  public void testAllJVMHeapConfigurations() {
    String[] heapConfigs = {
      "jvm.conf?Xms=1g&Xmx=2g",
      "jvm.conf?Xms=2g&Xmx=4g",
      "jvm.conf?Xms=4g&Xmx=8g",
      "jvm.conf?Xms=8g&Xmx=16g",
      "jvm.conf?Xms=16g&Xmx=32g",
      "jvm.conf?Xms=32g&Xmx=64g",
      "jvm.conf?Xms=64g&Xmx=128g"
    };

    for (String config : heapConfigs) {
      var common = new Common("production", config, "log4j2.xml", "metrics.yaml", "runtime.properties");
      assertNotNull(common);
      assertTrue(common.jvmConf().contains("Xms="));
      assertTrue(common.jvmConf().contains("Xmx="));
    }
  }

  @Test
  public void testAllGarbageCollectors() {
    String[] gcConfigs = {
      "jvm-g1gc.conf?XX:+UseG1GC&XX:MaxGCPauseMillis=200",
      "jvm-zgc.conf?XX:+UseZGC&XX:ZCollectionInterval=5",
      "jvm-shenandoah.conf?XX:+UseShenandoahGC&XX:ShenandoahGCHeuristics=adaptive",
      "jvm-parallel.conf?XX:+UseParallelGC&XX:ParallelGCThreads=8",
      "jvm-cms.conf?XX:+UseConcMarkSweepGC&XX:CMSInitiatingOccupancyFraction=70",
      "jvm-serial.conf?XX:+UseSerialGC"
    };

    for (String gc : gcConfigs) {
      var common = new Common("production", gc, "log4j2.xml", "metrics.yaml", "runtime.properties");
      assertNotNull(common);
      assertTrue(common.jvmConf().contains("jvm-"));
    }
  }

  @Test
  public void testJVMGCLoggingOptions() {
    String[] gcLoggingOptions = {
      "jvm.conf?Xlog:gc*:file=/var/log/gc.log:time,uptime:filecount=5,filesize=20M",
      "jvm.conf?verbose:gc&XX:+PrintGCDetails&XX:+PrintGCDateStamps",
      "jvm.conf?Xlog:gc:gc.log:time&Xlog:gc,age*=debug:gc-age.log",
      "jvm.conf?XX:+UseGCLogFileRotation&XX:NumberOfGCLogFiles=10&XX:GCLogFileSize=50M"
    };

    for (String logOpt : gcLoggingOptions) {
      var common = new Common("production", logOpt, "log4j2.xml", "metrics.yaml", "runtime.properties");
      assertNotNull(common);
      assertTrue(common.jvmConf().contains("jvm.conf"));
    }
  }

  @Test
  public void testJVMMemoryTuningParameters() {
    var common = new Common(
      "production",
      "jvm.conf?Xmn=4g&XX:MetaspaceSize=256m&XX:MaxMetaspaceSize=512m&XX:ReservedCodeCacheSize=240m",
      "log4j2.xml",
      "metrics.yaml",
      "runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.jvmConf().contains("Xmn=4g"));
    assertTrue(common.jvmConf().contains("MetaspaceSize"));
  }

  @Test
  public void testJVMThreadingParameters() {
    var common = new Common(
      "production",
      "jvm.conf?Xss512k&XX:ParallelGCThreads=8&XX:ConcGCThreads=4",
      "log4j2.xml",
      "metrics.yaml",
      "runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.jvmConf().contains("Xss512k"));
    assertTrue(common.jvmConf().contains("ParallelGCThreads"));
  }

  @Test
  public void testJVMPerformanceFlags() {
    String[] perfFlags = {
      "jvm.conf?XX:+AlwaysPreTouch&XX:+UseStringDeduplication",
      "jvm.conf?XX:+DisableExplicitGC&XX:+UseCompressedOops",
      "jvm.conf?XX:+OptimizeStringConcat&XX:+UseCompressedClassPointers",
      "jvm.conf?XX:+AggressiveOpts&XX:+UseFastAccessorMethods"
    };

    for (String flags : perfFlags) {
      var common = new Common("production", flags, "log4j2.xml", "metrics.yaml", "runtime.properties");
      assertNotNull(common);
    }
  }

  @Test
  public void testLog4j2AppenderTypes() {
    String[] appenders = {
      "log4j2-console-appender.xml",
      "log4j2-file-appender.xml",
      "log4j2-rolling-file-appender.xml",
      "log4j2-async-appender.xml",
      "log4j2-syslog-appender.xml",
      "log4j2-socket-appender.xml",
      "log4j2-kafka-appender.xml",
      "log4j2-jdbc-appender.xml",
      "log4j2-fluentd-appender.xml",
      "log4j2-elasticsearch-appender.xml"
    };

    for (String appender : appenders) {
      var common = new Common("production", "jvm.conf", appender, "metrics.yaml", "runtime.properties");
      assertNotNull(common);
      assertTrue(common.log4jConf().contains("appender"));
    }
  }

  @Test
  public void testLog4j2LayoutTypes() {
    String[] layouts = {
      "log4j2-pattern-layout.xml",
      "log4j2-json-layout.xml",
      "log4j2-xml-layout.xml",
      "log4j2-yaml-layout.xml",
      "log4j2-html-layout.xml",
      "log4j2-csv-layout.xml",
      "log4j2-rfc5424-layout.xml",
      "log4j2-gelf-layout.xml"
    };

    for (String layout : layouts) {
      var common = new Common("production", "jvm.conf", layout, "metrics.yaml", "runtime.properties");
      assertNotNull(common);
      assertTrue(common.log4jConf().contains("layout"));
    }
  }

  @Test
  public void testLog4j2FilterTypes() {
    String[] filters = {
      "log4j2-threshold-filter.xml",
      "log4j2-burst-filter.xml",
      "log4j2-marker-filter.xml",
      "log4j2-regex-filter.xml",
      "log4j2-time-filter.xml",
      "log4j2-thread-context-map-filter.xml"
    };

    for (String filter : filters) {
      var common = new Common("production", "jvm.conf", filter, "metrics.yaml", "runtime.properties");
      assertNotNull(common);
      assertTrue(common.log4jConf().contains("filter"));
    }
  }

  @Test
  public void testLog4j2LogLevels() {
    String[] logLevels = {
      "log4j2.xml?level=TRACE",
      "log4j2.xml?level=DEBUG",
      "log4j2.xml?level=INFO",
      "log4j2.xml?level=WARN",
      "log4j2.xml?level=ERROR",
      "log4j2.xml?level=FATAL",
      "log4j2.xml?level=OFF"
    };

    for (String level : logLevels) {
      var common = new Common("production", "jvm.conf", level, "metrics.yaml", "runtime.properties");
      assertNotNull(common);
      assertTrue(common.log4jConf().contains("level="));
    }
  }

  @Test
  public void testMetricExporterConfiguration() {
    String[] exporters = {
      "metrics-prometheus.yaml?port=9090&path=/metrics",
      "metrics-statsd.yaml?host=statsd.internal&port=8125",
      "metrics-datadog.yaml?apiKey=xxx&host=datadog.internal",
      "metrics-cloudwatch.yaml?namespace=Druid&region=us-east-1",
      "metrics-graphite.yaml?host=graphite.internal&port=2003",
      "metrics-influxdb.yaml?url=http://influxdb:8086&database=metrics",
      "metrics-jmx.yaml?port=9999",
      "metrics-newrelic.yaml?licenseKey=xxx",
      "metrics-dynatrace.yaml?endpoint=https://dynatrace.internal"
    };

    for (String exporter : exporters) {
      var common = new Common("production", "jvm.conf", "log4j2.xml", exporter, "runtime.properties");
      assertNotNull(common);
      assertTrue(common.metricConf().contains("metrics-"));
    }
  }

  @Test
  public void testMetricTypes() {
    String[] metricTypes = {
      "metrics.yaml?type=counter",
      "metrics.yaml?type=gauge",
      "metrics.yaml?type=histogram",
      "metrics.yaml?type=meter",
      "metrics.yaml?type=timer",
      "metrics.yaml?type=summary"
    };

    for (String metricType : metricTypes) {
      var common = new Common("production", "jvm.conf", "log4j2.xml", metricType, "runtime.properties");
      assertNotNull(common);
      assertTrue(common.metricConf().contains("type="));
    }
  }

  @Test
  public void testRuntimePropertiesCategories() {
    String[] propCategories = {
      "runtime-coordinator.properties",
      "runtime-overlord.properties",
      "runtime-broker.properties",
      "runtime-historical.properties",
      "runtime-middlemanager.properties",
      "runtime-router.properties",
      "runtime-indexer.properties"
    };

    for (String category : propCategories) {
      var common = new Common("production", "jvm.conf", "log4j2.xml", "metrics.yaml", category);
      assertNotNull(common);
      assertTrue(common.runtimeProperties().contains("runtime-"));
    }
  }

  @Test
  public void testStressWith100KPropertyLoads() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    for (int i = 0; i < 100000; i++) {
      var common = new Common(
        "env-" + i,
        "jvm-" + i + ".conf",
        "log4j2-" + i + ".xml",
        "metrics-" + i + ".yaml",
        "runtime-" + i + ".properties"
      );

      if (i % 10000 == 0) {
        String yaml = mapper.writeValueAsString(common);
        assertNotNull(yaml);
      }
    }
  }

  @Test
  public void testEnvironmentSpecificOverrides() {
    String[][] envConfigs = {
      {"dev", "dev-us-east-1", "dev-eu-west-1"},
      {"staging", "staging-us-west-2", "staging-ap-south-1"},
      {"production", "prod-us-east-1", "prod-eu-central-1"},
      {"qa", "qa-us-east-1", "qa-eu-west-2"},
      {"perf", "perf-us-west-1", "perf-ap-southeast-1"}
    };

    for (String[] envGroup : envConfigs) {
      for (String env : envGroup) {
        var common = new Common(
          env,
          "config/" + env + "/jvm.conf",
          "config/" + env + "/log4j2.xml",
          "config/" + env + "/metrics.yaml",
          "config/" + env + "/runtime.properties"
        );

        assertNotNull(common);
        assertTrue(common.env().contains(env.split("-")[0]));
      }
    }
  }

  @Test
  public void testConfigurationHotReloadEdgeCases() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    for (int iteration = 1; iteration <= 50; iteration++) {
      var common = new Common(
        "production",
        "jvm-v" + iteration + ".conf",
        "log4j2-v" + iteration + ".xml",
        "metrics-v" + iteration + ".yaml",
        "runtime-v" + iteration + ".properties"
      );

      String yaml = mapper.writeValueAsString(common);
      var reloaded = mapper.readValue(yaml, Common.class);
      assertEquals(common, reloaded);
    }
  }

  @Test
  public void testEncryptionKeyRotation() {
    String[] rotationVersions = {
      "jvm.conf?keyVersion=1&rotationDate=2024-01-01",
      "jvm.conf?keyVersion=2&rotationDate=2024-02-01",
      "jvm.conf?keyVersion=3&rotationDate=2024-03-01",
      "jvm.conf?keyVersion=4&rotationDate=2024-04-01"
    };

    for (String version : rotationVersions) {
      var common = new Common("production", version, "log4j2.xml", "metrics.yaml", "runtime.properties");
      assertNotNull(common);
      assertTrue(common.jvmConf().contains("keyVersion="));
    }
  }

  @Test
  public void testJVMDiagnosticFlags() {
    String[] diagnosticFlags = {
      "jvm.conf?XX:+HeapDumpOnOutOfMemoryError&XX:HeapDumpPath=/var/dumps",
      "jvm.conf?XX:+PrintFlagsFinal&XX:+UnlockDiagnosticVMOptions",
      "jvm.conf?XX:NativeMemoryTracking=detail",
      "jvm.conf?XX:+FlightRecorder&XX:StartFlightRecording=duration=200s,filename=recording.jfr"
    };

    for (String flags : diagnosticFlags) {
      var common = new Common("production", flags, "log4j2.xml", "metrics.yaml", "runtime.properties");
      assertNotNull(common);
    }
  }

  @Test
  public void testJVMSecurityParameters() {
    var common = new Common(
      "production",
      "jvm.conf?Djava.security.policy=/etc/druid/security.policy&Djava.security.manager",
      "log4j2.xml",
      "metrics.yaml",
      "runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.jvmConf().contains("security"));
  }

  @Test
  public void testJVMNetworkingParameters() {
    var common = new Common(
      "production",
      "jvm.conf?Djava.net.preferIPv4Stack=true&Djava.rmi.server.hostname=localhost",
      "log4j2.xml",
      "metrics.yaml",
      "runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.jvmConf().contains("java.net"));
  }

  @Test
  public void testLog4j2AsyncLoggerConfig() {
    String[] asyncConfigs = {
      "log4j2-async.xml?asyncLoggerRingBufferSize=262144",
      "log4j2-async.xml?asyncLoggerWaitStrategy=Block",
      "log4j2-async.xml?asyncLoggerTimeout=10000",
      "log4j2-async.xml?asyncQueueFullPolicy=Discard"
    };

    for (String config : asyncConfigs) {
      var common = new Common("production", "jvm.conf", config, "metrics.yaml", "runtime.properties");
      assertNotNull(common);
      assertTrue(common.log4jConf().contains("async"));
    }
  }

  @Test
  public void testMetricCollectionIntervals() {
    String[] intervals = {
      "metrics.yaml?interval=1s",
      "metrics.yaml?interval=5s",
      "metrics.yaml?interval=10s",
      "metrics.yaml?interval=30s",
      "metrics.yaml?interval=60s",
      "metrics.yaml?interval=300s"
    };

    for (String interval : intervals) {
      var common = new Common("production", "jvm.conf", "log4j2.xml", interval, "runtime.properties");
      assertNotNull(common);
      assertTrue(common.metricConf().contains("interval="));
    }
  }

  @Test
  public void testRuntimePropertyOverrides() {
    var common = new Common(
      "production",
      "jvm.conf",
      "log4j2.xml",
      "metrics.yaml",
      "runtime.properties?override.coordinator.period=PT30S&override.broker.cache.size=1GB"
    );

    assertNotNull(common);
    assertTrue(common.runtimeProperties().contains("override."));
  }

  @Test
  public void testConcurrentPropertyLoading200x500() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var threads = new java.util.concurrent.CountDownLatch(200);
    var errors = new java.util.concurrent.CopyOnWriteArrayList<Exception>();

    for (int i = 0; i < 200; i++) {
      final int index = i;
      new Thread(() -> {
        try {
          for (int j = 0; j < 500; j++) {
            var common = new Common(
              "env-" + index + "-" + j,
              "jvm-" + index + "-" + j + ".conf",
              "log4j2-" + index + "-" + j + ".xml",
              "metrics-" + index + "-" + j + ".yaml",
              "runtime-" + index + "-" + j + ".properties"
            );
            String yaml = mapper.writeValueAsString(common);
            var loaded = mapper.readValue(yaml, Common.class);
            assertEquals(common, loaded);
          }
        } catch (Exception e) {
          errors.add(e);
        } finally {
          threads.countDown();
        }
      }).start();
    }

    threads.await(60, java.util.concurrent.TimeUnit.SECONDS);
    assertTrue(errors.isEmpty(), "Concurrent loading with 200×500 should not produce errors");
  }

  @Test
  public void testJVMModuleSystemOptions() {
    String[] moduleOptions = {
      "jvm.conf?add-exports=java.base/jdk.internal.ref=ALL-UNNAMED",
      "jvm.conf?add-opens=java.base/java.lang=ALL-UNNAMED",
      "jvm.conf?add-modules=jdk.incubator.foreign",
      "jvm.conf?illegal-access=warn"
    };

    for (String option : moduleOptions) {
      var common = new Common("production", option, "log4j2.xml", "metrics.yaml", "runtime.properties");
      assertNotNull(common);
    }
  }

  @Test
  public void testJVMExperimentalOptions() {
    String[] experimentalOpts = {
      "jvm.conf?XX:+UnlockExperimentalVMOptions&XX:+UseJVMCICompiler",
      "jvm.conf?XX:+UnlockExperimentalVMOptions&XX:+EnableJVMCI",
      "jvm.conf?XX:+UnlockExperimentalVMOptions&XX:+UseEpsilonGC"
    };

    for (String opt : experimentalOpts) {
      var common = new Common("production", opt, "log4j2.xml", "metrics.yaml", "runtime.properties");
      assertNotNull(common);
    }
  }

  @Test
  public void testLog4j2ContextConfiguration() {
    var common = new Common(
      "production",
      "jvm.conf",
      "log4j2.xml?monitorInterval=30&shutdownHook=disable&shutdownTimeout=5000",
      "metrics.yaml",
      "runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.log4jConf().contains("monitorInterval"));
  }

  @Test
  public void testMetricDimensionsAndLabels() {
    var common = new Common(
      "production",
      "jvm.conf",
      "log4j2.xml",
      "metrics.yaml?dimensions=env:prod,cluster:main,region:us-east-1",
      "runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.metricConf().contains("dimensions="));
  }

  @Test
  public void testRuntimePropertiesWithSystemOverrides() {
    var common = new Common(
      "production",
      "jvm.conf",
      "log4j2.xml",
      "metrics.yaml",
      "runtime.properties?sys.druid.host=${HOSTNAME}&sys.druid.port=${PORT}"
    );

    assertNotNull(common);
    assertTrue(common.runtimeProperties().contains("${HOSTNAME}"));
  }

  @Test
  public void testAllJVMGCAlgorithms() {
    String[] gcAlgorithms = {
      "jvm.conf?XX:+UseG1GC&XX:G1HeapRegionSize=16m",
      "jvm.conf?XX:+UseZGC&XX:ZAllocationSpikeTolerance=5",
      "jvm.conf?XX:+UseShenandoahGC&XX:ShenandoahUncommitDelay=5000",
      "jvm.conf?XX:+UseParallelGC&XX:GCTimeRatio=99",
      "jvm.conf?XX:+UseSerialGC",
      "jvm.conf?XX:+UseConcMarkSweepGC&XX:+CMSParallelRemarkEnabled"
    };

    for (String gc : gcAlgorithms) {
      var common = new Common("production", gc, "log4j2.xml", "metrics.yaml", "runtime.properties");
      assertNotNull(common);
    }
  }

  @Test
  public void testLog4j2RoutingAppenders() {
    var common = new Common(
      "production",
      "jvm.conf",
      "log4j2-routing.xml?route=${ctx:environment}",
      "metrics.yaml",
      "runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.log4jConf().contains("routing"));
  }

  @Test
  public void testMetricAggregationStrategies() {
    String[] strategies = {
      "metrics.yaml?aggregation=sum",
      "metrics.yaml?aggregation=avg",
      "metrics.yaml?aggregation=min",
      "metrics.yaml?aggregation=max",
      "metrics.yaml?aggregation=count",
      "metrics.yaml?aggregation=p50,p95,p99"
    };

    for (String strategy : strategies) {
      var common = new Common("production", "jvm.conf", "log4j2.xml", strategy, "runtime.properties");
      assertNotNull(common);
      assertTrue(common.metricConf().contains("aggregation="));
    }
  }

  @Test
  public void testJVMCompilerOptions() {
    String[] compilerOpts = {
      "jvm.conf?XX:+TieredCompilation&XX:TieredStopAtLevel=1",
      "jvm.conf?XX:CICompilerCount=4&XX:CompileThreshold=10000",
      "jvm.conf?XX:+DoEscapeAnalysis&XX:+Inline"
    };

    for (String opt : compilerOpts) {
      var common = new Common("production", opt, "log4j2.xml", "metrics.yaml", "runtime.properties");
      assertNotNull(common);
    }
  }

  @Test
  public void testLog4j2ScriptConfiguration() {
    var common = new Common(
      "production",
      "jvm.conf",
      "log4j2-script.xml?scriptEngine=groovy",
      "metrics.yaml",
      "runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.log4jConf().contains("script"));
  }

  @Test
  public void testMetricRetentionPolicies() {
    String[] retentionPolicies = {
      "metrics.yaml?retention=1h",
      "metrics.yaml?retention=6h",
      "metrics.yaml?retention=24h",
      "metrics.yaml?retention=7d",
      "metrics.yaml?retention=30d"
    };

    for (String policy : retentionPolicies) {
      var common = new Common("production", "jvm.conf", "log4j2.xml", policy, "runtime.properties");
      assertNotNull(common);
      assertTrue(common.metricConf().contains("retention="));
    }
  }

  @Test
  public void testJVMSafepointTuning() {
    var common = new Common(
      "production",
      "jvm.conf?XX:+PrintSafepointStatistics&XX:PrintSafepointStatisticsCount=1",
      "log4j2.xml",
      "metrics.yaml",
      "runtime.properties"
    );

    assertNotNull(common);
    assertTrue(common.jvmConf().contains("Safepoint"));
  }

  @Test
  public void testRuntimeDruidSpecificProperties() {
    var common = new Common(
      "production",
      "jvm.conf",
      "log4j2.xml",
      "metrics.yaml",
      "runtime.properties?druid.processing.numThreads=16&druid.processing.buffer.sizeBytes=536870912"
    );

    assertNotNull(common);
    assertTrue(common.runtimeProperties().contains("druid.processing"));
  }

  @Test
  public void testComplexMultiLayerConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    var common = new Common(
      "production-us-east-1-cluster-main",
      "s3://config/prod/us-east-1/jvm-coordinator-tuned.conf?version=2.1.0",
      "s3://config/prod/us-east-1/log4j2-async-cloudwatch.xml?logGroup=/druid/coordinator",
      "consul://kv/metrics/prod/us-east-1/prometheus-detailed.yaml?interval=10s",
      "vault://secret/druid/prod/us-east-1/runtime.properties?version=latest"
    );

    assertNotNull(common);
    String yaml = mapper.writeValueAsString(common);
    var deserialized = mapper.readValue(yaml, Common.class);
    assertEquals(common, deserialized);
  }
}
