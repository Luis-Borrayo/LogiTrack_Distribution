package com.luisborrayo.logitrack_distribution.repositoriees;

import com.luisborrayo.logitrack_distribution.models.OrdenItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class OrdenItemRepository extends BaseRepository<OrdenItem, Long>{
    @Override
    protected Class<OrdenItem> entity() {
        return OrdenItem.class;
    }

    public List<OrdenItem> findByOrderId(Long orderId) {
        return entityManager.createQuery(
                        "SELECT oi FROM OrdenItem oi WHERE oi.orden.orderId = :orderId", OrdenItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public void deleteByOrderId(Long orderId) {
        entityManager.createQuery("DELETE FROM OrdenItem oi WHERE oi.orden.orderId = :orderId")
                .setParameter("orderId", orderId)
                .executeUpdate();
    }

    public BigDecimal calculateOrderTotal(Long orderId) {
        try {
            TypedQuery<BigDecimal> query = entityManager.createQuery(
                    "SELECT SUM(oi.subtotal) FROM OrdenItem oi WHERE oi.orden.orderId = :orderId", BigDecimal.class);
            query.setParameter("orderId", orderId);
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public boolean isValidActiveProduct(Long productId) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(p) FROM Producto p WHERE p.productId = :productId AND p.activo = true", Long.class);
            query.setParameter("productId", productId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        }
    }


}
