// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.service;

import java.util.*;
import org.dddml.ffvtraceability.domain.*;

public class BffSupplierServiceCommands {

    private BffSupplierServiceCommands() {
    }
    
    public static class GetSuppliers extends org.dddml.ffvtraceability.domain.AbstractCommand {
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

    public static class GetSupplier extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Supplier Id
         */
        private String supplierId;

        public String getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }

    }

    public static class CreateSupplier extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Supplier
         */
        private BffSupplierDto supplier;

        public BffSupplierDto getSupplier() {
            return supplier;
        }

        public void setSupplier(BffSupplierDto supplier) {
            this.supplier = supplier;
        }

    }

    public static class UpdateSupplier extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Supplier Id
         */
        private String supplierId;

        public String getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }

        /**
         * Supplier
         */
        private BffSupplierDto supplier;

        public BffSupplierDto getSupplier() {
            return supplier;
        }

        public void setSupplier(BffSupplierDto supplier) {
            this.supplier = supplier;
        }

    }

    public static class ActivateSupplier extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Supplier Id
         */
        private String supplierId;

        public String getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
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
