package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.InMemoryTimeEntryRepository;
import io.pivotal.pal.tracker.TimeEntry;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTimeEntryRepositoryTest {
    @Test
    public void create_whenEntryIsNull() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        TimeEntry createdTimeEntry = repo.create(null);

        assertThat(createdTimeEntry).isNull();
    }

    @Test
    public void create() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        TimeEntry createdTimeEntry = repo.create(new TimeEntry(projectId, userId, LocalDate.parse("2017-01-08"), 8));

        long timeEntryId = 1L;
        TimeEntry expected = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);
        assertThat(createdTimeEntry).isEqualTo(expected);

        TimeEntry readEntry = repo.find(createdTimeEntry.getId());
        assertThat(readEntry).isEqualTo(expected);
    }

    @Test
    public void find_whenEntryIsNull() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        TimeEntry readEntry = repo.find(null);
        assertThat(readEntry).isNull();
    }

    @Test
    public void find() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        repo.create(new TimeEntry(projectId, userId, LocalDate.parse("2017-01-08"), 8));

        long timeEntryId = 1L;
        TimeEntry expected = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);
        TimeEntry readEntry = repo.find(timeEntryId);
        assertThat(readEntry).isEqualTo(expected);
    }

    @Test
    public void list() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        repo.create(new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8));
        repo.create(new TimeEntry(789L, 654L, LocalDate.parse("2017-01-07"), 4));

        List<TimeEntry> expected = asList(
                new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8),
                new TimeEntry(2L, 789L, 654L, LocalDate.parse("2017-01-07"), 4)
        );
        assertThat(repo.list()).isEqualTo(expected);
    }

    @Test
    public void update_whenIdIsNull() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        TimeEntry created = repo.create(new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8));

        TimeEntry updatedEntry = repo.update(
                null,
                new TimeEntry());

        assertThat(updatedEntry).isNull();
    }

    @Test
    public void update_whenEntryIsNull() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        TimeEntry created = repo.create(new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8));


        TimeEntry updatedEntry = repo.update(
                created.getId(),
                null);

        assertThat(updatedEntry).isNull();
    }

    @Test
    public void update() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        TimeEntry created = repo.create(new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8));

        TimeEntry updatedEntry = repo.update(
                created.getId(),
                new TimeEntry(321L, 654L, LocalDate.parse("2017-01-09"), 5));

        TimeEntry expected = new TimeEntry(created.getId(), 321L, 654L, LocalDate.parse("2017-01-09"), 5);
        assertThat(updatedEntry).isEqualTo(expected);
        assertThat(repo.find(created.getId())).isEqualTo(expected);
    }

    @Test
    public void delete_whenIdIsNull() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        TimeEntry created = repo.create(new TimeEntry(projectId, userId, LocalDate.parse("2017-01-08"), 8));

        repo.delete(null);
        assertThat(repo.list()).hasSize(1);
    }

    @Test
    public void delete() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        TimeEntry created = repo.create(new TimeEntry(projectId, userId, LocalDate.parse("2017-01-08"), 8));

        repo.delete(created.getId());
        assertThat(repo.list()).isEmpty();
    }
}