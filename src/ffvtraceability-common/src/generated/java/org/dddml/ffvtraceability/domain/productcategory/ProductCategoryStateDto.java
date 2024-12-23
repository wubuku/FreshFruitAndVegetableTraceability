// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.productcategory;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;


public class ProductCategoryStateDto {

    private String productCategoryId;

    public String getProductCategoryId()
    {
        return this.productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId)
    {
        this.productCategoryId = productCategoryId;
    }

    private String productCategoryTypeId;

    public String getProductCategoryTypeId()
    {
        return this.productCategoryTypeId;
    }

    public void setProductCategoryTypeId(String productCategoryTypeId)
    {
        this.productCategoryTypeId = productCategoryTypeId;
    }

    private String primaryParentCategoryId;

    public String getPrimaryParentCategoryId()
    {
        return this.primaryParentCategoryId;
    }

    public void setPrimaryParentCategoryId(String primaryParentCategoryId)
    {
        this.primaryParentCategoryId = primaryParentCategoryId;
    }

    private String categoryName;

    public String getCategoryName()
    {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
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

    private String categoryImageUrl;

    public String getCategoryImageUrl()
    {
        return this.categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl)
    {
        this.categoryImageUrl = categoryImageUrl;
    }

    private String detailScreen;

    public String getDetailScreen()
    {
        return this.detailScreen;
    }

    public void setDetailScreen(String detailScreen)
    {
        this.detailScreen = detailScreen;
    }

    private Boolean showInSelect;

    public Boolean getShowInSelect()
    {
        return this.showInSelect;
    }

    public void setShowInSelect(Boolean showInSelect)
    {
        this.showInSelect = showInSelect;
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

    private String createdBy;

    public String getCreatedBy()
    {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    private OffsetDateTime createdAt;

    public OffsetDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    private String updatedBy;

    public String getUpdatedBy()
    {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    private OffsetDateTime updatedAt;

    public OffsetDateTime getUpdatedAt()
    {
        return this.updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }


    public static class DtoConverter extends AbstractStateDtoConverter
    {
        public static Collection<String> collectionFieldNames = Arrays.asList(new String[]{});

        @Override
        protected boolean isCollectionField(String fieldName) {
            return CollectionUtils.collectionContainsIgnoringCase(collectionFieldNames, fieldName);
        }

        public ProductCategoryStateDto[] toProductCategoryStateDtoArray(Iterable<ProductCategoryState> states) {
            return toProductCategoryStateDtoList(states).toArray(new ProductCategoryStateDto[0]);
        }

        public List<ProductCategoryStateDto> toProductCategoryStateDtoList(Iterable<ProductCategoryState> states) {
            ArrayList<ProductCategoryStateDto> stateDtos = new ArrayList();
            for (ProductCategoryState s : states) {
                ProductCategoryStateDto dto = toProductCategoryStateDto(s);
                stateDtos.add(dto);
            }
            return stateDtos;
        }

        public ProductCategoryStateDto toProductCategoryStateDto(ProductCategoryState state)
        {
            if(state == null) {
                return null;
            }
            ProductCategoryStateDto dto = new ProductCategoryStateDto();
            if (returnedFieldsContains("ProductCategoryId")) {
                dto.setProductCategoryId(state.getProductCategoryId());
            }
            if (returnedFieldsContains("ProductCategoryTypeId")) {
                dto.setProductCategoryTypeId(state.getProductCategoryTypeId());
            }
            if (returnedFieldsContains("PrimaryParentCategoryId")) {
                dto.setPrimaryParentCategoryId(state.getPrimaryParentCategoryId());
            }
            if (returnedFieldsContains("CategoryName")) {
                dto.setCategoryName(state.getCategoryName());
            }
            if (returnedFieldsContains("Description")) {
                dto.setDescription(state.getDescription());
            }
            if (returnedFieldsContains("CategoryImageUrl")) {
                dto.setCategoryImageUrl(state.getCategoryImageUrl());
            }
            if (returnedFieldsContains("DetailScreen")) {
                dto.setDetailScreen(state.getDetailScreen());
            }
            if (returnedFieldsContains("ShowInSelect")) {
                dto.setShowInSelect(state.getShowInSelect());
            }
            if (returnedFieldsContains("Version")) {
                dto.setVersion(state.getVersion());
            }
            if (returnedFieldsContains("CreatedBy")) {
                dto.setCreatedBy(state.getCreatedBy());
            }
            if (returnedFieldsContains("CreatedAt")) {
                dto.setCreatedAt(state.getCreatedAt());
            }
            if (returnedFieldsContains("UpdatedBy")) {
                dto.setUpdatedBy(state.getUpdatedBy());
            }
            if (returnedFieldsContains("UpdatedAt")) {
                dto.setUpdatedAt(state.getUpdatedAt());
            }
            return dto;
        }

    }
}

