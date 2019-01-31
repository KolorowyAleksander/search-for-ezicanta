package wizard.views;

import io.dropwizard.views.View;
import search.Page;

import java.util.List;

public class ListView extends View {
  private final List<Page> pages;

  public ListView(List<Page> pages) {
    super("list.mustache");
    this.pages = pages;
  }

  public List<Page> getPages() {
    return this.pages;
  }
}
