package search;

import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Search {
  private static final String OUTPUT_DIRECTORY = "/Users/a/Desktop/results";
  private static final String INDEX_DIRECTORY = "/Users/a/Desktop/index/";

  private static final String START_URL = "https://en.wikipedia.org/wiki/Downtempo";
  private static final int CRAWL_PAGES_COUNT = 150;

  public final HashSet<String> urls = new HashSet<>();
  public final List<Page> pages = new ArrayList<>();
  public TFIDFSearcher.TFIDFResults tf;

  // you can run this as if you wanted to crawl and index
  public static void main(String[] args) throws IOException {
    Crawler.crawl(START_URL, OUTPUT_DIRECTORY, CRAWL_PAGES_COUNT);
    LuceneIndexer.indexDirectory(INDEX_DIRECTORY, OUTPUT_DIRECTORY);
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

  public void search(String query) throws IOException {
    PageRankCounter.countPageRank(pages);

    TFIDFSearcher.similarity(query, pages, tf.keywords, tf.words, tf.frequencies);

    LuceneIndexer.search(INDEX_DIRECTORY, query, pages);
  }
}