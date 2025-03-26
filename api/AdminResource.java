package api;
import java.util.Collection;
import java.util.List;

import IRoominterface.IRoom;
import model.Customer;
import Service.ReservationService;

public class AdminResource {

    private static final AdminResource instance = new AdminResource();  
    private final ReservationService reservationService = ReservationService.getInstance();

    private AdminResource() {}

    public static AdminResource getInstance() {
        return instance;
    }

    public Customer getCustomer(String email) {
        return HotelResource.getInstance().getCustomer(email);  
    }

    public void addRoom(List<IRoom> rooms) {
        for (IRoom room : rooms) {
            reservationService.addRoom(room);  
        }
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();  
    }

    public Collection<Customer> getAllCustomers() {
        return HotelResource.getInstance().getAllCustomers(); 
    }

    public void displayAllReservations() {
        reservationService.printAllReservations();  
    }
}
