package wizard;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.apache.tika.exception.TikaException;
import search.Search;
import wizard.resources.IndexResource;

import java.io.IOException;

public class SearchApplication extends Application<SearchConfiguration> {
  public static void main(String[] args) throws Exception {
    new SearchApplication().run(args);
  }

  @Override
  public String getName() {
    return "search-engine";
  }

  @Override
  public void initialize(Bootstrap<SearchConfiguration> bootstrap) {
    bootstrap.addBundle(new ViewBundle());
  }

  @Override
  public void run(SearchConfiguration configuration,
                  Environment environment) throws IOException, TikaException {
    // This sets up in-memory processes required
    Search s = new Search();
    s.setup();

    final IndexResource resource = new IndexResource(s);

    environment.jersey().register(resource);
  }
}