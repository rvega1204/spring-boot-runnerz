package dev.ricardovega.runnerz.run;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

public record Run (
        @Id
        Integer id, // Unique identifier for the run
        @NotEmpty
        String title, // Title of the run, cannot be empty
        @JsonProperty("started_on") LocalDateTime startedOn, // Start time of the run, serialized as "started_on"
        @JsonProperty("completed_on") LocalDateTime completedOn, // Completion time of the run, serialized as "completed_on"
        @Positive
        Integer miles, // Distance of the run in miles, must be a positive value
        Location location, // Location of the run, of type Location (enum or class)
        @Version
        Integer version // Optimistic lock version for concurrent updates
) {

    // Constructor with validation logic for run times
    public Run {
        // Set the start time to the current time if it is not provided
        if (startedOn == null) {
            startedOn = LocalDateTime.now();
        }

        // Set the completion time to one hour after the start time if not provided or invalid
        if (completedOn == null || !completedOn.isAfter(startedOn)) {
            completedOn = startedOn.plusHours(1);
        }

        // Ensure that the completion time is after the start time
        if (!completedOn.isAfter(startedOn)) {
            throw new IllegalArgumentException("Completed On must be after Started On"); // Throw error if validation fails
        }
    }
}
