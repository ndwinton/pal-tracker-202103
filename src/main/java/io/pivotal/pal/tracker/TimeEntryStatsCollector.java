package io.pivotal.pal.tracker;

public interface TimeEntryStatsCollector {
    enum Action { CREATE, READ, LIST, UPDATE, DELETE };

    void record(Action action);
}
