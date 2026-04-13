package com.wigell.cinema.controller;

import com.wigell.cinema.dto.ScreeningRequest;
import com.wigell.cinema.entity.Screening;
import com.wigell.cinema.service.ScreeningService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

// Controller för föreställningshantering.
// USER kan lista/söka föreställningar, ADMIN kan skapa och ta bort dem.
// Föreställningar kopplar ihop en film med en lokal vid en given tidpunkt.
@RestController
@RequestMapping("/api/v1/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    // Filtrerar på movieId + date om angivna, annars alla
    @GetMapping
    public ResponseEntity<List<Screening>> getScreenings(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (movieId != null && date != null) {
            return ResponseEntity.ok(screeningService.getScreeningsByMovieAndDate(movieId, date));
        }
        return ResponseEntity.ok(screeningService.getAllScreenings());
    }

    @PostMapping
    public ResponseEntity<Screening> createScreening(@RequestBody ScreeningRequest request) {
        Screening created = screeningService.createScreening(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{screeningId}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Long screeningId) {
        screeningService.deleteScreening(screeningId);
        return ResponseEntity.noContent().build();
    }

}