# Produce Traceability Implementation

## Produce Traceability Initiative FSMA 204 Implementation Guidance

https://producetraceability.org/wp-content/uploads/2024/02/PTI-FSMA-204-Implementation-Guidance-FINAL-2.12.24.pdf

### FSMA 204 Requirements

#### CRITICAL TRACKING EVENTS (CTES) AND KEY DATA ELEMENTS (KDES)

##### Shipping

For each traceability lot of food covered by the rule you ship, you must maintain records containing the following information and linking this information to the traceability lot:

| PTI KDE | FSMA 204 KDE | Comments |
|---------|--------------|----------|
| Traceability Lot Code (TLC) | The traceability lot code for the food | - Required - <br> AI (01) Case GTIN and AI (10) Case Batch/Lot <br> - Optional - <br> AI (00) Pallet SSCC <br> AI (13) Pack Date <br> AI (13) Harvest Date <br> AI (15) Best if Used by Date <br> AI (21) Serial Number |
| Quantity and UOM | The quantity and unit of measure of the packed food (e.g., 6 cases, 25 reusable plastic containers, 100 tanks, 200 pounds) | This is the number of cases shipped. |
| Product Description | The product description for the food. | This is the description for the Case, not the saleable unit in the case. Product description should include product name (including, if applicable, the brand name, commodity, and variety), packaging size, and packaging style. |
| Ship To Location | The location description for the immediate subsequent recipient (other than a transporter) of the food. | Ship-to location is the actual physical location where the product was shipped to. Location description includes: <br> • business name <br> • phone number <br> • physical location address (or geographic coordinates) <br> • city <br> • State <br> • Zip code for domestic locations and comparable information for foreign locations, including country |
| Ship From Location | The location description for the location from which you shipped the food. | Ship-from location is the actual physical location where product was shipped from. Location description includes: <br> business name <br> phone number <br> physical location address (or geographic coordinates) <br> city <br> State <br> zip code for domestic locations and comparable information for foreign locations, including country |
| Ship Date | The date you shipped the food | The ship date is the actual date when product leaves the physical ship-from location. |
| TLC Source or TLC Source Reference | The location description for the traceability lot code source, or the traceability lot code source reference | TLC Source is the actual physical location where TLC was assigned during initial packing or transformation. The description includes: <br> • business name <br> • phone number <br> • physical location address (or geographic coordinates) <br> • city <br> • State <br> • zip code for domestic locations and comparable information for foreign locations, including country. <br><br> Suppliers may provide a TLC Source Reference instead of a TLC Source. This could include: <br> • Web site <br> • FDA FFRN <br> • GLN from a registry available to FDA |
| Reference Document Type and Reference Document Number | The reference document type and reference document number. | May list more than one document. For example, a shipper may list the customer's purchase order, their outbound packing list, bill of lading, invoice and advanced ship notice numbers. By capturing the document type and number, the shipper can provide FDA with additional information about the shipment that may assist in an outbreak investigation. This information is useful for double-checking your traceability records. |


###### Shipping Spreadsheets

示例：

