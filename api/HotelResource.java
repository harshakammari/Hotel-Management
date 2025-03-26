package api;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import IRoominterface.IRoom;
import model.Customer;
import model.Reservation;
import Service.ReservationService;

public class HotelResource {
    private static final HotelResource instance = new HotelResource();
    private final ReservationService reservationService = ReservationService.getInstance();
    private final Map<String, Customer> customers = new HashMap<>();

    private HotelResource() {}

    public static HotelResource getInstance() {
        return instance;
    }

    public Customer getCustomer(String email) {
        return customers.get(email);
    }

    public void createACustomer(String firstName, String lastName, String email, String phone) {
        Customer customer = new Customer(firstName, lastName, email, phone);
        customers.put(email, customer);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String email, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = getCustomer(email);
        if (customer == null) {
            System.out.println("Customer not found with email: " + email);
            return null;
        }
        
        // First check if the room exists and is available for the given dates
        if (room == null) {
            System.out.println("Room not found.");
            return null;
        }
        
        Reservation reservation = reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
        if (reservation == null) {
            System.out.println("Room is not available for the selected dates.");
        }
        return reservation;
    }

    public Collection<Reservation> getCustomerReservations(String email) {
        return reservationService.getCustomerReservations(email);
    }
    public Collection<Customer> getAllCustomers() {
        return customers.values(); 
    }

    public Collection<IRoom> findARoom(Date checkInDate, Date checkOutDate) {
        // First update the status of all rooms
        reservationService.updateRoomStatus();
        return reservationService.findRooms(checkInDate, checkOutDate);
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }
}
