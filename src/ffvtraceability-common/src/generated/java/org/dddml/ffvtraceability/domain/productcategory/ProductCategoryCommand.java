// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.productcategory;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.specialization.DomainError;

public interface ProductCategoryCommand extends Command {

    String getProductCategoryId();

    void setProductCategoryId(String productCategoryId);

    Long getVersion();

    void setVersion(Long version);

    static void throwOnInvalidStateTransition(ProductCategoryState state, Command c) {
        if (state.getVersion() == null) {
            if (isCreationCommand((ProductCategoryCommand)c)) {
                return;
            }
            throw DomainError.named("premature", "Can't do anything to unexistent aggregate");
        }
        if (state.getDeleted() != null && state.getDeleted()) {
            throw DomainError.named("zombie", "Can't do anything to deleted aggregate.");
        }
        if (isCreationCommand((ProductCategoryCommand)c))
            throw DomainError.named("rebirth", "Can't create aggregate that already exists");
    }

    static boolean isCreationCommand(ProductCategoryCommand c) {
        if ((c instanceof ProductCategoryCommand.CreateProductCategory) 
            && (COMMAND_TYPE_CREATE.equals(c.getCommandType()) || c.getVersion().equals(ProductCategoryState.VERSION_NULL)))
            return true;
        if ((c instanceof ProductCategoryCommand.MergePatchProductCategory))
            return false;
        if ((c instanceof ProductCategoryCommand.DeleteProductCategory))
            return false;
        if (c.getCommandType() != null) {
            String commandType = c.getCommandType();
        }

        if (c.getVersion().equals(ProductCategoryState.VERSION_NULL))
            return true;
        return false;
    }

    interface CreateOrMergePatchProductCategory extends ProductCategoryCommand
    {

        String getProductCategoryTypeId();

        void setProductCategoryTypeId(String productCategoryTypeId);

        String getPrimaryParentCategoryId();

        void setPrimaryParentCategoryId(String primaryParentCategoryId);

        String getCategoryName();

        void setCategoryName(String categoryName);

        String getDescription();

        void setDescription(String description);

        String getCategoryImageUrl();

        void setCategoryImageUrl(String categoryImageUrl);

        String getDetailScreen();

        void setDetailScreen(String detailScreen);

        Boolean getShowInSelect();

        void setShowInSelect(Boolean showInSelect);

        Boolean getActive();

        void setActive(Boolean active);

    }

    interface CreateProductCategory extends CreateOrMergePatchProductCategory
    {
    }

    interface MergePatchProductCategory extends CreateOrMergePatchProductCategory
    {
        Boolean getIsPropertyProductCategoryTypeIdRemoved();

        void setIsPropertyProductCategoryTypeIdRemoved(Boolean removed);

        Boolean getIsPropertyPrimaryParentCategoryIdRemoved();

        void setIsPropertyPrimaryParentCategoryIdRemoved(Boolean removed);

        Boolean getIsPropertyCategoryNameRemoved();

        void setIsPropertyCategoryNameRemoved(Boolean removed);

        Boolean getIsPropertyDescriptionRemoved();

        void setIsPropertyDescriptionRemoved(Boolean removed);

        Boolean getIsPropertyCategoryImageUrlRemoved();

        void setIsPropertyCategoryImageUrlRemoved(Boolean removed);

        Boolean getIsPropertyDetailScreenRemoved();

        void setIsPropertyDetailScreenRemoved(Boolean removed);

        Boolean getIsPropertyShowInSelectRemoved();

        void setIsPropertyShowInSelectRemoved(Boolean removed);

        Boolean getIsPropertyActiveRemoved();

        void setIsPropertyActiveRemoved(Boolean removed);


    }

    interface DeleteProductCategory extends ProductCategoryCommand
    {
    }

}
