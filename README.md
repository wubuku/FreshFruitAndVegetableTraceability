# Fresh Fruit and Vegetable Traceability

## 需求分析与领域建模

###  What are the Critical Tracking Events?

| CRITICAL TRACKING EVENT DEFINITIONS                          |                                                              |                                                              |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **Beginning of life – type event** *Events that typically support the introduction of an item in the supply chain* | Beginning of life (B)                                        | An event where a new item is introduced into the supply chain. (e.g. Harvesting a specific batch/lot of apples). |
| **Transformation – type event** *Events that typically support internal traceability within the four walls of a supply chain company* | Transformation (T) Input/Output                              | An event where one or more materials are used to produce a traceable product that enters the supply chain. Note: materials used to produce products for immediate consumption by consumers are reported as consumption events |
| **Transportation – type event** *Events that typically support external traceability between supply chain companies* | Shipping (S) event                                           | An event where traceable product is dispatched from a defined location to another defined location | 
| _ | Receiving (R) event                                          | An event where traceable product is received at a defined location from another defined location |                                                              |
| **Depletion – type event** *Events that capture how traceable product is removed from the supply chain* | Consumption (C) event                                        | An event where a traceable product becomes available to consumers (point of sale or prepared) |
| _ | Disposal (D) event                                           | An event where a traceable product is destroyed or discarded or otherwise handled in a manner that the product could no longer be used as a food ingredient or become available to consumers. |                                                              |


### Critical Tracking Events (CTEs) for the Produce Industry


#### Harvest (picking)

Tomatoes (input) are harvested in the field into bushels, then loaded into “packaging materials” (input). Packed tomatoes are the output. These tomatoes are loose in the bushels; they are not packed in the field in the form of a finished good. Sometimes they are packed into sacks or bins instead of bushels.

| Category | Key Data Element Name | EPCIS Translation | Example Value |
|----------|----------------------|-------------------|---------------|
| What     | Type                 | EPCIS Event Type  | Object |
| When     | Date/Time            | Event Time        | 2017-05-22T13:15:00+06:00 |
|          |                      | Record Time       | 2017-05-22T16:15:00+06:00 |
|          |                      | GTIN                 | 9504000219109* |
|          | Batch/Lot            | GTIN + Lot (LGTIN) | B20171202-1 |
|          | Serial #             | GTIN + Serial # (SGTIN) | - |
|          | Quantity             | QTY               | 200 |
| Where    | Unit of Measure      | UOM               | Bushel |
|          | -                    | Read Point        | 9504000219901.PL-A023 |
|          | -                    | Biz Location      | 9504000219000 |
| Why      | -                    | Biz Step          | Commissioning |
|          | -                    | Disposition       | Active |
|          | Business Transaction | Biz Transaction Type | ProdOrder |
|          | Activity ID          | Biz Transaction ID | WO234 |
|          | -                    | ILMD (mda:)       | harvestdate 2017-05-22 |


#### Harvest (packing materials)

Packaging materials have been previously created for use. This input step identifies the packaging materials to be used in this transformation step.

| Key Data Element | Name | EPCIS Translation | Example Value |
|------------------|------|-------------------|---------------|
| Type | - | EPCIS Event Type | Object |
| When | Date/Time | Event Time | 2017-07-14T23:20:00+01:00 |
| | | Record Time | 2017-07-15T08:20:00+01:00 |
| What | GTIN | - | 9501101530003 |
| | Batch/Lot | GTIN + Lot (LGTIN) | AB-123 |
| | Serial # | GTIN + Serial # (SGTIN) | - |
| | Quantity | QTY | 500 |
| Where | Unit of Measure | UOM | cases |
| | - | Read Point | 9504000219901.PL-A023 |
| | - | Biz Location | 9504000219000 |
| Why | - | Biz Step | Commissioning |
| | - | Disposition | Active |
| | Business Transaction | Biz Transaction Type | ProdOrder |
| | Activity ID | Biz Transaction ID | WO234 |

#### Transformation (case pack)

The harvested tomatoes (input) are put into the packaging materials (input) to create the cases of packed tomatoes (output) for shipment to the shed.

