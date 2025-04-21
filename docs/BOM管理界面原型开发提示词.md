# BOM管理界面原型开发提示词

## 项目概述

创建一个产品BOM（物料清单）管理界面原型，使用户能够以可视化方式创建、编辑和维护产品的物料清单结构。该界面应提供直观的拖放功能，支持查看和编辑BOM关系属性，并防止创建循环依赖关系。

## 核心功能需求

1. **产品浏览面板**：
   - 右侧显示可搜索的产品列表
   - 支持按产品名称/ID过滤
   - 显示产品基本信息（ID、名称、类型、单位）

2. **BOM可视化画布**：
   - 支持将产品拖入作为根节点或子节点
   - 自动展开已有BOM的子产品
   - 树形结构清晰展示父子关系

3. **关系属性编辑**：
   - 添加子产品时弹窗输入关系属性
   - 点击产品或关系线显示属性面板
   - 可编辑quantity和scrap factor属性

4. **验证逻辑**：
   - 防止创建循环依赖关系
   - 数据有效性验证
   - 用户操作反馈

## 数据模型

### 产品(Product)
```typescript
interface Product {
  productId: string;        // 产品唯一标识符
  productName: string;      // 产品名称
  description: string;      // 产品描述
  productTypeId: string;    // 产品类型ID
  internalName: string;     // 内部名称
  quantityUomId: string;    // 数量单位ID
  isVirtual?: boolean;      // 是否为虚拟产品，在本原型中先忽略
  isVariant?: boolean;      // 是否为变体产品，在本原型中先忽略
}
```

### 产品关系(ProductAssoc)
```typescript
interface ProductAssoc {
  productId: string;             // 父产品ID
  productIdTo: string;           // 子产品ID
  productAssocTypeId: string;    // 关系类型，在本原型中先硬编码为 MANUF_COMPONENT
  fromDate: string;              // 关系有效起始日期，在本原型中先硬编码为 2024-01-01 00:00:00
  quantity: number;              // 需要的子产品数量
  scrapFactor?: number;          // 报废率(百分比)，默认为 0
  sequenceNum?: number;          // 序号，自动根据父子关系生成
  routingWorkEffortId?: string;  // 工艺路线任务ID，默认为 null。本原型中先忽略
}
```

## 模拟数据

从提供的XML文件中提取的模拟数据:

