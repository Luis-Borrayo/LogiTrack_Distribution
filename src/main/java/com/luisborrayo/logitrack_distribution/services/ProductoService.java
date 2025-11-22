package com.luisborrayo.logitrack_distribution.services;

import com.luisborrayo.logitrack_distribution.models.Product;
import com.luisborrayo.logitrack_distribution.repositoriees.ProductoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductoService {
    @Inject
    private ProductoRepository productoRepository;

    public List<Product> getAll() {
        return productoRepository.getAll();
    }

    public List<Product> getActiveProducts() {
        return productoRepository.findActiveProducts();
    }

    public List<Product> getProductsByCategory(String category) {
        return productoRepository.findByCategory(category);
    }

    public Optional<Product> findById(Long id) {
        return productoRepository.findById(id);
    }

    public Optional<Product> findByName(String name) {
        return productoRepository.findByName(name);
    }

    public Optional<Product> save(Product product) {
        try {
            System.out.println("=== ProductService.save() ===");
            System.out.println("Producto ID: " + product.getProductId());
            System.out.println("Nombre del Producto: " + product.getName());

            if (product.getProductId() != null) {
                System.out.println("Modo: ACTUALIZACIÓN");
                Optional<Product> existingByName = productoRepository.findByName(product.getName());
                if (existingByName.isPresent() &&
                        !existingByName.get().getProductId().equals(product.getProductId())) {
                    System.err.println("ERROR con el nombre de produto, nombre ya existe en otro producto");
                    return Optional.empty();
                }
            } else {
                System.out.println("Modo: Crear Nuevo");
                Optional<Product> existingByName = productoRepository.findByName(product.getName());
                if (existingByName.isPresent()) {
                    System.err.println("ERROR, nombre de producto ya existente: " + existingByName.get().getProductId());
                    return Optional.empty();
                }
            }

            System.out.println("Producto guardado correctamente en repositorio...");
            Optional<Product> saved = productoRepository.save(product);

            if (saved.isPresent()) {
                System.out.println("Producto guardado exitosamente con ID: " + saved.get().getProductId());
            } else {
                System.err.println("Error, produto presento error al momento de guardar, retornó Optional.empty()");
            }

            return saved;
        } catch (Exception e) {
            System.err.println("EXCEPCIÓN en ProductService.save(): " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete(Product product) {
        productoRepository.delete(product);
    }

    public void deactivateProduct(Long productId) {
        Optional<Product> product = findById(productId);
        if (product.isPresent()) {
            Product p = product.get();
            p.setActive(false);
            save(p);
        }
    }

    public void activateProduct(Long productId) {
        Optional<Product> product = findById(productId);
        if (product.isPresent()) {
            Product p = product.get();
            p.setActive(true);
            save(p);
        }
    }
}
