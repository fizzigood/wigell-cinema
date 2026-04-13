package com.wigell.cinema.controller;

import com.wigell.cinema.dto.TicketRequest;
import com.wigell.cinema.entity.Ticket;
import com.wigell.cinema.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Controller för biljetthantering (USER).
// Kunder kan köpa biljetter till föreställningar och se sina köpta biljetter.
// Biljettpriset hämtas från föreställningen och presenteras i SEK och USD.
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getTickets(@RequestParam Long customerId) {
        return ResponseEntity.ok(ticketService.getTicketsByCustomerId(customerId));
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest request) {
        Ticket created = ticketService.createTicket(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);

    }

}