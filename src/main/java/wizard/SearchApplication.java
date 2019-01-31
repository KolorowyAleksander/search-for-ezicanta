package wizard;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.apache.tika.exception.TikaException;
import search.Search;
import wizard.resources.IndexResource;
import wizard.resources.ListResource;

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
  @SuppressWarnings("unchecked")
  public void initialize(Bootstrap<SearchConfiguration> bootstrap) {
    bootstrap.addBundle(new AssetsBundle("/assets/", "/assets/"));
    bootstrap.addBundle(new ViewBundle());
  }

  @Override
  public void run(SearchConfiguration configuration,
                  Environment environment) throws IOException, TikaException {
    // This sets up in-memory processes required
    Search s = new Search(configuration);
    s.setup();

    final IndexResource resource = new IndexResource(s);
    final ListResource res2 = new ListResource(s);

    environment.jersey().register(resource);
    environment.jersey().register(resource);
  }
}