package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.Supplier;
import java.util.List;

public interface SupplierFacadeLocal {
    List<Supplier> getAllSuppliers();
    Supplier getSupplierById(int id);
}