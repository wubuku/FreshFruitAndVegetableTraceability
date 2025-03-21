// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortgoodstandard;

import java.io.Serializable;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class WorkEffortGoodStandardId implements Serializable {
    private String workEffortId;

    public String getWorkEffortId()
    {
        return this.workEffortId;
    }

    public void setWorkEffortId(String workEffortId)
    {
        this.workEffortId = workEffortId;
    }

    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    private String workEffortGoodStdTypeId;

    public String getWorkEffortGoodStdTypeId()
    {
        return this.workEffortGoodStdTypeId;
    }

    public void setWorkEffortGoodStdTypeId(String workEffortGoodStdTypeId)
    {
        this.workEffortGoodStdTypeId = workEffortGoodStdTypeId;
    }

    private OffsetDateTime fromDate;

    public OffsetDateTime getFromDate()
    {
        return this.fromDate;
    }

    public void setFromDate(OffsetDateTime fromDate)
    {
        this.fromDate = fromDate;
    }

    public WorkEffortGoodStandardId()
    {
    }

    public WorkEffortGoodStandardId(String workEffortId, String productId, String workEffortGoodStdTypeId, OffsetDateTime fromDate)
    {
        this.workEffortId = workEffortId;
        this.productId = productId;
        this.workEffortGoodStdTypeId = workEffortGoodStdTypeId;
        this.fromDate = fromDate;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        WorkEffortGoodStandardId other = (WorkEffortGoodStandardId)obj;
        return true 
            && (workEffortId == other.workEffortId || (workEffortId != null && workEffortId.equals(other.workEffortId)))
            && (productId == other.productId || (productId != null && productId.equals(other.productId)))
            && (workEffortGoodStdTypeId == other.workEffortGoodStdTypeId || (workEffortGoodStdTypeId != null && workEffortGoodStdTypeId.equals(other.workEffortGoodStdTypeId)))
            && (fromDate == other.fromDate || (fromDate != null && fromDate.equals(other.fromDate)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.workEffortId != null) {
            hash += 13 * this.workEffortId.hashCode();
        }
        if (this.productId != null) {
            hash += 13 * this.productId.hashCode();
        }
        if (this.workEffortGoodStdTypeId != null) {
            hash += 13 * this.workEffortGoodStdTypeId.hashCode();
        }
        if (this.fromDate != null) {
            hash += 13 * this.fromDate.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "WorkEffortGoodStandardId{" +
                "workEffortId=" + '\'' + workEffortId + '\'' +
                ", productId=" + '\'' + productId + '\'' +
                ", workEffortGoodStdTypeId=" + '\'' + workEffortGoodStdTypeId + '\'' +
                ", fromDate=" + fromDate +
                '}';
    }

    protected static final String[] FLATTENED_PROPERTY_NAMES = new String[]{
            "workEffortId",
            "productId",
            "workEffortGoodStdTypeId",
            "fromDate",
    };

    protected static final String[] FLATTENED_PROPERTY_TYPES = new String[]{
            "String",
            "String",
            "String",
            "OffsetDateTime",
    };

    protected static final java.util.Map<String, String> FLATTENED_PROPERTY_TYPE_MAP;

    static {
        java.util.Map<String, String> m = new java.util.HashMap<String, String>();
        for (int i = 0; i < FLATTENED_PROPERTY_NAMES.length; i++) {
            m.put(FLATTENED_PROPERTY_NAMES[i], FLATTENED_PROPERTY_TYPES[i]);
        }
        FLATTENED_PROPERTY_TYPE_MAP = m;
    }

    protected void forEachFlattenedProperty(java.util.function.BiConsumer<String, Object> consumer) {
        for (int i = 0; i < FLATTENED_PROPERTY_NAMES.length; i++) {
            String pn = FLATTENED_PROPERTY_NAMES[i];
            if (Character.isLowerCase(pn.charAt(0))) {
                pn = Character.toUpperCase(pn.charAt(0)) + pn.substring(1);
            }
            java.lang.reflect.Method m = null;
            try {
                m = this.getClass().getDeclaredMethod("get" + pn, new Class[0]);
            } catch (NoSuchMethodException e) {
                try {
                    m = this.getClass().getMethod("get" + pn, new Class[0]);
                } catch (NoSuchMethodException e1) {
                    throw new RuntimeException(e);
                }
            }
            Object pv = null;
            try {
                pv = m.invoke(this);
            } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            consumer.accept(pn, pv);
        }
    }

    protected void setFlattenedPropertyValues(Object... values) {
        for (int i = 0; i < FLATTENED_PROPERTY_NAMES.length; i++) {
            String pn = FLATTENED_PROPERTY_NAMES[i];
            if (Character.isLowerCase(pn.charAt(0))) {
                pn = Character.toUpperCase(pn.charAt(0)) + pn.substring(1);
            }
            Object v = values[i];
            Class propCls = v == null ? Object.class : v.getClass();
            java.lang.reflect.Method setterMethod = null;
            if (v == null) {
                setterMethod = getNullValueSetterMethod(pn);
            }
            if (setterMethod == null) {
                try {
                    setterMethod = this.getClass().getDeclaredMethod("set" + pn, new Class[]{propCls});
                } catch (NoSuchMethodException e) {
                    try {
                        setterMethod = this.getClass().getMethod("set" + pn, new Class[]{propCls});
                    } catch (NoSuchMethodException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
            try {
                setterMethod.invoke(this, v);
            } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private java.lang.reflect.Method getNullValueSetterMethod(String pascalPropName) {
        java.lang.reflect.Method m;
        final String methodName = "set" + pascalPropName;
        m = java.util.Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(me -> me.getName().equals(methodName) && me.getParameterCount() == 1)
                .findFirst().orElse(null);
        if (m == null) {
            m = java.util.Arrays.stream(this.getClass().getMethods())
                    .filter(me -> me.getName().equals(methodName) && me.getParameterCount() == 1)
                    .findFirst().orElse(null);
        }
        return m;
    }

}

