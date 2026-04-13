package com.wigell.cinema.dto;

// DTO för att uppdatera en bokning (PATCH).
// Enligt spec får bara datum och teknisk utrustning ändras.
// Null-värden ignoreras (fältet uppdateras inte).
public class BookingUpdateRequest {

    private String bookingDate;
    private String technicalEquipment;

    public BookingUpdateRequest() {
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getTechnicalEquipment() {
        return technicalEquipment;
    }

    public void setTechnicalEquipment(String technicalEquipment) {
        this.technicalEquipment = technicalEquipment;
    }
}
