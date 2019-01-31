package search;

import java.io.File;

class ResourceUtils {

  public static File getFile(String name) {
    String fName = ResourceUtils
        .class
        .getResource(name)
        .getFile();

    return new File(fName);
  }
}