| Key Data Element | Name | EPCIS Translation | Example Value |
|------------------|------|-------------------|---------------|
| Type | - | EPCIS Event Type | Object |
| When | Date/Time | Event Time | 2017-07-14T23:20:00+01:00 |
| | | Record Time | 2017-07-15T08:20:00+01:00 |
| What Input | GTIN | - | 9504000219109 |
| | Batch/Lot | GTIN + Lot (LGTIN) | C20171202-1 |
| | Serial # | GTIN + Serial # (SGTIN) | - |
| | Quantity | QTY | 500 |
| | Unit of Measure | UOM | cases |
| What Input | GTIN | - | 9504000219109 |
| | Batch/Lot | GTIN + Lot (LGTIN) | B20171202-1 |
| | Serial # | GTIN + Serial # (SGTIN) | - |
| | Quantity | QTY | 500 |
| | Unit of Measure | UOM | cases |
| What Output | GTIN | - | 9501101530003 |
| | Batch/Lot | GTIN + Lot (LGTIN) | AB-123 |
| | Serial # | GTIN + Serial # (SGTIN) | - |
| | Quantity | QTY | 500 |
| | Unit of Measure | UOM | cases |
| Where | - | Read Point | 9501101530911 |
| | - | Biz Location | 9501101530911 |
| Why | - | Biz Step | Commissioning |
| | - | Disposition | Active |
| | Business Transaction | Biz Transaction Type | ProdOrder |
| | Activity ID | Biz Transaction ID | WO234 |
| | - | ILMD(mda:) | productiondate 2017-07-15 |


#### Transport (packed to repacker)

Packed tomatoes are shipped to the re-packers.

| Key Data Element | Name | EPCIS Translation | Example Value |
|------------------|------|-------------------|---------------|
| Type | - | EPCIS Event Type | Object |
| When | Date/Time | Event Time | 2017-05-22T13:15:00+06:00 |
| | | Record Time | 2017-05-22T13:15:00+09:00 |
| What | SSCC | SSCC | 395011015300022000 |
| | GTIN | - | 9504000219109 |
| | Batch/Lot | GTIN + Lot (LGTIN) | AB-123 |
| | Serial # | GTIN + Serial # (SGTIN) | - |
| | Quantity | QTY | 20 |
| | Unit of Measure | UOM | CASES |
| Where | - | Read Point | 9501101530928.PL-A023 |
| | - | Biz Location | 9501101530928 |
| Why | - | Biz Step | Shipping |
| | - | Disposition | In Transit |
| | Business Transaction | Biz Transaction Type | DesAdv |
| | Activity ID | Biz Transaction ID | ASN123 |
| | - | Sources | 9501101530911 |
| | - | Destination | 9501101530928 |

#### Transport (tomatoes received at repacker)

Packed tomatoes received at Repacker.

| Key Data Element | Name | EPCIS Translation | Example Value |
|------------------|------|-------------------|---------------|
| Type | - | EPCIS Event Type | Object |
| When | Date/Time | Event Time | 2017-05-22T13:15:00+06:00 |
| | | Record Time | 2017-05-22T13:15:00+09:00 |
| What | SSCC | SSCC | 395011015300022000 |
| | GTIN | - | 9504000219109 |
| | Batch/Lot | GTIN + Lot (LGTIN) | AB-123 |
| | Serial # | GTIN + Serial # (SGTIN) | - |
| | Quantity | QTY | 20 |
| | Unit of Measure | UOM | CASES |
| Where | - | Read Point | 9501101530928.ST5 |
| | - | Biz Location | 9501101530928 |
| Why | - | Biz Step | Shipping |
| | - | Disposition | In Transit |
| | Business Transaction | Biz Transaction Type | RecAdv |
| | Activity ID | Biz Transaction ID | RA123 |
| | - | Sources | 9501101530911 |
| | - | Destination | 9501101530928 |


#### Create Finished Goods

Finished goods are created (flat of tomatoes) for transport to final point of sale.

