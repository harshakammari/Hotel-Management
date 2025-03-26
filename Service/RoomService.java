package Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import IRoominterface.IRoom;

public class RoomService {

    private static final RoomService instance = new RoomService();
    private final List<IRoom> rooms;

    private RoomService() {
        rooms = new ArrayList<>();
    }

    public static RoomService getInstance() {
        return instance;
    }

    public void addRoom(IRoom room) {
        rooms.add(room);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms;
    }

    public IRoom getRoomByNumber(String roomNumber) {
        for (IRoom room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    public void printAllRooms() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }

    public void removeRoom(String roomNumber) {
        IRoom roomToRemove = getRoomByNumber(roomNumber);
        if (roomToRemove != null) {
            rooms.remove(roomToRemove);
            System.out.println("Room " + roomNumber + " has been removed successfully.");
        } else {
            System.out.println("Room " + roomNumber + " not found.");
        }
    }
}
