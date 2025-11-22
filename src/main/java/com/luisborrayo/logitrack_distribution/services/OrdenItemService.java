package com.luisborrayo.logitrack_distribution.services;

import com.luisborrayo.logitrack_distribution.models.OrderItem;
import com.luisborrayo.logitrack_distribution.repositoriees.OrdenItemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrdenItemService {
    @Inject
    private OrdenItemRepository ordenItemRepository;

    public List<OrderItem> getAll() {
        return ordenItemRepository.getAll();
    }

    public Optional<OrderItem> findById(Long id) {
        return ordenItemRepository.findById(id);
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return ordenItemRepository.findByOrderId(orderId);
    }

    public Optional<OrderItem> save(OrderItem orderItem) {
        return ordenItemRepository.save(orderItem);
    }

    public void delete(OrderItem orderItem) {
        ordenItemRepository.delete(orderItem);
    }

    public void deleteById(Long id) {
        ordenItemRepository.deleteById(id);
    }
}
