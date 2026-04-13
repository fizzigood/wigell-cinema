package com.wigell.cinema.controller;

import com.wigell.cinema.dto.BookingRequest;
import com.wigell.cinema.dto.BookingUpdateRequest;
import com.wigell.cinema.entity.Booking;
import com.wigell.cinema.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Controller för bokningshantering.
// Kunder (USER) kan skapa, uppdatera och se sina bokningar.
// En bokning avser en hel salong och inkluderar antal gäster,
// önskad föreställning och teknisk utrustning.
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(@RequestParam Long customerId) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomerId(customerId));
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        Booking created = bookingService.createBooking(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // PATCH – bara datum och teknisk utrustning kan ändras
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long bookingId,
                                                  @RequestBody BookingUpdateRequest request) {
        return ResponseEntity.ok(bookingService.updateBooking(bookingId, request));
    }

}