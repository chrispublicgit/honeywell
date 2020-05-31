package honeywell.command.admin;

import disparse.discord.jda.Dispatcher;
import disparse.parser.dispatch.IncomingScope;
import disparse.parser.reflection.CommandHandler;
import disparse.parser.reflection.Flag;
import disparse.parser.reflection.ParsedEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmbedCreatorCommand {

  private static Map<String, EmbedBuilder> embedBuilderMap = new HashMap<>();

  @CommandHandler(commandName = "admin.embedhelper", description = "utility command to help create embeds", acceptFrom = IncomingScope.DM)
  public void embedHelper(MessageReceivedEvent event, Dispatcher dispatcher, EmbedHelperOpts opts) {
    String userId = dispatcher.identityFromEvent(event);

    if (opts.clear) {
      embedBuilderMap.put(userId, new EmbedBuilder());
    }

    embedBuilderMap.putIfAbsent(userId, new EmbedBuilder());

    EmbedBuilder builder = embedBuilderMap.get(userId);

    if (opts.title != null) builder.setTitle(opts.title);

    if (opts.description != null) builder.setDescription(opts.description);

    if (opts.author != null) builder.setAuthor(opts.author);

    if (opts.color != null) builder.setColor(opts.color);

    if (opts.imageUrl != null) builder.setImage(opts.imageUrl);
  }

  @CommandHandler(commandName = "admin.embedhelper.addfield", description = "add field to your current embed", acceptFrom = IncomingScope.DM)
  public void embedHelperAddField(MessageReceivedEvent event, Dispatcher dispatcher, FieldOpts opts) {
    String userId = dispatcher.identityFromEvent(event);
    embedBuilderMap.putIfAbsent(userId, new EmbedBuilder());

    EmbedBuilder builder = embedBuilderMap.get(userId);

    if (opts.blank) {
      builder.addBlankField(opts.inline);
      dispatcher.sendMessage(event, "Blank field added!");
      return;
    }

    if (opts.key == null || opts.value == null) {
      dispatcher.sendMessage(event, "Key or Value cannot be null in a field!");
    }

    builder.addField(opts.key, opts.value, opts.inline);
  }

  @CommandHandler(commandName = "admin.embedhelper.deletefield", description = "delete field from current embed", acceptFrom = IncomingScope.DM)
  public void embedHelperDeleteField(MessageReceivedEvent event, Dispatcher dispatcher, RemoveOpts opts) {
    String userId = dispatcher.identityFromEvent(event);
    embedBuilderMap.putIfAbsent(userId, new EmbedBuilder());

    EmbedBuilder builder = embedBuilderMap.get(userId);

    List<MessageEmbed.Field> fields = builder.getFields();

    opts.numbers.stream().map(Integer::valueOf).map(i -> i - 1).mapToInt(Integer::intValue).forEach(i -> {
      if (fields.size() > i) builder.getFields().remove(i);
    });

  }

  @CommandHandler(commandName = "admin.embedhelper.show", description = "show your current embed", acceptFrom = IncomingScope.DM)
  public EmbedBuilder embedHelperShow(MessageReceivedEvent event, Dispatcher dispatcher) {
    String userId = dispatcher.identityFromEvent(event);
    return embedBuilderMap.getOrDefault(userId, new EmbedBuilder());
  }

  @ParsedEntity
  private static class EmbedHelperOpts {
    @Flag(shortName = 'c', longName = "clear", description = "clear current embed")
    Boolean clear = false;

    @Flag(shortName = 't', longName = "title", description = "title of embed")
    String title = null;

    @Flag(shortName = 'd', longName = "description", description = "description of embed")
    String description = null;

    @Flag(shortName = 'a', longName = "author", description = "author of embed")
    String author = null;

    @Flag(shortName = 'C', longName = "color", description = "color of embed")
    Integer color = null;

    @Flag(shortName = 'i', longName = "image-url", description = "image url to use in embed")
    String imageUrl = null;
  }

  @ParsedEntity
  private static class FieldOpts {
    @Flag(shortName = 'v', longName = "value", description = "field value")
    String value = null;

    @Flag(shortName = 'k', longName = "key", description = "field key")
    String key = null;

    @Flag(shortName = 'i', longName = "inline", description = "display inline")
    Boolean inline = false;

    @Flag(shortName = 'b', longName = "blank", description = "display blank field")
    Boolean blank = false;
  }

  @ParsedEntity
  private static class RemoveOpts {
    @Flag(shortName = 'n', longName = "number", description = "what field number to delete starting at 1")
    List<String> numbers = new ArrayList<>();
  }
}
