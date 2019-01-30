package wizard.resources;

import search.Search;
import wizard.views.IndexView;
import wizard.views.ResultsView;
import wizard.views.SearchView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class IndexResource {

  private final Search search;

  public IndexResource(Search s) { search = s;}

  @GET
  public IndexView getIndex() {
    return new IndexView();
  }

  @POST

  public ResultsView getSearch(@QueryParam("query") String query) throws IOException {
    search.search(query);
    return new ResultsView(search.pages);
  }
}
