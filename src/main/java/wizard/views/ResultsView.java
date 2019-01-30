package wizard.views;

import io.dropwizard.views.View;
import search.Page;

import java.util.List;

public class ResultsView extends View {
  private final List<Page> pages;

  public ResultsView(List<Page> pages) {
    super("results.mustache");
    this.pages = pages;
  }

  public List<Page> getPages() {
    return this.pages;
  }
}
