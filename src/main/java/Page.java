import lombok.Data;

import java.util.List;

@Data
public class Page {
    public String url;
    public String title;
    public String filename;
    public String language;
    public String contents;
    public double pageRank;
    public List<String> links;
}

