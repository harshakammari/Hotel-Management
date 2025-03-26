package Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.Customer;

public class CustomerService {
    private static final CustomerService instance = new CustomerService();
    private final List<Customer> customers;

    private CustomerService() {
        this.customers = new ArrayList<>();
    }

    public static CustomerService getInstance() {
        return instance;
    }

    public void addCustomer(String firstName, String lastName, String email, String phone) {
        Customer newCustomer = new Customer(firstName, lastName, email, phone);
        customers.add(newCustomer);
    }

    public Customer getCustomer(String customerEmail) {
        for (Customer customer : customers) {
            if (customer.getEmail().equalsIgnoreCase(customerEmail)) {
                return customer;
            }
        }
        return null;
    }

    public Collection<Customer> getAllCustomers() {
        return customers;
    }
}
