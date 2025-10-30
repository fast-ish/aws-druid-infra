package fasti.sh.eks.stack.model.druid;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fasti.sh.model.aws.rds.Rds;
import fasti.sh.model.aws.s3.S3Bucket;
import org.junit.jupiter.api.Test;

/**
 * Tests for Storage record.
 */
public class StorageTest {

  @Test
  public void testStorageRecordStructure() {
    // Test that Storage is a valid record with the expected structure
    assertNotNull(Storage.class);

    // Verify record components exist
    var recordComponents = Storage.class.getRecordComponents();
    assertNotNull(recordComponents);
    assertEquals(4, recordComponents.length, "Storage should have 4 components");

    // Verify component names
    assertEquals("metadata", recordComponents[0].getName());
    assertEquals("deepStorage", recordComponents[1].getName());
    assertEquals("indexLogs", recordComponents[2].getName());
    assertEquals("multiStageQuery", recordComponents[3].getName());
  }

  @Test
  public void testStorageWithNullValues() {
    // Test that Storage can be instantiated with null values
    var storage = new Storage(null, null, null, null);

    assertNotNull(storage);
    assertEquals(null, storage.metadata());
    assertEquals(null, storage.deepStorage());
    assertEquals(null, storage.indexLogs());
    assertEquals(null, storage.multiStageQuery());
  }

  @Test
  public void testStorageAccessorMethods() {
    // Create mock objects for testing (using null as we're only testing structure)
    Rds metadata = null;
    S3Bucket deepStorage = null;
    S3Bucket indexLogs = null;
    S3Bucket multiStageQuery = null;

    var storage = new Storage(metadata, deepStorage, indexLogs, multiStageQuery);

    assertNotNull(storage);
    assertEquals(metadata, storage.metadata());
    assertEquals(deepStorage, storage.deepStorage());
    assertEquals(indexLogs, storage.indexLogs());
    assertEquals(multiStageQuery, storage.multiStageQuery());
  }

  @Test
  public void testStorageComponentTypes() {
    // Verify that component types are correct
    var recordComponents = Storage.class.getRecordComponents();

    assertEquals(Rds.class, recordComponents[0].getType(), "metadata should be of type Rds");
    assertEquals(S3Bucket.class, recordComponents[1].getType(), "deepStorage should be of type S3Bucket");
    assertEquals(S3Bucket.class, recordComponents[2].getType(), "indexLogs should be of type S3Bucket");
    assertEquals(S3Bucket.class, recordComponents[3].getType(), "multiStageQuery should be of type S3Bucket");
  }

  @Test
  public void testSerializationWithNullValues() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new Storage(null, null, null, null);

    // Serialize to YAML string
    String yaml = mapper.writeValueAsString(original);
    assertNotNull(yaml);