```javascript
// 产品类型数据
const productTypes = [
  { id: "RAW_MATERIAL", description: "原材料" },
  { id: "WIP", description: "半成品" },
  { id: "FINISHED_GOOD", description: "成品" }
];

// 单位数据
const uomData = [
  { id: "GRM", description: "克" },
  { id: "PCE", description: "个" },
  { id: "MLT", description: "毫升" }
];

// 产品数据
const products = [
  // 原材料
  {
    productId: "RAW_FLOUR",
    productName: "Cake Flour",
    description: "Standard Cake Flour",
    internalName: "CAKE_FLOUR",
    productTypeId: "RAW_MATERIAL",
    quantityUomId: "GRM"
  },
  {
    productId: "RAW_EGG",
    productName: "Fresh Eggs",
    description: "Fresh Chicken Eggs",
    internalName: "FRESH_EGG",
    productTypeId: "RAW_MATERIAL",
    quantityUomId: "PCE"
  },
  {
    productId: "RAW_SUGAR",
    productName: "White Sugar",
    description: "Refined White Sugar",
    internalName: "WHITE_SUGAR",
    productTypeId: "RAW_MATERIAL",
    quantityUomId: "GRM"
  },
  {
    productId: "RAW_MILK",
    productName: "Whole Milk",
    description: "Fresh Whole Milk",
    internalName: "WHOLE_MILK",
    productTypeId: "RAW_MATERIAL",
    quantityUomId: "MLT"
  },
  {
    productId: "RAW_CHOC",
    productName: "Dark Chocolate",
    description: "Premium Dark Chocolate",
    internalName: "DARK_CHOCOLATE",
    productTypeId: "RAW_MATERIAL",
    quantityUomId: "GRM"
  },
  {
    productId: "RAW_CREAM",
    productName: "Whipping Cream",
    description: "Fresh Whipping Cream",
    internalName: "WHIPPING_CREAM",
    productTypeId: "RAW_MATERIAL",
    quantityUomId: "MLT"
  },
  {
    productId: "RAW_FRUIT",
    productName: "Mixed Fruits",
    description: "Mixed Berries (Strawberry/Blueberry)",
    internalName: "MIXED_FRUIT",
    productTypeId: "RAW_MATERIAL",
    quantityUomId: "GRM"
  },
  
  // 半成品
  {
    productId: "WIP_CAKE_BASE",
    productName: "Cake Base",
    description: "Standard Cake Base",
    internalName: "CAKE_BASE",
    productTypeId: "WIP",
    quantityUomId: "PCE"
  },
  {
    productId: "WIP_CREAM_FILL",
    productName: "Cream Filling",
    description: "Whipped Cream Filling",
    internalName: "CREAM_FILLING",
    productTypeId: "WIP",
    quantityUomId: "PCE"
  },
  {
    productId: "WIP_CHOC_SAUCE",
    productName: "Chocolate Sauce",
    description: "Chocolate Sauce",
    internalName: "CHOCOLATE_SAUCE",
    productTypeId: "WIP",
    quantityUomId: "PCE"
  },
  
  // 成品
  {
    productId: "CHOC_CAKE_VIRTUAL",
    productName: "Chocolate Cake",
    description: "Chocolate Cake (Virtual Product)",
    internalName: "CHOCOLATE_CAKE",
    productTypeId: "FINISHED_GOOD",
    quantityUomId: "PCE",
    isVirtual: true
  },
  {
    productId: "CHOC_CAKE_STD",
    productName: "Standard Chocolate Cake",
    description: "Standard Chocolate Cake",
    internalName: "STANDARD_CHOC_CAKE",
    productTypeId: "FINISHED_GOOD",
    quantityUomId: "PCE",
    isVariant: true
  },
  {
    productId: "CHOC_CAKE_DLX",
    productName: "Deluxe Chocolate Cake",
    description: "Deluxe Chocolate Cake",
    internalName: "DELUXE_CHOC_CAKE",
    productTypeId: "FINISHED_GOOD",
    quantityUomId: "PCE",
    isVariant: true
  },
  {
    productId: "CHOC_CAKE_MINI",
    productName: "Mini Chocolate Cake",
    description: "Mini Chocolate Cake",
    internalName: "MINI_CHOC_CAKE",
    productTypeId: "FINISHED_GOOD",
    quantityUomId: "PCE",
    isVariant: true
  }
];

// BOM关系数据
const productAssocs = [
  // 蛋糕胚配方
  {
    productId: "WIP_CAKE_BASE",
    productIdTo: "RAW_FLOUR",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 500.0,
    scrapFactor: 5,
    sequenceNum: 1
  },
  {
    productId: "WIP_CAKE_BASE",
    productIdTo: "RAW_EGG",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 4,
    scrapFactor: 10,
    sequenceNum: 2
  },
  {
    productId: "WIP_CAKE_BASE",
    productIdTo: "RAW_SUGAR",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 200.0,
    scrapFactor: 2,
    sequenceNum: 3
  },
  {
    productId: "WIP_CAKE_BASE",
    productIdTo: "RAW_MILK",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 250.0,
    scrapFactor: 3,
    sequenceNum: 4
  },
  
  // 奶油夹心配方
  {
    productId: "WIP_CREAM_FILL",
    productIdTo: "RAW_CREAM",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 500.0,
    scrapFactor: 8,
    sequenceNum: 1
  },
  {
    productId: "WIP_CREAM_FILL",
    productIdTo: "RAW_SUGAR",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 100.0,
    scrapFactor: 2,
    sequenceNum: 2
  },
  
  // 巧克力酱配方
  {
    productId: "WIP_CHOC_SAUCE",
    productIdTo: "RAW_CHOC",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 300.0,
    scrapFactor: 5,
    sequenceNum: 1
  },
  {
    productId: "WIP_CHOC_SAUCE",
    productIdTo: "RAW_MILK",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 100.0,
    scrapFactor: 3,
    sequenceNum: 2
  },
  
  // 标准蛋糕配方
  {
    productId: "CHOC_CAKE_STD",
    productIdTo: "WIP_CAKE_BASE",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 1,
    scrapFactor: 10,
    sequenceNum: 1,
    routingWorkEffortId: "TASK_CAKE_SLICE"
  },
  {
    productId: "CHOC_CAKE_STD",
    productIdTo: "WIP_CREAM_FILL",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 1,
    scrapFactor: 15,
    sequenceNum: 2,
    routingWorkEffortId: "TASK_CAKE_FILL"
  },
  
  // 豪华蛋糕配方
  {
    productId: "CHOC_CAKE_DLX",
    productIdTo: "WIP_CAKE_BASE",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 2,
    scrapFactor: 10,
    sequenceNum: 1,
    routingWorkEffortId: "TASK_CAKE_SLICE"
  },
  {
    productId: "CHOC_CAKE_DLX",
    productIdTo: "WIP_CREAM_FILL",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 2,
    scrapFactor: 15,
    sequenceNum: 2,
    routingWorkEffortId: "TASK_CAKE_FILL"
  },
  {
    productId: "CHOC_CAKE_DLX",
    productIdTo: "WIP_CHOC_SAUCE",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 1,
    scrapFactor: 5,
    sequenceNum: 3,
    routingWorkEffortId: "TASK_CAKE_DECOR"
  },
  {
    productId: "CHOC_CAKE_DLX",
    productIdTo: "RAW_FRUIT",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 200.0,
    scrapFactor: 20,
    sequenceNum: 4,
    routingWorkEffortId: "TASK_CAKE_DECOR"
  },
  
  // 迷你蛋糕配方
  {
    productId: "CHOC_CAKE_MINI",
    productIdTo: "WIP_CAKE_BASE",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 0.5,
    scrapFactor: 10,
    sequenceNum: 1,
    routingWorkEffortId: "TASK_CAKE_SLICE"
  },
  {
    productId: "CHOC_CAKE_MINI",
    productIdTo: "WIP_CREAM_FILL",
    productAssocTypeId: "MANUF_COMPONENT",
    fromDate: "2024-01-01 00:00:00",
    quantity: 0.3,
    scrapFactor: 15,
    sequenceNum: 2,
    routingWorkEffortId: "TASK_CAKE_FILL"
  }
];
```

