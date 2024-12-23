// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class DocumentAssociationId implements Serializable {
    private String documentId;

    public String getDocumentId()
    {
        return this.documentId;
    }

    public void setDocumentId(String documentId)
    {
        this.documentId = documentId;
    }

    private String documentIdTo;

    public String getDocumentIdTo()
    {
        return this.documentIdTo;
    }

    public void setDocumentIdTo(String documentIdTo)
    {
        this.documentIdTo = documentIdTo;
    }

    private String documentAssocTypeId;

    public String getDocumentAssocTypeId()
    {
        return this.documentAssocTypeId;
    }

    public void setDocumentAssocTypeId(String documentAssocTypeId)
    {
        this.documentAssocTypeId = documentAssocTypeId;
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

    public DocumentAssociationId()
    {
    }

    public DocumentAssociationId(String documentId, String documentIdTo, String documentAssocTypeId, OffsetDateTime fromDate)
    {
        this.documentId = documentId;
        this.documentIdTo = documentIdTo;
        this.documentAssocTypeId = documentAssocTypeId;
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

        DocumentAssociationId other = (DocumentAssociationId)obj;
        return true 
            && (documentId == other.documentId || (documentId != null && documentId.equals(other.documentId)))
            && (documentIdTo == other.documentIdTo || (documentIdTo != null && documentIdTo.equals(other.documentIdTo)))
            && (documentAssocTypeId == other.documentAssocTypeId || (documentAssocTypeId != null && documentAssocTypeId.equals(other.documentAssocTypeId)))
            && (fromDate == other.fromDate || (fromDate != null && fromDate.equals(other.fromDate)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.documentId != null) {
            hash += 13 * this.documentId.hashCode();
        }
        if (this.documentIdTo != null) {
            hash += 13 * this.documentIdTo.hashCode();
        }
        if (this.documentAssocTypeId != null) {
            hash += 13 * this.documentAssocTypeId.hashCode();
        }
        if (this.fromDate != null) {
            hash += 13 * this.fromDate.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "DocumentAssociationId{" +
                "documentId=" + '\'' + documentId + '\'' +
                ", documentIdTo=" + '\'' + documentIdTo + '\'' +
                ", documentAssocTypeId=" + '\'' + documentAssocTypeId + '\'' +
                ", fromDate=" + fromDate +
                '}';
    }

    protected static final String[] FLATTENED_PROPERTY_NAMES = new String[]{
            "documentId",
            "documentIdTo",
            "documentAssocTypeId",
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

