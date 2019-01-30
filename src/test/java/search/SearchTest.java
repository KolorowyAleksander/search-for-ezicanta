package search;

import org.apache.tika.exception.TikaException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SearchTest {

  private Search sut;

  @Before
  public void setUp() throws IOException, TikaException {
    this.sut = new Search();
    sut.setup();
  }

  @Test
  public void search() throws IOException {
    sut.search("downtempo music industry");
  }
}