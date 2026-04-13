package com.wigell.cinema.service;

import com.wigell.cinema.config.ResourceNotFoundException;
import com.wigell.cinema.dto.BookingRequest;
import com.wigell.cinema.dto.BookingUpdateRequest;
import com.wigell.cinema.entity.Booking;
import com.wigell.cinema.entity.Customer;
import com.wigell.cinema.entity.Room;
import com.wigell.cinema.entity.Screening;
import com.wigell.cinema.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// Bokningar av salonger. Pris baserat på lokal, konverterat till USD.
@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final CustomerService customerService;
    private final RoomService roomService;
    private final ScreeningService screeningService;
    private final CurrencyService currencyService;

    public BookingService(BookingRepository bookingRepository, CustomerService customerService,
                          RoomService roomService, ScreeningService screeningService,
                          CurrencyService currencyService) {
        this.bookingRepository = bookingRepository;
        this.customerService = customerService;
        this.roomService = roomService;
        this.screeningService = screeningService;
        this.currencyService = currencyService;
    }
    //Hämtar alla bokningar (tidigare och aktiva) för en specifik kund
    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    // Validerar gästantal mot kapacitet. Sätter pris i SEK + USD.
    @Transactional
    public Booking createBooking(BookingRequest request) {
        Customer customer = customerService.getCustomerById(request.getCustomerId());
        Room room = roomService.getRoomById(request.getRoomId());

        // Validera att antal gäster ryms i lokalen
        if (request.getNumberOfGuests() > room.getMaxGuests()) {
            throw new IllegalArgumentException("Antal gäster (" + request.getNumberOfGuests()
                    + ") överskrider lokalens kapacitet (" + room.getMaxGuests() + ")");
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setBookingDate(LocalDate.parse(request.getBookingDate()));
        booking.setTechnicalEquipment(request.getTechnicalEquipment());

        // Koppla till föreställning om angivet av användaren
        if (request.getScreeningId() != null) {
            Screening screening = screeningService.getScreeningById(request.getScreeningId());
            booking.setScreening(screening);
        }

        // Beräkna totalpris baserat på lokalens baspris, konvertera till USD via mikrotjänst
        double priceSek = room.getBasePrice();
        booking.setTotalPriceSek(priceSek);
        booking.setTotalPriceUsd(currencyService.sekToUsd(priceSek));

        Booking saved = bookingRepository.save(booking);
        logger.info("Kund '{}' skapade bokning för lokal '{}' den {} (id={})",
                customer.getUsername(), room.getName(), saved.getBookingDate(), saved.getId());
        return saved;
    }

    // PATCH – bara datum och utrustning kan ändras
    @Transactional
    public Booking updateBooking(Long bookingId, BookingUpdateRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Bokning hittades inte med id: " + bookingId));

        if (request.getBookingDate() != null) {
            booking.setBookingDate(LocalDate.parse(request.getBookingDate()));
        }
        if (request.getTechnicalEquipment() != null) {
            booking.setTechnicalEquipment(request.getTechnicalEquipment());
        }

        Booking saved = bookingRepository.save(booking);
        logger.info("Uppdaterade bokning id={} (datum={}, utrustning={})",
                bookingId, saved.getBookingDate(), saved.getTechnicalEquipment());
        return saved;

    }

}