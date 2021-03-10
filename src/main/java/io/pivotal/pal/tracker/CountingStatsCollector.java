package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CountingStatsCollector implements StatsCollector {

    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> counters = new HashMap<>();

    public CountingStatsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        counters.put("create", meterRegistry.counter("timeEntry.counters.create"));
        counters.put("read", meterRegistry.counter("timeEntry.counters.read"));
        counters.put("list", meterRegistry.counter("timeEntry.counters.list"));
        counters.put("update", meterRegistry.counter("timeEntry.counters.update"));
        counters.put("delete", meterRegistry.counter("timeEntry.counters.delete"));
    }

    @Override
    public void record(String action) {
        var counter = counters.get(action);
        if (counter != null) {
            counter.increment();
        }
    }
}
