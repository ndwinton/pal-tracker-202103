package io.pivotal.pal.tracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private final TimeEntryRepository timeEntryRepository;
    private final StatsCollector statsCollector;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, StatsCollector statsCollector) {
        this.timeEntryRepository = timeEntryRepository;
        this.statsCollector = statsCollector;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        statsCollector.record("create");
        var created = timeEntryRepository.create(timeEntryToCreate);
        return ResponseEntity.created(URI.create("/time-entries/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {
        statsCollector.record("read");
        TimeEntry found = timeEntryRepository.find(timeEntryId);
        if (found != null) {
            return ResponseEntity.ok(found);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<TimeEntry>> list() {
        statsCollector.record("list");
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry timeEntryToUpdate) {
        statsCollector.record("update");
        TimeEntry updated = timeEntryRepository.update(timeEntryId, timeEntryToUpdate);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long timeEntryId) {
        statsCollector.record("delete");
        timeEntryRepository.delete(timeEntryId);
        return ResponseEntity.noContent().build();
    }
}
