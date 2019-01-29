import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class TFIDFSearcher {

  private static class VectorEntry {
    public String token;
    public int frequency;
  }

  static void calculate(List<Page> pages) {
    Set<String> keywords = new HashSet<>();

    for (Page p : pages) {
      String contents = p.contents;

      String c2 = contents.replaceAll("\\s", " ");
      String c3 = c2.replaceAll("\\s+", "");

      String[] tokens = c3.split("\\s");

      keywords.addAll(Arrays.asList(tokens));
    }
  }

  static String[] readStopWords() throws IOException {
    File f = ResourceUtils.getFile("stop_words");
    String contents = FileUtils.readFile(f);

    return contents.split("\\n");
  }
}
