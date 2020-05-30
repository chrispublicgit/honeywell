package honeywell.injectable;

import com.stubbornjava.common.Configs;
import com.typesafe.config.Config;
import disparse.parser.reflection.Injectable;
import honeywell.middleware.CommandFrequencyMiddleware;

public class Injectables {

    private static CommandFrequencyMiddleware commandFrequencyMiddleware = new CommandFrequencyMiddleware();

    /**
     *
     * Create a fresh type-safe config instance to be used from within command handlers
     * as well as on bot start-up / configuration
     *
     * @return Type-safe Configuration Object
     */
    @Injectable
    public static Config injectConfig() {
        return Configs.newBuilder()
                .withSystemProperties()
                .withSystemEnvironment()
                .withResource("honeywell.conf")
                .build();
    }

    @Injectable
    public static CommandFrequencyMiddleware injectCommandFrequencyMiddleware() {
        return commandFrequencyMiddleware;
    }
}
