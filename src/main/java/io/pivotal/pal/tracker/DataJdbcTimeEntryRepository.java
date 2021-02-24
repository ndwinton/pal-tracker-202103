package io.pivotal.pal.tracker;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DataJdbcTimeEntryRepository implements TimeEntryRepository {
    private final TimeEntryCrudRepository crudRepository;

    public DataJdbcTimeEntryRepository(TimeEntryCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        return crudRepository.save(timeEntry);
    }

    @Override
    public TimeEntry find(long id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public List<TimeEntry> list() {
        return StreamSupport
                .stream(crudRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (crudRepository.existsById(id)) {
            return crudRepository.save(new TimeEntry(
                    id,
                    timeEntry.getProjectId(),
                    timeEntry.getUserId(),
                    timeEntry.getDate(),
                    timeEntry.getHours()
            ));
        } else {
            return null;
        }
    }

    @Override
    public void delete(long id) {
        crudRepository.deleteById(id);
    }
}
