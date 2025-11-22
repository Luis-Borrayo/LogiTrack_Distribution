package com.luisborrayo.logitrack_distribution.services;

import com.luisborrayo.logitrack_distribution.dtos.OrdenItemSoliDto;
import com.luisborrayo.logitrack_distribution.models.OrdenItem;
import com.luisborrayo.logitrack_distribution.repositoriees.OrdenItemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrdenItemService {
    @Inject
    private OrdenItemRepository ordenItemRepository;

    public List<OrdenItem> getAll() {
        return ordenItemRepository.getAll();
    }

    public Optional<OrdenItem> findById(Long id) {
        return ordenItemRepository.findById(id);
    }

    public List<OrdenItem> findByOrderId(Long orderId) {
        return ordenItemRepository.findByOrderId(orderId);
    }

    public Optional<OrdenItem> save(OrdenItem orderItem) {
        return ordenItemRepository.save(orderItem);
    }

    public void delete(OrdenItem orderItem) {
        ordenItemRepository.delete(orderItem);
    }

    public void deleteById(Long id) {
        ordenItemRepository.deleteById(id);
    }
}
