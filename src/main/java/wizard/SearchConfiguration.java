package wizard;

import io.dropwizard.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchConfiguration extends Configuration {
  public double luc_w;
  public double tf_w;
  public double pr_w;
}
