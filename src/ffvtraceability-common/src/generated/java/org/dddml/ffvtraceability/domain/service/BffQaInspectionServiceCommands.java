// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.service;

import java.util.*;
import org.dddml.ffvtraceability.domain.*;

public class BffQaInspectionServiceCommands {

    private BffQaInspectionServiceCommands() {
    }
    
    public static class GetQaInspections extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Receiving Document Id
         */
        private String receivingDocumentId;

        public String getReceivingDocumentId() {
            return receivingDocumentId;
        }

        public void setReceivingDocumentId(String receivingDocumentId) {
            this.receivingDocumentId = receivingDocumentId;
        }

        /**
         * Receipt Id
         */
        private String receiptId;

        public String getReceiptId() {
            return receiptId;
        }

        public void setReceiptId(String receiptId) {
            this.receiptId = receiptId;
        }

    }

    public static class GetQaInspection extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Qa Inspection Id
         */
        private String qaInspectionId;

        public String getQaInspectionId() {
            return qaInspectionId;
        }

        public void setQaInspectionId(String qaInspectionId) {
            this.qaInspectionId = qaInspectionId;
        }

    }

    public static class CreateQaInspection extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Qa Inspection
         */
        private BffQaInspectionDto qaInspection;

        public BffQaInspectionDto getQaInspection() {
            return qaInspection;
        }

        public void setQaInspection(BffQaInspectionDto qaInspection) {
            this.qaInspection = qaInspection;
        }

    }

    public static class UpdateQaInspection extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Qa Inspection Id
         */
        private String qaInspectionId;

        public String getQaInspectionId() {
            return qaInspectionId;
        }

        public void setQaInspectionId(String qaInspectionId) {
            this.qaInspectionId = qaInspectionId;
        }

        /**
         * Qa Inspection
         */
        private BffQaInspectionDto qaInspection;

        public BffQaInspectionDto getQaInspection() {
            return qaInspection;
        }

        public void setQaInspection(BffQaInspectionDto qaInspection) {
            this.qaInspection = qaInspection;
        }

    }

    public static class BatchAddQaInspections extends org.dddml.ffvtraceability.domain.AbstractCommand {

        /**
         * Qa Inspections
         */
        private BffQaInspectionDto[] qaInspections;

        public BffQaInspectionDto[] getQaInspections() {
            return qaInspections;
        }

        public void setQaInspections(BffQaInspectionDto[] qaInspections) {
            this.qaInspections = qaInspections;
        }

    }


}