| **(1) TLC - GTIN** | **(1) TLC - Batch** | **Date Code\**** | **Date Type\**** | **VoiceCode\**** | **Pallet ID\***    | **TLC - Assigned By\**** | **(2) Qty & UOM** | **(3) Product Description**             | **(4) Ship-To Location**    | **Ship-To Location ID\**** | **(5) Ship-From Location**  | **Ship-From Location ID\**** | **(6) Ship Date** | **(7) TLC Source Reference GLN** | **(7) TLC Source Reference FFRN** | **(7) TLC Source Reference URL**                             | **(7) TLC Source Reference GGN** | **TLC Source Reference - Assigned By\**** | **(8) Ref Doc Type and Number** |
| ------------------ | ------------------- | ---------------- | ---------------- | ---------------- | ------------------ | ------------------------ | ----------------- | --------------------------------------- | --------------------------- | -------------------------- | --------------------------- | ---------------------------- | ----------------- | -------------------------------- | --------------------------------- | ------------------------------------------------------------ | -------------------------------- | ----------------------------------------- | ------------------------------- |
| 10333830000016     | 186                 | 230711           | PACK DATE        | 7557             | 103338389000000677 | Grower / Shipper         | 10 CS             | Ed's Iceberg Lettuce Wrapped - 24 heads | Customer A Ship To Location | 0071430010440              | Company Distribution Center | 0071430010556                | 7/17/23           |                                  |                                   | [https://www.EdsFreshFoodCo.com/](https://www.edsfreshfoodco.com/) |                                  | Grower / Shipper                          | INV-12005 Line 1                |
| 10333830000016     | 187                 | 230712           | PACK DATE        | 3765             | 103338389000000677 | Grower / Shipper         | 10 CS             | Ed's Iceberg Lettuce Wrapped - 24 heads | Customer A Ship To Location | 0071430010440              | Company Distribution Center | 0071430010556                | 7/17/23           |                                  |                                   | [https://www.EdsFreshFoodCo.com/](https://www.edsfreshfoodco.com/) |                                  | Grower / Shipper                          | INV-12005 Line 1                |
| 10333830000023     | 188                 | 230713           | PACK DATE        | 6084             | 103338389000000684 | Grower / Shipper         | 20 CS             | Ed's Iceberg Lettuce Wrapped - 12 heads | Customer B Ship To Location | 0071430010550              | Company Distribution Center | 0071430010556                | 7/18/23           |                                  | 123456789                         |                                                              |                                  | Grower / Shipper                          | INV-12345 Line 3                |
| 10333830000030     | 189                 | 230714           | PACK DATE        | 3105             | 103338389000000691 | Grower / Shipper         | 16 CS             | Ed's Lettuce Iceberg Wrapped - 16 heads | Customer B Ship To Location | 0071430010550              | Company Distribution Center | 0071430010556                | 7/19/23           | 0071430010204                    |                                   |                                                              |                                  | Grower / Shipper                          | INV-12372 Line 5                |

我们来解读一下上面的表格：

1. **(1) TLC - GTIN**: Global Trade Item Number，全球贸易项目代码。
   例如：10333830000016

2. **(1) TLC - Batch**: 批次号。
   例如：186

3. **Date Code**: 日期代码，通常是包装日期。
   例如：230711（可能表示2023年7月11日）

4. **Date Type**: 日期类型。
   例如：PACK DATE

5. **VoiceCode**: 语音代码，可能用于语音拣选系统。
   例如：7557

6. **Pallet ID**: 托盘标识符。
   例如：103338389000000677

7. **TLC - Assigned By**: TLC（Traceability Lot Code）分配者。
   例如：Grower / Shipper

8. **(2) Qty & UOM**: 数量和计量单位。
   例如：10 CS（10箱）

9. **(3) Product Description**: 产品描述。
   例如：Ed's Iceberg Lettuce Wrapped - 24 heads

10. **(4) Ship-To Location**: 收货地点。
    例如：Customer A Ship To Location

11. **Ship-To Location ID**: 收货地点标识符。
    例如：0071430010440

12. **(5) Ship-From Location**: 发货地点。
    例如：Company Distribution Center

13. **Ship-From Location ID**: 发货地点标识符。
    例如：0071430010556

14. **(6) Ship Date**: 发货日期。
    例如：7/17/23

15. **(7) TLC Source Reference GLN**: TLC来源参考的全球位置码。
    例如：0071430010204（在最后一行数据中）

16. **(7) TLC Source Reference FFRN**: TLC来源参考的FDA设施注册号。
    例如：123456789（在第三行数据中）

17. **(7) TLC Source Reference URL**: TLC来源参考的URL。
    例如：https://www.EdsFreshFoodCo.com/

18. **(7) TLC Source Reference GGN**: TLC来源参考的全球GAP编号。
    （在给定数据中没有示例）

19. **TLC Source Reference - Assigned By**: TLC来源参考分配者。
    例如：Grower / Shipper

20. **(8) Ref Doc Type and Number**: 参考文档类型和编号。
    例如：INV-12005 Line 1


##### Receiving

For each traceability lot of a food on the Food Traceability List you receive, you must maintain records containing the following information and linking this information to the traceability lot:

