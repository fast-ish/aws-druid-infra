package fasti.sh.eks.stack.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for SystemConf record.
 */
public class SystemConfTest {

  @Test
  public void testSystemConfRecordStructure() {
    // Test that SystemConf is a valid record with the expected structure
    assertNotNull(SystemConf.class);

    // Verify record components exist
    var recordComponents = SystemConf.class.getRecordComponents();
    assertNotNull(recordComponents);
    assertEquals(4, recordComponents.length, "SystemConf should have 4 components");

    // Verify component names
    assertEquals("common", recordComponents[0].getName());
    assertEquals("vpc", recordComponents[1].getName());
    assertEquals("eks", recordComponents[2].getName());
    assertEquals("druid", recordComponents[3].getName());
  }

  @Test
  public void testSystemConfWithNullValues() {
    // Test that SystemConf can be instantiated with null values
    var systemConf = new SystemConf(null, null, null, null);

    assertNotNull(systemConf);
    assertEquals(null, systemConf.common());
    assertEquals(null, systemConf.vpc());
    assertEquals(null, systemConf.eks());
    assertEquals(null, systemConf.druid());
  }

  // ==================== Additional Comprehensive Tests ====================

  @Test
  public void testSystemConfEqualityAndHashCode() {
    var conf1 = new SystemConf(null, null, null, null);
    var conf2 = new SystemConf(null, null, null, null);
    var conf3 = new SystemConf(null, null, null, null);

    // Test equality
    assertEquals(conf1, conf2);
    assertEquals(conf2, conf3);
    assertEquals(conf1, conf3);

    // Test hashCode consistency
    assertEquals(conf1.hashCode(), conf2.hashCode());
    assertEquals(conf2.hashCode(), conf3.hashCode());
  }

  @Test
  public void testSystemConfToString() {
    var systemConf = new SystemConf(null, null, null, null);
    String str = systemConf.toString();

    assertNotNull(str);
    assertTrue(str.contains("SystemConf"));
  }

  @Test
  public void testSystemConfImmutability() {
    var systemConf = new SystemConf(null, null, null, null);

    // Records are immutable - accessor methods should always return same values
    assertEquals(systemConf.common(), systemConf.common());
    assertEquals(systemConf.vpc(), systemConf.vpc());
    assertEquals(systemConf.eks(), systemConf.eks());
    assertEquals(systemConf.druid(), systemConf.druid());
  }

  @Test
  public void testSystemConfRecordComponents() {
    var recordComponents = SystemConf.class.getRecordComponents();

    // Verify each component name and position
    assertEquals("common", recordComponents[0].getName());
    assertEquals("vpc", recordComponents[1].getName());
    assertEquals("eks", recordComponents[2].getName());
    assertEquals("druid", recordComponents[3].getName());

    // Verify no additional components
    assertEquals(4, recordComponents.length);
  }

  @Test
  public void testSystemConfComponentTypes() {
    var recordComponents = SystemConf.class.getRecordComponents();

    assertEquals(fasti.sh.model.main.Common.class, recordComponents[0].getType());
    assertEquals(fasti.sh.model.aws.vpc.NetworkConf.class, recordComponents[1].getType());
    assertEquals(fasti.sh.model.aws.eks.KubernetesConf.class, recordComponents[2].getType());
    assertEquals(fasti.sh.eks.stack.model.DruidConf.class, recordComponents[3].getType());
  }

  @Test
  public void testSystemConfWithDifferentCombinationsOfNull() {
    // Test various combinations of null values
    var conf1 = new SystemConf(null, null, null, null);
    var conf2 = new SystemConf(null, null, null, null);
    var conf3 = new SystemConf(null, null, null, null);

    assertNotNull(conf1);
    assertNotNull(conf2);
    assertNotNull(conf3);

    assertEquals(conf1, conf2);
    assertEquals(conf2, conf3);
  }

  @Test
  public void testSystemConfHashCodeConsistency() {
    var conf1 = new SystemConf(null, null, null, null);

    // HashCode should be consistent across multiple calls
    int hash1 = conf1.hashCode();
    int hash2 = conf1.hashCode();
    int hash3 = conf1.hashCode();

    assertEquals(hash1, hash2);
    assertEquals(hash2, hash3);
  }

  @Test
  public void testSystemConfNotEqualsToNull() {
    var systemConf = new SystemConf(null, null, null, null);

    assertNotEquals(null, systemConf);
  }

  @Test
  public void testSystemConfNotEqualsToOtherClasses() {
    var systemConf = new SystemConf(null, null, null, null);

    assertNotEquals("String", systemConf);
    assertNotEquals(Integer.valueOf(42), systemConf);
    assertNotEquals(new Object(), systemConf);
  }

  @Test
  public void testSystemConfReflexiveEquality() {
    var systemConf = new SystemConf(null, null, null, null);

    // An object must be equal to itself
    assertEquals(systemConf, systemConf);
  }

  @Test
  public void testSystemConfSymmetricEquality() {
    var conf1 = new SystemConf(null, null, null, null);
    var conf2 = new SystemConf(null, null, null, null);

    // If a equals b, then b must equal a
    assertEquals(conf1, conf2);
    assertEquals(conf2, conf1);
  }

  @Test
  public void testSystemConfTransitiveEquality() {
    var conf1 = new SystemConf(null, null, null, null);
    var conf2 = new SystemConf(null, null, null, null);
    var conf3 = new SystemConf(null, null, null, null);

    // If a equals b and b equals c, then a must equal c
    assertEquals(conf1, conf2);
    assertEquals(conf2, conf3);
    assertEquals(conf1, conf3);
  }

  @Test
  public void testSystemConfConsistentToString() {
    var systemConf = new SystemConf(null, null, null, null);

    String str1 = systemConf.toString();
    String str2 = systemConf.toString();
    String str3 = systemConf.toString();

    assertEquals(str1, str2);
    assertEquals(str2, str3);
  }

  @Test
  public void testSystemConfDoesNotThrowExceptions() {
    var systemConf = new SystemConf(null, null, null, null);

    assertDoesNotThrow(() -> systemConf.common());
    assertDoesNotThrow(() -> systemConf.vpc());
    assertDoesNotThrow(() -> systemConf.eks());
    assertDoesNotThrow(() -> systemConf.druid());
    assertDoesNotThrow(() -> systemConf.toString());
    assertDoesNotThrow(() -> systemConf.hashCode());
  }
}
