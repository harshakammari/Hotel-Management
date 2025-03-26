package main;

import Service.CustomerService;
import Service.ReservationService;
import model.Customer;
import model.Room;
import model.Reservation;
import IRoominterface.IRoom;
import RoomTypeenum.RoomType;

import java.util.Collection;
import java.util.Date;
import java.util.InputMismatchException;
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

    private static boolean isValidRoomNumber(String roomNumber) {
        return roomNumber != null && !roomNumber.trim().isEmpty();
    }
    
    private static boolean isValidRoomPrice(double price) {
        return price >= 0;
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
            System.out.println("7. Remove a Room");
            System.out.print("Enter your choice: ");
    
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue;
            }
    
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
                    try {
                        // Room number validation
                        System.out.print("Enter room number: ");
                        String roomNumber = scanner.nextLine();
                        if (!isValidRoomNumber(roomNumber)) {
                            System.out.println("Room number cannot be empty. Room not added.");
                            break;
                        }
                        
                        // Check if room already exists
                        if (ReservationService.getInstance().getARoom(roomNumber) != null) {
                            System.out.println("A room with this number already exists. Please use a different room number.");
                            break;
                        }
                        
                        // Room price validation
                        System.out.print("Enter room price: ");
                        double price;
                        try {
                            price = scanner.nextDouble();
                            if (!isValidRoomPrice(price)) {
                                System.out.println("Price cannot be negative. Please enter a valid price.");
                                scanner.nextLine(); // Clear the input buffer
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid price format. Please enter a valid number.");
                            scanner.nextLine(); // Clear the invalid input
                            break;
                        }
                        
                        // Room type validation
                        System.out.print("Enter room type (1 for SINGLE, 2 for DOUBLE): ");
                        int roomTypeChoice;
                        try {
                            roomTypeChoice = scanner.nextInt();
                            if (roomTypeChoice != 1 && roomTypeChoice != 2) {
                                System.out.println("Invalid room type choice. Please enter 1 for SINGLE or 2 for DOUBLE.");
                                scanner.nextLine(); // Clear the input buffer
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter 1 for SINGLE or 2 for DOUBLE.");
                            scanner.nextLine(); // Clear the invalid input
                            break;
                        }
                        
                        scanner.nextLine(); // Clear the input buffer after nextInt()
                        RoomType roomType = (roomTypeChoice == 1) ? RoomType.SINGLE : RoomType.DOUBLE;
                        Room room = new Room(roomNumber, price, roomType);
                        ReservationService.getInstance().addRoom(room);
                        System.out.println("Room added successfully.");
                    } catch (Exception e) {
                        System.out.println("An error occurred while adding the room: " + e.getMessage());
                        scanner.nextLine(); // Ensure the scanner buffer is clear
                    }
                    break;
                case 5:
                    AdminMenu.getInstance().populateTestData();
                    break;
                case 6:
                    System.out.println("Exiting Admin Menu.");
                    return;
                case 7:
                    try {
                        System.out.print("Enter room number to remove: ");
                        String roomNumber = scanner.nextLine();
                        
                        if (!isValidRoomNumber(roomNumber)) {
                            System.out.println("Room number cannot be empty.");
                            break;
                        }
                        
                        IRoom room = ReservationService.getInstance().getARoom(roomNumber);
                        if (room == null) {
                            System.out.println("Room with number " + roomNumber + " does not exist.");
                            break;
                        }
                        
                        // Check for active reservations using the service method
                        if (ReservationService.getInstance().hasActiveReservations(roomNumber)) {
                            System.out.println("Cannot remove room " + roomNumber + " because it has active reservations.");
                            break;
                        }
                        
                        // Confirm removal
                        System.out.print("Are you sure you want to remove room " + roomNumber + "? (y/n): ");
                        String confirmation = scanner.nextLine().trim().toLowerCase();
                        
                        if (confirmation.equals("y") || confirmation.equals("yes")) {
                            boolean removed = ReservationService.getInstance().removeRoom(roomNumber);
                            if (removed) {
                                System.out.println("Room " + roomNumber + " has been removed successfully.");
                            } else {
                                System.out.println("Failed to remove room " + roomNumber + ". It may have active reservations.");
                            }
                        } else {
                            System.out.println("Room removal cancelled.");
                        }
                    } catch (Exception e) {
                        System.out.println("An error occurred while removing the room: " + e.getMessage());
                        scanner.nextLine(); // Ensure the scanner buffer is clear
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

