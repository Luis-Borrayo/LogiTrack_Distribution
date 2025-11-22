package com.luisborrayo.logitrack_distribution.repositoriees;

import com.luisborrayo.logitrack_distribution.dtos.ProductosStatsDto;
import com.luisborrayo.logitrack_distribution.models.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductoRepository extends BaseRepository<Product, Long>{
    @Override
    protected Class<Product> entity() {
        return Product.class;
    }

    public List<Product> findActiveProducts() {
        return entityManager.createQuery(
                        "SELECT p FROM Product p WHERE p.active = true ORDER BY p.name", Product.class)
                .getResultList();
    }

    public List<Product> findByCategory(String category) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p WHERE p.category = :category AND p.active = true", Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    public Optional<Product> findByName(String name) {
        try {
            TypedQuery<Product> query = entityManager.createQuery(
                    "SELECT p FROM Product p WHERE p.name = :name", Product.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<ProductosStatsDto> findTopSellingProducts(int limit) {
        String jpql = "SELECT new com.luisborrayo.logitrack_distribution.dtos.ProductosStatsDto(" +
                "p.productId, " +
                "p.name, " +
                "p.category, " +
                "SUM(oi.quantity), " +
                "SUM(oi.subtotal), " +
                "COUNT(DISTINCT o.orderId)) " +
                "FROM OrderItem oi " +
                "JOIN oi.productId p " +
                "JOIN oi.orderId o " +
                "WHERE o.status <> 'Cancelled' " +
                "GROUP BY p.productId, p.name, p.category " +
                "ORDER BY SUM(oi.quantity) DESC";

        TypedQuery<ProductosStatsDto> query = entityManager.createQuery(jpql, ProductosStatsDto.class);
        query.setMaxResults(limit);

        return query.getResultList();
    }


    public Optional<ProductosStatsDto> getProductSalesStats(Long productId) {
        try {
            String jpql = "SELECT new com.luisborrayo.logitrack_distribution.dtos.ProductosStatsDto(" +
                    "p.productId, " +
                    "p.name, " +
                    "p.category, " +
                    "COALESCE(SUM(oi.quantity), 0), " +
                    "COALESCE(SUM(oi.subtotal), 0), " +
                    "COUNT(DISTINCT o.orderId)) " +
                    "FROM Product p " +
                    "LEFT JOIN OrderItem oi ON oi.productId.productId = p.productId " +
                    "LEFT JOIN Order o ON oi.orderId.orderId = o.orderId AND o.status <> 'Cancelled' " +
                    "WHERE p.productId = :productId " +
                    "GROUP BY p.productId, p.name, p.category";

            TypedQuery<ProductosStatsDto> query = entityManager.createQuery(jpql, ProductosStatsDto.class);
            query.setParameter("productId", productId);

            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<ProductosStatsDto> findTopSellingProductsByCategory(String category, int limit) {
        String jpql = "SELECT new com.luisborrayo.logitrack_distribution.dtos.ProductosStatsDto(" +
                "p.productId, " +
                "p.name, " +
                "p.category, " +
                "SUM(oi.quantity), " +
                "SUM(oi.subtotal), " +
                "COUNT(DISTINCT o.orderId)) " +
                "FROM OrderItem oi " +
                "JOIN oi.productId p " +
                "JOIN oi.orderId o " +
                "WHERE o.status <> 'Cancelled' AND p.category = :category " +
                "GROUP BY p.productId, p.name, p.category " +
                "ORDER BY SUM(oi.quantity) DESC";

        TypedQuery<ProductosStatsDto> query = entityManager.createQuery(jpql, ProductosStatsDto.class);
        query.setParameter("category", category);
        query.setMaxResults(limit);

        return query.getResultList();
    }
}
