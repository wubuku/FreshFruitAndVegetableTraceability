// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.productcategory;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface ProductCategoryState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    String getProductCategoryId();

    String getProductCategoryTypeId();

    String getPrimaryParentCategoryId();

    String getCategoryName();

    String getDescription();

    String getCategoryImageUrl();

    String getDetailScreen();

    Boolean getShowInSelect();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    String getTenantId();

    interface MutableProductCategoryState extends ProductCategoryState {
        void setProductCategoryId(String productCategoryId);

        void setProductCategoryTypeId(String productCategoryTypeId);

        void setPrimaryParentCategoryId(String primaryParentCategoryId);

        void setCategoryName(String categoryName);

        void setDescription(String description);

        void setCategoryImageUrl(String categoryImageUrl);

        void setDetailScreen(String detailScreen);

        void setShowInSelect(Boolean showInSelect);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setTenantId(String tenantId);


        void mutate(Event e);

        //void when(ProductCategoryEvent.ProductCategoryStateCreated e);

        //void when(ProductCategoryEvent.ProductCategoryStateMergePatched e);

    }

    interface SqlProductCategoryState extends MutableProductCategoryState {

        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}