| PTI KDE | FSMA 204 KDE | Comments |
|---------|--------------|----------|
| Code (TLC) | The traceability lot code for the food. | - Required - <br> AI (01) Case GTIN and AI (10) Case Batch/Lot <br> - Optional - <br> AI (00) Pallet SSCC <br> AI (13) Pack Date <br> AI (13) Harvest Date <br> AI (15) Best if Used by Date <br> AI (21) Serial Number |
| Quantity and UOM | The quantity and unit of measure of the food (e.g., 6 cases, 25 reusable plastic containers, 100 tanks, 200 pounds) | This is the number of cases received |
| Product Description | The product description for the food. | This is the description for the case, not the saleable unit in the case. Product description should include: <br> • product name (including, if applicable, the brand name, commodity, and variety) <br> • packaging size <br> • packaging style. |
| Ship To Location | The location description for where the food was received | Ship-to location is the actual physical location where the product was shipped to. Location description includes: <br> • business name <br> • phone number <br> • physical location address (or geographic coordinates) <br> • city <br> • State <br> • zip code for domestic locations and comparable information for foreign locations, including country |
| Ship From Location | The location description for the immediate previous source (other than a transporter) for the food | Ship-from location is the actual physical location where the product was shipped from. Location description includes: <br> • business name <br> • phone number <br> • physical location address (or geographic coordinates) <br> • city <br> • State <br> • zip code for domestic locations and comparable information for foreign locations, including country |
| Receive Date | The date you received the food. | The receive date is the actual date when product reaches the physical ship-to location. |
| TLC Source or TLC Source Reference | The location description for the traceability lot code source, or the traceability lot code source reference | TLC Source location is the actual physical location where TLC was assigned during initial packing or transformation. The description includes: <br> • business name <br> • phone number <br> • physical location address (or geographic coordinates) <br> • city <br> • State <br> • zip code for domestic locations and comparable information for foreign locations, including country. <br><br> Suppliers may provide a TLC Source Reference instead of a TLC Source. This could include: <br> • Web site <br> • FDA FFRN <br> • GLN from a registry available to FDA |
| Reference Document Type and Reference Document Number | The reference document type and reference document number. | One may list one or more than one document. For example, a receiver may list their original purchase order, their supplier's packing list or bill of lading, invoice and advanced ship notice numbers. By capturing the document type and number, the receiver can provide FDA with additional information about the receipt that may assist in an outbreak investigation. This information is useful for double-checking your traceability records. |


###### Reveiving Spreadsheets

示例：

