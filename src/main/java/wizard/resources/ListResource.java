package wizard.resources;

import search.Search;
import wizard.views.ListView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/pages")
@Produces(MediaType.TEXT_HTML)
public class ListResource {
  private final Search search;

  public ListResource(Search s) {
    search = s;
  }

  @GET
  public ListView getIndex() {
    return new ListView(search.pages);
  }
}