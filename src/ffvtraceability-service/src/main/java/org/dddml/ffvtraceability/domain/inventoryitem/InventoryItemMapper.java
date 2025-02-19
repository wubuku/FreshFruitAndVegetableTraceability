package org.dddml.ffvtraceability.domain.inventoryitem;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryItemMapper {
    void updateInventoryItemState(@MappingTarget InventoryItemState.MutableInventoryItemState state, InventoryItemAttributes attributes);

    void updateInventoryItemDetailState(@MappingTarget InventoryItemDetailState.MutableInventoryItemDetailState state, InventoryItemDetailAttributes attributes);

}