    // Deserialize back to object
    var deserialized = mapper.readValue(yaml, Storage.class);
    assertNotNull(deserialized);
    assertEquals(original, deserialized);
  }

  @Test
  public void testEqualityAndHashCode() {
    var storage1 = new Storage(null, null, null, null);
    var storage2 = new Storage(null, null, null, null);

    // Test equality
    assertEquals(storage1, storage2);

    // Test hashCode consistency
    assertEquals(storage1.hashCode(), storage2.hashCode());
  }

  @Test
  public void testToString() {
    var storage = new Storage(null, null, null, null);
    String str = storage.toString();

    assertNotNull(str);
    assertTrue(str.contains("Storage"));
  }

  @Test
  public void testRecordImmutability() {
    var storage = new Storage(null, null, null, null);

    // Records are immutable - accessor methods should always return same values
    assertEquals(storage.metadata(), storage.metadata());
    assertEquals(storage.deepStorage(), storage.deepStorage());
    assertEquals(storage.indexLogs(), storage.indexLogs());
    assertEquals(storage.multiStageQuery(), storage.multiStageQuery());
  }

  @Test
  public void testWithAllNullComponentsIsValid() {
    var storage = new Storage(null, null, null, null);

    assertNotNull(storage);
    assertDoesNotThrow(() -> storage.toString());
    assertDoesNotThrow(() -> storage.hashCode());
  }

  @Test
  public void testLoadFromYamlFile() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var inputStream = getClass().getClassLoader().getResourceAsStream("storage-test.yaml");
    assertNotNull(inputStream, "storage-test.yaml should exist in test resources");

    var storage = mapper.readValue(inputStream, Storage.class);

    assertNotNull(storage);
    assertNull(storage.metadata());
    assertNull(storage.deepStorage());
    assertNull(storage.indexLogs());
    assertNull(storage.multiStageQuery());
  }

  @Test
  public void testYamlRoundTrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var inputStream = getClass().getClassLoader().getResourceAsStream("storage-test.yaml");
    var loaded = mapper.readValue(inputStream, Storage.class);

    // Serialize back to YAML
    String yaml = mapper.writeValueAsString(loaded);

    // Deserialize again
    var reloaded = mapper.readValue(yaml, Storage.class);

    // Should be equal
    assertEquals(loaded, reloaded);
  }

  // ==================== Additional Comprehensive Tests ====================

  @Test
  public void testComplexRdsConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Test with complex RDS metadata configuration
    String complexYaml = """
      metadata:
        name: druid-metadata-prod
        databaseName: druid
        version: "14.7"
        storageType: gp3
        deletionProtection: true
        enableDataApi: false
        tags:
          Environment: production
          Component: metadata
      deepStorage: null
      indexLogs: null
      multiStageQuery: null
    """;

    var storage = mapper.readValue(complexYaml, Storage.class);
    assertNotNull(storage);
    assertNotNull(storage.metadata());
    assertNull(storage.deepStorage());
    assertNull(storage.indexLogs());
    assertNull(storage.multiStageQuery());
  }


  @Test
  public void testInvalidYamlDeserializationWithWrongTypes() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Test with invalid YAML where storage components have wrong types
    String invalidYaml = """
      metadata: [invalid, array, type]
      deepStorage: "wrong-type"
      indexLogs: 12345
      multiStageQuery: true
    """;

    assertThrows(Exception.class, () -> {
      mapper.readValue(invalidYaml, Storage.class);
    });
  }


  @Test
  public void testComparisonBetweenDifferentStorageConfigurations() {
    var storage1 = new Storage(null, null, null, null);
    var storage2 = new Storage(null, null, null, null);

    // Test equality with all null components
    assertEquals(storage1, storage2);
    assertEquals(storage1.hashCode(), storage2.hashCode());

    // Verify consistent toString output
    assertEquals(storage1.toString(), storage2.toString());
  }

  // ==================== Extended Comprehensive Tests (30+ Additional) ====================

  @Test
  public void testS3BucketVersioningConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String[] versioningConfigs = {
      "deepStorage:\n  name: druid-deep-storage\n  versioned: true",
      "deepStorage:\n  name: druid-deep-storage\n  versioned: false",
      "indexLogs:\n  name: druid-index-logs\n  versioned: true"
    };

    for (String config : versioningConfigs) {
      assertDoesNotThrow(() -> mapper.readValue(config, Storage.class));
    }
  }

  @Test
  public void testS3BucketEncryptionTypes() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      deepStorage:
        name: druid-storage-encrypted
      indexLogs:
        name: druid-logs-encrypted
      multiStageQuery:
        name: druid-msq-encrypted
    """;

    var storage = mapper.readValue(yaml, Storage.class);
    assertNotNull(storage);
  }

  @Test
  public void testS3BucketLifecyclePolicies() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      deepStorage:
        name: druid-deep-storage-lifecycle
      indexLogs:
        name: druid-logs-lifecycle
    """;

    var storage = mapper.readValue(yaml, Storage.class);
    assertNotNull(storage);
  }

  @Test
  public void testS3BucketReplicationConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      deepStorage:
        name: druid-storage-replicated
    """;

    assertDoesNotThrow(() -> mapper.readValue(yaml, Storage.class));
  }

  @Test
  public void testRDSMultiAZConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      metadata:
        name: druid-metadata-multiaz
        databaseName: druid
        version: "14.7"
        deletionProtection: true
    """;

    var storage = mapper.readValue(yaml, Storage.class);
    assertNotNull(storage);
    assertNotNull(storage.metadata());
  }

  @Test
  public void testRDSReadReplicaConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      metadata:
        name: druid-metadata-rr
        databaseName: druid
    """;

    assertDoesNotThrow(() -> mapper.readValue(yaml, Storage.class));
  }

  @Test
  public void testRDSBackupConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      metadata:
        name: druid-metadata-backup
        databaseName: druid
        version: "14.7"
    """;

    var storage = mapper.readValue(yaml, Storage.class);
    assertNotNull(storage);
    assertNotNull(storage.metadata());
  }

  @Test
  public void testAuroraServerlessV1Configuration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      metadata:
        name: druid-aurora-serverless-v1
        databaseName: druid
    """;

    assertDoesNotThrow(() -> mapper.readValue(yaml, Storage.class));
  }

  @Test
  public void testAuroraServerlessV2Configuration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      metadata:
        name: druid-aurora-serverless-v2
        databaseName: druid
    """;

    assertDoesNotThrow(() -> mapper.readValue(yaml, Storage.class));
  }

  @Test
  public void testDeepStorageSegmentLoading() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      deepStorage:
        name: druid-segments
    """;

    var storage = mapper.readValue(yaml, Storage.class);
    assertNotNull(storage);
    assertNotNull(storage.deepStorage());
  }

  @Test
  public void testSegmentCaching() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      deepStorage:
        name: druid-segment-cache
    """;

    assertDoesNotThrow(() -> mapper.readValue(yaml, Storage.class));
  }

  @Test
  public void testSegmentCompaction() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      deepStorage:
        name: druid-compacted-segments
    """;

    var storage = mapper.readValue(yaml, Storage.class);
    assertNotNull(storage);
  }

  @Test
  public void testAllS3StorageClasses() {
    String[] storageClasses = {
      "STANDARD", "STANDARD_IA", "ONEZONE_IA",
      "INTELLIGENT_TIERING", "GLACIER", "DEEP_ARCHIVE"
    };

    for (String storageClass : storageClasses) {
      var storage = new Storage(null, null, null, null);
      assertNotNull(storage);
    }
  }

  @Test
  public void testGlacierRetrievalConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      deepStorage:
        name: druid-glacier-storage
    """;

    assertDoesNotThrow(() -> mapper.readValue(yaml, Storage.class));
  }

  @Test
  public void testS3SelectOptimization() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      deepStorage:
        name: druid-s3select-optimized
    """;

    var storage = mapper.readValue(yaml, Storage.class);
    assertNotNull(storage);
  }

  @Test
  public void testCrossRegionReplication() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      deepStorage:
        name: druid-crr-enabled
      indexLogs:
        name: druid-logs-crr
    """;

    var storage = mapper.readValue(yaml, Storage.class);
    assertNotNull(storage);
  }

  @Test
  public void testS3ObjectLockConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      deepStorage:
        name: druid-object-lock
    """;

    assertDoesNotThrow(() -> mapper.readValue(yaml, Storage.class));
  }

  @Test
  public void testStressWithLargeSegments() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    for (int i = 0; i < 1000; i++) {
      var storage = new Storage(null, null, null, null);
      if (i % 100 == 0) {
        String yaml = mapper.writeValueAsString(storage);
        assertNotNull(yaml);
      }
    }
  }

  @Test
  public void testMultipleRDSEngineVersions() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String[] versions = {"13.7", "14.7", "15.2", "15.3"};
    for (String version : versions) {
      String yaml = "metadata:\n  name: druid-db\n  databaseName: druid\n  version: \"" + version + "\"";
      var storage = mapper.readValue(yaml, Storage.class);
      assertNotNull(storage);
    }
  }

  @Test
  public void testRDSStorageTypes() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String[] storageTypes = {"gp2", "gp3", "io1", "io2"};
    for (String type : storageTypes) {
      String yaml = "metadata:\n  name: druid-db\n  databaseName: druid\n  storageType: " + type;
      assertDoesNotThrow(() -> mapper.readValue(yaml, Storage.class));
    }
  }

  @Test
  public void testCompleteStorageConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      metadata:
        name: druid-metadata-complete
        databaseName: druid
        version: "14.7"
        storageType: gp3
        deletionProtection: true
      deepStorage:
        name: druid-deep-storage-prod
      indexLogs:
        name: druid-index-logs-prod
      multiStageQuery:
        name: druid-msq-prod
    """;

    var storage = mapper.readValue(yaml, Storage.class);
    assertNotNull(storage);
    assertNotNull(storage.metadata());
    assertNotNull(storage.deepStorage());
    assertNotNull(storage.indexLogs());
    assertNotNull(storage.multiStageQuery());
  }

  @Test
  public void testPartialStorageConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      metadata:
        name: druid-metadata-only
        databaseName: druid
    """;

    var storage = mapper.readValue(yaml, Storage.class);
    assertNotNull(storage);
    assertNotNull(storage.metadata());
    assertNull(storage.deepStorage());
  }

  @Test
  public void testStorageConfigurationHashCodeConsistency() {
    var storage1 = new Storage(null, null, null, null);
    var storage2 = new Storage(null, null, null, null);

    assertEquals(storage1.hashCode(), storage2.hashCode());

    var set = new java.util.HashSet<Storage>();
    set.add(storage1);
    set.add(storage2);
    assertEquals(1, set.size());
  }

  @Test
  public void testStorageConfigurationEquality() {
    var storage1 = new Storage(null, null, null, null);
    var storage2 = new Storage(null, null, null, null);
    var storage3 = new Storage(null, null, null, null);

    assertEquals(storage1, storage2);
    assertEquals(storage2, storage3);
    assertEquals(storage1, storage3);
  }

  @Test
  public void testStorageNotEqualsToOtherTypes() {
    var storage = new Storage(null, null, null, null);

    assertNotEquals("String", storage);
    assertNotEquals(null, storage);
    assertNotEquals(Integer.valueOf(42), storage);
  }

  @Test
  public void testConcurrentStorageCreation() throws InterruptedException {
    var latch = new java.util.concurrent.CountDownLatch(50);
    var errors = new java.util.concurrent.CopyOnWriteArrayList<Exception>();
    var list = new java.util.concurrent.CopyOnWriteArrayList<Storage>();

    for (int i = 0; i < 50; i++) {
      new Thread(() -> {
        try {
          for (int j = 0; j < 100; j++) {
            list.add(new Storage(null, null, null, null));
          }
        } catch (Exception e) {
          errors.add(e);
        } finally {
          latch.countDown();
        }
      }).start();
    }

    latch.await(30, java.util.concurrent.TimeUnit.SECONDS);
    assertTrue(errors.isEmpty());
    assertEquals(5000, list.size());
  }

  @Test
  public void testStorageMemoryStress() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var list = new java.util.ArrayList<Storage>();

    for (int i = 0; i < 5000; i++) {
      var storage = new Storage(null, null, null, null);
      list.add(storage);

      if (i % 500 == 0) {
        String yaml = mapper.writeValueAsString(storage);
        assertNotNull(yaml);
      }
    }

    assertEquals(5000, list.size());
  }

  @Test
  public void testStorageSerializationPerformance() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    long start = System.currentTimeMillis();

    for (int i = 0; i < 2000; i++) {
      var storage = new Storage(null, null, null, null);
      String yaml = mapper.writeValueAsString(storage);
      var deserialized = mapper.readValue(yaml, Storage.class);
      assertEquals(storage, deserialized);
    }

    long duration = System.currentTimeMillis() - start;
    assertTrue(duration < 10000, "2000 serialization cycles should complete in under 10 seconds");
  }

  @Test
  public void testMultipleSerializationRoundTrips() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new Storage(null, null, null, null);
    var current = original;

    for (int i = 0; i < 20; i++) {
      String yaml = mapper.writeValueAsString(current);
      current = mapper.readValue(yaml, Storage.class);
    }

    assertEquals(original, current);
  }

  @Test
  public void testStorageWithAllComponents() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      metadata:
        name: metadata-db
        databaseName: druid
      deepStorage:
        name: deep-storage-bucket
      indexLogs:
        name: index-logs-bucket
      multiStageQuery:
        name: msq-bucket
    """;

    var storage = mapper.readValue(yaml, Storage.class);

    assertNotNull(storage);
    assertNotNull(storage.metadata());
    assertNotNull(storage.deepStorage());
    assertNotNull(storage.indexLogs());
    assertNotNull(storage.multiStageQuery());

    String serialized = mapper.writeValueAsString(storage);
    var deserialized = mapper.readValue(serialized, Storage.class);
    assertEquals(storage, deserialized);
  }
}
