package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleTimeEntryStatsCollector implements TimeEntryStatsCollector {

    private final MeterRegistry meterRegistry;
    private final Map<Action, Counter> counters = new HashMap<>();

    public SimpleTimeEntryStatsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        counters.put(Action.CREATE, meterRegistry.counter("timeEntry.counters.create"));
        counters.put(Action.READ, meterRegistry.counter("timeEntry.counters.read"));
        counters.put(Action.LIST, meterRegistry.counter("timeEntry.counters.list"));
        counters.put(Action.UPDATE, meterRegistry.counter("timeEntry.counters.update"));
        counters.put(Action.DELETE, meterRegistry.counter("timeEntry.counters.delete"));
    }

    @Override
    public void record(Action action) {
        var counter = counters.get(action);
        if (counter != null) {
            counter.increment();
        }
    }
}
