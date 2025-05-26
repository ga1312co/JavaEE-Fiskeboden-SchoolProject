package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.PickupPoint;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PickupPointEAOImpl implements PickupPointEAOLocal {

    @PersistenceContext(unitName = "FiskebodenPU")
    private EntityManager em;

    @Override
    public PickupPoint findByPickupPointId(int id) {
        return em.find(PickupPoint.class, id);
    }

    @Override
    public PickupPoint createPickupPoint(PickupPoint pickupPoint) {
        em.persist(pickupPoint);
        return pickupPoint;
    }

    @Override
    public PickupPoint updatePickupPoint(PickupPoint pickupPoint) {
        return em.merge(pickupPoint);
    }

    @Override
    public void deletePickupPoint(int id) {
        PickupPoint pickupPoint = findByPickupPointId(id);
        if (pickupPoint != null) {
            em.remove(pickupPoint);
        }
    }

    @Override
    public List<PickupPoint> findAll() {
        return em.createQuery("SELECT p FROM PickupPoint p", PickupPoint.class).getResultList();
    }
}
