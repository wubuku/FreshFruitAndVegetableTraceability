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

        /**
         * Active
         */
        private String active;

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
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

        /**
         * Includes Facilities
         */
        private Boolean includesFacilities;

        public Boolean getIncludesFacilities() {
            return includesFacilities;
        }

        public void setIncludesFacilities(Boolean includesFacilities) {
            this.includesFacilities = includesFacilities;
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

    public static class UpdateBusinessContact extends org.dddml.ffvtraceability.domain.AbstractCommand {

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
         * Business Contact
         */
        private BffBusinessContactDto businessContact;

        public BffBusinessContactDto getBusinessContact() {
            return businessContact;
        }

        public void setBusinessContact(BffBusinessContactDto businessContact) {
            this.businessContact = businessContact;
        }

    }

    public static class BatchAddSuppliers extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Suppliers
         */
        private BffSupplierDto[] suppliers;

        public BffSupplierDto[] getSuppliers() {
            return suppliers;
        }

        public void setSuppliers(BffSupplierDto[] suppliers) {
            this.suppliers = suppliers;
        }

    }

    public static class BatchActivateSuppliers extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Supplier Ids
         */
        private String[] supplierIds;

        public String[] getSupplierIds() {
            return supplierIds;
        }

        public void setSupplierIds(String[] supplierIds) {
            this.supplierIds = supplierIds;
        }

    }

    public static class BatchDeactivateSuppliers extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Supplier Ids
         */
        private String[] supplierIds;

        public String[] getSupplierIds() {
            return supplierIds;
        }

        public void setSupplierIds(String[] supplierIds) {
            this.supplierIds = supplierIds;
        }

    }


}

