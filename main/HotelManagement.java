package main;

import java.util.Scanner;
import api.HotelResource;
import model.Customer;
import IRoominterface.IRoom;
import model.Reservation;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.sql.Date;

public class HotelManagement {
    private static final Scanner scanner = new Scanner(System.in);
    private static final HotelResource hotelResource = HotelResource.getInstance();

    public static void main(String[] args) {
    boolean exit = false;

    while (!exit) {
        displayMainMenu();
        try {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) {
                    continue;  
                }
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        findAndReserveRoom();
                        break;
                    case 2:
                        seeMyReservations();
                        break;
                    case 3:
                        createAnAccount();
                        break;
                    case 4:
                        AdminMenu.displayAdminMenu(scanner);
                        break;
                    case 5:
                        displayAvailableRooms();
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (NoSuchElementException e) {
            System.out.println("Input error. Please try again.");
            break;
        }
    }

    System.out.println("Thank you for using the Hotel Management System.");
}

    private static void displayMainMenu() {
        System.out.println("\nMain Menu");
        System.out.println("1. Find and Reserve a Room");
        System.out.println("2. See My Reservations");
        System.out.println("3. Create an Account");
        System.out.println("4. Admin");
        System.out.println("5. Show Available Rooms");
        System.out.println("6. Exit");
        System.out.print("Please select an option: ");
    }

    private static void seeMyReservations() {
        System.out.print("Enter your email: ");
        if (scanner.hasNextLine()) {
            String email = scanner.nextLine();
            Collection<Reservation> reservations = hotelResource.getCustomerReservations(email);

            if (reservations.isEmpty()) {
                System.out.println("No reservations found for this email.");
            } else {
                reservations.forEach(System.out::println);
            }
        }
    }

    private static void findAndReserveRoom() {
        System.out.print("Enter your email: ");
        if (scanner.hasNextLine()) {
            String email = scanner.nextLine();
            Customer customer = hotelResource.getCustomer(email);

            if (customer == null) {
                System.out.println("No account found with the given email. Please create an account first.");
                return;
            }

            System.out.print("Enter room number to reserve: ");
            if (scanner.hasNextLine()) {
                String roomNumber = scanner.nextLine();
                IRoom room = hotelResource.getRoom(roomNumber);

                if (room == null || !room.isFree()) {
                    System.out.println("Room is not available.");
                    return;
                }

                try {
                    System.out.print("Enter Check-in date (yyyy-mm-dd): ");
                    Date checkInDate = Date.valueOf(scanner.nextLine());
                    System.out.print("Enter Check-out date (yyyy-mm-dd): ");
                    Date checkOutDate = Date.valueOf(scanner.nextLine());

                    Reservation reservation = hotelResource.bookARoom(email, room, checkInDate, checkOutDate);

                    if (reservation != null) {
                        System.out.println("Reservation Successful: " + reservation);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format. Please try again.");
                }
            }
        }
    }

    private static void createAnAccount() {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Email (format: name@domain.com): ");
        String email = scanner.nextLine();

        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();

        try {
            hotelResource.createACustomer(firstName, lastName, email, phone);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayAvailableRooms() {
        Collection<IRoom> availableRooms = hotelResource.getAllRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms at the moment.");
        } else {
            System.out.println("Available Rooms:");
            availableRooms.forEach(System.out::println);
        }
    }
}