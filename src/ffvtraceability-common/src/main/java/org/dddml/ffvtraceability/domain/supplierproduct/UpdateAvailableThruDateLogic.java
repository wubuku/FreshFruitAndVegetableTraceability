// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.supplierproduct;

import org.dddml.ffvtraceability.specialization.*;

import java.time.OffsetDateTime;

public class UpdateAvailableThruDateLogic {

    public static SupplierProductEvent.AvailableThruDateUpdated verify(java.util.function.Supplier<SupplierProductEvent.AvailableThruDateUpdated> eventFactory, SupplierProductState supplierProductState, OffsetDateTime availableThruDate, VerificationContext verificationContext) {
        SupplierProductEvent.AvailableThruDateUpdated e = eventFactory.get();
        if (availableThruDate.isBefore(supplierProductState.getSupplierProductAssocId().getAvailableFromDate())) {
            throw new IllegalStateException("AvailableThruDate cannot be before AvailableFromDate");
        }
        e.setAvailableThruDate(availableThruDate);
        return e;
    }

    public static SupplierProductState mutate(SupplierProductState supplierProductState, OffsetDateTime availableThruDate, MutationContext<SupplierProductState, SupplierProductState.MutableSupplierProductState> mutationContext) {
        SupplierProductState.MutableSupplierProductState s = mutationContext.toMutableState(supplierProductState);
        s.setAvailableThruDate(availableThruDate);
        return s;
    }

}


