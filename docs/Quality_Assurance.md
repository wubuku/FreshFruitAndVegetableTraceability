# ERP系统质量检验（Quality Assurance, QA）模型设计方案

> 注意：以下内容基于与 AI 对话的结果整理而成，仅供参考。

## 1. 核心实体设计

### 1.1 检验单（QaInspection）
```sql
CREATE TABLE qa_inspection (
    inspection_id        VARCHAR(50) PRIMARY KEY,    -- 检验单号
    receipt_id          VARCHAR(50),                 -- 关联的收货单号
    inspector_id        VARCHAR(50),                 -- 检验员ID
    inspection_date     TIMESTAMP,                   -- 检验日期时间
    inspection_type     VARCHAR(20),                 -- 检验类型（收货/生产/出货）
    status             VARCHAR(20),                  -- 状态（待检/检验中/已完成/已关闭）
    lot_no             VARCHAR(50),                  -- 批次号
    sample_size        DECIMAL(10,2),               -- 抽样数量
    sample_unit        VARCHAR(10),                  -- 抽样单位
    result             VARCHAR(20),                  -- 检验结果（合格/不合格/部分合格）
    supplier_id        VARCHAR(50),                  -- 供应商ID
    warehouse_id       VARCHAR(50),                  -- 仓库ID
    quality_level      VARCHAR(20),                  -- 质量等级
    reference_docs     VARCHAR(255),                 -- 相关单据号
    comments           TEXT,                         -- 备注
    created_at         TIMESTAMP,                    -- 创建时间
    updated_at         TIMESTAMP                     -- 更新时间
);
```

### 1.2 检验项目（QaInspectionItem）
```sql
CREATE TABLE qa_inspection_item (
    item_id            VARCHAR(50) PRIMARY KEY,      -- 检验项目ID
    inspection_id      VARCHAR(50),                  -- 关联的检验单号
    check_item_id      VARCHAR(50),                  -- 检验项目定义ID
    standard_value     VARCHAR(100),                 -- 标准值
    actual_value       VARCHAR(100),                 -- 实测值
    tolerance_upper    DECIMAL(10,2),               -- 上限公差
    tolerance_lower    DECIMAL(10,2),               -- 下限公差
    result             VARCHAR(20),                  -- 项目检验结果
    defect_qty         INTEGER,                      -- 不合格数量
    severity           VARCHAR(20),                  -- 缺陷程度（严重/一般/轻微）
    test_equipment     VARCHAR(50),                  -- 测试设备
    measurement_unit   VARCHAR(20),                  -- 测量单位
    test_condition     TEXT,                         -- 测试条件
    comments           TEXT,                         -- 备注
    created_at         TIMESTAMP                     -- 创建时间
);
```

### 1.3 检验项目定义（QaCheckItem）
```sql
CREATE TABLE qa_check_item (
    check_item_id      VARCHAR(50) PRIMARY KEY,      -- 检验项目定义ID
    item_code          VARCHAR(50),                  -- 项目代码
    item_name          VARCHAR(100),                 -- 项目名称
    item_type          VARCHAR(20),                  -- 项目类型（定性/定量）
    check_method       TEXT,                         -- 检验方法
    standard_value     VARCHAR(100),                 -- 标准值
    tolerance_upper    DECIMAL(10,2),               -- 默认上限公差
    tolerance_lower    DECIMAL(10,2),               -- 默认下限公差
    is_required        BOOLEAN,                      -- 是否必检
    active             BOOLEAN,                      -- 是否启用
    created_at         TIMESTAMP                     -- 创建时间
);
```

### 1.4 检验方案（QaScheme）
```sql
CREATE TABLE qa_scheme (
    scheme_id          VARCHAR(50) PRIMARY KEY,      -- 方案ID
    scheme_code        VARCHAR(50),                  -- 方案代码
    scheme_name        VARCHAR(100),                 -- 方案名称
    product_category   VARCHAR(50),                  -- 适用产品类别
    product_id         VARCHAR(50),                  -- 具体产品ID（可选）
    supplier_id        VARCHAR(50),                  -- 适用供应商（可选）
    inspection_type    VARCHAR(20),                  -- 检验类型
    sampling_method    VARCHAR(50),                  -- 抽样方法
    sampling_rate      DECIMAL(5,2),                -- 抽样比例
    min_sample_size    INTEGER,                      -- 最小抽样数
    max_sample_size    INTEGER,                      -- 最大抽样数
    aql_value         DECIMAL(5,2),                 -- AQL值
    version           VARCHAR(20),                   -- 版本号
    effective_date     DATE,                         -- 生效日期
    expiry_date       DATE,                         -- 失效日期
    approval_status    VARCHAR(20),                  -- 审批状态
    active            BOOLEAN,                       -- 是否启用
    created_at        TIMESTAMP,                     -- 创建时间
    updated_at        TIMESTAMP                      -- 更新时间
);
```

