import ir.webutils.Spider;
import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.Link;
import org.apache.tika.sax.LinkContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
  private static final String OUTPUT_DIRECTORY = "/Users/a/Desktop/results";
  private static final String INDEX_DIRECTORY = "/Users/a/Desktop/index/";

  private static final String START_URL = "https://en.wikipedia.org/wiki/Downtempo";
  private static final int CRAWL_PAGES_COUNT = 150;

  static final HashSet<String> urls = new HashSet<>();
  static final List<Page> pages = new ArrayList<>();

  public static void main(String[] args) throws IOException, TikaException {
    // Crawler.crawl(START_URL, OUTPUT_DIRECTORY, CRAWL_PAGES_COUNT);

    File[] files = FileUtils.listFiles(OUTPUT_DIRECTORY);
    for (File f : files) {
      String originalURL = FileUtils.getOriginalURL(f);
      urls.add(originalURL);
    }

    for (File f : files) {
      Page p = parseFile(f);
      pages.add(p);
    }

    PageRankCounter.countPageRank(pages);

    // LuceneIndexer.indexDirectory(INDEX_DIRECTORY, OUTPUT_DIRECTORY);
    // LuceneIndexer.search(INDEX_DIRECTORY, "downtempo");

    TFIDFSearcher.calculate(pages);

    return;
  }

  static Page parseFile(File f) throws IOException, TikaException {
    Page p = new Page();

    p.filename = f.getName();
    p.url = FileUtils.getOriginalURL(f);


    try (InputStream stream = new FileInputStream(f)) {
      AutoDetectParser parser = new AutoDetectParser();

      BodyContentHandler handler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      try {
        parser.parse(stream, handler, metadata);
      } catch (SAXException ignored) { }

      String contents = handler.toString();

      LanguageIdentifier l = new LanguageIdentifier(contents);

      p.language = l.getLanguage();
      p.contents = contents;
      p.title = metadata.get("title");
    }

    try (InputStream stream = new FileInputStream(f)) {
      AutoDetectParser parser = new AutoDetectParser();

      LinkContentHandler handler = new LinkContentHandler();
      Metadata metadata = new Metadata();

      try {
        parser.parse(stream, handler, metadata);
      } catch (SAXException ignored) { }

      p.links = handler.getLinks()
          .stream()
          .filter(l -> l.isLink() || l.isAnchor())
          .filter(l -> urls.contains(l.getUri()))
          .map(Link::getUri)
          .distinct()
          .collect(Collectors.toList());
    }

    return p;
  }

}