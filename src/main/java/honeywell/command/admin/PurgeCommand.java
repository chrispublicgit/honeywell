package honeywell.command.admin;

import disparse.parser.dispatch.IncomingScope;
import disparse.parser.reflection.CommandHandler;
import disparse.parser.reflection.Flag;
import disparse.parser.reflection.ParsedEntity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PurgeCommand {

  @CommandHandler(
      commandName = "admin.purge",
      description = "Purge a number of messages from a text channel.",
      roles = {"Admin"},
      acceptFrom = IncomingScope.CHANNEL)
  public void purge(MessageReceivedEvent event, PurgeOptions purgeOptions) {

    MessageChannel channel = event.getChannel();

    channel
        .getHistoryBefore(event.getMessage(), purgeOptions.amount)
        .queue(
            messageHistory -> {
              messageHistory.getRetrievedHistory().forEach(m -> m.delete().queue());
            });

    event.getMessage().delete().queue();
  }

  @ParsedEntity
  private static class PurgeOptions {
    @Flag(
        shortName = 'a',
        longName = "amount",
        description = "Amount of messages to delete in the channel",
        required = true)
    Integer amount;
  }
}
