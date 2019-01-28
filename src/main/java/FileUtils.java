import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class FileUtils {
  static File[] listFiles(String directoryName) {
    File d = new File(directoryName);
    if (!d.exists() || !d.isDirectory()) {
      throw new RuntimeException("Cannot open the results directory!");
    }

    return d.listFiles(pathname -> pathname.getName().endsWith(".html"));
  }


  static String getOriginalURL(File f) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
      String line = reader.readLine();
      return line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
    }
  }
}
