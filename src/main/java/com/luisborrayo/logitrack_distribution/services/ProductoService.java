package com.luisborrayo.logitrack_distribution.services;

import com.luisborrayo.logitrack_distribution.models.Producto;
import com.luisborrayo.logitrack_distribution.repositoriees.ProductoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductoService {
    @Inject
    private ProductoRepository productoRepository;

    public List<Producto> getAll() {
        return productoRepository.getAll();
    }

    public List<Producto> getActiveProducts() {
        return productoRepository.findActiveProducts();
    }

    public List<Producto> getProductsByCategory(String category) {
        return productoRepository.findByCategory(category);
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> findByName(String name) {
        return productoRepository.findByName(name);
    }

    public Optional<Producto> save(Producto product) {
        try {
            System.out.println("=== ProductService.save() ===");
            System.out.println("Product ID: " + product.getProductId());
            System.out.println("Product Name: " + product.getNombre());

            // Verificar si es una actualización o creación nueva
            if (product.getProductId() != null) {
                System.out.println("Modo: ACTUALIZACIÓN");
                // Es una actualización - verificar si el nombre ya existe en OTRO producto
                Optional<Producto> existingByName = productoRepository.findByName(product.getNombre());
                if (existingByName.isPresent() &&
                        !existingByName.get().getProductId().equals(product.getProductId())) {
                    System.err.println("ERROR: El nombre ya existe en otro producto");
                    return Optional.empty();
                }
            } else {
                System.out.println("Modo: CREACIÓN NUEVA");
                // Es creación nueva - verificar si el nombre ya existe
                Optional<Producto> existingByName = productoRepository.findByName(product.getNombre());
                if (existingByName.isPresent()) {
                    System.err.println("ERROR: El nombre ya existe: " + existingByName.get().getProductId());
                    return Optional.empty();
                }
            }

            System.out.println("Guardando producto en repositorio...");
            Optional<Producto> saved = productoRepository.save(product);

            if (saved.isPresent()) {
                System.out.println("Producto guardado exitosamente con ID: " + saved.get().getProductId());
            } else {
                System.err.println("ERROR: El repositorio retornó Optional.empty()");
            }

            return saved;
        } catch (Exception e) {
            System.err.println("EXCEPCIÓN en ProductService.save(): " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete(Producto product) {
        productoRepository.delete(product);
    }

    public void deactivateProduct(Long productId) {
        Optional<Producto> product = findById(productId);
        if (product.isPresent()) {
            Producto p = product.get();
            p.setActivo(false);
            save(p);
        }
    }

    public void activateProduct(Long productId) {
        Optional<Producto> product = findById(productId);
        if (product.isPresent()) {
            Producto p = product.get();
            p.setActivo(true);
            save(p);
        }
    }
}
