package org.dddml.ffvtraceability.domain.documentnumbergenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class DocumentNumberGeneratorApplicationServiceImpl extends AbstractDocumentNumberGeneratorApplicationService {

    private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    private static final String DEFAULT_PREFIX = "";
    private static final long DEFAULT_SEQUENCE_LENGTH = 5;

    @Autowired
    public DocumentNumberGeneratorApplicationServiceImpl(
            DocumentNumberGeneratorStateRepository stateRepository,
            DocumentNumberGeneratorStateQueryRepository stateQueryRepository
    ) {
        super(stateRepository, stateQueryRepository);
    }

    @Override
    @Transactional
    public String when(DocumentNumberGeneratorCommands.GenerateNextNumber c) {
        String generatorId = c.getGeneratorId();
        DocumentNumberGeneratorState s = getStateRepository().get(generatorId, true);
        if (s == null) {
            throw new IllegalArgumentException("Document number generator not found with id: " + generatorId);
        }

        DocumentNumberGeneratorState.MutableDocumentNumberGeneratorState generatorState =
                (DocumentNumberGeneratorState.MutableDocumentNumberGeneratorState) s;

        // 获取生成器配置并处理空值
        String dateFormat = generatorState.getDateFormat();
        if (dateFormat == null || dateFormat.trim().isEmpty()) {
            dateFormat = DEFAULT_DATE_FORMAT;
        }

        String prefix = generatorState.getPrefix();
        if (prefix == null) {
            prefix = DEFAULT_PREFIX;
        }

        String timeZoneId = generatorState.getTimeZoneId();
        if (timeZoneId == null || timeZoneId.trim().isEmpty()) {
            throw new IllegalStateException("TimeZoneId must not be null for generator: " + generatorId);
        }

        Long seqLen = generatorState.getSequenceLength();
        if (seqLen == null || seqLen <= 0) {
            seqLen = DEFAULT_SEQUENCE_LENGTH;
        }

        String lastGenDate = generatorState.getLastGeneratedDate();
        Long currentSeq = generatorState.getCurrentSequence();
        if (currentSeq == null) {
            currentSeq = 0L;
        }

        // 获取当前时区的日期
        LocalDate currentDate = LocalDate.now(ZoneId.of(timeZoneId));
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern(dateFormat));

        // 如果是新的一天或者没有上次生成日期，重置序号
        if (!formattedDate.equals(lastGenDate)) {
            currentSeq = 1L;
        } else {
            currentSeq++;
        }

        // 格式化序号
        String sequenceStr = String.format("%0" + seqLen + "d", currentSeq);

        // 更新生成器状态
        generatorState.setLastGeneratedDate(formattedDate);
        generatorState.setCurrentSequence(currentSeq);

        // 保存状态
        getStateRepository().save(generatorState);

        // 返回生成的单号
        return prefix + formattedDate + sequenceStr;
    }
}
