package dev.ricardovega.runnerz.run;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryRunRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryRunRepository.class);
    private final List<Run> runs = new ArrayList<>();

    // Retrieve all runs from the in-memory list
    public List<Run> findAll() {
        return runs;
    }

    // Find a run by its ID
    // Returns an Optional containing the run if found; throws RunNotFoundException otherwise
    public Optional<Run> findById(Integer id) {
        return Optional.ofNullable(runs.stream()
                .filter(run -> Objects.equals(run.id(), id)) // Filter runs by matching the ID
                .findFirst()
                .orElseThrow(RunNotFoundException::new)); // Throw exception if no match is found
    }

    // Create a new run and add it to the list
    public void create(Run run) {
        // Create a new Run instance with the provided data and a null parentRun (if applicable)
        Run newRun = new Run(run.id(),
                run.title(),
                run.startedOn(),
                run.completedOn(),
                run.miles(),
                run.location(), null);

        // Add the new run to the in-memory list
        runs.add(newRun);
    }

    // Update an existing run by replacing it with a new instance
    public void update(Run newRun, Integer id) {
        // Find the existing run by its ID
        Optional<Run> existingRun = findById(id);
        if (existingRun.isPresent()) {
            var r = existingRun.get();
            log.info("Updating Existing Run: {}", existingRun.get()); // Log the update action
            runs.set(runs.indexOf(r), newRun); // Replace the existing run in the list
        }
    }

    // Delete a run by its ID
    public void delete(Integer id) {
        log.info("Deleting Run: {}", id); // Log the deletion action
        // Remove the run with the matching ID from the list
        runs.removeIf(run -> run.id().equals(id));
    }

    // Count the total number of runs in the in-memory list
    public int count() {
        return runs.size();
    }

    // Save multiple runs by iterating through the list and adding them one by one
    public void saveAll(List<Run> runs) {
        runs.forEach(this::create); // Use the create method for each run
    }

    // Find all runs that match the specified location
    public List<Run> findByLocation(String location) {
        return runs.stream()
                .filter(run -> Objects.equals(run.location(), location)) // Match by location
                .toList(); // Collect results as a list
    }

    // Initialize the repository with some default data
    @PostConstruct
    private void init() {
        // Add a sample run representing a 30-minute indoor run
        runs.add(new Run(1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                3, // Distance in miles
                Location.INDOOR, null));

        // Add another sample run representing a 60-minute indoor run
        runs.add(new Run(2,
                "Wednesday Evening Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60),
                6, // Distance in miles
                Location.INDOOR, null));
    }
}
