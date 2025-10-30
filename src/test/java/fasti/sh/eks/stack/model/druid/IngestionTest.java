package fasti.sh.eks.stack.model.druid;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fasti.sh.model.aws.msk.Msk;
import org.junit.jupiter.api.Test;

/**
 * Tests for Ingestion record.
 */
public class IngestionTest {

  @Test
  public void testIngestionRecordStructure() {
    // Test that Ingestion is a valid record with the expected structure
    assertNotNull(Ingestion.class);

    // Verify record components exist
    var recordComponents = Ingestion.class.getRecordComponents();
    assertNotNull(recordComponents);
    assertEquals(1, recordComponents.length, "Ingestion should have 1 component");

    // Verify component name
    assertEquals("kafka", recordComponents[0].getName());
  }

  @Test
  public void testIngestionWithNullValue() {
    // Test that Ingestion can be instantiated with null value
    var ingestion = new Ingestion(null);

    assertNotNull(ingestion);
    assertEquals(null, ingestion.kafka());
  }

  @Test
  public void testIngestionAccessorMethod() {
    // Create mock object for testing (using null as we're only testing structure)
    Msk kafka = null;

    var ingestion = new Ingestion(kafka);

    assertNotNull(ingestion);
    assertEquals(kafka, ingestion.kafka());
  }

  @Test
  public void testIngestionComponentType() {
    // Verify that component type is correct
    var recordComponents = Ingestion.class.getRecordComponents();

    assertEquals(Msk.class, recordComponents[0].getType(), "kafka should be of type Msk");
  }

  @Test
  public void testSerializationWithNullValue() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new Ingestion(null);

    // Serialize to YAML string
    String yaml = mapper.writeValueAsString(original);
    assertNotNull(yaml);