## 用户界面需求

1. **布局设计**:
   - 左侧为主画布区域，用于展示和编辑BOM树形结构
   - 右侧为产品面板，包含搜索框和产品列表
   - 顶部为工具栏，包含保存、刷新、缩放等操作
   - 底部状态栏显示当前操作状态和提示信息

2. **产品面板功能**:
   - 支持按产品ID、名称或类型过滤
   - 产品卡片显示基本信息(ID、名称、类型、单位)
   - 使用不同颜色标识不同类型的产品(原材料/半成品/成品)
   - 支持拖放产品到画布

3. **画布功能**:
   - 支持拖入产品创建节点
   - 支持连接产品节点创建BOM关系
   - 自动展开已有BOM的子产品
   - 支持放大/缩小/平移视图
   - 支持折叠/展开子树
   - 节点显示产品ID、名称和单位
   - 连线上显示（关系的）数量信息

4. **交互需求**:
   - 拖入子产品时弹出关系属性对话框，输入 quantity 和 scrapFactor
   - 点击产品节点或关系线显示详细属性面板
   - 属性面板可编辑“关系”的 quantity 和 scrapFactor
   - 编辑后即时更新视图
   - 双击节点可折叠/展开子树
   - 右键菜单支持添加/删除子节点、查看详情等操作

5. **验证与提示**:
   - 添加子产品时检查是否会形成循环依赖
   - 如发现循环依赖，显示错误提示并阻止操作
   - 数值输入验证(数量不能为负，报废率范围0-100%)
   - 操作成功/失败的视觉反馈

## 技术需求

1. 使用React框架开发前端界面
2. 可以考虑使用React Flow或类似库实现可视化图形编辑功能
3. 使用TypeScript确保类型安全

## 交互流程示例

1. **创建新BOM**:
   - 用户从右侧产品面板拖动一个产品到画布作为根节点
   - 用户从产品面板拖动另一个产品到画布的某个已有的节点上，作为它的子节点
   - 系统弹出对话框请求输入关系属性(quantity和scrapFactor)
   - 用户输入数据并确认
   - 系统创建连接线并显示关系信息
   - 如果子产品已有BOM，系统自动展开其子产品

2. **编辑BOM关系**:
   - 用户点击产品节点或关系线
   - 系统显示属性面板
   - 用户编辑quantity或scrapFactor
   - 系统实时更新视图

3. **循环依赖检查**:
   - 用户尝试添加一个会导致循环依赖的子产品
   - 系统检测到潜在循环
   - 系统显示错误提示并阻止操作
   - 用户必须选择其他产品或取消操作

## 额外功能建议

1. 支持撤销/重做操作
2. 自动布局功能优化大型BOM显示
3. 多层级BOM的展开层级控制

---

请根据以上需求创建一个完整的BOM管理界面原型，确保用户体验流畅，交互直观，并满足所有功能要求。特别关注BOM关系的创建、展示和编辑，以及循环依赖的检测与防止。


---

## 生成后改进的提示词

根据以上提示词，v0 生成了使用 React Flow 的初始版本。但是节点排列比较无序。

使用下面的提示词，让 v0 改进：

```txt
节点排列太无序，希望使用更常规的"树"UI组件或者增加约束让现有组件模拟更常规的树视图。
```

调整后效果仍然不行，继续提示：

```txt
希望实现类似命令行 `tree` 命令那样的效果，即树的节点从左向右排列，而不是从上到下。同时，每个节点/子树应该可以折叠。
```

提示后，v0 换用 react-complex-tree 库来实现功能。

此后更多的改进提示略。





