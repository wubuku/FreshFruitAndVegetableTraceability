// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.service;

import java.util.*;
import org.dddml.ffvtraceability.domain.*;

public class BffUomServiceCommands {

    private BffUomServiceCommands() {
    }
    
    public static class GetUnitsOfMeasure extends org.dddml.ffvtraceability.domain.AbstractCommand {
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

    public static class GetUnitOfMeasure extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Uom Id
         */
        private String uomId;

        public String getUomId() {
            return uomId;
        }

        public void setUomId(String uomId) {
            this.uomId = uomId;
        }

    }

    public static class CreateUnitOfMeasure extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Uom
         */
        private BffUomDto uom;

        public BffUomDto getUom() {
            return uom;
        }

        public void setUom(BffUomDto uom) {
            this.uom = uom;
        }

    }

    public static class UpdateUnitOfMeasure extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Uom Id
         */
        private String uomId;

        public String getUomId() {
            return uomId;
        }

        public void setUomId(String uomId) {
            this.uomId = uomId;
        }

        /**
         * Uom
         */
        private BffUomDto uom;

        public BffUomDto getUom() {
            return uom;
        }

        public void setUom(BffUomDto uom) {
            this.uom = uom;
        }

    }

    public static class ActivateUnitOfMeasure extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Uom Id
         */
        private String uomId;

        public String getUomId() {
            return uomId;
        }

        public void setUomId(String uomId) {
            this.uomId = uomId;
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
