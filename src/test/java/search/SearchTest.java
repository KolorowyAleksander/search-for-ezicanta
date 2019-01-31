package search;

import org.apache.tika.exception.TikaException;
import org.junit.Before;
import org.junit.Test;
import wizard.SearchConfiguration;

import java.io.IOException;

public class SearchTest {

  private Search sut;
  private final SearchConfiguration config = new SearchConfiguration(1.0, 1.0, 1.0);

  @Before
  public void setUp() throws IOException, TikaException {

    this.sut = new Search(this.config);
    // sut.setup();
  }

  @Test
  public void search() throws IOException {
    // sut.search("downtempo music industry");
  }
}