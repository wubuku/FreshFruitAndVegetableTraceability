package org.dddml.ffvtraceability.domain.repository;

import org.dddml.ffvtraceability.domain.lot.AbstractLotState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotTracingRepository extends JpaRepository<AbstractLotState.SimpleLotState, String> {

    // 基本的追溯链路 - 包含表连接和流向判断
    String TRACING_CHAIN = """
            FROM inventory_item ii_output
                JOIN inventory_item_detail iid_output 
                    ON ii_output.inventory_item_id = iid_output.inventory_item_id
                JOIN work_effort we 
                    ON iid_output.work_effort_id = we.work_effort_id
                JOIN inventory_item_detail iid_input 
                    ON we.work_effort_id = iid_input.work_effort_id
                JOIN inventory_item ii_input 
                    ON iid_input.inventory_item_id = ii_input.inventory_item_id
            WHERE iid_output.quantity_on_hand_diff > 0
                AND iid_input.quantity_on_hand_diff < 0
            """;

    /**
     * 向上追溯 - 查询指定批次使用的原材料批次
     */
    @Query(value = """
            SELECT DISTINCT
                ii_input.product_id as productId,
                ii_input.lot_id as lotId,
                we.work_effort_id as workEffortId
            """ + TRACING_CHAIN + """
                AND ii_output.product_id = :productId 
                AND ii_output.lot_id = :lotId
            """, nativeQuery = true)
    List<LotTracingNodeProjection> findInputLots(
            @Param("productId") String productId,
            @Param("lotId") String lotId);

    /**
     * 向下追溯 - 查询指定批次产生的产品批次
     */
    @Query(value = """
            SELECT DISTINCT
                ii_output.product_id as productId,
                ii_output.lot_id as lotId,
                we.work_effort_id as workEffortId
            """ + TRACING_CHAIN + """
                AND ii_input.product_id = :productId 
                AND ii_input.lot_id = :lotId
            """, nativeQuery = true)
    List<LotTracingNodeProjection> findOutputLots(
            @Param("productId") String productId,
            @Param("lotId") String lotId);

    /**
     * 表示批次追溯链中的一个节点，包含产品、批次和工序信息
     */
    interface LotTracingNodeProjection {
        String getProductId();

        String getLotId();

        String getWorkEffortId();
    }
} 