package honeywell.command.admin;

import com.typesafe.config.Config;
import disparse.parser.reflection.CommandHandler;
import disparse.parser.reflection.Flag;
import disparse.parser.reflection.ParsedEntity;
import honeywell.middleware.CommandFrequencyMiddleware;
import net.dv8tion.jda.api.EmbedBuilder;

import javax.swing.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class StatisticsCommand {

  @CommandHandler(
      commandName = "stats.commands",
      description = "Get usage information for commands.")
  public static EmbedBuilder commandStats(CommandFrequencyMiddleware commandFrequencyMiddleware, StatOpts opts) {
    Map<String, Integer> occurrences = commandFrequencyMiddleware.getOccurrences();

    int total = occurrences.values().stream().mapToInt(Integer::intValue).sum();

    EmbedBuilder builder = new EmbedBuilder()
            .setTitle("Command Occurrences")
            .setDescription("Statistics for commands being used.")
            .addField("total usage", String.valueOf(total), true);

    occurrences.entrySet().stream()
        .sorted(Collections.reverseOrder(Comparator.comparingInt(Map.Entry::getValue)))
        .forEachOrdered(
            entry -> {
              if (opts.showPercentage) {
                double percent = ((entry.getValue() * 1.0) / total) * 100.0;
                builder.addField(entry.getKey(), String.format("%3.2f%%", percent), true);
              } else {
                builder.addField(entry.getKey(), entry.getValue().toString(), true);
              }
            });

    return builder;
  }

  @ParsedEntity
  private static class StatOpts {
    @Flag(shortName = 'p', longName = "show-percentage", description = "Convert absolute values into percentage.")
    Boolean showPercentage = false;
  }
}