| **(a)(1) TLC - GTIN** | **(a)(1) TLC - Batch** | **(a)(1) TLC - Date\**** | **(a)(1) TLC - Date Type\**** | **(a)(1) TLC - SSCC\**** | **(b)(1) TLC - Assigned By** | **(a)(2) Qty & UOM** | **(a)(3) Product Description**        | **(a)(4) Immediate Previous Source (IPS) Location - (Shipped from Location)** | **(a)(5) Receive Location** | **(a)(6) Receive Date** | **(a)(7) TLC Source ReferenceGLN** | **(a)(7) TLC Source ReferenceFFRN** | **(a)(7) TLC Source ReferenceURL**               | **(a)(7) TLC Source ReferenceGGN** | **(b)(5) TLC Source Reference - Assigned By** | **(a)(8) Ref Doc** | **(a)(7) TLC Source Name**             | **(a)(7) TLC Source Street** | **(a)(7) TLC Source City** | **(a)(7) TLC Source State** | **(a)(7) TLC Source Country** | **(a)(7) TLC Source Zip Code** | **(a)(7) TLC Source Phone Number** |
| --------------------- | ---------------------- | ------------------------ | ----------------------------- | ------------------------ | ---------------------------- | -------------------- | ------------------------------------- | ------------------------------------------------------------ | --------------------------- | ----------------------- | ---------------------------------- | ----------------------------------- | ------------------------------------------------ | ---------------------------------- | --------------------------------------------- | ------------------ | -------------------------------------- | ---------------------------- | -------------------------- | --------------------------- | ----------------------------- | ------------------------------ | ---------------------------------- |
| 10333830000016        | 187                    | 230712                   | Pack Date                     | 103338389000000677       | Grower / Shipper             | 20 CS                | Iceberg Lettuce Wrapped - 24 heads    | Company Distribution Center                                  | My Company Main DC          | 7/18/23                 |                                    | 123456789                           |                                                  |                                    | Grower / Shipper                              | ASN-12005          | **Ed's Fresh Vegetables - Salinas**    | 3000 Salinas Hwy             | Salinas                    | CA                          | 93940                         | USA                            | (555)641-7777                      |
| 10333830000016        | 188                    | 230713                   | Pack Date                     | 103338389012345674       | Grower / Shipper             | 30 CS                | Iceberg Lettuce Wrapped - 24 heads    | Company Distribution Center                                  | My Company Main DC          | 7/18/23                 |                                    | 123456789                           |                                                  |                                    | Grower / Shipper                              | ASN-12005          | **Ed's Fresh Vegetables - Salinas**    | 3000 Salinas Hwy             | Salinas                    | CA                          | 93940                         | USA                            | (555)641-7777                      |
| 10123456000022        | 123456                 | 230715                   | Pack Date                     | 101234560000000008       | Grower / Shipper             | 40 CS                | Iceburg Lettuce Whole - Georgia Grown | Local Wholesaler of Georgia                                  | My Company Main DC          | 7/17/23                 |                                    |                                     | [http://tlclookupurl.com](http://tlclookup.com/) |                                    | Grower / Shipper                              | ASN-12005          | **LocaL Farm of Georgia Packhouse #3** | 80 Packinghouse Road         | Lagrange                   | GA                          | 30241                         | USA                            | (555) 444-1333                     |
| 10124523000020        | 654321                 | 230711                   | Pack Date                     | 101245230000000006       | Grower / Shipper             | 40 CS                | Iceburg Lettuce Whole                 | LocaL Farm of Georgia Packhouse #3                           | My Company Main DC          | 7/16/23                 | 0071430010228                      |                                     |                                                  |                                    | Grower / Shipper                              | Sales Receipt      | **LocaL Farm of Georgia Packhouse #3** | 80 Packinghouse Road         | Lagrange                   | GA                          | 30241                         | USA                            | (555) 444-1333                     |

上面的表格内容，我们来解读一下：

1. **(a)(1) TLC - GTIN**: Global Trade Item Number，全球贸易项目代码。
   例如：10333830000016

2. **(a)(1) TLC - Batch**: 批次号。
   例如：187

3. **(a)(1) TLC - Date**: 日期，通常是包装日期。
   例如：230712（可能表示2023年7月12日）

4. **(a)(1) TLC - Date Type**: 日期类型。
   例如：Pack Date（包装日期）

5. **(a)(1) TLC - SSCC**: Serial Shipping Container Code，系列货运容器代码。
   例如：103338389000000677

6. **(b)(1) TLC - Assigned By**: TLC（Traceability Lot Code）分配者。
   例如：Grower / Shipper

7. **(a)(2) Qty & UOM**: 数量和计量单位。
   例如：20 CS（20箱）

8. **(a)(3) Product Description**: 产品描述。
   例如：Iceberg Lettuce Wrapped - 24 heads（包装的冰山莴苣 - 24颗）

9. **(a)(4) Immediate Previous Source (IPS) Location - (Shipped from Location)**: 直接前一个来源位置（发货地）。
   例如：Company Distribution Center

10. **(a)(5) Receive Location**: 接收位置。
    例如：My Company Main DC

11. **(a)(6) Receive Date**: 接收日期。
    例如：7/18/23

12. **(a)(7) TLC Source ReferenceGLN**: TLC来源参考的全球位置码。
    例如：0071430010228

13. **(a)(7) TLC Source ReferenceFFRN**: TLC来源参考的FDA设施注册号。
    例如：123456789

14. **(a)(7) TLC Source ReferenceURL**: TLC来源参考的URL。
    例如：http://tlclookupurl.com

15. **(a)(7) TLC Source ReferenceGGN**: TLC来源参考的全球GAP编号。
    （在给定数据中没有示例）

16. **(b)(5) TLC Source Reference - Assigned By**: TLC来源参考分配者。
    例如：Grower / Shipper

17. **(a)(8) Ref Doc**: 参考文档。
    例如：ASN-12005

18. **(a)(7) TLC Source Name**: TLC来源名称。
    例如：Ed's Fresh Vegetables - Salinas

19. **(a)(7) TLC Source Street**: TLC来源街道地址。
    例如：3000 Salinas Hwy

20. **(a)(7) TLC Source City**: TLC来源城市。
    例如：Salinas

21. **(a)(7) TLC Source State**: TLC来源州。
    例如：CA

22. **(a)(7) TLC Source Country**: TLC来源国家。
    例如：USA

23. **(a)(7) TLC Source Zip Code**: TLC来源邮政编码。
    例如：93940

24. **(a)(7) TLC Source Phone Number**: TLC来源电话号码。
    例如：(555)641-7777


##### Transformation

This can include repacking, commingling when items from various original bins, packs, etc. are combined, or cutting, trimming, etc., to create a fresh-cut product (i.e., minimal processing).
For each new traceability lot of food, you produce through transformation, you must maintain records containing the following information and linking this information to the new traceability lot code:

| PTI KDE | FSMA 204 KDE | Comments |
|---------|--------------|----------|
| FTL Food Used TLC | The traceability lot code for the food used | Minimum attributes:<br>AI (01) Case GTIN and AI (10) Case Batch/Lot<br>Optional attributes:<br>AI (00) Pallet SSCC<br>AI (13) Pack Date<br>AI (13) Harvest Date<br>AI (15) Best if Used by Date<br>AI (21) Serial Number |
| FTL Food Used Product Description | The product description for the food to which the traceability lot code applies. | This is the description for the Case, not the saleable unit in the case. Product description should include:<br>• product name (including, if applicable, the brand name, commodity, and variety)<br>• packaging size<br>• packaging style. |
| FTL Food Used Qty and FTL Food Used UOM | For each traceability lot used, the quantity and unit of measure of the food used from that lot. | Could be a variety of UOMs including cases, lbs., bins, etc. |
| FTL Food Produced New TLC | The new traceability lot code for the food. | Minimum attributes:<br>AI (01) Case GTIN and AI (10) Case Batch/Lot<br>Optional attributes:<br>AI (00) Pallet SSCC<br>AI (13) Pack Date<br>AI (13) Harvest Date<br>AI (15) Best if Used by Date<br>AI (21) Serial Number |
| Transformation Location | The location description for where you transformed the food (i.e., the traceability lot code source), and (if applicable) the traceability lot code source reference. | TLC Source is the actual physical location where TLC was assigned during initial packing or transformation. The description includes:<br>• business name<br>• phone number<br>• physical location address (or geographic coordinates),<br>• city<br>• State<br>• zip code for domestic locations and comparable information for foreign locations, including country.<br><br>Suppliers may provide a TLC Source Reference instead of a TLC Source. This could include:<br>• Web site<br>• FDA FFRN<br>• GLN from a registry available to FDA |
| Date Transformed | The date transformation activities were completed. | This is the actual date when transformation was completed. |
| FTL Food Produced Product Description | The product description for the food. | This is the description for the Case, not the saleable unit in the case. Product description should include:<br>• product name (including, if applicable, the brand name, commodity, and variety)<br>• packaging size<br>• packaging style. |
| FTL Food Produced Quantity and FTL Food Produced UOM | The quantity and unit of measure of the food (e.g., 6 cases, 25 reusable plastic containers, 100 tanks, 200 pounds). | Usually this is expressed in the number of cases. |
| Reference Document Type and Reference Document Number | The reference document type and reference document number. | One may list one or more than one document. For example, a receiver may list their original purchase order, their supplier's packing list or bill of lading, invoice and advanced ship notice numbers. By capturing the document type and number, the receiver can provide FDA with additional information about the receipt that may assist in an outbreak investigation. This information is useful for double-checking your traceability records. |


###### Transformation Spreadsheets

示例：

| **Reference Document Type**   | **Reference Document Number for Transformation Event** | **FTL Food Used TLC - GTIN** | **FTL Food Used TLC - Batch** | **FTL Food Used TLC - SSCC** | **FTL Food Used TLC - Date** | **FTL Food Used TLC - Date Type** | **FTL Food Used Qty** | **FTL Food Used UOM** | **FTL Food Used Product Description** | **FTL Food Produced TLC - GTIN** | **FTL Food Produced TLC - Batch** | **FTL Food Produced TLC - SSCC** | **FTL Food Produced TLC - Date** | **FTL Food Produced TLC - Date Type** | **Transform Location- TLCS** | **TLCS Reference GLN** | **TLCS Reference FFRN** | **TLCS Reference URL** | **TLCS Reference GGN** | **Date Transformed** | **FTL Food Produced Product Description** | **FTL Food Produced Qty** | **FTL Food Produced UOM** |
| ----------------------------- | ------------------------------------------------------ | ---------------------------- | ----------------------------- | ---------------------------- | ---------------------------- | --------------------------------- | --------------------- | --------------------- | ------------------------------------- | -------------------------------- | --------------------------------- | -------------------------------- | -------------------------------- | ------------------------------------- | ---------------------------- | ---------------------- | ----------------------- | ---------------------- | ---------------------- | -------------------- | ----------------------------------------- | ------------------------- | ------------------------- |
| Re-pack Order, Packout Number | RPO-12345                                              | 30071430011059               | 2071231339                    | 123456789012345675           | 8/24/23                      | Pack Date                         | 100                   | Bags                  | Romaine Hearts- 3 Pack                | 30071430011066                   | 2074562447                        | 123456789012345883               | 9/10/23                          | Best Before Date                      | **DEF Packers**              |                        | 111111111111            |                        |                        | 8/31/23              | Romaine- 4 pack                           | 20                        | Cases                     |

解读：

这个表格描述了一个食品转换（Transformation）事件的详细信息。它包含了输入食品（FTL Food Used）和输出食品（FTL Food Produced）的信息，以及转换过程的相关细节。

1. 参考文档信息：
   - Reference Document Type: 参考文档类型（例如：Re-pack Order, Packout Number）
   - Reference Document Number: 参考文档编号（例如：RPO-12345）

2. 输入食品信息（FTL Food Used）：
   - TLC (Traceability Lot Code) 信息：包括GTIN, Batch, SSCC, Date, Date Type
   - 数量和单位：Qty和UOM
   - 产品描述

3. 输出食品信息（FTL Food Produced）：
   - TLC信息：包括GTIN, Batch, SSCC, Date, Date Type
   - 产品描述
   - 数量和单位：Qty和UOM

4. 转换位置信息：
   - Transform Location - TLCS (Traceability Lot Code Source)
   - TLCS参考信息：包括GLN, FFRN, URL, GGN

5. 转换日期：Date Transformed

这个表格按照FSMA 204规定的转换事件记录要求，包含了输入和输出食品的可追溯批次代码（TLC）、数量、描述等关键信息，以及转换地点和日期等重要数据，有助于在需要时快速追踪和定位食品来源。


### Supply Chain Role Considerations

#### Persons who grow, harvest, cool, and initially pack covered foods

#### PERSONS WHO RE-PACK OR TRANSFORM COVERED FOODS

Transformation means an event in a food's supply chain that involves manufacturing or processing a food or changing a food (e.g., by commingling, repacking, or relabeling) or its packaging or packing, when the output is a food on the Food Traceability List. Transformation does not include the initial packing of a food or activities preceding that event (e.g., harvesting, cooling).

Transformation CTE: for foods used in transformation:

- For each FTL food used in a transformation the following information should be recorded:
  * Traceability lot code
  * Product description
  * Quantity and Unit of Measure
- For each non-FTL food used in a transformation one may record the same or similar information as an FTL food, if available. This is not required by the rule, but is a best practice to capture all ingredients, whether or not they have a GTIN and Lot or not.

Foods produced through transformation.

* For each FTL food produced through transformation, the following information should be recorded:
  * Traceabilitylotcode
  * Product description
  * Quantity and Unit of Measure
  * Transformationdate
  * The location where you transformed the food. This is also known as the TLC Source and may be pointed to using TLC Source Reference.
  * Referenced Document Type and Number.
  * For each traceability lot produced through transformation of a raw agricultural commodity (other than a food obtained from a fishing vessel) on the Food Traceability List that was not initially packed prior to your transformation of the food, you must maintain records containing the information specified in § 1.1330(a) Initial Packing or (c) Initial Packing of foods from exempt entities, and, if the raw agricultural commodity is sprouts, the information specified in § 1.1330(b) Initial Packing of Sprouts.

##### Sortable Spreadsheets

The sortable spreadsheet for Transformation is a bit challenging since it could **contain multiple inputs and multiple outputs**. One approach is to list the inputs first, then the outputs. Another is to connect each input with each output.

Either way, it would be good to note this in your traceability plan and on the header of your sortable spreadsheet.

Please see the following example of a sortable spreadsheet [here](https://producetraceability.org/resources/#sortable).


### 参考资料

#### [PTI FSMA 204 Electronic Sortable Spreadsheet Templates (2024)](https://producetraceability.org/resources/#sortable)

- [Harvester/Cooler/Initial Packer/Transformer](https://producetraceability.org/wp-content/uploads/2023/09/Harv-Init-Pk-Cool-Trans-2.xlsx)
- [Receiver](https://producetraceability.org/wp-content/uploads/2023/09/PTI-Sortable-Spreadsheet-Receiving-2-1.xlsx)
- [Shipper](https://producetraceability.org/wp-content/uploads/2023/09/PTI-Sortable-Spreadsheet-Shipping-2-1.xlsx)
- [Final Distributor](https://producetraceability.org/wp-content/uploads/2023/09/PTI-Sortable-Spreadsheet-DC-to-Store-2-1.xlsx)
- [Retail Store](https://producetraceability.org/wp-content/uploads/2023/09/PTI-Sortable-Spreadsheet-Retail-Store-2-1.xlsx)
- [Foodservice Operation/Restaurant](https://producetraceability.org/wp-content/uploads/2023/09/PTI-Sortable-Spreadsheet-Foodservice-2-1.xlsx)


#### PTI FSMA 204 Resources

https://producetraceability.org/resources/#FSMA

https://producetraceability.org/wp-content/uploads/2024/02/PTI-FSMA-204-TLC-Source-and-Reference-Guidance-final-1.pdf

https://producetraceability.org/wp-content/uploads/2024/06/Produce-Traceability-Initiative-PTI-Best-Practices-for-Formatting-Case-Labels-v2.2.pdf


#### GS1 Digital Link

GS1 Digital Link 是一项标准，用于将实体产品与网络上的数字信息连接。主要特点包括：

1. **统一标识符**：将 GS1 标识符（如 GTIN）转换为标准 URL 格式。
   例：`https://example.com/gtin/614141123452`

2. **多功能性**：一个 URL 可包含多种 GS1 标识符。
   例：`https://example.com/gtin/614141123452/lot/ABC123/ser/XYZ789`

3. **可扫描性**：可编码为二维码或条形码，便于设备扫描。

4. **动态信息访问**：提供实时、定制的产品信息。

5. **多应用支持**：支持产品信息、真伪验证、召回信息等多种应用。

6. **标准化和互操作性**：促进不同系统间的互操作。

7. **消费者互动**：简化消费者获取产品信息的方式。

8. **供应链可见性**：支持产品追踪和食品安全追溯。

9. **未来兼容性**：考虑物联网等未来技术发展。

GS1 Digital Link 适用于食品安全追溯、产品信息管理等领域，符合 FSMA 204 等法规要求。

详细信息：[GS1 Digital Link 官方文档](https://www.gs1.org/standards/gs1-digital-link)


#### Others

GS1 EPCIS STANDARD：https://byteally.com/insights/supply-chain/gs1-epcis-standard/

[更多参考链接](docs/电子数据格式标准.md)。


### Tips

#### FSMA 204 标准下 Transformation CTE 中的多个输入/输出 TLC

根据 FSMA 204 的标准，对于 Transformation（转换）这种类型的关键追溯事件（CTE），可能需要在一个事件中记录多个被使用的食品的可追溯批次代码（TLC）。例如，一个成品可能需要消耗多种原材料。还可能需要记录多个输出 TLC。

#### EPCIS 事件类型

EPCIS（Electronic Product Code Information Services）定义了几种主要的事件类型，用于捕获供应链中不同类型的活动：

1. **ObjectEvent（对象事件）**
   - 用途：跟踪单个物品或一批物品的状态变化。
   - 示例：产品被制造、销售、运输或丢弃。

2. **AggregationEvent（聚合事件）**
   - 用途：记录物品被添加到或从更大的容器中移除。
   - 示例：将多个产品装入一个箱子，或从箱子中取出产品。

3. **TransactionEvent（交易事件）**
   - 用途：将物品与业务交易相关联。
   - 示例：将产品与销售订单或发票关联。

4. **TransformationEvent（转换事件）**
   - 用途：记录输入物品被转换或组合成输出物品的过程。
   - 示例：原材料被加工成成品。

5. **QuantityEvent（数量事件）**（在 EPCIS 2.0 中已弃用）
   - 用途：报告特定类型物品的数量，而不是单个物品。
   - 示例：库存盘点。

6. **AssociationEvent（关联事件，EPCIS 2.0 新增）**
   - 用途：记录物品与其他物品或信息的关联。
   - 示例：将传感器附加到集装箱上。

这些事件类型允许企业详细记录和共享供应链中的各种活动，提高可见性和可追溯性。每种事件类型都有特定的数据结构和字段，用于捕获相关的详细信息，如时间、地点、涉及的物品等。


#### ILMD

https://gist.github.com/wubuku/525a892c07a9bc56b59572ee8314dec0

#### Event Capture Notification for Sending EPCIS Events

https://help.sap.com/doc/saphelp_aii710/7.1/en-US/48/d1cbfe90d75430e10000000a42189b/content.htm?no_cache=true

#### JSON-LD

JSON-LD，全称为 JavaScript Object Notation for Linked Data，是一种基于 JSON 的轻量级数据交换格式，旨在使数据在 Web 上更易于互操作。

JSON-LD 的主要特点：
1. 兼容性：JSON-LD 基于 JSON，因此与现有的 JSON 解析器和生成器兼容。
2. 灵活性：支持在单个文档中混合表示互联数据和非互联数据。
3. 可扩展性：允许使用自定义词汇表来扩展其功能。
4. 语义化：不仅仅是一种数据交换格式，还支持语义化数据，使其对机器也有意义。

JSON-LD 的应用场景：

* Web 服务：构建可互操作的 Web 服务。
* 语义网：在语义网中组织和表示数据。
* 数据集成：整合来自不同源的数据。
* 富互联网应用程序：处理大量交互式数据。

#### GS1 系统

参考：http://www.gs1cn.org/Knowledge/GS1System2

编码体系：http://www.gs1cn.org/Knowledge/GS1System/bmtx

GTIN 解析：https://www.lspedia.com/zh-cn/blog/anatomy-of-a-gtin


--------

## Fresh Fruit and Vegetable Traceability Guideline

https://www.gs1.org/standards/fresh-fruit-and-vegetable-traceability-guideline/current-standard

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


#### 术语表

https://www.gs1.org/standards/fresh-fruit-and-vegetable-traceability-guideline/current-standard#A-Glossary+A-1-Glossary-of-terms

部分术语：

1. GLN (Global Location Number):
   - 全球位置编码
   - 由 GS1 分配的唯一标识符，用于识别公司、工厂、仓库等物理位置或法律实体
   - 在供应链中广泛使用，用于精确定位和识别参与者
2. FFRN (FDA Food Facility Registration Number):
   - FDA 食品设施注册号
   - 由美国食品和药物管理局 (FDA) 分配给食品生产、加工、包装或储存设施的唯一标识号
   - 根据生物恐怖主义法案要求，所有处理食品的设施都必须向 FDA 注册并获得此号码
3. URL (Uniform Resource Locator):
   - 统一资源定位符
   - 在这个上下文中，可能指向包含设施或公司信息的网页地址
   - 用于提供额外的在线信息或验证来源
4. GGN (GLOBALG.A.P. Number):
   - GLOBALG.A.P. 编号
   - 由 GLOBALG.A.P. 组织分配的 13 位唯一标识符
   - 用于识别通过 GLOBALG.A.P. 认证的生产者或公司
   - 主要用于农业和水产养殖领域，表示符合良好农业规范标准
5. GTIN (Global Trade Item Number):
   - 全球贸易项目代码
   - 用于唯一标识贸易项目的代码，通常用于条形码中
   - 有多种格式，如 GTIN-12、GTIN-13、GTIN-14 等
6. TLC (Traceability Lot Code):
   - 可追溯性批次代码
   - 用于识别特定批次或批量的产品
7. CTE (Critical Tracking Event):
   - 关键追踪事件
   - 在供应链中的重要节点，需要记录详细信息以确保可追溯性
8. KDE (Key Data Element):
   - 关键数据元素
   - 与 CTE 相关的重要信息，需要被记录和传递
9. FSMA (Food Safety Modernization Act):
   - 食品安全现代化法案
   - 美国的一项重要食品安全法规
10. FTL (Food Traceability List):
    - 食品可追溯性清单
    - FDA 规定的需要特别关注可追溯性的食品清单
11. SSCC (Serial Shipping Container Code):
    - 系列货运容器代码
    - 用于唯一标识物流单元的 18 位数字代码
12. UOM (Unit of Measure):
    - 计量单位
    - 用于描述产品数量的标准单位

##### GIAI 和 GRAI

GIAI 和 GRAI 是 GS1 标准中的两种标识符，用于不同的资产管理场景：
1. GIAI（Global Individual Asset Identifier）：全球个体资产标识符，用于唯一标识单个资产。它由 GS1 公司前缀和个体资产参考组成，适用于固定资产的管理，例如计算机、办公桌或运输设备。
2. GRAI（Global Returnable Asset Identifier）：全球可回收资产标识符，用于标识可重复使用的资产，如托盘、集装箱等。GRAI 由 GS1 公司前缀、资产类型和可选的序列号组成，帮助企业跟踪和管理这些可回收资产。

这些标识符在供应链管理中起到重要作用，确保资产的唯一性和可追溯性。

##### GDSN（Global Data Synchronization Network）与 GTIN（Global Trade Item Number）

GTIN（全球贸易项目代码）：
GTIN 是用于唯一标识贸易项目的代码，通常用于条形码中。GTIN 可以有多种格式，包括 GTIN-12、GTIN-13、GTIN-14 等，具体取决于应用场景。每个 GTIN 都是唯一的，确保每个产品在全球范围内都能被唯一识别。

GDSN（全球数据同步网络）：
GDSN 是一个全球性的网络，用于在供应链中的不同参与者之间同步和共享高质量的产品数据。通过 GDSN，企业可以确保其产品信息在全球范围内的一致性和准确性。GDSN 通过数据池（Data Pools）运作，这些数据池允许企业上传、维护和共享产品信息。
