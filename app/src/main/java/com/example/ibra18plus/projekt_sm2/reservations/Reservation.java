package com.example.ibra18plus.projekt_sm2.reservations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reservation {

    @SerializedName("$id_room")
    @Expose
    private String $id;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ReservationStartDate")
    @Expose
    private String reservationStartDate;
    @SerializedName("ReservationEndDate")
    @Expose
    private String reservationEndDate;
    @SerializedName("RoomId")
    @Expose
    private Integer roomId;
    @SerializedName("UserId")
    @Expose
    private Object userId;
    @SerializedName("Room")
    @Expose
    private Object room;
    @SerializedName("RoomName")
    @Expose
    private String roomName;
    @SerializedName("RoomNr")
    @Expose
    private Integer roomNr;

    @Override
    public String toString() {
        return "Start date = " + reservationStartDate  +
                "  |  End date = " + reservationEndDate +"/n" +
                "Room name " + roomName  +
                "  |  Nr " + roomNr;
    }

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReservationStartDate() {
        return reservationStartDate;
    }

    public void setReservationStartDate(String reservationStartDate) {
        this.reservationStartDate = reservationStartDate;
    }

    public String getReservationEndDate() {
        return reservationEndDate;
    }

    public void setReservationEndDate(String reservationEndDate) {
        this.reservationEndDate = reservationEndDate;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getRoom() {
        return room;
    }

    public void setRoom(Object room) {
        this.room = room;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getRoomNr() {
        return roomNr;
    }

    public void setRoomNr(Integer roomNr) {
        this.roomNr = roomNr;
    }

}