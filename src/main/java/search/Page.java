package search;

import lombok.Data;

import java.util.List;

@Data
public class Page {
  public String url;
  public String title;
  public String filename;
  public String language;
  public String contents;
  public String contentsShort;
  public double pageRankScore;
  public double tfidfScore;
  public double luceneScore;
  public double finalScore;
  public int incoming;
  public int outgoing;
  public List<String> links;

  public static void setFinalScore(Page p, double pr_w, double luc_w, double tf_w) {
    p.finalScore = p.pageRankScore * pr_w + p.luceneScore * luc_w + p.tfidfScore * tf_w;
  }
}