| Key Data Element | Name | EPCIS Translation | Example Value |
|-------------------|------|-------------------|---------------|
| Type | - | EPCIS Event Type | Object |
| When | Date/Time | Event Time | 2017-07-14T23:20:00+01:00 |
| | | Record Time | 2017-07-15T08:20:00+01:00 |
| What Input | GTIN | - | 9504000219109 |
| | Batch/Lot | GTIN + Lot (LGTIN) | C20171202-1 |
| | Serial # | GTIN + Serial # (SGTIN) | - |
| | Quantity | QTY | 20 |
| | Unit of Measure | UOM | - |
| What Output | GTIN | - | 9501101530003 |
| | Batch/Lot | GTIN + Lot (LGTIN) | AB-123 |
| | Serial # | GTIN + Serial # (SGTIN) | - |
| | Quantity | QTY | 15 |
| | Unit of Measure | UOM | Tray |
| Where | - | Read Point | 9504000357001 |
| | - | Biz Location | 9504000357001 |
| Why | - | Biz Step | Commissioning |
| | - | Disposition | Active |
| | Business Transaction | Biz Transaction Type | ProdOrder |
| | Activity ID | Biz Transaction ID | WO234 |

#### Ship Finished Goods

Finished goods are shipped to final point of sale.

| Key Data Element | Name | EPCIS Translation | Example Value |
|-------------------|------|-------------------|---------------|
| Type | - | EPCIS Event Type | Object |
| When | Date/Time | Event Time | 2017-05-22T13:15:00+06:00 |
| | | Record Time | 2017-05-22T13:15:00+09:00 |
| What | SSCC | SSCC | 095040001234567000 |
| | GTIN | - | 9504000219109 |
| | Batch/Lot | GTIN + Lot (LGTIN) | AB-123 |
| | Serial # | GTIN + Serial # (SGTIN) | - |
| | Quantity | QTY | 20 |
| Where | Unit of Measure | UOM | CASES |
| | - | Read Point | 9501101530928.PL-A023 |
| | - | Biz Location | 9501101530928 |
| Why | - | Biz Step | Shipping |
| | - | Disposition | In Transit |
| | Business Transaction | Biz Transaction Type | DesAdv |
| | Activity ID | Biz Transaction ID | ASN789 |
| | - | Sources | 9501101530911 |
| | - | Destination | 9504000357001 |


#### Receive Finished Goods

Finished goods are received at final point of sale.

| Key Data Element | Name | EPCIS Translation | Example Value |
|-------------------|------|-------------------|---------------|
| Type | - | EPCIS Event Type | Object |
| When | Date/Time | Event Time | 2017-05-22T13:15:00+06:00 |
| | | Record Time | 2017-05-22T13:15:00+09:00 |
| What | SSCC | SSCC | 395011015300022000 |
| | GTIN | - | 9504000219109 |
| | Batch/Lot | GTIN + Lot (LGTIN) | AB-123 |
| | Serial # | GTIN + Serial # (SGTIN) | - |
| | Quantity | QTY | 10 |
| Where | Unit of Measure | UOM | CASES |
| | - | Read Point | 9504000357001 |
| | - | Biz Location | 9504000357001 |
| Why | - | Biz Step | Shipping |
| | - | Disposition | In Transit |
| | Business Transaction | Biz Transaction Type | RecAdv |
| | Activity ID | Biz Transaction ID | RA789 |
| | - | Sources | 9501101530911 |
| | - | Destination | 9501101530928 |


#### Finished Goods Sold at Point of Sale or Consumption

Finished goods (flat of tomatoes) are sold at point of sale.

| Category | Key Data Element Name | EPCIS Translation | Example Value |
|----------|----------------------|-------------------|---------------|
| Type     | -                    | EPCIS Event Type  | Object |
| When     | Date/Time            | Event Time        | 2017-05-22T13:15:00+06:00 |
|          |                      | Record Time       | 2017-05-22T13:15:00+09:00 |
| What     | GTIN                 | -                 | 9504000357662 |
|          | Batch/Lot            | GTIN + Lot (LGTIN) | 2018040G11440 |
|          | Serial #             | GTIN + Serial # (SGTIN) | - |
|          | Quantity             | QTY               | 8 |
| Where    | Unit of Measure      | UOM               | - |
|          | -                    | Read Point        | 9504000357001 |
|          | -                    | Biz Location      | 9504000357001 |
| Why      | -                    | Biz Step          | Retail Selling |
|          | -                    | Disposition       | Retail Sold |
|          | Business Transaction | Biz Transaction Type | Receipt Transaction |
|          | Activity ID          | Biz Transaction ID | POS 123 |

