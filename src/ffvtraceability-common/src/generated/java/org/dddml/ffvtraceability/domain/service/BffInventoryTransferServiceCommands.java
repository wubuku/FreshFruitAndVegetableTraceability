// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.service;

import java.util.*;
import org.dddml.ffvtraceability.domain.*;

public class BffInventoryTransferServiceCommands {

    private BffInventoryTransferServiceCommands() {
    }
    
    public static class LocationAdjustment extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Inventory Item Id
         */
        private String inventoryItemId;

        public String getInventoryItemId() {
            return inventoryItemId;
        }

        public void setInventoryItemId(String inventoryItemId) {
            this.inventoryItemId = inventoryItemId;
        }

        /**
         * Adjustment Quantity
         */
        private java.math.BigDecimal adjustmentQuantity;

        public java.math.BigDecimal getAdjustmentQuantity() {
            return adjustmentQuantity;
        }

        public void setAdjustmentQuantity(java.math.BigDecimal adjustmentQuantity) {
            this.adjustmentQuantity = adjustmentQuantity;
        }

        /**
         * Facility Id To
         */
        private String facilityIdTo;

        public String getFacilityIdTo() {
            return facilityIdTo;
        }

        public void setFacilityIdTo(String facilityIdTo) {
            this.facilityIdTo = facilityIdTo;
        }

        /**
         * Location Seq Id To
         */
        private String locationSeqIdTo;

        public String getLocationSeqIdTo() {
            return locationSeqIdTo;
        }

        public void setLocationSeqIdTo(String locationSeqIdTo) {
            this.locationSeqIdTo = locationSeqIdTo;
        }

        /**
         * Comments
         */
        private String comments;

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

    }


}

