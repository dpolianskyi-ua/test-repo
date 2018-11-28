package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private final CounterService counter;
    private final GaugeService gauge;
    private TimeEntryRepository timeEntriesRepository;

    public TimeEntryController(TimeEntryRepository timeEntriesRepository,
                               CounterService counter,
                               GaugeService gauge) {
        this.timeEntriesRepository = timeEntriesRepository;
        this.counter = counter;
        this.gauge = gauge;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody @NotNull TimeEntry entry) {
        TimeEntry createdTimeEntry = timeEntriesRepository.create(entry);
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntriesRepository.list().size());

        return new ResponseEntity<>(createdTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable @NotNull Long id) {
        return ofNullable(timeEntriesRepository.find(id))
                .map(entry -> {
                    counter.increment("TimeEntry.read");
                    return new ResponseEntity<>(entry, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        counter.increment("TimeEntry.listed");
        return new ResponseEntity<>(timeEntriesRepository.list(), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable @NotNull Long id, @RequestBody @NotNull TimeEntry entry) {
        return ofNullable(timeEntriesRepository.update(id, entry))
                .map(updatedEntry -> {
                    counter.increment("TimeEntry.updated");
                    return new ResponseEntity<>(updatedEntry, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable @NotNull Long id) {
        timeEntriesRepository.delete(id);

        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", timeEntriesRepository.list().size());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
