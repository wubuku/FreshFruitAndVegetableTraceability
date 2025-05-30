// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.facility;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface FacilityCommand extends Command {

    String getFacilityId();

    void setFacilityId(String facilityId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(FacilityState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((FacilityCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (isCreationCommand((FacilityCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(FacilityCommand c) {
        if ((c instanceof FacilityCommand.CreateFacility) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(FacilityState.VERSION_NULL)))
            return true;
        if ((c instanceof FacilityCommand.MergePatchFacility))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(FacilityState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchFacility extends FacilityCommand {
        String getFacilityTypeId();

        void setFacilityTypeId(String facilityTypeId);

        String getParentFacilityId();

        void setParentFacilityId(String parentFacilityId);

        String getOwnerPartyId();

        void setOwnerPartyId(String ownerPartyId);

        String getDefaultInventoryItemTypeId();

        void setDefaultInventoryItemTypeId(String defaultInventoryItemTypeId);

        String getFacilityName();

        void setFacilityName(String facilityName);

        String getPrimaryFacilityGroupId();

        void setPrimaryFacilityGroupId(String primaryFacilityGroupId);

        Long getOldSquareFootage();

        void setOldSquareFootage(Long oldSquareFootage);

        java.math.BigDecimal getFacilitySize();

        void setFacilitySize(java.math.BigDecimal facilitySize);

        String getFacilitySizeUomId();

        void setFacilitySizeUomId(String facilitySizeUomId);

        String getProductStoreId();

        void setProductStoreId(String productStoreId);

        Long getDefaultDaysToShip();

        void setDefaultDaysToShip(Long defaultDaysToShip);

        OffsetDateTime getOpenedDate();

        void setOpenedDate(OffsetDateTime openedDate);

        OffsetDateTime getClosedDate();

        void setClosedDate(OffsetDateTime closedDate);

        String getDescription();

        void setDescription(String description);

        String getDefaultDimensionUomId();

        void setDefaultDimensionUomId(String defaultDimensionUomId);

        String getDefaultWeightUomId();

        void setDefaultWeightUomId(String defaultWeightUomId);

        String getGeoPointId();

        void setGeoPointId(String geoPointId);

        String getGeoId();

        void setGeoId(String geoId);

        Long getFacilityLevel();

        void setFacilityLevel(Long facilityLevel);

        String getActive();

        void setActive(String active);

        Long getSequenceNumber();

        void setSequenceNumber(Long sequenceNumber);

    }

    interface CreateFacility extends CreateOrMergePatchFacility {
        CreateFacilityIdentificationCommandCollection getCreateFacilityIdentificationCommands();

        FacilityIdentificationCommand.CreateFacilityIdentification newCreateFacilityIdentification();

    }

    interface MergePatchFacility extends CreateOrMergePatchFacility {
        Boolean getIsPropertyFacilityTypeIdRemoved();

        void setIsPropertyFacilityTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyParentFacilityIdRemoved();

        void setIsPropertyParentFacilityIdRemoved(Boolean removed);

        Boolean getIsPropertyOwnerPartyIdRemoved();

        void setIsPropertyOwnerPartyIdRemoved(Boolean removed);

        Boolean getIsPropertyDefaultInventoryItemTypeIdRemoved();

        void setIsPropertyDefaultInventoryItemTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyFacilityNameRemoved();

        void setIsPropertyFacilityNameRemoved(Boolean removed);

        Boolean getIsPropertyPrimaryFacilityGroupIdRemoved();

        void setIsPropertyPrimaryFacilityGroupIdRemoved(Boolean removed);

        Boolean getIsPropertyOldSquareFootageRemoved();

        void setIsPropertyOldSquareFootageRemoved(Boolean removed);

        Boolean getIsPropertyFacilitySizeRemoved();

        void setIsPropertyFacilitySizeRemoved(Boolean removed);

        Boolean getIsPropertyFacilitySizeUomIdRemoved();

        void setIsPropertyFacilitySizeUomIdRemoved(Boolean removed);

        Boolean getIsPropertyProductStoreIdRemoved();

        void setIsPropertyProductStoreIdRemoved(Boolean removed);

        Boolean getIsPropertyDefaultDaysToShipRemoved();

        void setIsPropertyDefaultDaysToShipRemoved(Boolean removed);

        Boolean getIsPropertyOpenedDateRemoved();

        void setIsPropertyOpenedDateRemoved(Boolean removed);

        Boolean getIsPropertyClosedDateRemoved();

        void setIsPropertyClosedDateRemoved(Boolean removed);

        Boolean getIsPropertyDescriptionRemoved();

        void setIsPropertyDescriptionRemoved(Boolean removed);

        Boolean getIsPropertyDefaultDimensionUomIdRemoved();

        void setIsPropertyDefaultDimensionUomIdRemoved(Boolean removed);

        Boolean getIsPropertyDefaultWeightUomIdRemoved();

        void setIsPropertyDefaultWeightUomIdRemoved(Boolean removed);

        Boolean getIsPropertyGeoPointIdRemoved();

        void setIsPropertyGeoPointIdRemoved(Boolean removed);

        Boolean getIsPropertyGeoIdRemoved();

        void setIsPropertyGeoIdRemoved(Boolean removed);

        Boolean getIsPropertyFacilityLevelRemoved();

        void setIsPropertyFacilityLevelRemoved(Boolean removed);

        Boolean getIsPropertyActiveRemoved();

        void setIsPropertyActiveRemoved(Boolean removed);

        Boolean getIsPropertySequenceNumberRemoved();

        void setIsPropertySequenceNumberRemoved(Boolean removed);


        FacilityIdentificationCommandCollection getFacilityIdentificationCommands();

        FacilityIdentificationCommand.CreateFacilityIdentification newCreateFacilityIdentification();

        FacilityIdentificationCommand.MergePatchFacilityIdentification newMergePatchFacilityIdentification();

        FacilityIdentificationCommand.RemoveFacilityIdentification newRemoveFacilityIdentification();

    }

    interface DeleteFacility extends FacilityCommand {
    }

    interface CreateFacilityIdentificationCommandCollection extends Iterable<FacilityIdentificationCommand.CreateFacilityIdentification> {
        void add(FacilityIdentificationCommand.CreateFacilityIdentification c);

        void remove(FacilityIdentificationCommand.CreateFacilityIdentification c);

        void clear();
    }

    interface FacilityIdentificationCommandCollection extends Iterable<FacilityIdentificationCommand> {
        void add(FacilityIdentificationCommand c);

        void remove(FacilityIdentificationCommand c);

        void clear();
    }

}

