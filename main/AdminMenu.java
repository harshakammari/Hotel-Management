package main;

import Service.CustomerService;
import Service.ReservationService;
import model.Customer;
import model.Room;
import IRoominterface.IRoom;
import RoomTypeenum.RoomType;

import java.util.Collection;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminMenu instance = new AdminMenu();
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private AdminMenu() {}

    public static AdminMenu getInstance() {
        return instance;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRoom(Room room) {
        reservationService.addRoom(room);
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations() {
        reservationService.printAllReservations();
    }

    public void populateTestData() {
        customerService.addCustomer("John", "Doe", "john.doe@example.com", "1234567890");
        customerService.addCustomer("Jane", "Smith", "jane.smith@example.com", "0987654321");
        reservationService.addRoom(new Room("101", 200.0, RoomType.SINGLE));
        reservationService.addRoom(new Room("102", 300.0, RoomType.DOUBLE));
        System.out.println("Test data has been populated successfully.");
    }

    public static void displayAdminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. See All Customers");
            System.out.println("2. See All Rooms");
            System.out.println("3. See All Reservations");
            System.out.println("4. Add a Room");
            System.out.println("5. Populate Test Data");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); 
    
            switch (choice) {
                case 1:
                    Collection<Customer> customers = CustomerService.getInstance().getAllCustomers();
                    if (customers.isEmpty()) {
                        System.out.println("No customers found.");
                    } else {
                        customers.forEach(System.out::println);
                    }
                    break;
                case 2:
                    Collection<IRoom> rooms = ReservationService.getInstance().getAllRooms();
                    if (rooms.isEmpty()) {
                        System.out.println("No rooms available.");
                    } else {
                        rooms.forEach(System.out::println);
                    }
                    break;
                case 3:
                    ReservationService.getInstance().printAllReservations();
                    break;
                case 4:
                    System.out.print("Enter room number: ");
                    String roomNumber = scanner.nextLine();
                    System.out.print("Enter room price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter room type (1 for SINGLE, 2 for DOUBLE): ");
                    int roomTypeChoice = scanner.nextInt();
                    scanner.nextLine();
                    RoomType roomType = (roomTypeChoice == 1) ? RoomType.SINGLE : RoomType.DOUBLE;
                    Room room = new Room(roomNumber, price, roomType);
                    ReservationService.getInstance().addRoom(room);
                    System.out.println("Room added successfully.");
                    break;
                case 5:
                    AdminMenu.getInstance().populateTestData();
                    break;
                case 6:
                    System.out.println("Exiting Admin Menu.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