### 1.5 检验方案明细（QaSchemeItem）
```sql
CREATE TABLE qa_scheme_item (
    scheme_item_id     VARCHAR(50) PRIMARY KEY,      -- 方案明细ID
    scheme_id          VARCHAR(50),                  -- 关联的方案ID
    check_item_id      VARCHAR(50),                  -- 关联的检验项目定义ID
    sequence_no        INTEGER,                      -- 序号
    is_required        BOOLEAN,                      -- 是否必检
    created_at         TIMESTAMP                     -- 创建时间
);
```

### 1.6 检验方案与检验单关联（QaInspectionSchemeRel）
```sql
CREATE TABLE qa_inspection_scheme_rel (
    inspection_id      VARCHAR(50),                  -- 检验单ID
    scheme_id          VARCHAR(50),                  -- 方案ID
    apply_date         TIMESTAMP,                    -- 应用日期
    applied_by         VARCHAR(50),                  -- 应用人
    PRIMARY KEY (inspection_id, scheme_id)
);
```

## 2. 辅助实体

### 2.1 不合格处理单（QaDefectHandle）
```sql
CREATE TABLE qa_defect_handle (
    handle_id          VARCHAR(50) PRIMARY KEY,      -- 处理单ID
    inspection_id      VARCHAR(50),                  -- 关联的检验单号
    handle_type        VARCHAR(20),                  -- 处理类型（退货/让步/返工/报废）
    handle_qty         DECIMAL(10,2),               -- 处理数量
    handler_id         VARCHAR(50),                  -- 处理人ID
    handle_date        TIMESTAMP,                    -- 处理日期
    status             VARCHAR(20),                  -- 状态
    comments           TEXT,                         -- 备注
    created_at         TIMESTAMP                     -- 创建时间
);
```

### 2.2 附件管理（QaAttachment）
```sql
CREATE TABLE qa_attachment (
    attachment_id      VARCHAR(50) PRIMARY KEY,      -- 附件ID
    inspection_id      VARCHAR(50),                  -- 关联检验单号
    file_path         VARCHAR(255),                 -- 文件路径
    file_type         VARCHAR(20),                  -- 文件类型
    upload_time       TIMESTAMP                     -- 上传时间
);
```

### 2.3 检验历史（QaInspectionHistory）
```sql
CREATE TABLE qa_inspection_history (
    history_id         VARCHAR(50) PRIMARY KEY,      -- 历史记录ID
    inspection_id      VARCHAR(50),                  -- 检验单号
    action_type        VARCHAR(20),                  -- 操作类型
    action_time        TIMESTAMP,                    -- 操作时间
    operator_id        VARCHAR(50),                  -- 操作人
    comments           TEXT                          -- 备注
);
```

## 3. 检验方案应用流程

### 3.1 方案选择逻辑
```java
public QaScheme selectApplicableScheme(String productId, String supplierId, String inspectionType) {
    // 1. 优先查找特定产品+供应商的方案
    QaScheme scheme = findScheme(productId, supplierId, inspectionType);
    
    if (scheme == null) {
        // 2. 查找产品类别+供应商的方案
        String productCategory = getProductCategory(productId);
        scheme = findScheme(productCategory, supplierId, inspectionType);
    }
    
    if (scheme == null) {
        // 3. 查找仅产品类别的通用方案
        scheme = findScheme(productCategory, null, inspectionType);
    }
    
    return scheme;
}
```

### 3.2 检验单创建流程
```java
public QaInspection createInspection(String receiptId, String productId, String supplierId) {
    // 1. 选择适用的检验方案
    QaScheme scheme = selectApplicableScheme(productId, supplierId, "RECEIPT");
    
    // 2. 创建检验单
    QaInspection inspection = new QaInspection();
    inspection.setReceiptId(receiptId);
    
    // 3. 根据方案设置抽样信息
    inspection.setSampleSize(calculateSampleSize(scheme, getReceiptQuantity(receiptId)));
    
    // 4. 从方案复制检验项目
    copyInspectionItems(scheme, inspection);
    
    // 5. 记录方案应用关系
    recordSchemeApplication(scheme, inspection);
    
    return inspection;
}
```

## 4. 最佳实践建议

1. **检验方案管理**
   - 建立分层的方案体系（通用方案、供应商特定方案、产品特定方案）
   - 实施版本控制
   - 定期评审和更新

2. **数据完整性**
   - 使用外键约束确保数据关联完整性
   - 重要操作记录操作历史
   - 关键字段添加业务规则验证

3. **系统集成**
   - 与库存管理系统集成
   - 与供应商管理系统集成
   - 与生产系统集成

4. **报表需求**
   - 检验合格率统计
   - 供应商质量分析
   - 不合格原因分析
   - 检验效率分析

5. **特殊情况处理**
   - 紧急放行机制
   - 特殊检验要求的灵活配置
   - 异常情况的处理流程