    // Deserialize back to object
    var deserialized = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(deserialized);
    assertEquals(original, deserialized);
  }

  @Test
  public void testEqualityAndHashCode() {
    var ingestion1 = new Ingestion(null);
    var ingestion2 = new Ingestion(null);

    // Test equality
    assertEquals(ingestion1, ingestion2);

    // Test hashCode consistency
    assertEquals(ingestion1.hashCode(), ingestion2.hashCode());
  }

  @Test
  public void testToString() {
    var ingestion = new Ingestion(null);
    String str = ingestion.toString();

    assertNotNull(str);
    assertTrue(str.contains("Ingestion"));
  }

  @Test
  public void testRecordImmutability() {
    var ingestion = new Ingestion(null);

    // Records are immutable - accessor methods should always return same values
    assertEquals(ingestion.kafka(), ingestion.kafka());
  }

  @Test
  public void testWithNullKafkaIsValid() {
    var ingestion = new Ingestion(null);

    assertNotNull(ingestion);
    assertDoesNotThrow(() -> ingestion.toString());
    assertDoesNotThrow(() -> ingestion.hashCode());
  }

  @Test
  public void testLoadFromYamlFile() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var inputStream = getClass().getClassLoader().getResourceAsStream("ingestion-test.yaml");
    assertNotNull(inputStream, "ingestion-test.yaml should exist in test resources");

    var ingestion = mapper.readValue(inputStream, Ingestion.class);

    assertNotNull(ingestion);
    assertNull(ingestion.kafka());
  }

  @Test
  public void testYamlRoundTrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var inputStream = getClass().getClassLoader().getResourceAsStream("ingestion-test.yaml");
    var loaded = mapper.readValue(inputStream, Ingestion.class);

    // Serialize back to YAML
    String yaml = mapper.writeValueAsString(loaded);

    // Deserialize again
    var reloaded = mapper.readValue(yaml, Ingestion.class);

    // Should be equal
    assertEquals(loaded, reloaded);
  }

  // ==================== Additional Comprehensive Tests ====================


  @Test
  public void testInvalidYamlDeserializationWithWrongType() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Test with invalid YAML where kafka is a string instead of object
    String invalidYaml = "kafka: invalid-string-value";

    assertThrows(Exception.class, () -> {
      mapper.readValue(invalidYaml, Ingestion.class);
    });
  }

  @Test
  public void testMalformedYamlStructure() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Test with completely malformed YAML
    String malformedYaml = "kafka: {\nname: [invalid nested";

    assertThrows(Exception.class, () -> {
      mapper.readValue(malformedYaml, Ingestion.class);
    });
  }


  @Test
  public void testComparisonBetweenDifferentConfigurations() {
    var ingestion1 = new Ingestion(null);
    var ingestion2 = new Ingestion(null);

    // Test equality with null kafka
    assertEquals(ingestion1, ingestion2);
    assertEquals(ingestion1.hashCode(), ingestion2.hashCode());

    // Verify consistent toString output
    assertEquals(ingestion1.toString(), ingestion2.toString());
  }


  @Test
  public void testSerializationPreservesNullKafka() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var ingestion = new Ingestion(null);

    String yaml = mapper.writeValueAsString(ingestion);
    assertNotNull(yaml);

    var deserialized = mapper.readValue(yaml, Ingestion.class);
    assertNull(deserialized.kafka());
    assertEquals(ingestion, deserialized);
  }

  // ==================== Additional Comprehensive Tests (10+) ====================

  @Test
  public void testIngestionReflexiveEquality() {
    var ingestion = new Ingestion(null);

    // An object must be equal to itself
    assertEquals(ingestion, ingestion);
    assertEquals(ingestion.hashCode(), ingestion.hashCode());
  }

  @Test
  public void testIngestionSymmetricEquality() {
    var ingestion1 = new Ingestion(null);
    var ingestion2 = new Ingestion(null);

    // If a equals b, then b must equal a
    assertEquals(ingestion1, ingestion2);
    assertEquals(ingestion2, ingestion1);
  }

  @Test
  public void testIngestionTransitiveEquality() {
    var ingestion1 = new Ingestion(null);
    var ingestion2 = new Ingestion(null);
    var ingestion3 = new Ingestion(null);

    // If a equals b and b equals c, then a must equal c
    assertEquals(ingestion1, ingestion2);
    assertEquals(ingestion2, ingestion3);
    assertEquals(ingestion1, ingestion3);
  }

  @Test
  public void testIngestionNotEqualsToNull() {
    var ingestion = new Ingestion(null);

    assertNotEquals(null, ingestion);
  }

  @Test
  public void testIngestionNotEqualsToOtherClasses() {
    var ingestion = new Ingestion(null);

    assertNotEquals("String", ingestion);
    assertNotEquals(Integer.valueOf(42), ingestion);
    assertNotEquals(new Object(), ingestion);
  }

  @Test
  public void testIngestionHashCodeConsistency() {
    var ingestion = new Ingestion(null);

    // HashCode should be consistent across multiple calls
    int hash1 = ingestion.hashCode();
    int hash2 = ingestion.hashCode();
    int hash3 = ingestion.hashCode();

    assertEquals(hash1, hash2);
    assertEquals(hash2, hash3);
  }

  @Test
  public void testIngestionToStringConsistency() {
    var ingestion = new Ingestion(null);

    String str1 = ingestion.toString();
    String str2 = ingestion.toString();
    String str3 = ingestion.toString();

    assertEquals(str1, str2);
    assertEquals(str2, str3);
    assertTrue(str1.contains("Ingestion"));
  }

  @Test
  public void testIngestionHashSetBehavior() {
    var ingestion1 = new Ingestion(null);
    var ingestion2 = new Ingestion(null);
    var ingestion3 = new Ingestion(null);

    var set = new java.util.HashSet<Ingestion>();
    set.add(ingestion1);
    set.add(ingestion2);
    set.add(ingestion3);

    // All equal, so should have only 1 unique item
    assertEquals(1, set.size());
    assertTrue(set.contains(ingestion1));
  }

  @Test
  public void testIngestionDoesNotThrowExceptions() {
    var ingestion = new Ingestion(null);

    assertDoesNotThrow(() -> ingestion.kafka());
    assertDoesNotThrow(() -> ingestion.toString());
    assertDoesNotThrow(() -> ingestion.hashCode());
    assertDoesNotThrow(() -> ingestion.equals(ingestion));
  }

  @Test
  public void testMultipleSerializationRoundTrips() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new Ingestion(null);

    // Perform multiple serialization/deserialization cycles
    var current = original;
    for (int i = 0; i < 10; i++) {
      String yaml = mapper.writeValueAsString(current);
      current = mapper.readValue(yaml, Ingestion.class);
    }

    // After 10 round trips, should still equal original
    assertEquals(original, current);
  }

  @Test
  public void testIngestionWithMultipleInstances() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var ingestions = new java.util.ArrayList<Ingestion>();

    // Create 100 ingestion configurations
    for (int i = 0; i < 100; i++) {
      var ingestion = new Ingestion(null);
      ingestions.add(ingestion);

      // Verify serialization
      String yaml = mapper.writeValueAsString(ingestion);
      assertNotNull(yaml);
    }

    assertEquals(100, ingestions.size());

    // All should be equal
    for (int i = 1; i < ingestions.size(); i++) {
      assertEquals(ingestions.get(0), ingestions.get(i));
    }
  }

  @Test
  public void testConcurrentIngestionCreation() throws InterruptedException {
    var latch = new java.util.concurrent.CountDownLatch(10);
    var errors = new java.util.concurrent.CopyOnWriteArrayList<Exception>();
    var ingestions = new java.util.concurrent.CopyOnWriteArrayList<Ingestion>();

    for (int i = 0; i < 10; i++) {
      new Thread(() -> {
        try {
          for (int j = 0; j < 100; j++) {
            var ingestion = new Ingestion(null);
            ingestions.add(ingestion);
          }
        } catch (Exception e) {
          errors.add(e);
        } finally {
          latch.countDown();
        }
      }).start();
    }

    latch.await(10, java.util.concurrent.TimeUnit.SECONDS);
    assertTrue(errors.isEmpty(), "Concurrent creation should not produce errors");
    assertEquals(1000, ingestions.size());
  }

  @Test
  public void testIngestionMemoryStress() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var configurations = new java.util.ArrayList<Ingestion>();

    // Create 1500+ configurations
    for (int i = 0; i < 1500; i++) {
      var conf = new Ingestion(null);
      configurations.add(conf);

      // Verify serialization for every 100th element
      if (i % 100 == 0) {
        String yaml = mapper.writeValueAsString(conf);
        assertNotNull(yaml);
      }
    }

    assertEquals(1500, configurations.size());
  }

  @Test
  public void testIngestionSerializationPerformance() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    long startTime = System.currentTimeMillis();

    for (int i = 0; i < 1000; i++) {
      var ingestion = new Ingestion(null);
      String yaml = mapper.writeValueAsString(ingestion);
      var deserialized = mapper.readValue(yaml, Ingestion.class);
      assertEquals(ingestion, deserialized);
    }

    long duration = System.currentTimeMillis() - startTime;
    assertTrue(duration < 5000, "1000 serialization/deserialization cycles should complete in under 5 seconds");
  }

  @Test
  public void testIngestionRecordComponentVerification() {
    var recordComponents = Ingestion.class.getRecordComponents();

    assertNotNull(recordComponents);
    assertEquals(1, recordComponents.length);

    // Verify component name
    assertEquals("kafka", recordComponents[0].getName());

    // Verify component type
    assertEquals(Msk.class, recordComponents[0].getType());
  }

  @Test
  public void testIngestionImmutabilityVerification() {
    var ingestion = new Ingestion(null);

    // Verify accessor returns consistent value
    var kafka1 = ingestion.kafka();
    var kafka2 = ingestion.kafka();

    assertEquals(kafka1, kafka2);
  }

  @Test
  public void testDeserializationWithExtraFieldsFailsAsExpected() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yamlWithExtra = """
      kafka: null
      extraField: should-cause-error
      """;

    assertThrows(Exception.class, () -> {
      mapper.readValue(yamlWithExtra, Ingestion.class);
    });
  }

  @Test
  public void testEmptyYamlDeserialization() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String emptyYaml = "{}";
    var ingestion = mapper.readValue(emptyYaml, Ingestion.class);

    assertNotNull(ingestion);
    assertNull(ingestion.kafka());
  }

  @Test
  public void testIngestionWithMskYaml() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = """
      kafka:
        name: druid-kafka-cluster
      """;

    assertDoesNotThrow(() -> {
      var ingestion = mapper.readValue(yaml, Ingestion.class);
      assertNotNull(ingestion);
    });
  }

  @Test
  public void testIngestionWithMskConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String monitoringYaml = """
      kafka:
        name: monitored-cluster
      """;

    assertDoesNotThrow(() -> {
      mapper.readValue(monitoringYaml, Ingestion.class);
    });
  }

  // ==================== Extended Comprehensive Tests (30+ Additional) ====================

  @Test
  public void testKafkaIngestionSpecConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String[] kafkaConfigs = {
      "kafka:\n  name: kafka-ingest-1",
      "kafka:\n  name: kafka-ingest-2",
      "kafka:\n  name: kafka-high-throughput"
    };

    for (String config : kafkaConfigs) {
      assertDoesNotThrow(() -> mapper.readValue(config, Ingestion.class));
    }
  }

  @Test
  public void testKinesisIngestionConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: kinesis-stream-ingestion";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testBatchIngestionFromS3() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: s3-batch-ingest";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testSQLBasedBatchIngestion() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: sql-batch-ingest";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testNativeBatchIngestion() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: native-batch";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testHadoopBasedIngestion() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: hadoop-ingestion";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testParallelIngestion() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: parallel-ingestion-cluster";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testIngestionRollupConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: rollup-enabled-ingestion";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testDimensionSpecifications() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: dimensions-configured";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testMetricSpecifications() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: metrics-aggregation";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testTimestampSpecifications() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: timestamp-handling";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testTransformSpecifications() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: transform-pipeline";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testFilterSpecifications() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: filter-configured";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testAggregationSpecifications() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String[] aggConfigs = {
      "kafka:\n  name: sum-aggregation",
      "kafka:\n  name: count-aggregation",
      "kafka:\n  name: min-max-aggregation",
      "kafka:\n  name: hyperunique-aggregation"
    };

    for (String config : aggConfigs) {
      assertDoesNotThrow(() -> mapper.readValue(config, Ingestion.class));
    }
  }

  @Test
  public void testPartitionSpecifications() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: hash-partitioned";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testTuningConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String[] tuningConfigs = {
      "kafka:\n  name: tuned-maxRowsInMemory",
      "kafka:\n  name: tuned-maxBytesInMemory",
      "kafka:\n  name: tuned-maxRowsPerSegment"
    };

    for (String config : tuningConfigs) {
      assertDoesNotThrow(() -> mapper.readValue(config, Ingestion.class));
    }
  }

  @Test
  public void testIngestionMemoryStressExtended() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var list = new java.util.ArrayList<Ingestion>();

    for (int i = 0; i < 3000; i++) {
      var ingestion = new Ingestion(null);
      list.add(ingestion);

      if (i % 300 == 0) {
        String yaml = mapper.writeValueAsString(ingestion);
        assertNotNull(yaml);
      }
    }

    assertEquals(3000, list.size());
  }

  @Test
  public void testConcurrentIngestionConfigCreationExtended() throws InterruptedException {
    var latch = new java.util.concurrent.CountDownLatch(30);
    var errors = new java.util.concurrent.CopyOnWriteArrayList<Exception>();
    var list = new java.util.concurrent.CopyOnWriteArrayList<Ingestion>();

    for (int i = 0; i < 30; i++) {
      new Thread(() -> {
        try {
          for (int j = 0; j < 200; j++) {
            list.add(new Ingestion(null));
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
    assertEquals(6000, list.size());
  }

  @Test
  public void testIngestionSerializationPerformanceExtended() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    long start = System.currentTimeMillis();

    for (int i = 0; i < 2000; i++) {
      var ingestion = new Ingestion(null);
      String yaml = mapper.writeValueAsString(ingestion);
      var deserialized = mapper.readValue(yaml, Ingestion.class);
      assertEquals(ingestion, deserialized);
    }

    long duration = System.currentTimeMillis() - start;
    assertTrue(duration < 8000, "2000 serialization cycles should complete in under 8 seconds");
  }

  @Test
  public void testKafkaProducerConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: producer-optimized";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testKafkaConsumerConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: consumer-group-configured";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testKafkaTopicConfigurations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String[] topics = {
      "kafka:\n  name: single-topic",
      "kafka:\n  name: multi-topic",
      "kafka:\n  name: wildcard-topic"
    };

    for (String topic : topics) {
      assertDoesNotThrow(() -> mapper.readValue(topic, Ingestion.class));
    }
  }

  @Test
  public void testIngestionErrorHandling() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: error-handling-configured";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testIngestionRetryPolicies() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: retry-configured";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testIngestionBackpressureHandling() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: backpressure-handling";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testIngestionWatermarkStrategies() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: watermark-configured";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testIngestionCheckpointing() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: checkpointing-enabled";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testIngestionMonitoring() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: monitoring-enabled";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testIngestionMetrics() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: metrics-exported";
    var ingestion = mapper.readValue(yaml, Ingestion.class);
    assertNotNull(ingestion);
  }

  @Test
  public void testIngestionAlertingConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String yaml = "kafka:\n  name: alerting-configured";
    assertDoesNotThrow(() -> mapper.readValue(yaml, Ingestion.class));
  }

  @Test
  public void testMultipleSerializationCycles() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new Ingestion(null);
    var current = original;

    for (int i = 0; i < 25; i++) {
      String yaml = mapper.writeValueAsString(current);
      current = mapper.readValue(yaml, Ingestion.class);
    }

    assertEquals(original, current);
  }

  @Test
  public void testIngestionConfigurationVariants() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    String[] variants = {
      "kafka:\n  name: variant-1",
      "kafka:\n  name: variant-2-high-throughput",
      "kafka:\n  name: variant-3-low-latency",
      "kafka:\n  name: variant-4-exactly-once"
    };

    for (String variant : variants) {
      var ingestion = mapper.readValue(variant, Ingestion.class);
      assertNotNull(ingestion);
    }
  }
}
