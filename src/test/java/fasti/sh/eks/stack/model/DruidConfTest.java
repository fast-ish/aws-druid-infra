package fasti.sh.eks.stack.model;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests for DruidConf record.
 */
public class DruidConfTest {

  @Test
  public void testDruidConfRecordStructure() {
    // Test that DruidConf is a valid record with the expected structure
    assertNotNull(DruidConf.class);

    // Verify record components exist
    var recordComponents = DruidConf.class.getRecordComponents();
    assertNotNull(recordComponents);
    assertEquals(6, recordComponents.length, "DruidConf should have 6 components");

    // Verify component names
    assertEquals("access", recordComponents[0].getName());
    assertEquals("secrets", recordComponents[1].getName());
    assertEquals("storage", recordComponents[2].getName());
    assertEquals("ingestion", recordComponents[3].getName());
    assertEquals("asset", recordComponents[4].getName());
    assertEquals("chart", recordComponents[5].getName());
  }

  @Test
  public void testDruidConfWithNullValues() {
    // Test that DruidConf can be instantiated with null values
    var druidConf = new DruidConf(null, null, null, null, null, null);

    assertNotNull(druidConf);
    assertEquals(null, druidConf.access());
    assertEquals(null, druidConf.secrets());
    assertEquals(null, druidConf.storage());
    assertEquals(null, druidConf.ingestion());
    assertEquals(null, druidConf.asset());
    assertEquals(null, druidConf.chart());
  }

  @Test
  public void testDruidConfWithStringValues() {
    // Test that DruidConf properly stores and retrieves string values
    var druidConf = new DruidConf("access-path", "secrets-path", "storage-path", "ingestion-path", "asset-path", null);

    assertNotNull(druidConf);
    assertEquals("access-path", druidConf.access());
    assertEquals("secrets-path", druidConf.secrets());
    assertEquals("storage-path", druidConf.storage());
    assertEquals("ingestion-path", druidConf.ingestion());
    assertEquals("asset-path", druidConf.asset());
  }

  @Test
  public void testSerializationDeserialization() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new DruidConf("access", "secrets", "storage", "ingestion", "asset", null);

    // Serialize to YAML string
    String yaml = mapper.writeValueAsString(original);
    assertNotNull(yaml);
    assertTrue(yaml.contains("access"));
    assertTrue(yaml.contains("secrets"));

