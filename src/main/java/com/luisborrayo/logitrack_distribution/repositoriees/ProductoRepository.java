package com.luisborrayo.logitrack_distribution.repositoriees;

import com.luisborrayo.logitrack_distribution.dtos.ProductosStatsDto;
import com.luisborrayo.logitrack_distribution.models.Producto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductoRepository extends BaseRepository<Producto, Long>{
    @Override
    protected Class<Producto> entity() {
        return Producto.class;
    }

    public List<Producto> findActiveProducts() {
        return entityManager.createQuery(
                        "SELECT p FROM Producto p WHERE p.activo = true ORDER BY p.nombre", Producto.class)
                .getResultList();
    }

    public List<Producto> findByCategory(String category) {
        return entityManager.createQuery(
                        "SELECT p FROM Producto p WHERE p.categoria = :category AND p.activo = true", Producto.class)
                .setParameter("category", category)
                .getResultList();
    }

    public Optional<Producto> findByName(String name) {
        try {
            TypedQuery<Producto> query = entityManager.createQuery(
                    "SELECT p FROM Producto p WHERE p.nombre = :name", Producto.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<ProductosStatsDto> findTopSellingProducts(int limit) {
        String jpql = "SELECT new com.luisborrayo.logitrack_distribution.dtos.ProductosStatsDto(" +
                "p.productId, " +
                "p.nombre, " +
                "p.categoria, " +
                "SUM(oi.quantity), " +
                "SUM(oi.subtotal), " +
                "COUNT(DISTINCT o.orderId)) " +
                "FROM OrdenItem oi " +
                "JOIN oi.producto p " +
                "JOIN oi.orden o " +
                "WHERE o.estado <> 'Cancelled' " +
                "GROUP BY p.productId, p.nombre, p.categoria " +
                "ORDER BY SUM(oi.quantity) DESC";

        TypedQuery<ProductosStatsDto> query = entityManager.createQuery(jpql, ProductosStatsDto.class);
        query.setMaxResults(limit);

        return query.getResultList();
    }


    public Optional<ProductosStatsDto> getProductSalesStats(Long productId) {
        try {
            String jpql = "SELECT new com.luisborrayo.logitrack_distribution.dtos.ProductosStatsDto(" +
                    "p.productId, " +
                    "p.nombre, " +
                    "p.categoria, " +
                    "COALESCE(SUM(oi.quantity), 0), " +
                    "COALESCE(SUM(oi.subtotal), 0), " +
                    "COUNT(DISTINCT o.orderId)) " +
                    "FROM Producto p " +
                    "LEFT JOIN OrdenItem oi ON oi.producto.productId = p.productId " +
                    "LEFT JOIN Orden o ON oi.orden.orderId = o.orderId AND o.estado <> 'Cancelled' " +
                    "WHERE p.productId = :productId " +
                    "GROUP BY p.productId, p.nombre, p.categoria";

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
                "p.nombre, " +
                "p.categoria, " +
                "SUM(oi.quantity), " +
                "SUM(oi.subtotal), " +
                "COUNT(DISTINCT o.orderId)) " +
                "FROM OrdenItem oi " +
                "JOIN oi.producto p " +
                "JOIN oi.orden o " +
                "WHERE o.estado <> 'Cancelled' AND p.categoria = :category " +
                "GROUP BY p.productId, p.nombre, p.categoria " +
                "ORDER BY SUM(oi.quantity) DESC";

        TypedQuery<ProductosStatsDto> query = entityManager.createQuery(jpql, ProductosStatsDto.class);
        query.setParameter("category", category);
        query.setMaxResults(limit);

        return query.getResultList();
    }
}
