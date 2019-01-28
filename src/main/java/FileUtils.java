import java.io.File;

public class FileUtils {
  public static File[] listFiles(String directoryName) {
    File d = new File(directoryName);
    if (!d.exists() || !d.isDirectory()) {
      throw new RuntimeException("Cannot open the results directory!");
    }

    return d.listFiles(pathname -> pathname.getName().endsWith(".html"));
  }
}
