package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.PickupPoint;
import fiskeboden.eao.ics.PickupPointEAOLocal;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class PickupPointFacade implements PickupPointFacadeLocal {

    @EJB
    private PickupPointEAOLocal pickupPointEAO;

    @Override
    public List<PickupPoint> getAllPickupPoints() {
        return pickupPointEAO.findAll();
    }

    @Override
    public PickupPoint getPickupPointById(int id) {
        return pickupPointEAO.findByPickupPointId(id);
    }

    @Override
    public PickupPoint createPickupPoint(PickupPoint pickupPoint) {
        return pickupPointEAO.createPickupPoint(pickupPoint);
    }

    @Override
    public PickupPoint updatePickupPoint(PickupPoint pickupPoint) {
        return pickupPointEAO.updatePickupPoint(pickupPoint);
    }

    @Override
    public void deletePickupPoint(int id) {
        pickupPointEAO.deletePickupPoint(id);
    }
}