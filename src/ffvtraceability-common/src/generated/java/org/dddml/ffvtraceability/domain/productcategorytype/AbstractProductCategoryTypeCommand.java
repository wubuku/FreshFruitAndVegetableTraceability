// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.productcategorytype;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractProductCategoryTypeCommand extends AbstractCommand implements ProductCategoryTypeCommand {

    private String productCategoryTypeId;

    public String getProductCategoryTypeId()
    {
        return this.productCategoryTypeId;
    }

    public void setProductCategoryTypeId(String productCategoryTypeId)
    {
        this.productCategoryTypeId = productCategoryTypeId;
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


    public static abstract class AbstractCreateOrMergePatchProductCategoryType extends AbstractProductCategoryTypeCommand implements CreateOrMergePatchProductCategoryType
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

    public static abstract class AbstractCreateProductCategoryType extends AbstractCreateOrMergePatchProductCategoryType implements CreateProductCategoryType
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

    }

    public static abstract class AbstractMergePatchProductCategoryType extends AbstractCreateOrMergePatchProductCategoryType implements MergePatchProductCategoryType
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

    public static class SimpleCreateProductCategoryType extends AbstractCreateProductCategoryType
    {
    }

    
    public static class SimpleMergePatchProductCategoryType extends AbstractMergePatchProductCategoryType
    {
    }

    
    public static class SimpleDeleteProductCategoryType extends AbstractProductCategoryTypeCommand implements DeleteProductCategoryType
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_DELETE;
        }
    }

    

}
