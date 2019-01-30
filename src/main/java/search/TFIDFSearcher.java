package search;

import Jama.Matrix;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


class TFIDFSearcher {

  @Data
  public static class TFIDFResults {
    public double[][] words;
    public SortedSet<String> keywords;
    public List<Page> pages;
  }

  static TFIDFResults calculate(List<Page> pages) throws IOException {
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

    ArrayUtils.normalizeWordsArray(wordsMatrix);

//    // count idf for all columns
//    double[] idfs = new double[keywords.size()];
//    for (int j = 0; j < wordsMatrix[0].length; j++) {
//      for (int i = 0; i < wordsMatrix.length; i++) {
//        idfs[j] += wordsMatrix[i][j] != 0.0 ? 1.0 : 0.0;
//      }
//    }
//
//    for (int i = 0; i < wordsMatrix[0].length; i++) {
//      idfs[i] = Math.log(wordsMatrix.length / idfs[i]);
//    }
//
//    // multiply wordsMatrix by idf
//    for (int j = 0; j < wordsMatrix[0].length; j++) {
//      for (int i = 0; i < wordsMatrix.length; i++) {
//        wordsMatrix[i][j] *= idfs[j];
//      }
//    }

    TFIDFResults tfidf = new TFIDFResults();
    tfidf.keywords = keywords;
    tfidf.words = wordsMatrix;
    tfidf.pages = pages;
    return tfidf;
  }

  static List similarity(String query, SortedSet<String> keywords, double[][] wordsMatrix) {
    String[] tokens = query
        .trim()
        .replaceAll("\\s", " ")
        .replaceAll("\\s+", " ")
        .replaceAll("[^A-Za-z\\d\\s\\-\']", " ")
        .split("\\s");

    List<String> tokens2 = Arrays
        .stream(tokens)
        .map(String::toLowerCase)
        .map(StemmerHelperKt::stem)
        .collect(Collectors.toList());

    // construct the query vector
    double[] queryVector = new double[keywords.size()];

    Iterator<String> it = keywords.iterator();
    for (int i = 0; it.hasNext(); i++) {
      String token = it.next();
      queryVector[i] += tokens2.stream().filter(token::equals).count();
    }

    ArrayUtils.normalizeQueryVector(queryVector);

    Matrix wm = ArrayUtils.arrayToMatrixInv(wordsMatrix);
    double[][] queryVectorMatrix = new double[1][queryVector.length];

    Matrix qvm = ArrayUtils.arrayToMatrixInv(queryVectorMatrix);

    LSIKt.lsi(wm, qvm);

    return null;
  }

  private static Set<String> readStopWords() throws IOException {
    File f = ResourceUtils.getFile("stop_words");
    String[] sws = FileUtils.readFile(f).split("\\n");
    return Arrays.stream(sws).collect(Collectors.toSet());
  }
}
