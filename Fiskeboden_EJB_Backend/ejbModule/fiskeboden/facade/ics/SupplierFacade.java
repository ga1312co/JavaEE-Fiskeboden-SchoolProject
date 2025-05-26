package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.Supplier;
import fiskeboden.eao.ics.SupplierEAOLocal;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class SupplierFacade implements SupplierFacadeLocal {

    @EJB
    private SupplierEAOLocal supplierEAO;

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierEAO.findAll();
    }

    @Override
    public Supplier getSupplierById(int id) {
        return supplierEAO.findBySupplierId(id);
    }
}