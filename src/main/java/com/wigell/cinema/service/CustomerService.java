package com.wigell.cinema.service;

import com.wigell.cinema.config.ResourceNotFoundException;
import com.wigell.cinema.entity.Address;
import com.wigell.cinema.entity.Customer;
import com.wigell.cinema.repository.AddressRepository;
import com.wigell.cinema.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Tjänst för kundhantering.
// Hanterar CRUD för kunder och deras adresser.
// Använder @Transactional på skapa, uppdatera och ta bort för att alla relaterade operationer ska lyckas eller misslyckas tillsammans
// Alla "not found"-fel kastar ResourceNotFoundException som ger HTTP 404.
@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    // Hämtar alla kunder
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Hämtar en kund via ID. Kastar 404 om kunden inte finns
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kund hittades inte med id: " + id));
    }

    // Skapar en ny kund och loggar händelsen.
    // Kopplar adresser till kund (krävs p.g.a. @JsonIgnore på Address.customer)
    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customer.getAddresses() != null) {
            for (Address address : customer.getAddresses()) {
                address.setCustomer(customer);
            }
        }
        Customer saved = customerRepository.save(customer);
        logger.info("Admin skapade kund '{}' (id={})", saved.getUsername(), saved.getId());
        return saved;
    }

    // Uppdaterar en befintlig kunds uppgifter (användarnamn, för- och efternamn)
    @Transactional
    public Customer updateCustomer(Long id, Customer updated) {
        Customer existing = getCustomerById(id);
        existing.setUsername(updated.getUsername());
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        Customer saved = customerRepository.save(existing);
        logger.info("Admin uppdaterade kund '{}' (id={})", saved.getUsername(), saved.getId());
        return saved;
    }

    // Tar bort en kund och alla relaterade adresser, bokningar och biljetter (cascade)
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
        logger.info("Admin tog bort kund '{}' (id={})", customer.getUsername(), id);
    }

    // Lägger till en adress till en kund.
    // Sparar adressen direkt via addressRepository så att den sätter IDn korrekt.
    @Transactional
    public Address addAddress(Long customerId, Address address) {
        Customer customer = getCustomerById(customerId);
        address.setCustomer(customer);
        Address saved = addressRepository.save(address);
        logger.info("Admin lade till adress för kund id={}", customerId);
        return saved;
    }

    // Tar bort en adress från en kund.
    // Validerar att adressen tillhör den angivna kunden innan borttagning.
    @Transactional
    public void deleteAddress(Long customerId, Long addressId) {
        Customer customer = getCustomerById(customerId);
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Adress hittades inte med id: " + addressId));
        if (!address.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException("Adressen tillhör inte kund id: " + customerId);
        }
        customer.getAddresses().remove(address);
        addressRepository.delete(address);
        logger.info("Admin tog bort adress id={} från kund id={}", addressId, customerId);

    }

}