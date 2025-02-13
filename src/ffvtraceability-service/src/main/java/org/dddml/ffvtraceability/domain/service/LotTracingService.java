package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.repository.LotTracingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LotTracingService {

    private final LotTracingRepository lotTracingRepository;

    public LotTracingService(LotTracingRepository lotTracingRepository) {
        this.lotTracingRepository = lotTracingRepository;
    }

    /**
     * 递归查询所有投入的原材料批次
     */
    @Transactional(readOnly = true)
    public List<TracingNode> findAllInputLots(String productId, String lotId) {
        Set<TracingNode> result = new LinkedHashSet<>();
        findInputLotsRecursively(productId, lotId, result);
        return new ArrayList<>(result);
    }

    private void findInputLotsRecursively(String productId, String lotId, Set<TracingNode> result) {
        List<LotTracingRepository.LotTracingNodeProjection> inputLots =
                lotTracingRepository.findInputLots(productId, lotId);

        for (LotTracingRepository.LotTracingNodeProjection lot : inputLots) {
            TracingNode node = new TracingNode(
                    lot.getProductId(),
                    lot.getLotId(),
                    lot.getWorkEffortId()
            );

            if (result.add(node)) { // 如果是新的节点才继续递归
                findInputLotsRecursively(lot.getProductId(), lot.getLotId(), result);
            }
        }
    }

    /**
     * 递归查询所有产出的成品批次
     */
    @Transactional(readOnly = true)
    public List<TracingNode> findAllOutputLots(String productId, String lotId) {
        Set<TracingNode> result = new LinkedHashSet<>();
        findOutputLotsRecursively(productId, lotId, result);
        return new ArrayList<>(result);
    }

    private void findOutputLotsRecursively(String productId, String lotId, Set<TracingNode> result) {
        List<LotTracingRepository.LotTracingNodeProjection> outputLots =
                lotTracingRepository.findOutputLots(productId, lotId);

        for (LotTracingRepository.LotTracingNodeProjection lot : outputLots) {
            TracingNode node = new TracingNode(
                    lot.getProductId(),
                    lot.getLotId(),
                    lot.getWorkEffortId()
            );

            if (result.add(node)) { // 如果是新的节点才继续递归
                findOutputLotsRecursively(lot.getProductId(), lot.getLotId(), result);
            }
        }
    }

    /**
     * 表示追溯链中的一个节点
     */
    public static class TracingNode {
        private final String productId;
        private final String lotId;
        private final String workEffortId;

        public TracingNode(String productId, String lotId, String workEffortId) {
            this.productId = productId;
            this.lotId = lotId;
            this.workEffortId = workEffortId;
        }

        public String getProductId() {
            return productId;
        }

        public String getLotId() {
            return lotId;
        }

        public String getWorkEffortId() {
            return workEffortId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TracingNode that = (TracingNode) o;
            return Objects.equals(productId, that.productId) &&
                    Objects.equals(lotId, that.lotId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productId, lotId);
        }
    }
}