package honeywell;

import com.typesafe.config.Config;
import disparse.discord.jda.Dispatcher;
import honeywell.injectable.Injectables;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public class Honeywell {

  private static final Logger logger = LoggerFactory.getLogger(Honeywell.class);

  private static final String BOT_TOKEN = "conf.bot.token";
  private static final String BOT_NAME = "conf.bot.name";
  private static final String BOT_DESC = "conf.bot.description";
  private static final String BOT_PREFIX = "conf.bot.prefix";
  private static final String BOT_PAGE_LIMIT = "conf.bot.pageLimit";

  private static final int defaultPageLimit = 5;

  public static void main(String[] args) throws Exception {
    Config config = Injectables.injectConfig();

    String token = getPathOrThrow(config, config::getString, BOT_TOKEN, () -> new NullPointerException(
            "The bot token must be supplied as an environment variable with a key of:  " + BOT_TOKEN));

    String name = getPathOrThrow(config, config::getString, BOT_NAME, () -> new NullPointerException(
            "The bot must have a supplied name as a config value with a key of:  " + BOT_NAME));

    String description = getPathOrDefault(config, config::getString, BOT_DESC, "");
    String prefix = getPathOrDefault(config, config::getString, BOT_PREFIX, name);
    int pageLimit = getPathOrDefault(config, config::getInt, BOT_PAGE_LIMIT, defaultPageLimit);

    logger.info("Starting {} with prefix {}", name, prefix);

    Dispatcher dispatcher =
        new Dispatcher.Builder()
            .description(description)
            .prefix(prefix)
            .pageLimit(pageLimit)
            .withMiddleware(Injectables.injectCommandFrequencyMiddleware())
            .build();

    JDA jda = Dispatcher.init(new JDABuilder(), dispatcher).setToken(token).build();

    jda.awaitReady();
  }

  private static <T> T getPathOrDefault(
      Config config, Function<String, T> f, String path, T defaultVal) {
    return config.hasPath(path) ? f.apply(path) : defaultVal;
  }

  private static <T, E extends Throwable> T getPathOrThrow(
      Config config, Function<String, T> f, String path, Supplier<E> execSupplier) throws E {
    if (config.hasPath(path)) {
      return f.apply(path);
    }
    throw execSupplier.get();
  }
}
