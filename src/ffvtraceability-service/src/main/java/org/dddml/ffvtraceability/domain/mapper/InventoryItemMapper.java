package org.dddml.ffvtraceability.domain.mapper;

import org.dddml.ffvtraceability.domain.inventoryitem.InventoryItemAttributes;
import org.dddml.ffvtraceability.domain.inventoryitem.InventoryItemDetailAttributes;
import org.dddml.ffvtraceability.domain.inventoryitem.InventoryItemDetailState;
import org.dddml.ffvtraceability.domain.inventoryitem.InventoryItemState;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryItemMapper {
    void updateInventoryItemState(@MappingTarget InventoryItemState.MutableInventoryItemState state, InventoryItemAttributes attributes);

    void updateInventoryItemDetailState(@MappingTarget InventoryItemDetailState.MutableInventoryItemDetailState state, InventoryItemDetailAttributes attributes);

}
