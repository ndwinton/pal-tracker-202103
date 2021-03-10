package io.pivotal.pal.tracker;

public interface StatsCollector {
    void record(String action);
}
