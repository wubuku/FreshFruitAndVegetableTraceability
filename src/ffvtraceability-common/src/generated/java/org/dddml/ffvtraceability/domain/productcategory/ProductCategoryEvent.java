// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.productcategory;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface ProductCategoryEvent extends Event {

    interface SqlProductCategoryEvent extends ProductCategoryEvent {
        ProductCategoryEventId getProductCategoryEventId();

        boolean getEventReadOnly();

        void setEventReadOnly(boolean readOnly);
    }

    String getProductCategoryId();

    //void setProductCategoryId(String productCategoryId);

    Long getVersion();
    
    //void setVersion(Long version);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    OffsetDateTime getCreatedAt();

    void setCreatedAt(OffsetDateTime createdAt);

    String getCommandId();

    void setCommandId(String commandId);

    interface ProductCategoryStateEvent extends ProductCategoryEvent {
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

    }

    interface ProductCategoryStateCreated extends ProductCategoryStateEvent
    {
    
    }


    interface ProductCategoryStateMergePatched extends ProductCategoryStateEvent
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



    }

    interface ProductCategoryStateDeleted extends ProductCategoryStateEvent
    {
    }


}
