package honeywell.injectable;

import com.typesafe.config.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestInjectables {

  @Test
  public void testInjectableConfig() {
    Config config = Injectables.injectConfig();
    Assertions.assertEquals("honeywell", config.getString("conf.bot.name"));
    Assertions.assertEquals(
        "A general purpose Discord bot implemented with Disparse!",
        config.getString("conf.bot.description"));
  }
}
