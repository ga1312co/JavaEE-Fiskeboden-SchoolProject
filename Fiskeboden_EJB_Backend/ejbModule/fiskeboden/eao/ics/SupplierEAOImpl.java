package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.Supplier;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SupplierEAOImpl implements SupplierEAOLocal {

    @PersistenceContext(unitName = "FiskebodenPU")
    private EntityManager em;

    @Override
    public Supplier findBySupplierId(int id) {
        return em.find(Supplier.class, id);
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        em.persist(supplier);
        return supplier;
    }

    @Override
    public Supplier updateSupplier(Supplier supplier) {
        return em.merge(supplier);
    }

    @Override
    public void deleteSupplier(int id) {
        Supplier supplier = findBySupplierId(id);
        if (supplier != null) {
            em.remove(supplier);
        }
    }

    @Override
    public List<Supplier> findAll() {
        return em.createQuery("SELECT s FROM Supplier s", Supplier.class).getResultList();
    }
}
