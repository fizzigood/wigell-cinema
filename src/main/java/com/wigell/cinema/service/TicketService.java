package com.wigell.cinema.service;

import com.wigell.cinema.dto.TicketRequest;
import com.wigell.cinema.entity.Customer;
import com.wigell.cinema.entity.Screening;
import com.wigell.cinema.entity.Ticket;
import com.wigell.cinema.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// Biljetthantering. Platser är virtuella (1–maxGuests), unikhet via DB-constraint.
@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final CustomerService customerService;
    private final ScreeningService screeningService;
    private final CurrencyService currencyService;

    public TicketService(TicketRepository ticketRepository, CustomerService customerService,
                         ScreeningService screeningService, CurrencyService currencyService) {
        this.ticketRepository = ticketRepository;
        this.customerService = customerService;
        this.screeningService = screeningService;
        this.currencyService = currencyService;
    }

    // Hämtar alla biljetter för en specifik kund.
    public List<Ticket> getTicketsByCustomerId(Long customerId) {
        return ticketRepository.findByCustomerId(customerId);
    }

    // Validerar plats, kapacitet och dubbletter. Pris från föreställningen, konverterat till USD.
    @Transactional
    public Ticket createTicket(TicketRequest request) {
        Customer customer = customerService.getCustomerById(request.getCustomerId());
        Screening screening = screeningService.getScreeningById(request.getScreeningId());
        Integer seatNumber = request.getSeatNumber();

        if (seatNumber == null) {
            throw new IllegalArgumentException("Platsnummer krävs");
        }

        int maxSeats = screening.getRoom().getMaxGuests();
        if (seatNumber < 1 || seatNumber > maxSeats) {
            throw new IllegalArgumentException("Platsnummer måste vara mellan 1 och " + maxSeats);
        }

        long soldSeats = ticketRepository.countByScreeningId(screening.getId());
        if (soldSeats >= maxSeats) {
            throw new IllegalArgumentException("Inga platser tillgängliga för föreställning id: " + screening.getId());
        }

        if (ticketRepository.existsByScreeningIdAndSeatNumber(screening.getId(), seatNumber)) {
            throw new IllegalArgumentException(
                    "Plats " + seatNumber + " är redan bokad för föreställning id: " + screening.getId());
        }

        Ticket ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setScreening(screening);
        ticket.setSeatNumber(seatNumber);
        ticket.setPriceSek(screening.getTicketPrice());
        ticket.setPriceUsd(currencyService.sekToUsd(screening.getTicketPrice()));
        ticket.setPurchaseDate(LocalDateTime.now());

        Ticket saved;
        try {
            // saveAndFlush fångar samma request vid parallella köp.
            saved = ticketRepository.saveAndFlush(ticket);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(
                    "Plats " + seatNumber + " är redan bokad för föreställning id: " + screening.getId());
        }
        logger.info("Kund '{}' köpte biljett till '{}' (biljett id={})",
                customer.getUsername(), screening.getMovie().getTitle(), saved.getId());
        return saved;

    }

}