package Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

import IRoominterface.IRoom;
import model.Customer;
import model.Reservation;

public class ReservationService {

    private static final ReservationService instance = new ReservationService();  
    private final List<IRoom> rooms;
    private final List<Reservation> reservations;

    private ReservationService() {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public Collection<IRoom> getAllRooms() {
        return rooms;
    }

    public static ReservationService getInstance() {  
        return instance;
    }

    public void addRoom(IRoom room) {
        rooms.add(room);
    }

    public IRoom getARoom(String roomId) {
        for (IRoom room : rooms) {
            if (room.getRoomNumber().equals(roomId)) {
                return room; 
            }
        }
        return null; 
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        if (isRoomAvailable(room, checkInDate, checkOutDate)) {
            Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
            reservations.add(reservation); 
            return reservation; 
        }
        return null; 
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        List<IRoom> availableRooms = new ArrayList<>();
        for (IRoom room : rooms) {
            if (isRoomAvailable(room, checkInDate, checkOutDate)) {
                availableRooms.add(room); 
            }
        }
        return availableRooms; 
    }

    public Collection<Reservation> getCustomerReservations(String email) {
        List<Reservation> customerReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().getEmail().equals(email)) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    private boolean isRoomAvailable(IRoom room, Date checkInDate, Date checkOutDate) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().equals(room)) {
                if (checkInDate.before(reservation.getCheckOutDate()) && 
                    checkOutDate.after(reservation.getCheckInDate())) {
                    return false; 
                }
            }
        }
        return true;
    }

    public void printAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation); 
            }
        }
    }

    public Collection<IRoom> findRoomsWithFutureRecommendation(Date checkInDate, Date checkOutDate, boolean paidRoomsOnly, int daysOut) {
        Collection<IRoom> availableRooms = findRooms(checkInDate, checkOutDate);

        if (!availableRooms.isEmpty()) {
            return availableRooms;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);

        for (int i = 0; i < daysOut; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date newCheckIn = calendar.getTime();

            calendar.setTime(checkOutDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date newCheckOut = calendar.getTime();

            availableRooms = findRooms(newCheckIn, newCheckOut);
            if (!availableRooms.isEmpty()) {
                return availableRooms;
            }
        }

        return new ArrayList<>(); 
    }
}
