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
|          | GTIN                 |                   | 9504000219109* |
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


### 参考资料

#### ILMD

https://gist.github.com/wubuku/525a892c07a9bc56b59572ee8314dec0

#### Event Capture Notification for Sending EPCIS Events

https://help.sap.com/doc/saphelp_aii710/7.1/en-US/48/d1cbfe90d75430e10000000a42189b/content.htm?no_cache=true

#### Others

GS1 EPCIS STANDARD：https://byteally.com/insights/supply-chain/gs1-epcis-standard/
