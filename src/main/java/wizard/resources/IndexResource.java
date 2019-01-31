package wizard.resources;

import search.Page;
import search.Search;
import wizard.views.IndexView;
import wizard.views.ResultsView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class IndexResource {
  private final Search search;

  public IndexResource(Search s) {
    search = s;
  }

  @GET
  public IndexView getIndex() {
    return new IndexView();
  }

  @POST

  public ResultsView getSearch(@FormParam("query") String query) throws IOException {
    System.out.println(query);
    List<Page> pages = search.search(query);
    return new ResultsView(pages);
  }
}
