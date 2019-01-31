package search;

import org.apache.tika.exception.TikaException;
import wizard.SearchConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Search {
  private static final String OUTPUT_DIRECTORY = "/Users/a/Desktop/results";
  private static final String INDEX_DIRECTORY = "/Users/a/Desktop/index/";

  private static final String START_URL = "https://en.wikipedia.org/wiki/Downtempo";
  private static final int CRAWL_PAGES_COUNT = 150;

  private final HashSet<String> urls = new HashSet<>();
  public final List<Page> pages = new ArrayList<>();
  private final SearchConfiguration config;
  private TFIDFSearcher.TFIDFResults tf;

  // you can run this as if you wanted to crawl and index
  public static void main(String[] args) throws IOException {
    Crawler.crawl(START_URL, OUTPUT_DIRECTORY, CRAWL_PAGES_COUNT);
    LuceneIndexer.indexDirectory(INDEX_DIRECTORY, OUTPUT_DIRECTORY);
  }

  public Search(SearchConfiguration config) {
    this.config = config;
  }

  // you can run this as if you want to setup the search
  public void setup() throws IOException, TikaException {
    File[] files = FileUtils.listFiles(OUTPUT_DIRECTORY);
    for (File f : files) {
      String originalURL = FileUtils.getOriginalURL(f);
      urls.add(originalURL);
    }

    for (File f : files) {
      Page p = FileUtils.parseFile(f, urls);
      pages.add(p);
    }

    this.tf = TFIDFSearcher.calculate(pages);
  }

  public List<Page> search(String query) throws IOException {
    PageRankCounter.countPageRank(pages);

    TFIDFSearcher.similarity(query, pages, tf.keywords, tf.words, tf.frequencies);

    LuceneIndexer.search(INDEX_DIRECTORY, query, pages);

    countScores();

    return pages.stream()
        .sorted(Collections.reverseOrder(Comparator.comparing(Page::getFinalScore)))
        .limit(10)
        .collect(Collectors.toList());
  }

  private void countScores() {
    pages.forEach(p -> Page.setFinalScore(p, config.pr_w, config.luc_w, config.tf_w));
  }
}