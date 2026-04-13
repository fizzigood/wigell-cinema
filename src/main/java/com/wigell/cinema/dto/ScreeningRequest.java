package com.wigell.cinema.dto;

// DTO för att skapa en föreställning.
// Klienten anger film-ID, lokal-ID, datum/tid och biljettpris.
public class ScreeningRequest {

    private Long movieId;
    private Long roomId;
    private String dateTime;
    private double ticketPrice;

    public ScreeningRequest() {
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
