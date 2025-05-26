package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.SupplierPickupPoint;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface SupplierPickupPointEAOLocal {
    SupplierPickupPoint findBySupplierPickupPointId(int id);
    SupplierPickupPoint createSupplierPickupPoint(SupplierPickupPoint spp);
    SupplierPickupPoint updateSupplierPickupPoint(SupplierPickupPoint spp);
    void deleteSupplierPickupPoint(int id);
    List<SupplierPickupPoint> findAll();
}
