package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        var created = timeEntryRepository.create(timeEntryToCreate);
        return ResponseEntity.created(URI.create("/time-entries/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {
        TimeEntry found = timeEntryRepository.find(timeEntryId);
        if (found != null) {
            return ResponseEntity.ok(found);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry timeEntryToUpdate) {
        TimeEntry updated = timeEntryRepository.update(timeEntryId, timeEntryToUpdate);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return ResponseEntity.noContent().build();
    }
}