#### Destroy Unsaleable Finished Goods

Unsaleable finished goods are destroyed.

| Category | Key Data Element Name | EPCIS Translation | Example Value |
|----------|----------------------|-------------------|---------------|
| Type     | -                    | EPCIS Event Type  | Object |
| When     | Date/Time            | Event Time        | 2017-05-22T13:15:00+06:00 |
|          |                      | Record Time       | 2017-05-22T13:15:00+09:00 |
| What     | GTIN                 | -                 | 9504000357662 |
|          | Batch/Lot            | GTIN + Lot (LGTIN) | 2018040G11440 |
|          | Serial #             | GTIN + Serial # (SGTIN) | - |
|          | Quantity             | QTY               | 2 |
| Where    | Unit of Measure      | UOM               | - |
|          | -                    | Read Point        | 9504000357001 |
|          | -                    | Biz Location      | 9504000357001 |
| Why      | -                    | Biz Step          | Destroying |
|          | -                    | Disposition       | Destroyed |
|          | Business Transaction | Biz Transaction Type | Work Order |
|          | Activity ID          | Biz Transaction ID | WO456 |


### Additional internal data to support traceability

#### Seed

#### Fertilizer

#### Crop protection & Phytosanitary

#### Irrigation method

#### Harvesting

#### Post-harvest treatment

#### Packing

Below are some of the events where data collection should be performed which impact packing.

##### How can this be acquired/shared?

###### Purchase

- Business rational: Documentation of orders placed for possible recall issues
- DEI: Purchase Order

| KDE |  Data Source    |
| -------------- | ---- |
| Supplier (GLN) |      |
| PO #           |      |
| Invoice #      |      |
| GTIN           |    GDSN  |
| Quantity       |      |
| Date           |      |

###### Reception


- Business rational: First contact with GTIN + supplemental information (Batch/lot) as well as a record of the transport company used in case of recall from source or recall from transport contamination
- EDI: Despatch Advice

| KDE | Data Source |
| ----------------- | --------------- |
| Transporter (GLN) | |
| Invoice #         |                 |
| PO #              |                 |
| Reception Date    |                 |
| Receiver (GLN)    |                 |
| SSCC              |                 |
| GTIN              | GDSN            |
| Quantity          |                 |
| Lot/Batch         | GS1-128         |

###### Storage

- Business rational: Accurate inventory control (in & out)

| KDE            | Data Source |
| -------------- | ----------- |
| GTIN           | GDSN        |
| Quantity       |             |
| Lot/Batch      | GS1-128     |
| Location (GLN) |             |
| Date           |             |

###### Packaging

- Business rational: documentation of where items were used/applied.

| KDE            | Data Source |
| -------------- | ----------- |
| GTIN           | GDSN        |
| Quantity       |             |
| Lot/Batch      | GS1-128     |
| Location (GLN) |             |
| Date           |             |

#### Shipping

#### Receiving

Below are some of the events where data collection should be performed which impact receiving.

##### Reception

| Category | Key Data Element | Description |
|----------|------------------|-------------|
| What     | GTIN + batch/lot ID + quantity OR GTIN + serial ID | - |
| When     | Reception date and time | - |
| Where    | GLN of buyer | - |
| Why      | - | Initial traceability reception into inventory |


##### Storage

| Category | Key Data Element | Description |
|----------|------------------|-------------|
| What     | GTIN + batch/lot ID + quantity OR GTIN + serial ID | - |
| When     | Storage date and time | - |
| Where    | GLN of product sub-location | - |
| Why      | - | Efficient stock rotation |


#### Quality check

##### How can this be acquired/shared?

Business rational: Sorting touch point which impacts pre and post GTIN & Lot/batch

