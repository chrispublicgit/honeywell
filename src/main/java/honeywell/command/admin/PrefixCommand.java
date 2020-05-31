package honeywell.command.admin;

import disparse.discord.jda.Dispatcher;
import disparse.parser.reflection.CommandHandler;
import disparse.parser.reflection.Flag;
import disparse.parser.reflection.ParsedEntity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PrefixCommand {

  @CommandHandler(commandName = "admin.setprefix", description = "Configure the bot's prefix")
  public void setPrefix(PrefixOpts opts, MessageReceivedEvent event, Dispatcher dispatcher) {
    dispatcher.setPrefix(event, opts.prefix);
  }

  @ParsedEntity
  private static class PrefixOpts {
    @Flag(shortName = 'p', longName = "prefix", description = "Prefix to use with the bot", required = true)
    String prefix;
  }
}
