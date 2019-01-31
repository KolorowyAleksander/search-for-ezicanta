package search;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PageRankCounterTest {

  private List<Page> pages;

  @Before
  public void setUp() {
    Page p1 = new Page();
    p1.links = List.of("D2", "D3", "D4");
    p1.url = "D1";

    Page p2 = new Page();
    p2.links = List.of("D3", "D4");
    p2.url = "D2";

    Page p3 = new Page();
    p3.links = List.of("D1");
    p3.url = "D3";

    Page p4 = new Page();
    p4.links = List.of("D1", "D3");
    p4.url = "D4";

    pages = List.of(p1, p2, p3, p4);
  }


  @Test
  // 0,3865740741	0,12890625	0,2906539352	0,1938657407
  public void testPageRankCounterCountPageRank() {
    PageRankCounter.countPageRank(pages);
  }
}
