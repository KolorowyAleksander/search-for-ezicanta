import ir.webutils.Spider;

public class Crawler {
    public static void crawl(String startURL, String outputDirectory, int count) {
        Spider s = new Spider();
//        -safe : Check for and obey robots.txt and robots META tag directives.
//        -d <directory> : Store indexed files in <directory>.
//        -c <count> : Store at most <count> files.
//        -u <url> : Start at <url>.
//        -slow : Pause briefly before getting a page. This can be useful when debugging.
        try {
            s.go(new String[]{
                    "-safe", "false",
                    "-d", outputDirectory,
                    "-c", String.valueOf(count),
                    "-u", startURL,
                    "-slow", "false"
            });
        } catch (NullPointerException e) { }
    }
}
