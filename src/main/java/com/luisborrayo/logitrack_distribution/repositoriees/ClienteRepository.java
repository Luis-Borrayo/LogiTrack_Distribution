package com.luisborrayo.logitrack_distribution.repositoriees;

import com.luisborrayo.logitrack_distribution.models.Cliente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClienteRepository extends BaseRepository<Cliente, Long> {
    @Override
    protected Class<Cliente> entity() {
        return Cliente.class;
    }

    public Optional<Cliente> findByTaxId(String taxId) {
        try {
            TypedQuery<Cliente> query = entityManager.createQuery(
                    "SELECT c FROM Cliente c WHERE c.taxId = :taxId", Cliente.class);
            query.setParameter("taxId", taxId);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Cliente> findActiveCustomers() {
        return entityManager.createQuery(
                        "SELECT c FROM Cliente c WHERE c.activo = true ORDER BY c.Nombre", Cliente.class)
                .getResultList();
    }

    public Optional<Cliente> findByEmail(String email) {
        try {
            TypedQuery<Cliente> query = entityManager.createQuery(
                    "SELECT c FROM Cliente c WHERE c.correo = :email", Cliente.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
