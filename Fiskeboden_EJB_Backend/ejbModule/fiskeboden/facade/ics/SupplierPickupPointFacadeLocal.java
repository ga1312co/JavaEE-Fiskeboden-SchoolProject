package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.SupplierPickupPoint;
import java.util.List;

public interface SupplierPickupPointFacadeLocal {
    List<SupplierPickupPoint> getAllSupplierPickupPoints();
    SupplierPickupPoint getSupplierPickupPointById(int id);
    SupplierPickupPoint createSupplierPickupPoint(SupplierPickupPoint spp);
    SupplierPickupPoint updateSupplierPickupPoint(SupplierPickupPoint spp);
    void deleteSupplierPickupPoint(int id);
}