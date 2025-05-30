// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventoryitem;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public interface InventoryItemDetailStateDao {

    /**
     * Get entity state.
     * @param id Entity global Id.
     * @param nullAllowed If returning null is a allowed. It is true for query.
     * @param aggregateState Aggregate state.
     * @return Entity state.
     */
    InventoryItemDetailState get(InventoryItemDetailId id, boolean nullAllowed, InventoryItemState aggregateState);

    void save(InventoryItemDetailState state);

    Iterable<InventoryItemDetailState> findByInventoryItemId(String inventoryItemId, InventoryItemState aggregateState);

    void delete(InventoryItemDetailState state);
}


