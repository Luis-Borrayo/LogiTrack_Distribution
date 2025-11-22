package com.luisborrayo.logitrack_distribution.services;

import com.luisborrayo.logitrack_distribution.models.Customer;
import com.luisborrayo.logitrack_distribution.repositoriees.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClienteService {
    @Inject
    private ClienteRepository clienteRepository;

    public List<Customer> getAll() {
        return clienteRepository.getAll();
    }

    public List<Customer> getActiveCustomers() {
        return clienteRepository.findActiveCustomers();
    }

    public Optional<Customer> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Customer> findByTaxId(String taxId) {
        return clienteRepository.findByTaxId(taxId);
    }

    public Optional<Customer> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<Customer> save(Customer customer) {
        if (customer.getCustomerId() != null) {

            if (customer.getEmail() != null) {
                Optional<Customer> existingByEmail = clienteRepository.findByEmail(customer.getEmail());
                if (existingByEmail.isPresent() &&
                        !existingByEmail.get().getCustomerId().equals(customer.getCustomerId())) {
                    return Optional.empty();
                }
            }

            // Verificar taxId
            if (customer.getTaxId() != null) {
                Optional<Customer> existingByTaxId = clienteRepository.findByTaxId(customer.getTaxId());
                if (existingByTaxId.isPresent() &&
                        !existingByTaxId.get().getCustomerId().equals(customer.getCustomerId())) {
                    return Optional.empty();
                }
            }
        } else {
            // Es creación nueva - verificar si email o taxId ya existen
            if (customer.getEmail() != null) {
                Optional<Customer> existingByEmail = clienteRepository.findByEmail(customer.getEmail());
                if (existingByEmail.isPresent()) {
                    return Optional.empty();
                }
            }

            if (customer.getTaxId() != null) {
                Optional<Customer> existingByTaxId = clienteRepository.findByTaxId(customer.getTaxId());
                if (existingByTaxId.isPresent()) {
                    return Optional.empty();
                }
            }
        }

        return clienteRepository.save(customer);
    }

    public void delete(Customer customer) {
        clienteRepository.delete(customer);
    }

    public void deactivateCustomer(Long customerId) {
        Optional<Customer> customer = findById(customerId);
        if (customer.isPresent()) {
            Customer c = customer.get();
            c.setActive(false);
            save(c);
        }
    }

    public void activateCustomer(Long customerId) {
        Optional<Customer> customer = findById(customerId);
        if (customer.isPresent()) {
            Customer c = customer.get();
            c.setActive(true);
            save(c);
        }
    }

    public boolean isCustomerActive(Long customerId) {
        Optional<Customer> customer = findById(customerId);
        return customer.isPresent() && Boolean.TRUE.equals(customer.get().getActive());
    }
}
