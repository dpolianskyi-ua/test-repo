package io.pivotal.pal.tracker;

import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    //TODO: provide dataSource later (for now use Map)
    private Map<Long, TimeEntry> entries = new HashMap<>();

    @Override
    public TimeEntry create(TimeEntry entry) {
        return Optional.ofNullable(entry)
                .map(createdEntry -> {
                    entry.setId(entries.size() + 1L);
                    entries.put(entry.getId(), entry);

                    return createdEntry;
                }).orElse(null);
    }

    @Override
    public TimeEntry find(Long id) {
        return Optional.ofNullable(entries.get(id)).orElse(null);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(entries.values());
    }

    @Override
    public TimeEntry update(Long id, TimeEntry updatedEntry) {
        if(updatedEntry == null) {
            return null;
        }

        return Optional.ofNullable(find(id))
                .map(foundEntry -> {
                    foundEntry.setProjectId(updatedEntry.getProjectId());
                    foundEntry.setUserId(updatedEntry.getUserId());
                    foundEntry.setDate(updatedEntry.getDate());
                    foundEntry.setHours(updatedEntry.getHours());

                    return foundEntry;
                }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        entries.remove(id);
    }
}
