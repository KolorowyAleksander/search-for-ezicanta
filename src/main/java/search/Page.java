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
    public double pageRankScore;
    public double tfidfScore;
    public double luceneScore;
    public List<String> links;
}

