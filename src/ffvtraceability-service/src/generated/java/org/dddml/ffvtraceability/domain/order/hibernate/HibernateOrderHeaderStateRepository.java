// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.order.hibernate;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.partyrole.*;
import org.dddml.ffvtraceability.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.dddml.ffvtraceability.domain.order.*;
import org.dddml.ffvtraceability.specialization.*;
import org.dddml.ffvtraceability.specialization.hibernate.*;
import org.springframework.transaction.annotation.Transactional;

@Repository("orderHeaderStateRepository")
public class HibernateOrderHeaderStateRepository implements OrderHeaderStateRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        EntityManager em = this.entityManager;
        String currentTenantId = TenantContext.getTenantId();
        if (currentTenantId == null || currentTenantId.isEmpty()) {
            throw new IllegalStateException("Tenant context not set");
        }
        if (TenantSupport.SUPER_TENANT_ID != null && !TenantSupport.SUPER_TENANT_ID.isEmpty()
            && TenantSupport.SUPER_TENANT_ID.equals(currentTenantId)) {
            return em;
        }
        org.hibernate.Session session = em.unwrap(org.hibernate.Session.class);
        org.hibernate.Filter filter = session.enableFilter("tenantFilter");
        filter.setParameter("tenantId", currentTenantId);
        filter.validate();
        return em;
    }

    private static final Set<String> readOnlyPropertyPascalCaseNames = new HashSet<String>(Arrays.asList("OrderId", "OrderTypeId", "OrderName", "ExternalId", "SalesChannelEnumId", "OrderDate", "Priority", "EntryDate", "PickSheetPrintedDate", "VisitId", "StatusId", "FirstAttemptOrderId", "CurrencyUomId", "SyncStatusId", "BillingAccountId", "OriginFacilityId", "ProductStoreId", "TerminalId", "TransactionId", "AutoOrderShoppingListId", "NeedsInventoryIssuance", "IsRushOrder", "InternalCode", "RemainingSubTotal", "GrandTotal", "IsViewed", "InvoicePerShipment", "Memo", "ContactDescription", "FulfillmentStatusId", "OrderItems", "OrderRoles", "OrderAdjustments", "OrderContactMechanisms", "OrderShipGroups", "Version", "CreatedBy", "CreatedAt", "UpdatedBy", "UpdatedAt"));
    
    private ReadOnlyProxyGenerator readOnlyProxyGenerator;
    
    public ReadOnlyProxyGenerator getReadOnlyProxyGenerator() {
        return readOnlyProxyGenerator;
    }

    public void setReadOnlyProxyGenerator(ReadOnlyProxyGenerator readOnlyProxyGenerator) {
        this.readOnlyProxyGenerator = readOnlyProxyGenerator;
    }

    @Transactional(readOnly = true)
    public OrderHeaderState get(String id, boolean nullAllowed) {
        OrderHeaderState.SqlOrderHeaderState state = (OrderHeaderState.SqlOrderHeaderState)getEntityManager().find(AbstractOrderHeaderState.SimpleOrderHeaderState.class, id);
        if (!nullAllowed && state == null) {
            state = new AbstractOrderHeaderState.SimpleOrderHeaderState();
            state.setOrderId(id);
        }
        if (getReadOnlyProxyGenerator() != null && state != null) {
            return (OrderHeaderState) getReadOnlyProxyGenerator().createProxy(state, new Class[]{OrderHeaderState.SqlOrderHeaderState.class, Saveable.class}, "getStateReadOnly", readOnlyPropertyPascalCaseNames);
        }
        return state;
    }

    public void save(OrderHeaderState state) {
        OrderHeaderState s = state;
        if (getReadOnlyProxyGenerator() != null) {
            s = (OrderHeaderState) getReadOnlyProxyGenerator().getTarget(state);
        }
        if (s.getVersion() == null) {
            entityManager.persist(s);
        } else {
            entityManager.merge(s);
        }

        if (s instanceof Saveable) {
            Saveable saveable = (Saveable) s;
            saveable.save();
        }
        entityManager.flush();
    }

    public void merge(OrderHeaderState detached) {
        OrderHeaderState persistent = getEntityManager().find(AbstractOrderHeaderState.SimpleOrderHeaderState.class, detached.getOrderId());
        if (persistent != null) {
            merge(persistent, detached);
            entityManager.merge(persistent);
        } else {
            entityManager.persist(detached);
        }
        entityManager.flush();
    }

    private void merge(OrderHeaderState persistent, OrderHeaderState detached) {
        ((AbstractOrderHeaderState) persistent).merge(detached);
    }

}

