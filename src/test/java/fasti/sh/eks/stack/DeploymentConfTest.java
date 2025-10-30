package fasti.sh.eks.stack;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests for DeploymentConf record.
 */
public class DeploymentConfTest {

  @Test
  public void testDeploymentConfRecordStructure() {
    // Test that DeploymentConf is a valid record with the expected structure
    assertNotNull(DeploymentConf.class);

    // Verify record components exist
    var recordComponents = DeploymentConf.class.getRecordComponents();
    assertNotNull(recordComponents);
    assertEquals(4, recordComponents.length, "DeploymentConf should have 4 components");

    // Verify component names
    assertEquals("common", recordComponents[0].getName());
    assertEquals("vpc", recordComponents[1].getName());
    assertEquals("eks", recordComponents[2].getName());
    assertEquals("druid", recordComponents[3].getName());
  }

  @Test
  public void testDeploymentConfWithNullValues() {
    // Test that DeploymentConf can be instantiated with null values
    var deploymentConf = new DeploymentConf(null, null, null, null);

    assertNotNull(deploymentConf);
    assertEquals(null, deploymentConf.common());
    assertEquals(null, deploymentConf.vpc());
    assertEquals(null, deploymentConf.eks());
    assertEquals(null, deploymentConf.druid());
  }

  @Test
  public void testSerializationDeserialization() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new DeploymentConf(null, null, null, null);

    // Serialize to YAML string
    String yaml = mapper.writeValueAsString(original);
    assertNotNull(yaml);

