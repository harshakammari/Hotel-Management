package main;

import java.util.Scanner;
import api.HotelResource;
import model.Customer;
import IRoominterface.IRoom;
import model.Reservation;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.sql.Date;
import java.util.regex.Pattern;

public class HotelManagement {
    private static final Scanner scanner = new Scanner(System.in);
    private static final HotelResource hotelResource = HotelResource.getInstance();

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    private static boolean isValidPhone(String phone) {
        String phoneRegex = "^\\d{10}$";  // Basic 10-digit format
        return phone != null && phone.matches(phoneRegex);
    }

    private static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("^[a-zA-Z\\s]+$");
    }

    private static boolean areDatesValid(Date checkIn, Date checkOut) {
        if (checkIn == null || checkOut == null) return false;
        return checkOut.after(checkIn) && !checkIn.before(new Date(System.currentTimeMillis()));
    }
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
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            
            if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please use format: name@domain.com");
                return;
            }

            Collection<Reservation> reservations = hotelResource.getCustomerReservations(email);
            if (reservations.isEmpty()) {
                System.out.println("No reservations found for this email.");
            } else {
                System.out.println("\nYour Reservations:");
                reservations.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving reservations: " + e.getMessage());
        }
    }

    private static void findAndReserveRoom() {
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please use format: name@domain.com");
                return;
            }

            Customer customer = hotelResource.getCustomer(email);
            if (customer == null) {
                System.out.println("No account found with the given email. Please create an account first.");
                return;
            }

            System.out.print("Enter room number to reserve: ");
            String roomNumber = scanner.nextLine();
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                System.out.println("Room number cannot be empty.");
                return;
            }

            IRoom room = hotelResource.getRoom(roomNumber);
            if (room == null) {
                System.out.println("Room not found.");
                return;
            }
            if (!room.isFree()) {
                System.out.println("Room is not available.");
                return;
            }

            try {
                System.out.print("Enter Check-in date (yyyy-mm-dd): ");
                Date checkInDate = Date.valueOf(scanner.nextLine());
                System.out.print("Enter Check-out date (yyyy-mm-dd): ");
                Date checkOutDate = Date.valueOf(scanner.nextLine());

                if (!areDatesValid(checkInDate, checkOutDate)) {
                    System.out.println("Invalid dates. Check-out must be after check-in and dates must be in the future.");
                    return;
                }

                Reservation reservation = hotelResource.bookARoom(email, room, checkInDate, checkOutDate);
                if (reservation != null) {
                    System.out.println("Reservation Successful: " + reservation);
                } else {
                    System.out.println("Failed to create reservation.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use yyyy-mm-dd format.");
            }
        } catch (Exception e) {
            System.out.println("Error during room reservation: " + e.getMessage());
        }
    }

    private static void createAnAccount() {
        try {
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            if (!isValidName(firstName)) {
                System.out.println("Invalid first name. Name should contain only letters and spaces.");
                return;
            }

            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            if (!isValidName(lastName)) {
                System.out.println("Invalid last name. Name should contain only letters and spaces.");
                return;
            }

            System.out.print("Enter Email (format: name@domain.com): ");
            String email = scanner.nextLine();
            if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please use format: name@domain.com");
                return;
            }

            System.out.print("Enter Phone Number (10 digits): ");
            String phone = scanner.nextLine();
            if (!isValidPhone(phone)) {
                System.out.println("Invalid phone number. Please enter 10 digits.");
                return;
            }

            hotelResource.createACustomer(firstName, lastName, email, phone);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating account: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
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