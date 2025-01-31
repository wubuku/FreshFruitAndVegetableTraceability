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
  * Traceability lot code
  * Product description
  * Quantity and Unit of Measure
  * Transformation date
  * The location where you transformed the food. This is also known as the TLC Source and may be pointed to using TLC Source Reference.
  * Referenced Document Type and Number.
  * For each traceability lot produced through transformation of a raw agricultural commodity (other than a food obtained from a fishing vessel) on the Food Traceability List that was not initially packed prior to your transformation of the food, you must maintain records containing the information specified in § 1.1330(a) Initial Packing or (c) Initial Packing of foods from exempt entities, and, if the raw agricultural commodity is sprouts, the information specified in § 1.1330(b) Initial Packing of Sprouts.

> § 1.1330 What records must I keep when I am performing the initial packing of a raw agricultural commodity (other than a food obtained from a fishing vessel) on the Food Traceability List?
> 
> https://www.ecfr.gov/current/title-21/section-1.1330


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


#### CTE 中的产品描述

CTE 表格片段实例：

| Product Description | Quantity Packed | UOM Packed |
|-------------------|-----------------|------------|
| Romaine Hearts- 3 Pack | 100 | Cases |


1. Product Description（产品描述）
- 值：Romaine Hearts- 3 Pack。
- 含义：这里的 "3 Pack" 可能表示这是一个标准包装单位（一箱里装3包）

1. Quantity Packed（包装数量）
- 值：100
- 含义：表示包装了 100 个单位。

1. UOM Packed（包装计量单位）
- 值：Cases
- 含义：单位是"箱"


对于这样一个产品：

> If you have a six-pack of 12oz soda cans you would have quantityIncluded=12, quantityUomId=oz, piecesIncluded=6.

产品描述可简写为：

`Soda - 12oz - 6-Pack`


解析这个格式：
- Soda - 基础产品名称
- 12oz - 每单品的容量规格
- 6-Pack - 包装单位（表示每包6罐）

这种格式在行业中很常见，遵循了 "主产品 + 规格 + 包装单位" 的写法。

我们可以考虑在主要元素之间用" - "（连字符加空格）分隔。而在"6-Pack"中的连字符不加空格，因为这是一个复合词


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

--------

## 术语表

Fresh Fruit and Vegetable Traceability Guideline 中的术语：

https://www.gs1.org/standards/fresh-fruit-and-vegetable-traceability-guideline/current-standard#A-Glossary+A-1-Glossary-of-terms


### 供应链管理领域常见缩写词

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


### RAC 和 RTE

在食品工业中，特别是在食品安全和可追溯性的语境下：

- RAC (Raw Agricultural Commodity)
  - 原始农产品
  - 指未经加工的农产品，如刚从田地收获的水果、蔬菜等
  - 例如：新鲜采摘的生菜、未清洗的胡萝卜等

- RTE (Ready-To-Eat)
  - 即食食品
  - 指可以直接食用，无需进一步烹饪或加工的食品
  - 例如：预包装沙拉、切好的水果等

这两个术语在食品安全监管中经常出现，特别是在 FSMA（食品安全现代化法案）的背景下：

- RAC 通常需要更严格的初始处理和清洗程序
- RTE 产品因为可以直接食用，所以在加工过程中需要更高的卫生标准和更严格的控制措施。


### GIAI 和 GRAI（资产标识）

GIAI 和 GRAI 是 GS1 标准中的两种标识符，用于不同的资产管理场景：
1. GIAI（Global Individual Asset Identifier）：全球个体资产标识符，用于唯一标识单个资产。它由 GS1 公司前缀和个体资产参考组成，适用于固定资产的管理，例如计算机、办公桌或运输设备。
2. GRAI（Global Returnable Asset Identifier）：全球可回收资产标识符，用于标识可重复使用的资产，如托盘、集装箱等。GRAI 由 GS1 公司前缀、资产类型和可选的序列号组成，帮助企业跟踪和管理这些可回收资产。

这些标识符在供应链管理中起到重要作用，确保资产的唯一性和可追溯性。

### GDSN（Global Data Synchronization Network）与 GTIN（Global Trade Item Number）

GTIN（全球贸易项目代码）：
GTIN 是用于唯一标识贸易项目的代码，通常用于条形码中。GTIN 可以有多种格式，包括 GTIN-12、GTIN-13、GTIN-14 等，具体取决于应用场景。每个 GTIN 都是唯一的，确保每个产品在全球范围内都能被唯一识别。

GDSN（全球数据同步网络）：
GDSN 是一个全球性的网络，用于在供应链中的不同参与者之间同步和共享高质量的产品数据。通过 GDSN，企业可以确保其产品信息在全球范围内的一致性和准确性。GDSN 通过数据池（Data Pools）运作，这些数据池允许企业上传、维护和共享产品信息。


### ASN (Advanced Shipping Notice)

#### 基本概念

ASN 是发货方在发货前或发货时向收货方发送的详细电子通知。

#### 主要功能

1. **提前通知**
   - 告知收货方货物即将到达
   - 提供预计到达时间（ETA）
   - 便于收货方做好接收准备

2. **详细信息传递**
   - 货物清单和数量
   - 包装信息（包装层次、类型）
   - 运输信息（承运人、运单号）
   - 订单参考信息

