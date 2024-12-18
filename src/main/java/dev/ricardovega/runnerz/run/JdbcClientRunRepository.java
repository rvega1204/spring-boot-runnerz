package dev.ricardovega.runnerz.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcClientRunRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcClientRunRepository.class);
    private final JdbcClient jdbcClient;

    // Constructor to inject the JdbcClient dependency
    public JdbcClientRunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    // Retrieve all runs from the database
    public List<Run> findAll() {
        return jdbcClient.sql("SELECT * FROM run") // SQL query to fetch all runs
                .query(Run.class) // Map the result to the Run class
                .list(); // Return as a list
    }

    // Find a run by its ID
    // Returns an Optional containing the run if found, empty if not
    public Optional<Run> findById(Integer id) {
        return jdbcClient.sql("SELECT id, title, started_on, completed_on, miles, location FROM run WHERE id = :id")
                .param("id", id) // Bind the ID parameter to the query
                .query(Run.class) // Map the result to the Run class
                .optional(); // Return as an Optional
    }

    // Create a new run in the database
    public void create(Run run) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Format start and completion times as timestamps
        String startedOnFormatted = run.startedOn().format(formatter);
        String completedOnFormatted = run.completedOn().format(formatter);

        Timestamp startedOnTimestamp = Timestamp.valueOf(startedOnFormatted);
        Timestamp completedOnTimestamp = Timestamp.valueOf(completedOnFormatted);

        String location = run.location().toString(); // Convert location to string for storage

        var updated = jdbcClient.sql("INSERT INTO run(id, title, started_on, completed_on, miles, location) VALUES(?,?,?,?,?,?)")
                .params(List.of(run.id(), run.title(), startedOnTimestamp, completedOnTimestamp, run.miles(), location)) // Set query parameters
                .update(); // Execute the update

        Assert.state(updated == 1, "Failed to create run " + run.title()); // Assert exactly one row was inserted
    }

    // Update an existing run in the database
    public void update(Run run, Integer id) {
        var updated = jdbcClient.sql("UPDATE run SET title = ?, started_on = ?, completed_on = ?, miles = ?, location = ? WHERE id = ?")
                .params(List.of(run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString(), id)) // Set query parameters
                .update(); // Execute the update

        Assert.state(updated == 1, "Failed to update run " + run.title()); // Assert exactly one row was updated
    }

    // Delete a run by its ID
    public void delete(Integer id) {
        var updated = jdbcClient.sql("DELETE FROM run WHERE id = :id")
                .param("id", id) // Bind the ID parameter to the query
                .update(); // Execute the update

        Assert.state(updated == 1, "Failed to delete run " + id); // Assert exactly one row was deleted
    }

    // Count the total number of runs in the database
    public int count() {
        return jdbcClient.sql("SELECT * FROM run") // Fetch all rows
                .query() // Execute the query
                .listOfRows() // Return rows as a list
                .size(); // Return the count of rows
    }

    // Save multiple runs to the database
    public void saveAll(List<Run> runs) {
        runs.forEach(this::create); // Use the create method for each run
    }

    // Find runs by their location
    public List<Run> findByLocation(String location) {
        return jdbcClient.sql("SELECT * FROM run WHERE location = :location")
                .param("location", location) // Bind the location parameter to the query
                .query(Run.class) // Map the result to the Run class
                .list(); // Return as a list
    }

}