    // Deserialize back to object
    var deserialized = mapper.readValue(yaml, DeploymentConf.class);
    assertNotNull(deserialized);
    assertEquals(original, deserialized);
  }

  @Test
  public void testEqualityAndHashCode() {
    var conf1 = new DeploymentConf(null, null, null, null);
    var conf2 = new DeploymentConf(null, null, null, null);

    // Test equality
    assertEquals(conf1, conf2);

    // Test hashCode consistency
    assertEquals(conf1.hashCode(), conf2.hashCode());
  }

  @Test
  public void testToString() {
    var deploymentConf = new DeploymentConf(null, null, null, null);
    String str = deploymentConf.toString();

    assertNotNull(str);
    assertTrue(str.contains("DeploymentConf"));
  }

  @Test
  public void testRecordImmutability() {
    var deploymentConf = new DeploymentConf(null, null, null, null);

    // Records are immutable - accessor methods should always return same values
    assertEquals(deploymentConf.common(), deploymentConf.common());
    assertEquals(deploymentConf.vpc(), deploymentConf.vpc());
    assertEquals(deploymentConf.eks(), deploymentConf.eks());
    assertEquals(deploymentConf.druid(), deploymentConf.druid());
  }

  @Test
  public void testComponentTypes() {
    var recordComponents = DeploymentConf.class.getRecordComponents();

    assertEquals(fasti.sh.model.main.Common.class, recordComponents[0].getType());
    assertEquals(fasti.sh.model.aws.vpc.NetworkConf.class, recordComponents[1].getType());
    assertEquals(fasti.sh.model.aws.eks.KubernetesConf.class, recordComponents[2].getType());
    assertEquals(fasti.sh.eks.stack.model.DruidConf.class, recordComponents[3].getType());
  }

  @Test
  public void testWithAllNullComponentsIsValid() {
    var deploymentConf = new DeploymentConf(null, null, null, null);

    assertNotNull(deploymentConf);
    assertDoesNotThrow(() -> deploymentConf.toString());
    assertDoesNotThrow(() -> deploymentConf.hashCode());
  }

  // ==================== Additional Comprehensive Tests ====================

  @Test
  public void testInvalidYamlDeserialization() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    // Test with invalid YAML structure
    String invalidYaml = """
      common: [invalid, array]
      vpc: "wrong-type"
      eks: 12345
      druid: true
    """;

    assertThrows(Exception.class, () -> {
      mapper.readValue(invalidYaml, DeploymentConf.class);
    });
  }


  @Test
  public void testComparisonBetweenDifferentDeploymentConfigurations() {
    var deployment1 = new DeploymentConf(null, null, null, null);
    var deployment2 = new DeploymentConf(null, null, null, null);

    // Test equality with all null components
    assertEquals(deployment1, deployment2);
    assertEquals(deployment1.hashCode(), deployment2.hashCode());

    // Verify consistent toString output
    assertEquals(deployment1.toString(), deployment2.toString());
  }

  // ==================== EKS-Specific Comprehensive Tests ====================

  @Test
  public void testDeploymentConfWithSimpleVpcConfig() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String vpcYaml = """
      vpc:
        cidr: "10.0.0.0/16"
        natGateways: 3
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(vpcYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithEksVersion() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String eksYaml = """
      eks:
        version: "1.28"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(eksYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithSimpleLoggingTypes() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String loggingYaml = """
      eks:
        loggingTypes:
          - api
          - audit
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(loggingYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithNodeGroupsString() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String nodeGroupsYaml = """
      eks:
        nodeGroups: "default-ng"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(nodeGroupsYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithAddonsString() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String addonsYaml = """
      eks:
        addons: "vpc-cni,coredns"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(addonsYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithEndpointAccessString() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String endpointYaml = """
      eks:
        endpointAccess: "PRIVATE"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(endpointYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithAvailabilityZones() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String azYaml = """
      vpc:
        availabilityZones:
          - "us-east-1a"
          - "us-east-1b"
          - "us-east-1c"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(azYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithDnsSettings() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String dnsYaml = """
      vpc:
        enableDnsHostnames: true
        enableDnsSupport: true
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(dnsYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithTags() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String tagsYaml = """
      eks:
        tags:
          Environment: "Production"
          Team: "Platform"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(tagsYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithVpcSubnetTypes() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String subnetTypesYaml = """
      eks:
        vpcSubnetTypes:
          - PRIVATE
          - PUBLIC
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(subnetTypesYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithEksName() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String nameYaml = """
      eks:
        name: "production-cluster"
        version: "1.28"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(nameYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithLabels() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String labelsYaml = """
      eks:
        labels:
          tier: "production"
          managed-by: "cdk"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(labelsYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithAnnotations() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String annotationsYaml = """
      eks:
        annotations:
          description: "Production EKS cluster"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(annotationsYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithPrune() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String pruneYaml = """
      eks:
        prune: true
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(pruneYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithRbac() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String rbacYaml = """
      eks:
        rbac: true
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(rbacYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithTenancy() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String tenancyYaml = """
      eks:
        tenancy: "default"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(tenancyYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithComplexVpcAndEks() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String complexYaml = """
      vpc:
        cidr: "10.0.0.0/16"
        natGateways: 3
        enableDnsHostnames: true
        enableDnsSupport: true
      eks:
        version: "1.28"
        name: "production-cluster"
        loggingTypes:
          - api
          - audit
        tags:
          Environment: "Production"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(complexYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithSqs() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String sqsYaml = """
      eks:
        sqs: "my-sqs-queue"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(sqsYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithDefaultInstanceTenancy() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String tenancyYaml = """
      vpc:
        defaultInstanceTenancy: "DEFAULT"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(tenancyYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithIpProtocol() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String ipYaml = """
      vpc:
        ipProtocol: "IPV4_ONLY"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(ipYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithCreateInternetGateway() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String igwYaml = """
      vpc:
        createInternetGateway: true
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(igwYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithObservability() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String observabilityYaml = """
      eks:
        observability: "prometheus"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(observabilityYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithVpcName() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String vpcNameYaml = """
      vpc:
        name: "production-vpc"
        cidr: "10.0.0.0/16"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(vpcNameYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithVpcTags() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String tagsYaml = """
      vpc:
        tags:
          Environment: "Production"
          Team: "Infrastructure"
    """;

    assertDoesNotThrow(() -> {
      var conf = mapper.readValue(tagsYaml, DeploymentConf.class);
      assertNotNull(conf);
    });
  }

  @Test
  public void testDeploymentConfWithEmptyYaml() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String emptyYaml = "{}";

    var result = mapper.readValue(emptyYaml, DeploymentConf.class);
    assertNotNull(result);
  }

  @Test
  public void testDeploymentConfWithPartialConfiguration() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String partialYaml = """
      common:
        id: "test-cluster"
      eks:
        version: "1.28"
    """;

    var result = mapper.readValue(partialYaml, DeploymentConf.class);
    assertNotNull(result);
    assertNotNull(result.common());
    assertNotNull(result.eks());
  }

  @Test
  public void testDeploymentConfSerializationRoundTrip() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var original = new DeploymentConf(null, null, null, null);

    // Serialize
    String yaml = mapper.writeValueAsString(original);
    assertNotNull(yaml);

    // Deserialize
    var deserialized = mapper.readValue(yaml, DeploymentConf.class);

    // Verify equality
    assertEquals(original, deserialized);
  }

  @Test
  public void testDeploymentConfWithMultipleEksVersions() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    String[] versions = {"1.24", "1.25", "1.26", "1.27", "1.28", "1.29"};

    for (String version : versions) {
      String versionYaml = "eks:\n  version: \"" + version + "\"\n";
      assertDoesNotThrow(() -> {
        var conf = mapper.readValue(versionYaml, DeploymentConf.class);
        assertNotNull(conf);
      });
    }
  }

  @Test
  public void testDeploymentConfHashCodeConsistency() {
    var conf1 = new DeploymentConf(null, null, null, null);
    var conf2 = new DeploymentConf(null, null, null, null);

    // Multiple calls should return same hash code
    int hash1 = conf1.hashCode();
    int hash2 = conf1.hashCode();
    assertEquals(hash1, hash2);

    // Equal objects should have equal hash codes
    assertEquals(conf1.hashCode(), conf2.hashCode());
  }

  @Test
  public void testDeploymentConfNotEqualWithDifferentComponents() throws Exception {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    var conf1 = mapper.readValue("eks:\n  version: \"1.28\"\n", DeploymentConf.class);
    var conf2 = mapper.readValue("eks:\n  version: \"1.27\"\n", DeploymentConf.class);

    assertNotEquals(conf1, conf2);
  }

}
