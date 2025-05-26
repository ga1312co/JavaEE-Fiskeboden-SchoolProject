package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.SupplierPickupPoint;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SupplierPickupPointEAOImpl implements SupplierPickupPointEAOLocal {

    @PersistenceContext(unitName = "FiskebodenPU")
    private EntityManager em;

    @Override
    public SupplierPickupPoint findBySupplierPickupPointId(int id) {
        return em.find(SupplierPickupPoint.class, id);
    }

    @Override
    public SupplierPickupPoint createSupplierPickupPoint(SupplierPickupPoint spp) {
        em.persist(spp);
        return spp;
    }

    @Override
    public SupplierPickupPoint updateSupplierPickupPoint(SupplierPickupPoint spp) {
        return em.merge(spp);
    }

    @Override
    public void deleteSupplierPickupPoint(int id) {
        SupplierPickupPoint spp = findBySupplierPickupPointId(id);
        if (spp != null) {
            em.remove(spp);
        }
    }

    @Override
    public List<SupplierPickupPoint> findAll() {
        return em.createQuery("SELECT s FROM SupplierPickupPoint s", SupplierPickupPoint.class).getResultList();
    }
}
