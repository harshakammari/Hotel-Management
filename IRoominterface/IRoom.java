package IRoominterface;

import RoomTypeenum.RoomType;

public interface IRoom {
    String getRoomNumber();
    Double getRoomPrice();
    RoomType getRoomType();
    boolean isFree();
    void bookRoom();
    void vacateRoom();
}
