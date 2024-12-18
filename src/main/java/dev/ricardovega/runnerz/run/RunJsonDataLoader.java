package dev.ricardovega.runnerz.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;

@Component
public class RunJsonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RunJsonDataLoader.class);
    private final JdbcClientRunRepository jdbcClientRunRepository; // Repository for interacting with the database
    private final ObjectMapper objectMapper; // ObjectMapper to handle JSON deserialization

    // Constructor to inject dependencies (JdbcClientRunRepository and ObjectMapper)
    public RunJsonDataLoader(JdbcClientRunRepository jdbcClientRunRepository) {
        this.jdbcClientRunRepository = jdbcClientRunRepository;
        this.objectMapper = new ObjectMapper();

        // Registering the JavaTimeModule to handle Java 8 DateTime types in JSON
        objectMapper.registerModule(new JavaTimeModule());

        // Disable the serialization of dates as timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // This method is executed when the Spring Boot application starts
    @Override
    public void run(String... args) throws Exception {
        // Check if the database already has data before loading from the JSON file
        if(jdbcClientRunRepository.count() == 0) {
            try (InputStream inputStream = getClass().getResourceAsStream("/data/runs.json")) {
                // Read the runs from the JSON file and map them to a Runs object
                Runs allRuns = objectMapper.readValue(inputStream, Runs.class);
                // Log the number of runs that will be saved to the database
                log.info("Reading {} runs from JSON data and saving it to DB.", allRuns.runs().size());
                // Save the read runs to the database
                jdbcClientRunRepository.saveAll(allRuns.runs());
            } catch (IOException e) {
                // Handle failure in reading JSON file and throw runtime exception
                throw new RuntimeException("Failed to read JSON data", e);
            }
        } else {
            // Log that the data will not be loaded because the database is not empty
            log.info("Not loading Runs from JSON data because the collection contains data.");
        }
    }
}
