package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.SupplierPickupPoint;
import fiskeboden.eao.ics.SupplierPickupPointEAOLocal;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class SupplierPickupPointFacade implements SupplierPickupPointFacadeLocal {

    @EJB
    private SupplierPickupPointEAOLocal sppEAO;

    @Override
    public List<SupplierPickupPoint> getAllSupplierPickupPoints() {
        return sppEAO.findAll();
    }

    @Override
    public SupplierPickupPoint getSupplierPickupPointById(int id) {
        return sppEAO.findBySupplierPickupPointId(id);
    }

    @Override
    public SupplierPickupPoint createSupplierPickupPoint(SupplierPickupPoint spp) {
        return sppEAO.createSupplierPickupPoint(spp);
    }

    @Override
    public SupplierPickupPoint updateSupplierPickupPoint(SupplierPickupPoint spp) {
        return sppEAO.updateSupplierPickupPoint(spp);
    }

    @Override
    public void deleteSupplierPickupPoint(int id) {
        sppEAO.deleteSupplierPickupPoint(id);
    }
}