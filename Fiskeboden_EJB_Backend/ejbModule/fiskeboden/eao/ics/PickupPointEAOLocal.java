package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.PickupPoint;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface PickupPointEAOLocal {
    PickupPoint findByPickupPointId(int id);
    PickupPoint createPickupPoint(PickupPoint pickupPoint);
    PickupPoint updatePickupPoint(PickupPoint pickupPoint);
    void deletePickupPoint(int id);
    List<PickupPoint> findAll();
}
