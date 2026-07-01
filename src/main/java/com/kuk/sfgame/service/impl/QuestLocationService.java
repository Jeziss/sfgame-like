package com.kuk.sfgame.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuk.sfgame.model.QuestLocation;
import com.kuk.sfgame.repository.QuestLocationRepository;
import com.kuk.sfgame.util.Constants;

import jakarta.annotation.PostConstruct;

@Service
public class QuestLocationService {

    private final QuestLocationRepository questLocationRepository;

    public QuestLocationService(QuestLocationRepository questLocationRepository) {
        this.questLocationRepository = questLocationRepository;
    }

    @PostConstruct
    public void init() {
        ensureDefaultLocations();
    }

    @Transactional(readOnly = true)
    public List<String> getLocations() {
        return questLocationRepository.findAllByOrderByNameAsc().stream()
                .map(QuestLocation::getName)
                .toList();
    }

    @Transactional(readOnly = true)
    public String getRandomLocation() {
        List<String> locations = getLocations();
        if (locations.isEmpty()) {
            return "Forest";
        }
        return locations.get(ThreadLocalRandom.current().nextInt(locations.size()));
    }

    @Transactional
    public void setLocations(List<String> newLocations) {
        List<String> normalized = normalizeLocations(newLocations);
        questLocationRepository.deleteAll();
        if (normalized.isEmpty()) {
            seedLocations(Constants.LOCATION_NAMES);
        } else {
            seedLocations(normalized);
        }
    }

    @Transactional
    public void addLocation(String location) {
        String trimmed = normalizeLocation(location);
        if (trimmed.isEmpty()) {
            return;
        }
        if (!questLocationRepository.existsByNameIgnoreCase(trimmed)) {
            questLocationRepository.save(new QuestLocation(null, trimmed));
        }
    }

    @Transactional
    public void removeLocations(List<String> locationsToRemove) {
        if (locationsToRemove == null || locationsToRemove.isEmpty()) {
            return;
        }
        for (String location : locationsToRemove) {
            String trimmed = normalizeLocation(location);
            if (!trimmed.isEmpty()) {
                questLocationRepository.findByNameIgnoreCase(trimmed)
                        .ifPresent(questLocationRepository::delete);
            }
        }
        if (questLocationRepository.count() == 0) {
            seedLocations(Constants.LOCATION_NAMES);
        }
    }

    @Transactional
    public void updateLocations(List<String> newLocations) {
        setLocations(newLocations);
    }

    @Transactional
    public void updateLocations(String text) {
        List<String> parsed = new ArrayList<>();
        if (text != null) {
            for (String part : text.split("\\R")) {
                String trimmed = normalizeLocation(part);
                if (!trimmed.isEmpty()) {
                    parsed.add(trimmed);
                }
            }
        }
        updateLocations(parsed);
    }

    @Transactional
    public void resetToDefaults() {
        questLocationRepository.deleteAll();
        seedLocations(Constants.LOCATION_NAMES);
    }

    private void ensureDefaultLocations() {
        if (questLocationRepository.count() == 0) {
            seedLocations(Constants.LOCATION_NAMES);
        }
    }

    private void seedLocations(List<String> values) {
        for (String value : values) {
            String trimmed = normalizeLocation(value);
            if (!trimmed.isEmpty() && !questLocationRepository.existsByNameIgnoreCase(trimmed)) {
                questLocationRepository.save(new QuestLocation(null, trimmed));
            }
        }
    }

    private List<String> normalizeLocations(List<String> values) {
        List<String> normalized = new ArrayList<>();
        if (values == null) {
            return normalized;
        }
        for (String value : values) {
            String trimmed = normalizeLocation(value);
            if (!trimmed.isEmpty() && !normalized.contains(trimmed)) {
                normalized.add(trimmed);
            }
        }
        return normalized;
    }

    private String normalizeLocation(String location) {
        return location == null ? "" : location.trim();
    }
}
