package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.Supplier;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface SupplierEAOLocal {
    Supplier findBySupplierId(int id);
    Supplier createSupplier(Supplier supplier);
    Supplier updateSupplier(Supplier supplier);
    void deleteSupplier(int id);
    List<Supplier> findAll();
}
