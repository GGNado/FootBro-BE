package com.giggi.config;

import com.giggi.entity.Role;
import com.giggi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Data initializer that seeds default roles on application startup.
 * Roles are only created if they don't already exist in the database.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private static final Map<String, String> DEFAULT_ROLES = Map.of(
            "ROLE_USER", "Ruolo di Default",
            "ROLE_ADMIN", "Ruolo di Admin",
            "ROLE_LEAGUEADMIN", "Ruolo di Admin di lega"
    );

    @Override
    public void run(String... args) {
        log.info("Inizializzazione ruoli...");

        DEFAULT_ROLES.forEach((name, description) -> {
            if (roleRepository.findByName(name).isEmpty()) {
                Role role = new Role();
                role.setName(name);
                role.setDescription(description);
                roleRepository.save(role);
                log.info("Ruolo creato: {} - {}", name, description);
            } else {
                log.debug("Ruolo già esistente: {}", name);
            }
        });

        log.info("Inizializzazione ruoli completata. Totale ruoli: {}", roleRepository.count());
    }
}