| KDE                   | EDI  | Data Source |
| --------------------- | ---- | ----------- |
| Seller/grower (GLN)   |      |             |
| date of reception     |      |             |
| GTIN                  |      | GDSN        |
| Lot Batch             |      | GS1-128     |
| Quantity              |      |             |
| SSCC                  |      |             |
| Location (pre) (GLN)  |      |             |
| QC date               |      |             |
| Grade                 |      |             |
| Location (post) (GLN) |      |             |

#### Processing

##### How can this be acquired/shared?

| KDE                          | EDI  | Data Source |
| ---------------------------- | ---- | ----------- |
| Source GTIN                  |      | GDSN        |
| Source Lot/Batch             |      | GS1-128     |
| Source Quantity              |      |             |
| Storage Location (GLN)       |      |             |
| Date                         |      |             |
| Output GTIN                  |      | GDSN        |
| Output Lot/Batch             |      | GS1-128     |
| Output Quantity              |      |             |
| Process Location (GLN)       |      |             |
| Processing Materials GTIN    |      | GDSN        |
| Final Storage Location (GLN) |      |             |

#### Customs entry

#### Customs checking

#### Customs freight release




### 参考资料

#### ILMD

https://gist.github.com/wubuku/525a892c07a9bc56b59572ee8314dec0

#### Event Capture Notification for Sending EPCIS Events

https://help.sap.com/doc/saphelp_aii710/7.1/en-US/48/d1cbfe90d75430e10000000a42189b/content.htm?no_cache=true

#### GS1 Application Identifiers

https://ref.gs1.org/ai/


#### 术语表

https://www.gs1.org/standards/fresh-fruit-and-vegetable-traceability-guideline/current-standard#A-Glossary+A-1-Glossary-of-terms

#### Others

GS1 EPCIS STANDARD：https://byteally.com/insights/supply-chain/gs1-epcis-standard/


### Tips

#### GIAI 和 GRAI

GIAI 和 GRAI 是 GS1 标准中的两种标识符，用于不同的资产管理场景：
1. GIAI（Global Individual Asset Identifier）：全球个体资产标识符，用于唯一标识单个资产。它由 GS1 公司前缀和个体资产参考组成，适用于固定资产的管理，例如计算机、办公桌或运输设备。
2. GRAI（Global Returnable Asset Identifier）：全球可回收资产标识符，用于标识可重复使用的资产，如托盘、集装箱等。GRAI 由 GS1 公司前缀、资产类型和可选的序列号组成，帮助企业跟踪和管理这些可回收资产。

这些标识符在供应链管理中起到重要作用，确保资产的唯一性和可追溯性。

#### GDSN（Global Data Synchronization Network）

GTIN（全球贸易项目代码）。

GTIN 是用于唯一标识贸易项目的代码，通常用于条形码中。GTIN 可以有多种格式，包括 GTIN-12、GTIN-13、GTIN-14 等，具体取决于应用场景。每个 GTIN 都是唯一的，确保每个产品在全球范围内都能被唯一识别。

#### GTIN（Global Trade Item Number）

GDSN（全球数据同步网络）。

GDSN 是一个全球性的网络，用于在供应链中的不同参与者之间同步和共享高质量的产品数据。通过 GDSN，企业可以确保其产品信息在全球范围内的一致性和准确性。GDSN 通过数据池（Data Pools）运作，这些数据池允许企业上传、维护和共享产品信息。


#### JSON-LD

JSON-LD，全称为 JavaScript Object Notation for Linked Data，是一种基于 JSON 的轻量级数据交换格式，旨在使数据在 Web 上更易于互操作。

JSON-LD 的主要特点：
1. 兼容性：JSON-LD 基于 JSON，因此与现有的 JSON 解析器和生成器兼容。
2. 灵活性：支持在单个文档中混合表示互联数据和非互联数据。
3. 可扩展性：允许使用自定义词汇表来扩展其功能。
4. 语义化：不仅仅是一种数据交换格式，还支持语义化数据，使其对机器也有意义。

JSON-LD 的应用场景：
• Web 服务：构建可互操作的 Web 服务。
• 语义网：在语义网中组织和表示数据。
• 数据集成：整合来自不同源的数据。
• 富互联网应用程序：处理大量交互式数据。
