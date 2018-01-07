package com.example.ibra18plus.projekt_sm2.rooms;

import com.example.ibra18plus.projekt_sm2.reservations.Reservation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Room {

    @SerializedName("$id")
    @Expose
    private String $id;
    @SerializedName("Reservation")
    @Expose
    private List<Reservation> reservation = null;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Nr")
    @Expose
    private Integer nr;
    @SerializedName("Capacity")
    @Expose
    private Integer capacity;
    @SerializedName("RoomName")
    @Expose
    private String roomName;

    @Override
    public String toString() {
        return "Nr = " + nr +
                "  |  Capacity = " + capacity +
                "  |  Room name = " + roomName;
    }

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public List<Reservation> getReservation() {
        return reservation;
    }

    public void setReservation(List<Reservation> reservation) {
        this.reservation = reservation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNr() {
        return nr;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}