3. **供应链协同**
   - 提高收货效率
   - 减少等待时间
   - 优化仓库作业计划

#### ASN 包含的关键信息

1. **基础信息**
   - ASN 编号
   - 发送日期和时间
   - 预计到达日期（ETA）

2. **参与方信息**
   - 发货方信息
   - 收货方信息
   - 承运人信息

3. **参考文档**
   - 采购订单号
   - 销售订单号
   - 装运单号
   - 集装箱号

4. **货物信息**
   - 产品编号（SKU）
   - 批次号（Batch/Lot）
   - 序列号（Serial Number）
   - 数量和单位
   - 包装层次结构

#### ASN 的业务流程

1. **发送时机**
   ```
   订单确认 -> 拣货完成 -> 发送ASN -> 装车发运 -> 货物在途 -> 收货
   ```

2. **处理流程**
   ```
   ASN生成 -> EDI传输 -> 系统接收 -> 数据验证 -> 入库准备
   ```

#### 实施建议

1. **系统集成**
   - 与 WMS（仓库管理系统）集成
   - 与 TMS（运输管理系统）集成
   - 与 ERP 系统集成

2. **数据标准**
   - 使用标准的 EDI 格式（如 EDI 856）
   - 采用统一的编码标准（如 GS1）
   - 确保数据的准确性和完整性

3. **业务规范**
   - 制定 ASN 发送时间要求
   - 规范 ASN 内容格式
   - 建立异常处理机制

#### ASN 的优势

1. **对收货方**
   - 提前了解到货信息
   - 优化劳动力安排
   - 提高收货效率
   - 减少文档处理时间

2. **对发货方**
   - 提高发货准确性
   - 减少沟通成本
   - 提升客户满意度
   - 便于跟踪管理

3. **对供应链整体**
   - 提高透明度
   - 减少库存积压
   - 优化物流效率
   - 降低运营成本

#### 常见挑战和解决方案

1. **数据准确性**
   - 问题：ASN 信息与实际发货不符
   - 解决：建立数据验证机制，实施条码扫描

2. **及时性**
   - 问题：ASN 发送延迟
   - 解决：自动化 ASN 生成和发送流程

3. **系统集成**
   - 问题：不同系统间数据格式不兼容
   - 解决：采用标准化的 EDI 格式，建立数据映射规则

### Bill of Lading (提单/装船单)

#### 基本概念

Bill of Lading (B/L) 是一个非常重要的运输单据，它具有三重法律功能：
1. **收据凭证**：证明承运人已收到货物
2. **货物权利凭证**：代表货物的所有权
3. **运输合同**：证明承运人和托运人之间的运输协议

#### 主要类型

1. 海运提单 (Ocean/Marine B/L)
- **可转让提单** (Negotiable B/L)
  - 可以通过背书转让
  - 常用于信用证交易

- **不可转让提单** (Non-negotiable B/L)
  - 也称为 Sea Waybill
  - 直接指定收货人
  - 适用于信任度高的交易

2. 其他类型
- **清洁提单** (Clean B/L)
  - 表明货物状况良好
  - 没有不良批注

- **不清洁提单** (Claused/Dirty B/L)
  - 标注了货物的缺陷或损坏
  - 可能影响信用证支付

#### 关键信息项

1. **基本信息**
   - 提单号 (B/L Number)
   - 发行日期
   - 发行地点

2. **参与方信息**
   - 托运人 (Shipper)
   - 收货人 (Consignee)
   - 通知方 (Notify Party)

3. **货物信息**
   - 货物描述
   - 包装数量
   - 重量/体积
   - 标记和编号

4. **运输信息**
   - 装货港 (Port of Loading)
   - 卸货港 (Port of Discharge)
   - 船名和航次
   - 运费支付条款

#### 提单流转过程

```
托运人 -> 承运人签发提单 -> 银行处理 -> 收货人凭提单提货
```

#### 实际应用

1. **贸易融资**
```
托运人 -> 银行开立信用证 -> 提交提单 -> 银行付款 -> 收货人赎单
```

2. **货物交付**
```
承运人签发提单 -> 收货人凭正本提单 -> 承运人核验 -> 放行货物
```

#### 注意事项

1. **签发时注意**
   - 核实货物状况
   - 确认装运细节
   - 正确填写所有信息

2. **使用过程中**
   - 妥善保管正本提单
   - 及时办理背书转让
   - 注意提单条款约定

3. **提货时注意**
   - 核实提单真实性
   - 确认提单完整性
   - 及时办理提货手续

#### 常见问题和解决方案

1. **提单遗失**
   - 立即通知相关方
   - 办理遗失声明
   - 提供担保后提货

2. **提单瑕疵**
   - 及时与承运人沟通
   - 必要时换单处理
   - 记录相关情况

3. **交单延迟**
   - 考虑电放提单
   - 提供银行担保
   - 加快单据传递

#### 最佳实践建议

1. **文件管理**
   - 建立提单管理制度
   - 指定专人负责
   - 做好存档记录

2. **风险控制**
   - 审慎选择合作方
   - 核实单据真实性
   - 及时跟踪货物状态

3. **流程优化**
   - 考虑采用电子提单
   - 简化操作流程
   - 加强人员培训
