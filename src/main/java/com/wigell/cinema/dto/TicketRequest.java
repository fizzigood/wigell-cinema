package com.wigell.cinema.dto;

// DTO för att köpa en biljett.
// Klienten anger kund-ID och föreställnings-ID.
// Priset hämtas automatiskt från föreställningen.
// Genom att använda en DTO kan vi separera den data som krävs för att skapa en biljett från själva biljettentiteten,
// vilket gör det enklare att hantera och validera inkommande data i TicketService.
// Det gör också att vi inte behöver exponera hela Ticket-entiteten i API:et.
public class TicketRequest {

    private Long customerId;
    private Long screeningId;
    private Integer seatNumber;

    public TicketRequest() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Long screeningId) {
        this.screeningId = screeningId;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }
}
