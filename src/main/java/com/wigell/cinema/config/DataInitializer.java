package com.wigell.cinema.config;

import com.wigell.cinema.entity.*;
import com.wigell.cinema.repository.*;
import com.wigell.cinema.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Fyller databasen med testdata vid start.
// Körs bara om databasen är tom (kollar customerRepository.count()).
// Skapar minst 5 kunder, 5 filmer, 3 lokaler, 5 föreställningar och 2 bokningar enligt kravspecifikationen, samt 3 biljetter.
// Användare (USER/ADMIN) hanteras i Keycloak, inte i databasen.
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final CustomerRepository customerRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final CurrencyService currencyService;

    public DataInitializer(CustomerRepository customerRepository,
                           MovieRepository movieRepository,
                           RoomRepository roomRepository,
                           ScreeningRepository screeningRepository,
                           BookingRepository bookingRepository,
                           TicketRepository ticketRepository,
                           CurrencyService currencyService) {

        this.customerRepository = customerRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.screeningRepository = screeningRepository;
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.currencyService = currencyService;

    }

    @Override
    public void run(String... args) {
        if (customerRepository.count() > 0) {
            logger.info("Databasen redan seedat, hoppar över initialisering");
            return;
        }

        logger.info("Seedar databasen med startdata...");

        // Customers
        Customer c1 = new Customer("anna_s", "Anna", "Svensson");
        Address a1 = new Address("Storgatan 1", "Stockholm", "11122");
        a1.setCustomer(c1);
        c1.getAddresses().add(a1);

        Customer c2 = new Customer("erik_l", "Erik", "Lindgren");
        Address a2 = new Address("Kungsgatan 45", "Göteborg", "41120");
        a2.setCustomer(c2);
        c2.getAddresses().add(a2);

        Customer c3 = new Customer("maria_j", "Maria", "Johansson");
        Address a3 = new Address("Drottninggatan 10", "Malmö", "21144");
        a3.setCustomer(c3);
        c3.getAddresses().add(a3);

        Customer c4 = new Customer("karl_p", "Karl", "Pettersson");
        Address a4 = new Address("Vasagatan 22", "Uppsala", "75237");
        a4.setCustomer(c4);
        c4.getAddresses().add(a4);

        Customer c5 = new Customer("lisa_n", "Lisa", "Nilsson");
        Address a5 = new Address("Östra Hamngatan 8", "Linköping", "58223");
        a5.setCustomer(c5);
        c5.getAddresses().add(a5);

        customerRepository.save(c1);
        customerRepository.save(c2);
        customerRepository.save(c3);
        customerRepository.save(c4);
        customerRepository.save(c5);

        // Movies
        Movie m1 = new Movie("The Matrix", "Sci-Fi", 15, 136);
        Movie m2 = new Movie("Inception", "Thriller", 13, 148);
        Movie m3 = new Movie("Frozen", "Animation", 0, 102);
        Movie m4 = new Movie("Pulp Fiction", "Drama", 18, 154);
        Movie m5 = new Movie("Interstellar", "Sci-Fi", 11, 169);
        movieRepository.save(m1);
        movieRepository.save(m2);
        movieRepository.save(m3);
        movieRepository.save(m4);
        movieRepository.save(m5);

        // Rooms
        Room r1 = new Room("Stora Salongen", 200, "Dolby Atmos, 4K Projektor", 15000);
        Room r2 = new Room("Lilla Salongen", 80, "Standardprojektor, Stereo", 8000);
        Room r3 = new Room("VIP-Salongen", 40, "IMAX, Dolby Atmos, Läderfåtöljer", 25000);
        roomRepository.save(r1);
        roomRepository.save(r2);
        roomRepository.save(r3);

        // Screenings
        Screening s1 = new Screening(m1, r1, LocalDateTime.of(2026, 4, 5, 18, 0), 150);
        Screening s2 = new Screening(m2, r2, LocalDateTime.of(2026, 4, 5, 20, 0), 130);
        Screening s3 = new Screening(m3, r1, LocalDateTime.of(2026, 4, 6, 14, 0), 100);
        Screening s4 = new Screening(m4, r3, LocalDateTime.of(2026, 4, 6, 21, 0), 200);
        Screening s5 = new Screening(m5, r2, LocalDateTime.of(2026, 4, 7, 19, 0), 160);
        screeningRepository.save(s1);
        screeningRepository.save(s2);
        screeningRepository.save(s3);
        screeningRepository.save(s4);
        screeningRepository.save(s5);

        // Bookings
        Booking b1 = new Booking();
        b1.setCustomer(c1);
        b1.setRoom(r1);
        b1.setScreening(s1);
        b1.setNumberOfGuests(150);
        b1.setBookingDate(LocalDate.of(2026, 4, 5));
        b1.setTechnicalEquipment("Dolby Atmos, 4K Projektor");
        b1.setTotalPriceSek(r1.getBasePrice());
        b1.setTotalPriceUsd(currencyService.sekToUsd(r1.getBasePrice()));
        bookingRepository.save(b1);

        Booking b2 = new Booking();
        b2.setCustomer(c2);
        b2.setRoom(r3);
        b2.setScreening(s4);
        b2.setNumberOfGuests(30);
        b2.setBookingDate(LocalDate.of(2026, 4, 6));
        b2.setTechnicalEquipment("IMAX, Dolby Atmos");
        b2.setTotalPriceSek(r3.getBasePrice());
        b2.setTotalPriceUsd(currencyService.sekToUsd(r3.getBasePrice()));
        bookingRepository.save(b2);

        // Tickets
        Ticket t1 = new Ticket();
        t1.setCustomer(c1);
        t1.setScreening(s1);
        t1.setSeatNumber(1);
        t1.setPriceSek(s1.getTicketPrice());
        t1.setPriceUsd(currencyService.sekToUsd(s1.getTicketPrice()));
        t1.setPurchaseDate(LocalDateTime.now());
        ticketRepository.save(t1);

        Ticket t2 = new Ticket();
        t2.setCustomer(c3);
        t2.setScreening(s1);
        t2.setSeatNumber(2);
        t2.setPriceSek(s1.getTicketPrice());
        t2.setPriceUsd(currencyService.sekToUsd(s1.getTicketPrice()));
        t2.setPurchaseDate(LocalDateTime.now());
        ticketRepository.save(t2);

        Ticket t3 = new Ticket();
        t3.setCustomer(c2);
        t3.setScreening(s4);
        t3.setSeatNumber(5);
        t3.setPriceSek(s4.getTicketPrice());
        t3.setPriceUsd(currencyService.sekToUsd(s4.getTicketPrice()));
        t3.setPurchaseDate(LocalDateTime.now());
        ticketRepository.save(t3);

        logger.info("Databas seedat: 5 kunder, 5 filmer, 3 lokaler, 5 föreställningar, 2 bokningar, 3 biljetter (användare hanteras i Keycloak)");
        
    }
    
}