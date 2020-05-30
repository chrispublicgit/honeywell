package honeywell;

import disparse.parser.reflection.Injectable;
import honeywell.injectable.Injectables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Honeywell {

  private static final Logger logger = LoggerFactory.getLogger(Honeywell.class);

  public static void main(String[] args) {
    logger.info("Hello, World!");
    Injectables.injectConfig();
  }
}
