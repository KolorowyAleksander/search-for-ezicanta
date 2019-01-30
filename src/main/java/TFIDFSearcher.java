import com.google.common.primitives.Doubles;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;


class TFIDFSearcher {

  private static class VectorEntry {
    public String token;
    public int frequency;
  }

  static void calculate(List<Page> pages) throws IOException {
    SortedSet<String> keywords = new TreeSet<>();
    Set<String> stopWords = readStopWords();

    List<List<String>> kpp = new ArrayList<>();

    for (Page p : pages) {
      // removing spaces
      String[] tokens = p.contents
          .trim()
          .replaceAll("\\s", " ")
          .replaceAll("\\s+", " ")
          .replaceAll("[^A-Za-z\\d\\s\\-\']", " ")
          .split("\\s");

      // normalization, stemming
      List<String> tokens2 = Arrays
          .stream(tokens)
          .map(String::toLowerCase)
          .map(StemmerHelperKt::stem)
          .collect(Collectors.toList());

      tokens2.removeAll(stopWords);

      kpp.add(tokens2);
    }

    for (List<String> s : kpp) {
      keywords.addAll(s);
    }

    double[][] wordsMatrix = new double[kpp.size()][keywords.size()];
    // construct the words array
    for (int i = 0; i < kpp.size(); i++) {
      Iterator<String> it = keywords.iterator();
      for (int j = 0; it.hasNext(); j++) {
        String token = it.next();

        wordsMatrix[i][j] += kpp.get(i).stream()
            .filter(token::equals).count();
      }
    }

    // normalize array
    for (int i = 0; i < wordsMatrix.length; i++) {
      double max = Doubles.max(wordsMatrix[i]);
      for (int j = 0; j < wordsMatrix[i].length; j++) {
        wordsMatrix[i][j] /= max;
      }
    }

//    long nonzero = Arrays.stream(wordsMatrix)
//        .map(Arrays::stream)
//        .map(DoubleStream::boxed)
//        .flatMap(Function.identity())
//        .filter(x -> x != 0)
//        .count();

    double[] frequencies = new double[keywords.size()];
    for (int i = 0; i < wordsMatrix.length; i++) {
      
    }

    double frequency = Math.log(kpp.size() / nonzero);
  }

  private static Set<String> readStopWords() throws IOException {
    File f = ResourceUtils.getFile("stop_words");
    String[] sws = FileUtils.readFile(f).split("\\n");
    return Arrays.stream(sws).collect(Collectors.toSet());
  }
}
