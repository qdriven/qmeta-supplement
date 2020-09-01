package io.qmeta.supplement;

import static org.assertj.core.api.Assertions.assertThat;

import io.qmeta.supplement.yml.YamlUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** @author: patrick on 2020/1/19 @Description: */
class YamlUtilsTest {
  private static final String YAML_TEST_FILE = "test.yml";
  private static final String YAML_DUMP_FILE = "src/test/resources/test_dump.yml";

  @Test
  void testLoadYaml() throws FileNotFoundException {
    Map result = YamlUtils.loadYaml(YAML_TEST_FILE);
    assertThat(result).containsKey("logging");
  }

  @Test
  void testDumpYaml() throws IOException {
    Map result = YamlUtils.loadYaml(YAML_TEST_FILE);
    YamlUtils.dumpYaml(YAML_DUMP_FILE, (Map<?, ?>) result.get("logging"));
  }
}
