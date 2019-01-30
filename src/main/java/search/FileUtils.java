package search;

import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.Link;
import org.apache.tika.sax.LinkContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

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

  static String readFile(File f) throws IOException {
    StringBuilder fileContents = new StringBuilder((int) f.length());

    try (Scanner scanner = new Scanner(f)) {
      while (scanner.hasNextLine()) {
        fileContents
            .append(scanner.nextLine())
            .append(System.lineSeparator());
      }
    }

    return fileContents.toString();
  }

  static Page parseFile(File f, Set<String> urls) throws IOException, TikaException {
    Page p = new Page();

    p.filename = f.getName();
    p.url = getOriginalURL(f);


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
