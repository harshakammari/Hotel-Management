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
    
    /**
     * Checks if a room has any active (future) reservations
     * @param roomNumber The room number to check
     * @return true if the room has active reservations, false otherwise
     */
    public boolean hasActiveReservations(String roomNumber) {
        Date currentDate = new Date();
        
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().getRoomNumber().equals(roomNumber) && 
                reservation.getCheckOutDate().after(currentDate)) {
                return true;
            }
        }
        
        return false;
    }

    public boolean removeRoom(String roomNumber) {
        IRoom roomToRemove = getARoom(roomNumber);
        if (roomToRemove != null) {
            // Check if the room has active reservations
            if (hasActiveReservations(roomNumber)) {
                return false; // Cannot remove a room with active reservations
            }
            
            // If no active reservations, remove any past reservations associated with this room
            reservations.removeIf(reservation -> reservation.getRoom().getRoomNumber().equals(roomNumber));
            
            // Then remove the room itself
            return rooms.remove(roomToRemove);
        }
        return false;
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
            // Update the room status to booked if the check-in date is today or in the past
            Date today = new Date();
            if (checkInDate.before(today) || checkInDate.equals(today)) {
                room.bookRoom();
            }
            return reservation; 
        }
        return null; 
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        // Update the status of all rooms based on current reservations
        updateRoomStatus();
        
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
        // Check for conflicts with existing reservations
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().equals(room)) {
                // Check if the requested dates overlap with an existing reservation
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
    
    // Method to update room availability status based on current date
    public void updateRoomStatus() {
        Date currentDate = new Date();
        
        // First, set all rooms to be available
        for (IRoom room : rooms) {
            room.vacateRoom();
        }
        
        // Then check all active reservations to mark rooms as occupied
        for (Reservation reservation : reservations) {
            IRoom room = reservation.getRoom();
            
            // If the current date is between check-in and check-out, mark as occupied
            if (currentDate.after(reservation.getCheckInDate()) && 
                currentDate.before(reservation.getCheckOutDate())) {
                room.bookRoom();
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
