package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.PickupPoint;
import java.util.List;

public interface PickupPointFacadeLocal {
    List<PickupPoint> getAllPickupPoints();
    PickupPoint getPickupPointById(int id);
    PickupPoint createPickupPoint(PickupPoint pickupPoint);
    PickupPoint updatePickupPoint(PickupPoint pickupPoint);
    void deletePickupPoint(int id);
}