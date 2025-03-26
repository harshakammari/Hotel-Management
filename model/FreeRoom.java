package model;

import RoomTypeenum.RoomType;

public class FreeRoom extends Room {

    public FreeRoom(String roomNumber, RoomType roomType) {
        super(roomNumber, 0.0, roomType);
    }

    @Override
    public String toString() {
        return String.format(
            "Check-out (FreeRoom) {\n" +
            "  Room Number : %s\n" +
            "  Room Type   : %s\n" +
            "  Price       : %.2f\n" +
            "}",
            getRoomNumber(), getRoomType(), getRoomPrice()
        );
    }
}
