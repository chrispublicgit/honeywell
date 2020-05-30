package honeywell.middleware;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CommandFrequencyMiddleware
    implements BiFunction<MessageReceivedEvent, String, Boolean> {

  private ConcurrentHashMap<String, AtomicInteger> occurrences = new ConcurrentHashMap<>();

  @Override
  public Boolean apply(MessageReceivedEvent messageReceivedEvent, String command) {
    occurrences.computeIfAbsent(command, (k) -> new AtomicInteger(0)).incrementAndGet();
    return true;
  }

  public Map<String, Integer> getOccurrences() {
    return this.occurrences.entrySet().stream()
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, entry -> entry.getValue().get()));
  }
}
