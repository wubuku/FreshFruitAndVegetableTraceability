// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.service;

import java.util.*;
import org.dddml.ffvtraceability.domain.*;

public class BffLotServiceCommands {

    private BffLotServiceCommands() {
    }
    
    public static class GetLots extends org.dddml.ffvtraceability.domain.AbstractCommand {
        private Integer page;
        private Integer size;

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

    }

    public static class GetLot extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Lot Id
         */
        private String lotId;

        public String getLotId() {
            return lotId;
        }

        public void setLotId(String lotId) {
            this.lotId = lotId;
        }

    }

    public static class CreateLot extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Lot
         */
        private BffLotDto lot;

        public BffLotDto getLot() {
            return lot;
        }

        public void setLot(BffLotDto lot) {
            this.lot = lot;
        }

    }

    public static class UpdateLot extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Lot Id
         */
        private String lotId;

        public String getLotId() {
            return lotId;
        }

        public void setLotId(String lotId) {
            this.lotId = lotId;
        }

        /**
         * Lot
         */
        private BffLotDto lot;

        public BffLotDto getLot() {
            return lot;
        }

        public void setLot(BffLotDto lot) {
            this.lot = lot;
        }

    }

    public static class ActivateLot extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Lot Id
         */
        private String lotId;

        public String getLotId() {
            return lotId;
        }

        public void setLotId(String lotId) {
            this.lotId = lotId;
        }

        /**
         * Active
         */
        private Boolean active;

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

    }


}
