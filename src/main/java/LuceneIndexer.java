import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;

class LuceneIndexer {

  static final String CONTENT = "content";
  static final String PATH = "path";
  static final String URLF = "url";

  static void indexDirectory(String indexDirectory, String dataDirectory) throws IOException {
    Directory dir = FSDirectory.open(Paths.get(indexDirectory));
    Analyzer analyzer = new StandardAnalyzer();

    IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
    iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

    try (IndexWriter iw = new IndexWriter(dir, iwc)) {
      File[] files = FileUtils.listFiles(dataDirectory);

      for (File f : files) {
        indexDocument(iw, f);
      }
    }
  }

  private static void indexDocument(IndexWriter iw, File f) throws IOException {
    try (InputStream inputStream = new FileInputStream(f)) {
      Document doc = new Document();

      Field pathField = new StringField(PATH, f.getPath(), Field.Store.YES);
      doc.add(pathField);

      String documentURL = FileUtils.getOriginalURL(f);
      Field urlField = new StringField(URLF, documentURL, Field.Store.YES);
      doc.add(urlField);

      Field contentField = new TextField(CONTENT, new InputStreamReader(inputStream));
      doc.add(contentField);

      iw.addDocument(doc);
    }
  }

  static void search(String indexDirectory, String query) throws IOException {
    Directory directory = FSDirectory.open(Paths.get(indexDirectory));
    IndexReader indexReader = DirectoryReader.open(directory);
    IndexSearcher indexSearcher = new IndexSearcher(indexReader);

    Analyzer analyzer = new StandardAnalyzer();
    QueryParser queryParser = new QueryParser(CONTENT, analyzer);

    try {
      Query q = queryParser.parse(query);
      TopDocs topDocs = indexSearcher.search(q, 10);
      ScoreDoc hits[] = topDocs.scoreDocs;

      for (ScoreDoc scd : topDocs.scoreDocs) {
        Document doc = indexSearcher.doc(scd.doc);
        // extract scores and return a list of URL/lucene score
      }
    } catch (ParseException e) {
      System.out.println("The query which the query parser is trying to parse is invalid!");
    }
  }
}
