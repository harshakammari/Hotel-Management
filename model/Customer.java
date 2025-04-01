package model;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;

    public Customer(String firstName, String lastName, String email, String phone) {
        validateEmail(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    private void validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid Email Format");
        }
    }

    @Override
    public String toString() {
        return "\n-- Customer Details --" +
               "\nFirst Name    : " + firstName +
               "\nLast Name     : " + lastName +
               "\nEmail Address : " + email +
               "\nMobile Number : " + phone +
               "\n------------------------";
    }
}
