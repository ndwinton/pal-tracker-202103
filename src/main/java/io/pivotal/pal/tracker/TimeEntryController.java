package io.pivotal.pal.tracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private final TimeEntryRepository timeEntryRepository;
    private final TimeEntryStatsCollector statsCollector;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, TimeEntryStatsCollector statsCollector) {
        this.timeEntryRepository = timeEntryRepository;
        this.statsCollector = statsCollector;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        statsCollector.record(TimeEntryStatsCollector.Action.CREATE);
        var created = timeEntryRepository.create(timeEntryToCreate);
        return ResponseEntity.created(URI.create("/time-entries/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {
        statsCollector.record(TimeEntryStatsCollector.Action.READ);
        TimeEntry found = timeEntryRepository.find(timeEntryId);
        if (found != null) {
            return ResponseEntity.ok(found);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<TimeEntry>> list() {
        statsCollector.record(TimeEntryStatsCollector.Action.LIST);
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry timeEntryToUpdate) {
        statsCollector.record(TimeEntryStatsCollector.Action.UPDATE);
        TimeEntry updated = timeEntryRepository.update(timeEntryId, timeEntryToUpdate);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long timeEntryId) {
        statsCollector.record(TimeEntryStatsCollector.Action.DELETE);
        timeEntryRepository.delete(timeEntryId);
        return ResponseEntity.noContent().build();
    }
}
