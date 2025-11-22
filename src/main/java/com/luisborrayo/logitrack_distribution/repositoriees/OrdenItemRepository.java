package com.luisborrayo.logitrack_distribution.repositoriees;

import com.luisborrayo.logitrack_distribution.models.OrderItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class OrdenItemRepository extends BaseRepository<OrderItem, Long>{
    @Override
    protected Class<OrderItem> entity() {
        return OrderItem.class;
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return entityManager.createQuery(
                        "SELECT oi FROM OrderItem oi WHERE oi.orderId.orderId = :orderId", OrderItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public void deleteByOrderId(Long orderId) {
        entityManager.createQuery("DELETE FROM OrderItem oi WHERE oi.orderId.orderId = :orderId")
                .setParameter("orderId", orderId)
                .executeUpdate();
    }

    public BigDecimal calculateOrderTotal(Long orderId) {
        try {
            TypedQuery<BigDecimal> query = entityManager.createQuery(
                    "SELECT SUM(oi.subtotal) FROM OrderItem oi WHERE oi.orderId.orderId = :orderId", BigDecimal.class);
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
                    "SELECT COUNT(p) FROM Product p WHERE p.productId = :productId AND p.active = true", Long.class);
            query.setParameter("productId", productId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        }
    }


}