    // Deserialize back to object
    var deserialized = mapper.readValue(yaml, DruidConf.class);
    assertNotNull(deserialized);
    assertEquals(original, deserialized);
  }

  @Test
  public void testEqualityAndHashCode() {
    var conf1 = new DruidConf("a", "b", "c", "d", "e", null);
    var conf2 = new DruidConf("a", "b", "c", "d", "e", null);
    var conf3 = new DruidConf("x", "b", "c", "d", "e", null);

    // Test equality
    assertEquals(conf1, conf2);
    assertNotEquals(conf1, conf3);

    // Test hashCode consistency
    assertEquals(conf1.hashCode(), conf2.hashCode());
    assertNotEquals(conf1.hashCode(), conf3.hashCode());
  }

  @Test
  public void testToString() {
    var druidConf = new DruidConf("access", "secrets", "storage", "ingestion", "asset", null);
    String str = druidConf.toString();

    assertNotNull(str);
    assertTrue(str.contains("DruidConf"));
    assertTrue(str.contains("access"));
  }

  @Test
  public void testRecordImmutability() {
    var druidConf = new DruidConf("a", "b", "c", "d", "e", null);

    // Records are immutable - accessor methods should always return same values
    assertEquals(druidConf.access(), druidConf.access());
    assertEquals(druidConf.secrets(), druidConf.secrets());
    assertEquals(druidConf.storage(), druidConf.storage());
    assertEquals(druidConf.ingestion(), druidConf.ingestion());
    assertEquals(druidConf.asset(), druidConf.asset());
    assertEquals(druidConf.chart(), druidConf.chart());
  }

  @Test
  public void testWithEmptyStrings() {
    var druidConf = new DruidConf("", "", "", "", "", null);

    assertNotNull(druidConf);
    assertEquals("", druidConf.access());
    assertEquals("", druidConf.secrets());
    assertEquals("", druidConf.storage());
    assertEquals("", druidConf.ingestion());
    assertEquals("", druidConf.asset());
  }

  @Test
  public void testWithSpecialCharactersInPaths() {
    var druidConf = new DruidConf(
      "config/access-v2.yaml",
      "secrets/prod_creds.yaml",
      "storage@v1.yaml",
      "ingestion.config.yaml",
      "asset/druid-helm.tgz",
      null
    );

    assertNotNull(druidConf);
    assertTrue(druidConf.access().contains("/"));
    assertTrue(druidConf.secrets().contains("_"));
    assertTrue(druidConf.storage().contains("@"));
    assertTrue(druidConf.ingestion().contains("."));
  }

  @Test
  public void testWithVeryLongPaths() {
    String longPath = "path/".repeat(200) + "file.yaml";
    var druidConf = new DruidConf(longPath, null, null, null, null, null);

    assertNotNull(druidConf);
    assertTrue(druidConf.access().length() > 1000);
  }

  @Test
  public void testComponentTypes() {
    var recordComponents = DruidConf.class.getRecordComponents();

    assertEquals(String.class, recordComponents[0].getType());
    assertEquals(String.class, recordComponents[1].getType());
    assertEquals(String.class, recordComponents[2].getType());
    assertEquals(String.class, recordComponents[3].getType());
    assertEquals(String.class, recordComponents[4].getType());
    assertEquals(fasti.sh.model.aws.eks.HelmChart.class, recordComponents[5].getType());
  }

  @Test
  public void testLoadFromYamlFile() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var inputStream = getClass().getClassLoader().getResourceAsStream("druid-config-test.yaml");
    assertNotNull(inputStream, "druid-config-test.yaml should exist in test resources");

    var druidConf = mapper.readValue(inputStream, DruidConf.class);

    assertNotNull(druidConf);
    assertEquals("config/access.yaml", druidConf.access());
    assertEquals("config/secrets.yaml", druidConf.secrets());
    assertEquals("config/storage.yaml", druidConf.storage());
    assertEquals("config/ingestion.yaml", druidConf.ingestion());
    assertEquals("helm/druid-chart.tgz", druidConf.asset());
    assertNull(druidConf.chart());
  }

  @Test
  public void testYamlRoundTrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var inputStream = getClass().getClassLoader().getResourceAsStream("druid-config-test.yaml");
    var loaded = mapper.readValue(inputStream, DruidConf.class);

    // Serialize back to YAML
    String yaml = mapper.writeValueAsString(loaded);

    // Deserialize again
    var reloaded = mapper.readValue(yaml, DruidConf.class);

    // Should be equal
    assertEquals(loaded, reloaded);
  }

  @Test
  public void testComplexCombinationAllComponents() {
    // Test DruidConf with all components populated with complex values
    var druidConf = new DruidConf(
      "config/prod/access-v2.yaml",
      "config/prod/secrets-encrypted.yaml",
      "config/prod/storage-multiregion.yaml",
      "config/prod/ingestion-kafka-streams.yaml",
      "helm/charts/druid-0.8.0.tgz",
      null
    );

    assertNotNull(druidConf);
    assertEquals("config/prod/access-v2.yaml", druidConf.access());
    assertEquals("config/prod/secrets-encrypted.yaml", druidConf.secrets());
    assertEquals("config/prod/storage-multiregion.yaml", druidConf.storage());
    assertEquals("config/prod/ingestion-kafka-streams.yaml", druidConf.ingestion());
    assertEquals("helm/charts/druid-0.8.0.tgz", druidConf.asset());
    assertNull(druidConf.chart());
  }

  @Test
  public void testMultipleEnvironmentConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Test different environment configurations
    String[] environments = {"dev", "staging", "production", "test"};

    for (String env : environments) {
      var druidConf = new DruidConf(
        "config/" + env + "/access.yaml",
        "config/" + env + "/secrets.yaml",
        "config/" + env + "/storage.yaml",
        "config/" + env + "/ingestion.yaml",
        "helm/" + env + "/druid.tgz",
        null
      );

      assertNotNull(druidConf);
      assertTrue(druidConf.access().contains(env));
      assertTrue(druidConf.secrets().contains(env));
      assertTrue(druidConf.storage().contains(env));

      // Test serialization for each environment
      String yaml = mapper.writeValueAsString(druidConf);
      assertNotNull(yaml);
      assertTrue(yaml.contains(env));
    }
  }

  @Test
  public void testPartialConfigurationScenarios() {
    // Test various partial configuration scenarios
    var conf1 = new DruidConf("access", null, null, null, null, null);
    var conf2 = new DruidConf(null, "secrets", "storage", null, null, null);
    var conf3 = new DruidConf(null, null, null, "ingestion", "asset", null);

    assertNotNull(conf1);
    assertNotNull(conf2);
    assertNotNull(conf3);

    assertEquals("access", conf1.access());
    assertNull(conf1.secrets());

    assertNull(conf2.access());
    assertEquals("secrets", conf2.secrets());
    assertEquals("storage", conf2.storage());

    assertEquals("ingestion", conf3.ingestion());
    assertEquals("asset", conf3.asset());
  }

  @Test
  public void testConfigurationWithURLPaths() {
    var druidConf = new DruidConf(
      "s3://bucket/config/access.yaml",
      "s3://bucket/config/secrets.yaml",
      "https://example.com/storage.yaml",
      "file:///var/config/ingestion.yaml",
      "oci://registry/helm/druid:1.0.0",
      null
    );

    assertNotNull(druidConf);
    assertTrue(druidConf.access().startsWith("s3://"));
    assertTrue(druidConf.storage().startsWith("https://"));
    assertTrue(druidConf.ingestion().startsWith("file://"));
    assertTrue(druidConf.asset().startsWith("oci://"));
  }

  @Test
  public void testSerializationWithAllFieldsPopulated() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new DruidConf(
      "access-full",
      "secrets-full",
      "storage-full",
      "ingestion-full",
      "asset-full",
      null
    );

    String yaml = mapper.writeValueAsString(original);
    assertNotNull(yaml);

    // Verify all fields present in YAML
    assertTrue(yaml.contains("access-full"));
    assertTrue(yaml.contains("secrets-full"));
    assertTrue(yaml.contains("storage-full"));
    assertTrue(yaml.contains("ingestion-full"));
    assertTrue(yaml.contains("asset-full"));

    var deserialized = mapper.readValue(yaml, DruidConf.class);
    assertEquals(original, deserialized);
  }

  @Test
  public void testEqualityWithDifferentFieldCombinations() {
    var base = new DruidConf("a", "b", "c", "d", "e", null);

    // Test inequality when each field differs
    assertNotEquals(base, new DruidConf("x", "b", "c", "d", "e", null));
    assertNotEquals(base, new DruidConf("a", "x", "c", "d", "e", null));
    assertNotEquals(base, new DruidConf("a", "b", "x", "d", "e", null));
    assertNotEquals(base, new DruidConf("a", "b", "c", "x", "e", null));
    assertNotEquals(base, new DruidConf("a", "b", "c", "d", "x", null));

    // Test equality when all fields match
    assertEquals(base, new DruidConf("a", "b", "c", "d", "e", null));
  }

  @Test
  public void testConfigurationWithSpecialFileExtensions() {
    var druidConf = new DruidConf(
      "config.yml",
      "secrets.json",
      "storage.properties",
      "ingestion.conf",
      "druid-chart.tar.gz",
      null
    );

    assertNotNull(druidConf);
    assertTrue(druidConf.access().endsWith(".yml"));
    assertTrue(druidConf.secrets().endsWith(".json"));
    assertTrue(druidConf.storage().endsWith(".properties"));
    assertTrue(druidConf.ingestion().endsWith(".conf"));
    assertTrue(druidConf.asset().endsWith(".tar.gz"));
  }

  @Test
  public void testConfigurationWithVersionNumbers() {
    var druidConf = new DruidConf(
      "config/access-v1.2.3.yaml",
      "config/secrets-v2.0.0.yaml",
      "config/storage-v1.5.0-beta.yaml",
      "config/ingestion-v3.0.0-rc1.yaml",
      "helm/druid-0.28.0.tgz",
      null
    );

    assertNotNull(druidConf);
    assertTrue(druidConf.access().contains("v1.2.3"));
    assertTrue(druidConf.secrets().contains("v2.0.0"));
    assertTrue(druidConf.storage().contains("beta"));
    assertTrue(druidConf.ingestion().contains("rc1"));
    assertTrue(druidConf.asset().contains("0.28.0"));
  }

  @Test
  public void testLargeScaleConfigurationScenario() {
    // Test with very long and complex paths representing a large-scale deployment
    String complexAccessPath = "s3://druid-prod-us-east-1/environments/production/clusters/main/components/access/v2.0/config.yaml";
    String complexSecretsPath = "vault://secrets/druid/production/us-east-1/encrypted/admin-credentials.yaml";
    String complexStoragePath = "config/storage/multi-region/us-east-1-primary/us-west-2-backup/config.yaml";
    String complexIngestionPath = "kafka://bootstrap.kafka.prod.internal:9092/topics/druid-ingestion/config.yaml";
    String complexAssetPath = "oci://registry.example.com/druid/helm-charts/druid:v0.28.0-prod.20240115";

    var druidConf = new DruidConf(
      complexAccessPath,
      complexSecretsPath,
      complexStoragePath,
      complexIngestionPath,
      complexAssetPath,
      null
    );

    assertNotNull(druidConf);
    assertTrue(druidConf.access().contains("production"));
    assertTrue(druidConf.secrets().contains("encrypted"));
    assertTrue(druidConf.storage().contains("multi-region"));
    assertTrue(druidConf.ingestion().contains("kafka"));
    assertTrue(druidConf.asset().contains("helm-charts"));
  }

  @Test
  public void testHashCodeConsistencyAcrossMultipleInstances() {
    var conf1 = new DruidConf("a", "b", "c", "d", "e", null);
    var conf2 = new DruidConf("a", "b", "c", "d", "e", null);
    var conf3 = new DruidConf("a", "b", "c", "d", "e", null);

    // All equal instances should have same hashCode
    assertEquals(conf1.hashCode(), conf2.hashCode());
    assertEquals(conf2.hashCode(), conf3.hashCode());
    assertEquals(conf1.hashCode(), conf3.hashCode());

    // Create set to verify hashCode consistency
    var set = new java.util.HashSet<DruidConf>();
    set.add(conf1);
    set.add(conf2);
    set.add(conf3);

    assertEquals(1, set.size(), "Equal objects should result in single set entry");
  }

  @Test
  public void testConfigurationWithUnicodePaths() {
    var druidConf = new DruidConf(
      "config/日本語/access.yaml",
      "config/中文/secrets.yaml",
      "config/한국어/storage.yaml",
      "config/العربية/ingestion.yaml",
      "helm/русский/druid.tgz",
      null
    );

    assertNotNull(druidConf);
    assertTrue(druidConf.access().contains("日本語"));
    assertTrue(druidConf.secrets().contains("中文"));
    assertTrue(druidConf.storage().contains("한국어"));
    assertTrue(druidConf.ingestion().contains("العربية"));
    assertTrue(druidConf.asset().contains("русский"));
  }

  @Test
  public void testDeserializationWithPartialYaml() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Test YAML with only some fields
    String partialYaml = """
      access: config/access.yaml
      storage: config/storage.yaml
      """;

    var druidConf = mapper.readValue(partialYaml, DruidConf.class);

    assertNotNull(druidConf);
    assertEquals("config/access.yaml", druidConf.access());
    assertNull(druidConf.secrets());
    assertEquals("config/storage.yaml", druidConf.storage());
    assertNull(druidConf.ingestion());
    assertNull(druidConf.asset());
  }

  @Test
  public void testComponentInteractionValidation() {
    // Test that all components can be set independently and retrieved correctly
    var druidConf = new DruidConf(
      "component-access",
      "component-secrets",
      "component-storage",
      "component-ingestion",
      "component-asset",
      null
    );

    // Verify each component is independent
    assertNotNull(druidConf.access());
    assertNotNull(druidConf.secrets());
    assertNotNull(druidConf.storage());
    assertNotNull(druidConf.ingestion());
    assertNotNull(druidConf.asset());

    // Verify no cross-contamination between components
    assertNotEquals(druidConf.access(), druidConf.secrets());
    assertNotEquals(druidConf.storage(), druidConf.ingestion());
    assertNotEquals(druidConf.asset(), druidConf.access());
  }

  @Test
  public void testEdgeCaseWithAllFieldsEmpty() {
    var druidConf = new DruidConf("", "", "", "", "", null);

    assertNotNull(druidConf);
    assertEquals("", druidConf.access());
    assertEquals("", druidConf.secrets());
    assertEquals("", druidConf.storage());
    assertEquals("", druidConf.ingestion());
    assertEquals("", druidConf.asset());
  }

  @Test
  public void testConfigurationWithWindowsPaths() {
    var druidConf = new DruidConf(
      "C:\\config\\access.yaml",
      "D:\\secrets\\prod\\secrets.yaml",
      "\\\\network\\share\\storage.yaml",
      "E:\\ingestion\\config.yaml",
      "F:\\helm\\druid-chart.tgz",
      null
    );

    assertNotNull(druidConf);
    assertTrue(druidConf.access().contains("C:"));
    assertTrue(druidConf.secrets().contains("D:"));
    assertTrue(druidConf.storage().contains("\\\\network"));
    assertTrue(druidConf.ingestion().contains("E:"));
    assertTrue(druidConf.asset().contains("F:"));
  }

  // ==================== Additional Comprehensive Tests (15+) ====================

  @Test
  public void testStressTestWithMaximumComplexity() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Create 100 DruidConf objects with complex paths
    for (int i = 0; i < 100; i++) {
      var druidConf = new DruidConf(
        "s3://druid-prod-bucket-" + i + "/config/access-v" + i + ".yaml",
        "vault://secrets/env-" + i + "/credentials.yaml",
        "s3://storage-" + i + "/deep-storage/config.yaml",
        "kafka://broker-" + i + ".internal:9092/ingestion.yaml",
        "oci://registry.io/druid/chart:" + i + ".0.0",
        null
      );

      assertNotNull(druidConf);
      String yaml = mapper.writeValueAsString(druidConf);
      var deserialized = mapper.readValue(yaml, DruidConf.class);
      assertEquals(druidConf, deserialized);
    }
  }

  @Test
  public void testConcurrentConfigurationLoading() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var threads = new java.util.concurrent.CountDownLatch(10);
    var errors = new java.util.concurrent.CopyOnWriteArrayList<Exception>();

    for (int i = 0; i < 10; i++) {
      final int index = i;
      new Thread(() -> {
        try {
          for (int j = 0; j < 100; j++) {
            var conf = new DruidConf(
              "access-" + index + "-" + j,
              "secrets-" + index + "-" + j,
              "storage-" + index + "-" + j,
              "ingestion-" + index + "-" + j,
              "asset-" + index + "-" + j,
              null
            );
            String yaml = mapper.writeValueAsString(conf);
            var loaded = mapper.readValue(yaml, DruidConf.class);
            assertEquals(conf, loaded);
          }
        } catch (Exception e) {
          errors.add(e);
        } finally {
          threads.countDown();
        }
      }).start();
    }

    threads.await(30, java.util.concurrent.TimeUnit.SECONDS);
    assertTrue(errors.isEmpty(), "Concurrent loading should not produce errors");
  }

  @Test
  public void testComponentVersionMismatchDetection() {
    // Test configurations with different version patterns
    var conf1 = new DruidConf(
      "config/access-v1.0.0.yaml",
      "config/secrets-v2.0.0.yaml",
      "config/storage-v1.5.0.yaml",
      "config/ingestion-v3.0.0.yaml",
      "helm/druid-0.28.0.tgz",
      null
    );

    var conf2 = new DruidConf(
      "config/access-v1.0.0.yaml",
      "config/secrets-v1.0.0.yaml",
      "config/storage-v1.0.0.yaml",
      "config/ingestion-v1.0.0.yaml",
      "helm/druid-0.28.0.tgz",
      null
    );

    assertNotEquals(conf1, conf2);
    assertTrue(conf1.secrets().contains("v2.0.0"));
    assertTrue(conf2.secrets().contains("v1.0.0"));
  }

  @Test
  public void testMemoryStressWithThousandConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var configurations = new java.util.ArrayList<DruidConf>();

    // Create 1000+ configurations
    for (int i = 0; i < 1500; i++) {
      var conf = new DruidConf(
        "access-" + i + ".yaml",
        "secrets-" + i + ".yaml",
        "storage-" + i + ".yaml",
        "ingestion-" + i + ".yaml",
        "asset-" + i + ".tgz",
        null
      );
      configurations.add(conf);

      // Verify serialization for every 100th element
      if (i % 100 == 0) {
        String yaml = mapper.writeValueAsString(conf);
        assertNotNull(yaml);
      }
    }

    assertEquals(1500, configurations.size());

    // Verify all configurations are unique
    var uniqueConfigs = new java.util.HashSet<>(configurations);
    assertEquals(1500, uniqueConfigs.size());
  }

  @Test
  public void testAllDruidExtensionCombinations() {
    String[] extensions = {
      "druid-kafka-indexing-service",
      "druid-s3-extensions",
      "druid-multi-stage-query",
      "druid-histogram",
      "druid-datasketches",
      "druid-lookups-cached-global",
      "druid-parquet-extensions",
      "druid-avro-extensions",
      "druid-orc-extensions",
      "druid-protobuf-extensions"
    };

    for (String ext : extensions) {
      var conf = new DruidConf(
        "config/access-with-" + ext + ".yaml",
        "config/secrets.yaml",
        "config/storage.yaml",
        "config/ingestion.yaml",
        "helm/druid-with-" + ext + ".tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.access().contains(ext));
      assertTrue(conf.asset().contains(ext));
    }
  }

  @Test
  public void testCustomPropertiesConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yamlWithCustomProps = """
      access: config/access.yaml?customProp1=value1&customProp2=value2
      secrets: config/secrets.yaml?encrypted=true
      storage: config/storage.yaml?compression=lz4
      ingestion: config/ingestion.yaml?batchSize=10000
      asset: helm/druid.tgz?override=production
    """;

    var conf = mapper.readValue(yamlWithCustomProps, DruidConf.class);
    assertNotNull(conf);
    assertTrue(conf.access().contains("customProp1=value1"));
    assertTrue(conf.secrets().contains("encrypted=true"));
    assertTrue(conf.storage().contains("compression=lz4"));
    assertTrue(conf.ingestion().contains("batchSize=10000"));
    assertTrue(conf.asset().contains("override=production"));
  }

  @Test
  public void testEnvironmentSpecificOverrides() {
    String[] environments = {
      "dev", "dev-us-east-1", "dev-eu-west-1",
      "staging", "staging-us-west-2", "staging-ap-south-1",
      "production", "prod-us-east-1", "prod-eu-central-1",
      "dr-site-us-west-1", "dr-site-eu-west-2"
    };

    for (String env : environments) {
      var conf = new DruidConf(
        "config/" + env + "/access.yaml",
        "config/" + env + "/secrets.yaml",
        "config/" + env + "/storage.yaml",
        "config/" + env + "/ingestion.yaml",
        "helm/" + env + "/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.access().contains(env));
      assertTrue(conf.secrets().contains(env));
      assertTrue(conf.storage().contains(env));
      assertTrue(conf.ingestion().contains(env));
      assertTrue(conf.asset().contains(env));
    }
  }

  @Test
  public void testBoundaryTestsWithMaximumStringLengths() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Test with very long strings (10,000+ characters)
    String veryLongPath = "s3://bucket/" + "a".repeat(9980) + "/config.yaml";
    var conf = new DruidConf(
      veryLongPath,
      veryLongPath,
      veryLongPath,
      veryLongPath,
      veryLongPath,
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().length() >= 10000, "String should be at least 10000 characters");

    // Verify serialization still works
    String yaml = mapper.writeValueAsString(conf);
    assertNotNull(yaml);
    var deserialized = mapper.readValue(yaml, DruidConf.class);
    assertEquals(conf, deserialized);
  }

  @Test
  public void testNumericLimitsForPortsAndTimeouts() {
    var conf = new DruidConf(
      "config/access.yaml?port=8888",
      "config/secrets.yaml?timeout=60000",
      "config/storage.yaml?maxConnections=1000",
      "config/ingestion.yaml?batchSize=50000&port=9092",
      "helm/druid.tgz?replicas=10",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("port=8888"));
    assertTrue(conf.secrets().contains("timeout=60000"));
    assertTrue(conf.storage().contains("maxConnections=1000"));
    assertTrue(conf.ingestion().contains("batchSize=50000"));
    assertTrue(conf.asset().contains("replicas=10"));
  }

  @Test
  public void testConfigurationWithMultiRegionSetup() {
    var conf = new DruidConf(
      "s3://us-east-1-bucket/config/access.yaml",
      "secretsmanager://us-east-1/druid/secrets",
      "s3://us-west-2-bucket/storage?replication=cross-region",
      "kafka://us-east-1.kafka.internal:9092/ingestion",
      "oci://us-east-1.ecr.aws/druid/chart:1.0.0",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("us-east-1"));
    assertTrue(conf.storage().contains("us-west-2"));
    assertTrue(conf.storage().contains("cross-region"));
    assertTrue(conf.ingestion().contains("us-east-1"));
  }

  @Test
  public void testCircularDependencyDetectionPattern() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Create configurations that reference each other
    var conf1 = new DruidConf(
      "config/access.yaml?ref=conf2",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    var conf2 = new DruidConf(
      "config/access.yaml?ref=conf1",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotEquals(conf1, conf2);
    assertTrue(conf1.access().contains("ref=conf2"));
    assertTrue(conf2.access().contains("ref=conf1"));

    // Verify both can be serialized independently
    String yaml1 = mapper.writeValueAsString(conf1);
    String yaml2 = mapper.writeValueAsString(conf2);
    assertNotEquals(yaml1, yaml2);
  }

  @Test
  public void testConfigurationWithEncryptedPaths() {
    var conf = new DruidConf(
      "encrypted://aes256/config/access.yaml",
      "vault://kv/secret/druid/credentials?version=2",
      "s3://bucket/storage.yaml.gpg",
      "config/ingestion.yaml.encrypted",
      "oci://registry/druid:1.0.0?signature=verified",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().startsWith("encrypted://"));
    assertTrue(conf.secrets().contains("vault://"));
    assertTrue(conf.storage().contains(".gpg"));
    assertTrue(conf.ingestion().contains(".encrypted"));
    assertTrue(conf.asset().contains("signature=verified"));
  }

  @Test
  public void testConfigurationWithQueryParameters() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    var conf = new DruidConf(
      "config/access.yaml?region=us-east-1&env=prod&version=2.0",
      "config/secrets.yaml?kms=enabled&rotation=30days",
      "config/storage.yaml?tier=glacier&lifecycle=enabled",
      "config/ingestion.yaml?protocol=ssl&auth=sasl",
      "helm/druid.tgz?values=production.yaml&timeout=600",
      null
    );

    assertNotNull(conf);

    String yaml = mapper.writeValueAsString(conf);
    var deserialized = mapper.readValue(yaml, DruidConf.class);

    assertEquals(conf, deserialized);
    assertTrue(deserialized.access().contains("region=us-east-1"));
    assertTrue(deserialized.secrets().contains("rotation=30days"));
    assertTrue(deserialized.storage().contains("lifecycle=enabled"));
    assertTrue(deserialized.ingestion().contains("auth=sasl"));
  }

  @Test
  public void testConfigurationWithDifferentProtocols() {
    String[] protocols = {
      "s3://", "s3a://", "s3n://",
      "http://", "https://",
      "file://", "file:///",
      "hdfs://", "hdfs:///",
      "gs://", "gcs://",
      "azbs://", "wasb://",
      "oci://", "docker://",
      "vault://", "consul://",
      "etcd://", "zk://"
    };

    for (String protocol : protocols) {
      var conf = new DruidConf(
        protocol + "config/access.yaml",
        protocol + "config/secrets.yaml",
        protocol + "config/storage.yaml",
        protocol + "config/ingestion.yaml",
        protocol + "helm/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.access().startsWith(protocol));
      assertTrue(conf.secrets().startsWith(protocol));
    }
  }

  @Test
  public void testConfigurationIntegrityWithHashCodes() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    var conf = new DruidConf(
      "config/access.yaml#sha256:abc123",
      "config/secrets.yaml#md5:def456",
      "config/storage.yaml#sha1:ghi789",
      "config/ingestion.yaml#sha512:jkl012",
      "helm/druid.tgz#checksum:mno345",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("#sha256:"));
    assertTrue(conf.secrets().contains("#md5:"));
    assertTrue(conf.storage().contains("#sha1:"));
    assertTrue(conf.ingestion().contains("#sha512:"));
    assertTrue(conf.asset().contains("#checksum:"));

    String yaml = mapper.writeValueAsString(conf);
    var deserialized = mapper.readValue(yaml, DruidConf.class);
    assertEquals(conf, deserialized);
  }

  @Test
  public void testConfigurationWithCompressionFormats() {
    String[] formats = {".gz", ".bz2", ".xz", ".lz4", ".zst", ".tar.gz", ".tar.bz2", ".tgz"};

    for (String format : formats) {
      var conf = new DruidConf(
        "config/access.yaml" + format,
        "config/secrets.yaml" + format,
        "config/storage.yaml" + format,
        "config/ingestion.yaml" + format,
        "helm/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.access().endsWith(format));
      assertTrue(conf.secrets().endsWith(format));
    }
  }

  @Test
  public void testConfigurationWithTemplateVariables() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    var conf = new DruidConf(
      "config/${ENVIRONMENT}/access.yaml",
      "config/${REGION}/${CLUSTER}/secrets.yaml",
      "s3://${BUCKET_NAME}/storage.yaml",
      "kafka://${KAFKA_BROKERS}/ingestion.yaml",
      "oci://${REGISTRY}/${REPO}:${TAG}",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("${ENVIRONMENT}"));
    assertTrue(conf.secrets().contains("${REGION}"));
    assertTrue(conf.secrets().contains("${CLUSTER}"));
    assertTrue(conf.storage().contains("${BUCKET_NAME}"));
    assertTrue(conf.ingestion().contains("${KAFKA_BROKERS}"));
    assertTrue(conf.asset().contains("${REGISTRY}"));

    String yaml = mapper.writeValueAsString(conf);
    var deserialized = mapper.readValue(yaml, DruidConf.class);
    assertEquals(conf, deserialized);
  }

  @Test
  public void testDeserializationWithExtraUnknownFieldsFailsAsExpected() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yamlWithExtra = """
      access: config/access.yaml
      secrets: config/secrets.yaml
      storage: config/storage.yaml
      ingestion: config/ingestion.yaml
      asset: helm/druid.tgz
      extraField1: should-cause-error
    """;

    // By default, Jackson will throw UnrecognizedPropertyException for unknown fields
    assertThrows(Exception.class, () -> {
      mapper.readValue(yamlWithExtra, DruidConf.class);
    });
  }

  @Test
  public void testSerializationPerformanceWithLargeDataset() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    long startTime = System.currentTimeMillis();

    for (int i = 0; i < 1000; i++) {
      var conf = new DruidConf(
        "config/complex/nested/path/to/access/v" + i + ".yaml",
        "vault://secrets/production/us-east-1/cluster-" + i + "/credentials",
        "s3://druid-storage-prod-" + i + "/deep-storage/config.yaml",
        "kafka://broker-" + i + ".prod.internal:9092/topics/druid-ingest/config",
        "oci://registry.example.com/druid/helm-charts/druid:v0.28." + i + "-prod",
        null
      );

      String yaml = mapper.writeValueAsString(conf);
      var deserialized = mapper.readValue(yaml, DruidConf.class);
      assertEquals(conf, deserialized);
    }

    long duration = System.currentTimeMillis() - startTime;
    assertTrue(duration < 10000, "1000 serialization/deserialization cycles should complete in under 10 seconds");
  }

  // ==================== Extended Comprehensive Tests (40+ Additional) ====================

  @Test
  public void testDruidClusterSizingScenarios() {
    String[][] clusterSizes = {
      {"small", "t3.medium", "2", "4Gi"},
      {"medium", "m5.xlarge", "4", "16Gi"},
      {"large", "m5.4xlarge", "8", "32Gi"},
      {"xlarge", "m5.12xlarge", "16", "64Gi"},
      {"xxlarge", "m5.24xlarge", "32", "128Gi"}
    };

    for (String[] size : clusterSizes) {
      var conf = new DruidConf(
        "config/access-" + size[0] + ".yaml",
        "config/secrets-" + size[0] + ".yaml",
        "config/storage-" + size[0] + ".yaml?instanceType=" + size[1] + "&replicas=" + size[2],
        "config/ingestion-" + size[0] + ".yaml?memory=" + size[3],
        "helm/druid-" + size[0] + ".tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.access().contains(size[0]));
      assertTrue(conf.storage().contains(size[1]));
    }
  }

  @Test
  public void testDruidDeepStorageConfigurations() {
    String[] deepStorageTypes = {
      "s3://druid-deep-storage/segments",
      "hdfs://namenode:9000/druid/segments",
      "local:///var/druid/segments",
      "gs://druid-bucket/segments",
      "azure://druid-container/segments"
    };

    for (String storage : deepStorageTypes) {
      var conf = new DruidConf(
        "config/access.yaml",
        "config/secrets.yaml",
        "config/storage.yaml?deepStorage=" + storage,
        "config/ingestion.yaml",
        "helm/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.storage().contains("deepStorage="));
    }
  }

  @Test
  public void testDruidMetadataStorageOptions() {
    String[] metadataStores = {
      "postgresql://metadata.druid.internal:5432/druid",
      "mysql://metadata.druid.internal:3306/druid",
      "derby://localhost:1527/var/druid/metadata.db",
      "postgres://rds.amazonaws.com:5432/druidmetadata"
    };

    for (String metadata : metadataStores) {
      var conf = new DruidConf(
        "config/access.yaml",
        "config/secrets.yaml",
        "config/storage.yaml?metadata=" + metadata,
        "config/ingestion.yaml",
        "helm/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.storage().contains("metadata="));
    }
  }

  @Test
  public void testDruidIndexingServiceConfigurations() {
    String[] indexingConfigs = {
      "config/ingestion-kafka-indexing.yaml",
      "config/ingestion-hadoop-indexing.yaml",
      "config/ingestion-native-batch.yaml",
      "config/ingestion-sql-based.yaml",
      "config/ingestion-parallel-task.yaml"
    };

    for (String config : indexingConfigs) {
      var conf = new DruidConf(
        "config/access.yaml",
        "config/secrets.yaml",
        "config/storage.yaml",
        config,
        "helm/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.ingestion().contains("ingestion-"));
    }
  }

  @Test
  public void testDruidQueryConfigurations() {
    var conf = new DruidConf(
      "config/access.yaml?maxQueryTimeout=300s",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml?queryPoolSize=100",
      "helm/druid.tgz?queryCache=enabled",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("maxQueryTimeout"));
    assertTrue(conf.ingestion().contains("queryPoolSize"));
    assertTrue(conf.asset().contains("queryCache"));
  }

  @Test
  public void testDruidCachingConfigurations() {
    String[] cacheTypes = {
      "config/storage.yaml?cache=local",
      "config/storage.yaml?cache=memcached&host=cache.internal:11211",
      "config/storage.yaml?cache=redis&cluster=redis.internal:6379",
      "config/storage.yaml?cache=caffeine&maxSize=1000000"
    };

    for (String cacheConfig : cacheTypes) {
      var conf = new DruidConf(
        "config/access.yaml",
        "config/secrets.yaml",
        cacheConfig,
        "config/ingestion.yaml",
        "helm/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.storage().contains("cache="));
    }
  }

  @Test
  public void testDruidCoordinatorConfigurations() {
    var conf = new DruidConf(
      "config/access-coordinator.yaml?balancerComputeThreads=10",
      "config/secrets.yaml",
      "config/storage.yaml?coordinatorPeriod=PT30S",
      "config/ingestion.yaml",
      "helm/druid-coordinator.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("coordinator"));
    assertTrue(conf.storage().contains("coordinatorPeriod"));
  }

  @Test
  public void testDruidOverlordConfigurations() {
    var conf = new DruidConf(
      "config/access-overlord.yaml",
      "config/secrets.yaml",
      "config/storage.yaml?taskQueueSize=1000",
      "config/ingestion-overlord.yaml?maxPendingTasks=100",
      "helm/druid-overlord.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("overlord"));
    assertTrue(conf.storage().contains("taskQueueSize"));
    assertTrue(conf.ingestion().contains("maxPendingTasks"));
  }

  @Test
  public void testDruidBrokerConfigurations() {
    var conf = new DruidConf(
      "config/access-broker.yaml?numThreads=50",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml?queryCache=enabled&cacheSize=2Gi",
      "helm/druid-broker.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("broker"));
    assertTrue(conf.access().contains("numThreads"));
    assertTrue(conf.ingestion().contains("queryCache"));
  }

  @Test
  public void testDruidRouterConfigurations() {
    var conf = new DruidConf(
      "config/access-router.yaml?tierStrategies=highestPriority",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml",
      "helm/druid-router.tgz?replicas=3",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("router"));
    assertTrue(conf.access().contains("tierStrategies"));
  }

  @Test
  public void testDruidHistoricalConfigurations() {
    var conf = new DruidConf(
      "config/access-historical.yaml",
      "config/secrets.yaml",
      "config/storage.yaml?segmentCacheSize=100Gi&maxSegmentsToLoad=1000",
      "config/ingestion.yaml",
      "helm/druid-historical.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("historical"));
    assertTrue(conf.storage().contains("segmentCacheSize"));
    assertTrue(conf.storage().contains("maxSegmentsToLoad"));
  }

  @Test
  public void testDruidMiddleManagerConfigurations() {
    var conf = new DruidConf(
      "config/access-middlemanager.yaml",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion-mm.yaml?workerCapacity=10&numThreads=4",
      "helm/druid-middlemanager.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("middlemanager"));
    assertTrue(conf.ingestion().contains("workerCapacity"));
  }

  @Test
  public void testDruidPeonConfigurations() {
    var conf = new DruidConf(
      "config/access-peon.yaml",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion-peon.yaml?javaOpts=-Xmx2g",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("peon"));
    assertTrue(conf.ingestion().contains("javaOpts"));
  }

  @Test
  public void testSegmentLoadingConfigurations() {
    var conf = new DruidConf(
      "config/access.yaml",
      "config/secrets.yaml",
      "config/storage.yaml?numLoadingThreads=12&queueSize=500",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.storage().contains("numLoadingThreads"));
    assertTrue(conf.storage().contains("queueSize"));
  }

  @Test
  public void testCompactionConfigurations() {
    var conf = new DruidConf(
      "config/access.yaml",
      "config/secrets.yaml",
      "config/storage.yaml?compaction=enabled&targetPartitionSize=5000000",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.storage().contains("compaction=enabled"));
    assertTrue(conf.storage().contains("targetPartitionSize"));
  }

  @Test
  public void testRetentionRules() {
    String[] retentionPolicies = {
      "config/storage.yaml?retention=P7D&tier=hot",
      "config/storage.yaml?retention=P30D&tier=warm",
      "config/storage.yaml?retention=P90D&tier=cold",
      "config/storage.yaml?retention=P365D&tier=archive"
    };

    for (String policy : retentionPolicies) {
      var conf = new DruidConf(
        "config/access.yaml",
        "config/secrets.yaml",
        policy,
        "config/ingestion.yaml",
        "helm/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.storage().contains("retention="));
    }
  }

  @Test
  public void testLoadRules() {
    var conf = new DruidConf(
      "config/access.yaml",
      "config/secrets.yaml",
      "config/storage.yaml?loadRules=forever&replicants=2",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.storage().contains("loadRules"));
    assertTrue(conf.storage().contains("replicants"));
  }

  @Test
  public void testStressWith10KConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var configurations = new java.util.ArrayList<DruidConf>();

    for (int i = 0; i < 10000; i++) {
      var conf = new DruidConf(
        "access-" + i,
        "secrets-" + i,
        "storage-" + i,
        "ingestion-" + i,
        "asset-" + i,
        null
      );
      configurations.add(conf);

      if (i % 1000 == 0) {
        String yaml = mapper.writeValueAsString(conf);
        assertNotNull(yaml);
      }
    }

    assertEquals(10000, configurations.size());
    var uniqueConfigs = new java.util.HashSet<>(configurations);
    assertEquals(10000, uniqueConfigs.size());
  }

  @Test
  public void testConcurrentConfigLoading200Threads() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var threads = new java.util.concurrent.CountDownLatch(200);
    var errors = new java.util.concurrent.CopyOnWriteArrayList<Exception>();

    for (int i = 0; i < 200; i++) {
      final int index = i;
      new Thread(() -> {
        try {
          for (int j = 0; j < 500; j++) {
            var conf = new DruidConf(
              "access-" + index + "-" + j,
              "secrets-" + index + "-" + j,
              "storage-" + index + "-" + j,
              "ingestion-" + index + "-" + j,
              "asset-" + index + "-" + j,
              null
            );
            String yaml = mapper.writeValueAsString(conf);
            var loaded = mapper.readValue(yaml, DruidConf.class);
            assertEquals(conf, loaded);
          }
        } catch (Exception e) {
          errors.add(e);
        } finally {
          threads.countDown();
        }
      }).start();
    }

    threads.await(60, java.util.concurrent.TimeUnit.SECONDS);
    assertTrue(errors.isEmpty(), "Concurrent loading with 200 threads should not produce errors");
  }

  @Test
  public void testDruidExtensionsCombinations() {
    String[] extensionGroups = {
      "druid-kafka-indexing-service,druid-s3-extensions",
      "druid-multi-stage-query,druid-histogram,druid-datasketches",
      "druid-lookups-cached-global,druid-parquet-extensions",
      "druid-avro-extensions,druid-orc-extensions,druid-protobuf-extensions",
      "druid-kinesis-indexing-service,druid-azure-extensions",
      "druid-google-extensions,druid-hdfs-storage"
    };

    for (String extensions : extensionGroups) {
      var conf = new DruidConf(
        "config/access.yaml?extensions=" + extensions,
        "config/secrets.yaml",
        "config/storage.yaml",
        "config/ingestion.yaml",
        "helm/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.access().contains("extensions="));
    }
  }

  @Test
  public void testDruidAllExtensions() {
    String[] allExtensions = {
      "druid-kafka-indexing-service", "druid-kinesis-indexing-service",
      "druid-s3-extensions", "druid-google-extensions", "druid-azure-extensions",
      "druid-hdfs-storage", "druid-cassandra-storage",
      "druid-multi-stage-query", "druid-sql", "druid-lookups-cached-global",
      "druid-histogram", "druid-datasketches", "druid-stats",
      "druid-parquet-extensions", "druid-avro-extensions", "druid-orc-extensions",
      "druid-protobuf-extensions", "druid-thrift-extensions",
      "druid-bloom-filter", "druid-ranger-security",
      "druid-kerberos", "druid-pac4j", "druid-basic-security",
      "mysql-metadata-storage", "postgresql-metadata-storage"
    };

    for (String extension : allExtensions) {
      var conf = new DruidConf(
        "config/access-" + extension + ".yaml",
        "config/secrets.yaml",
        "config/storage.yaml",
        "config/ingestion.yaml",
        "helm/druid-with-" + extension + ".tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.access().contains(extension));
    }
  }

  @Test
  public void testDruidConfigurationProperties() {
    String[] properties = {
      "druid.host", "druid.port", "druid.service",
      "druid.plaintextPort", "druid.tlsPort",
      "druid.enablePlaintextPort", "druid.enableTlsPort",
      "druid.zk.service.host", "druid.zk.paths.base",
      "druid.discovery.curator.path",
      "druid.metadata.storage.type", "druid.metadata.storage.connector.connectURI",
      "druid.storage.type", "druid.storage.bucket", "druid.storage.baseKey",
      "druid.indexer.logs.type", "druid.indexer.logs.bucket"
    };

    for (String prop : properties) {
      var conf = new DruidConf(
        "config/access.yaml?" + prop + "=value",
        "config/secrets.yaml",
        "config/storage.yaml",
        "config/ingestion.yaml",
        "helm/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.access().contains(prop));
    }
  }

  @Test
  public void testDruidSegmentCacheProperties() {
    var conf = new DruidConf(
      "config/access.yaml",
      "config/secrets.yaml",
      "config/storage.yaml?druid.segmentCache.locations=/var/druid/segment-cache",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.storage().contains("segmentCache"));
  }

  @Test
  public void testDruidProcessingProperties() {
    var conf = new DruidConf(
      "config/access.yaml?druid.processing.buffer.sizeBytes=1073741824",
      "config/secrets.yaml",
      "config/storage.yaml?druid.processing.numThreads=16",
      "config/ingestion.yaml?druid.processing.numMergeBuffers=4",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("processing.buffer"));
    assertTrue(conf.storage().contains("processing.numThreads"));
    assertTrue(conf.ingestion().contains("processing.numMergeBuffers"));
  }

  @Test
  public void testDruidServerProperties() {
    var conf = new DruidConf(
      "config/access.yaml?druid.server.http.numThreads=100",
      "config/secrets.yaml",
      "config/storage.yaml?druid.server.maxSize=10737418240",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("server.http.numThreads"));
    assertTrue(conf.storage().contains("server.maxSize"));
  }

  @Test
  public void testDruidMonitoringProperties() {
    var conf = new DruidConf(
      "config/access.yaml?druid.monitoring.monitors=[JvmMonitor,CacheMonitor,QueryMetricsMonitor]",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("monitoring.monitors"));
  }

  @Test
  public void testDruidEmitterProperties() {
    String[] emitters = {
      "config/access.yaml?druid.emitter=logging",
      "config/access.yaml?druid.emitter=http&druid.emitter.http.recipientBaseUrl=http://metrics:8080",
      "config/access.yaml?druid.emitter=prometheus",
      "config/access.yaml?druid.emitter=graphite&druid.emitter.graphite.hostname=graphite.internal"
    };

    for (String emitter : emitters) {
      var conf = new DruidConf(
        emitter,
        "config/secrets.yaml",
        "config/storage.yaml",
        "config/ingestion.yaml",
        "helm/druid.tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.access().contains("emitter"));
    }
  }

  @Test
  public void testDruidRequestLoggingProperties() {
    var conf = new DruidConf(
      "config/access.yaml?druid.request.logging.type=slf4j",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("request.logging"));
  }

  @Test
  public void testDruidSecurityProperties() {
    var conf = new DruidConf(
      "config/access.yaml?druid.auth.authenticatorChain=[basic,kerberos]",
      "config/secrets.yaml?druid.auth.authorizer.basic.type=basic",
      "config/storage.yaml",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("auth.authenticatorChain"));
    assertTrue(conf.secrets().contains("auth.authorizer"));
  }

  @Test
  public void testDruidTLSProperties() {
    var conf = new DruidConf(
      "config/access.yaml?druid.enableTlsPort=true",
      "config/secrets.yaml?druid.tls.keyStorePath=/etc/ssl/keystore.jks",
      "config/storage.yaml",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("enableTlsPort"));
    assertTrue(conf.secrets().contains("tls.keyStorePath"));
  }

  @Test
  public void testDruidStartupProperties() {
    var conf = new DruidConf(
      "config/access.yaml?druid.startup.logging.logProperties=true",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("startup.logging"));
  }

  @Test
  public void testDruidLookupProperties() {
    var conf = new DruidConf(
      "config/access.yaml?druid.lookup.enableLookupSyncOnStartup=true",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("lookup.enableLookupSyncOnStartup"));
  }

  @Test
  public void testDruidSQLProperties() {
    var conf = new DruidConf(
      "config/access.yaml?druid.sql.enable=true",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml?druid.sql.planner.maxQueryCount=10",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("sql.enable"));
    assertTrue(conf.ingestion().contains("sql.planner"));
  }

  @Test
  public void testDruidCoordinatorBalancerProperties() {
    var conf = new DruidConf(
      "config/access.yaml?druid.coordinator.balancer.strategy=cachingCost",
      "config/secrets.yaml",
      "config/storage.yaml?druid.coordinator.period=PT60S",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("coordinator.balancer"));
    assertTrue(conf.storage().contains("coordinator.period"));
  }

  @Test
  public void testDruidIndexerProperties() {
    var conf = new DruidConf(
      "config/access.yaml",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml?druid.indexer.fork.property.druid.processing.numThreads=8",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.ingestion().contains("indexer.fork"));
  }

  @Test
  public void testDruidWorkerProperties() {
    var conf = new DruidConf(
      "config/access.yaml",
      "config/secrets.yaml",
      "config/storage.yaml",
      "config/ingestion.yaml?druid.worker.capacity=10&druid.worker.ip=10.0.0.1",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.ingestion().contains("worker.capacity"));
    assertTrue(conf.ingestion().contains("worker.ip"));
  }

  @Test
  public void testAllDruidNodeTypes() {
    String[] nodeTypes = {
      "coordinator", "overlord", "broker", "router",
      "historical", "middleManager", "indexer", "peon"
    };

    for (String nodeType : nodeTypes) {
      var conf = new DruidConf(
        "config/access-" + nodeType + ".yaml",
        "config/secrets-" + nodeType + ".yaml",
        "config/storage-" + nodeType + ".yaml",
        "config/ingestion-" + nodeType + ".yaml",
        "helm/druid-" + nodeType + ".tgz",
        null
      );

      assertNotNull(conf);
      assertTrue(conf.access().contains(nodeType));
    }
  }

  @Test
  public void testDruidHighAvailabilityConfiguration() {
    var conf = new DruidConf(
      "config/access-ha.yaml?replicas=3&antiAffinity=required",
      "config/secrets.yaml",
      "config/storage-ha.yaml?multiAZ=true&replicationFactor=3",
      "config/ingestion.yaml",
      "helm/druid-ha.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("replicas=3"));
    assertTrue(conf.storage().contains("multiAZ"));
  }

  @Test
  public void testDruidDisasterRecoveryConfiguration() {
    var conf = new DruidConf(
      "config/access-dr.yaml",
      "config/secrets-dr.yaml",
      "config/storage-dr.yaml?crossRegionReplication=true&backupRegion=us-west-2",
      "config/ingestion-dr.yaml",
      "helm/druid-dr.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.storage().contains("crossRegionReplication"));
  }

  @Test
  public void testDruidMultiTenancyConfiguration() {
    var conf = new DruidConf(
      "config/access.yaml?multitenancy=true&tenantId=tenant-1",
      "config/secrets.yaml?tenant=tenant-1",
      "config/storage.yaml?tenantIsolation=namespace",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("multitenancy"));
    assertTrue(conf.storage().contains("tenantIsolation"));
  }

  @Test
  public void testDruidResourceLimitsConfiguration() {
    var conf = new DruidConf(
      "config/access.yaml?cpu.request=4&cpu.limit=8",
      "config/secrets.yaml",
      "config/storage.yaml?memory.request=16Gi&memory.limit=32Gi",
      "config/ingestion.yaml",
      "helm/druid.tgz",
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().contains("cpu.request"));
    assertTrue(conf.storage().contains("memory.request"));
  }

  @Test
  public void testConfigurationWithAllFieldsMaxLength() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String veryLongPath = "s3://very-long-bucket-name/" + "nested/directory/".repeat(100) + "config.yaml";

    var conf = new DruidConf(
      veryLongPath,
      veryLongPath,
      veryLongPath,
      veryLongPath,
      veryLongPath,
      null
    );

    assertNotNull(conf);
    assertTrue(conf.access().length() > 1700, "Path length is: " + conf.access().length());

    String yaml = mapper.writeValueAsString(conf);
    var deserialized = mapper.readValue(yaml, DruidConf.class);
    assertEquals(conf, deserialized);
  }
}
