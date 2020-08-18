package io.qmeta.supplement.yml;

import io.qmeta.supplement.StringUtils;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlUtils {

  public static Map<?, ?> loadYaml(String fileName) throws FileNotFoundException {
    InputStream in = YamlUtils.class.getClassLoader().getResourceAsStream(fileName);
    return StringUtils.isNotEmpty(fileName) ? (LinkedHashMap<?, ?>) new Yaml().load(in) : null;
  }

  public static void dumpYaml(String fileName, Map<?, ?> map) throws IOException {
    if (StringUtils.isNotEmpty(fileName)) {
      FileWriter fileWriter = new FileWriter(new File(fileName));
      DumperOptions options = new DumperOptions();
      options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
      Yaml yaml = new Yaml(options);
      yaml.dump(map, fileWriter);
    }
  }
}
