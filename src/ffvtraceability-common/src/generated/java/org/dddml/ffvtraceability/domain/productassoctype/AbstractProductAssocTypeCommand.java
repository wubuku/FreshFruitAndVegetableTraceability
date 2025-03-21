// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.productassoctype;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractProductAssocTypeCommand extends AbstractCommand implements ProductAssocTypeCommand {

    private String productAssocTypeId;

    public String getProductAssocTypeId()
    {
        return this.productAssocTypeId;
    }

    public void setProductAssocTypeId(String productAssocTypeId)
    {
        this.productAssocTypeId = productAssocTypeId;
    }

    private Long version;

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }


    public static abstract class AbstractCreateOrMergePatchProductAssocType extends AbstractProductAssocTypeCommand implements CreateOrMergePatchProductAssocType
    {
        private String parentTypeId;

        public String getParentTypeId()
        {
            return this.parentTypeId;
        }

        public void setParentTypeId(String parentTypeId)
        {
            this.parentTypeId = parentTypeId;
        }

        private String hasTable;

        public String getHasTable()
        {
            return this.hasTable;
        }

        public void setHasTable(String hasTable)
        {
            this.hasTable = hasTable;
        }

        private String description;

        public String getDescription()
        {
            return this.description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

    }

    public static abstract class AbstractCreateProductAssocType extends AbstractCreateOrMergePatchProductAssocType implements CreateProductAssocType
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

    }

    public static abstract class AbstractMergePatchProductAssocType extends AbstractCreateOrMergePatchProductAssocType implements MergePatchProductAssocType
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }

        private Boolean isPropertyParentTypeIdRemoved;

        public Boolean getIsPropertyParentTypeIdRemoved()
        {
            return this.isPropertyParentTypeIdRemoved;
        }

        public void setIsPropertyParentTypeIdRemoved(Boolean removed)
        {
            this.isPropertyParentTypeIdRemoved = removed;
        }

        private Boolean isPropertyHasTableRemoved;

        public Boolean getIsPropertyHasTableRemoved()
        {
            return this.isPropertyHasTableRemoved;
        }

        public void setIsPropertyHasTableRemoved(Boolean removed)
        {
            this.isPropertyHasTableRemoved = removed;
        }

        private Boolean isPropertyDescriptionRemoved;

        public Boolean getIsPropertyDescriptionRemoved()
        {
            return this.isPropertyDescriptionRemoved;
        }

        public void setIsPropertyDescriptionRemoved(Boolean removed)
        {
            this.isPropertyDescriptionRemoved = removed;
        }


    }

    public static class SimpleCreateProductAssocType extends AbstractCreateProductAssocType
    {
    }

    
    public static class SimpleMergePatchProductAssocType extends AbstractMergePatchProductAssocType
    {
    }

    
    public static class SimpleDeleteProductAssocType extends AbstractProductAssocTypeCommand implements DeleteProductAssocType
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_DELETE;
        }
    }

    

}

