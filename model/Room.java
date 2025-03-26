package model;

import IRoominterface.IRoom; 
import RoomTypeenum.RoomType;

public class Room implements IRoom {
    private final String roomNumber;
    private final double price;
    private final RoomType roomType;
    private boolean isFree;

    public Room(String roomNumber, double price, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
        this.isFree = price == 0.0;  // Automatically mark room as free if price is 0.0
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return price;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return isFree;
    }

    public void bookRoom() {
        this.isFree = false;
    }

    public void vacateRoom() {
        this.isFree = true;
    }

    @Override
    public String toString() {
        return "Room {" + 
               "\n  Room Number: " + roomNumber + 
               "\n  Cost: " + price + 
               "\n  Room Type: " + roomType + 
               "\n  Room Availability: " + (isFree ? "Available" : "Occupied") + 
               "\n}";
    }

  
}
