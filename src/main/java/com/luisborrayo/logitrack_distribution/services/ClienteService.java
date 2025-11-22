package com.luisborrayo.logitrack_distribution.services;

import com.luisborrayo.logitrack_distribution.models.Cliente;
import com.luisborrayo.logitrack_distribution.repositoriees.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClienteService {
    @Inject
    private ClienteRepository clienteRepository;

    public List<Cliente> getAll() {
        return clienteRepository.getAll();
    }

    public List<Cliente> getActiveCustomers() {
        return clienteRepository.findActiveCustomers();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> findByTaxId(String taxId) {
        return clienteRepository.findByTaxId(taxId);
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<Cliente> save(Cliente customer) {
        // Verificar si es una actualización o creación nueva
        if (customer.getClienteId() != null) {
            // Es una actualización - verificar duplicados en OTROS clientes

            // Verificar email
            if (customer.getCorreo() != null) {
                Optional<Cliente> existingByEmail = clienteRepository.findByEmail(customer.getCorreo());
                if (existingByEmail.isPresent() &&
                        !existingByEmail.get().getClienteId().equals(customer.getClienteId())) {
                    return Optional.empty();
                }
            }

            // Verificar taxId
            if (customer.getTaxId() != null) {
                Optional<Cliente> existingByTaxId = clienteRepository.findByTaxId(customer.getTaxId());
                if (existingByTaxId.isPresent() &&
                        !existingByTaxId.get().getClienteId().equals(customer.getClienteId())) {
                    return Optional.empty();
                }
            }
        } else {
            // Es creación nueva - verificar si email o taxId ya existen
            if (customer.getCorreo() != null) {
                Optional<Cliente> existingByEmail = clienteRepository.findByEmail(customer.getCorreo());
                if (existingByEmail.isPresent()) {
                    return Optional.empty();
                }
            }

            if (customer.getTaxId() != null) {
                Optional<Cliente> existingByTaxId = clienteRepository.findByTaxId(customer.getTaxId());
                if (existingByTaxId.isPresent()) {
                    return Optional.empty();
                }
            }
        }

        return clienteRepository.save(customer);
    }

    public void delete(Cliente customer) {
        clienteRepository.delete(customer);
    }

    public void deactivateCustomer(Long customerId) {
        Optional<Cliente> customer = findById(customerId);
        if (customer.isPresent()) {
            Cliente c = customer.get();
            c.setActivo(false);
            save(c);
        }
    }

    public void activateCustomer(Long customerId) {
        Optional<Cliente> customer = findById(customerId);
        if (customer.isPresent()) {
            Cliente c = customer.get();
            c.setActivo(true);
            save(c);
        }
    }

    public boolean isCustomerActive(Long customerId) {
        Optional<Cliente> customer = findById(customerId);
        return customer.isPresent() && Boolean.TRUE.equals(customer.get().getActivo());
    }
}
