# 多版本构建与平台层

项目使用 Stonecutter 维护同一套业务源码，分别生成以下两个目标：

| 目标 | 构建后端 | 编译／运行 Java |
| --- | --- | --- |
| `1.20.1-forge` | Architectury Loom | 17 |
| `1.21.1-neoforge` | ModStitch + ModDevGradle，NeoForge **21.1.233** | 21 |

Gradle 守护进程使用 Java 25，配置位于 `gradle/gradle-daemon-jvm.properties`。这与各目标的 Java toolchain 独立；首次构建可能下载对应 JDK 和 Minecraft 开发依赖。

## 构建

在项目根目录执行：

```powershell
.\gradlew.bat :1.20.1-forge:build :1.21.1-neoforge:build
.\gradlew.bat :1.21.1-neoforge:runClient
.\gradlew.bat :1.21.1-neoforge:runServer
```

发行包位于各自的 `versions/<目标>/build/libs/`。Forge 保持原有 `run/` 目录，NeoForge 默认使用 `run/neoforge/`。可通过 `'-Pdustandash.runDir=相对项目根目录的路径'` 指定独立运行目录。

NeoForge 对应依赖版本为 GeckoLib 4.9.2、Curios 9.5.1+1.21.1、JEI 19.25.0.322、Patchouli 1.21.1-92-NEOFORGE，集中配置在目标的 `gradle.properties`。

## 源码结构

`com.tonywww.dustandash.platform` 是版本和加载器差异的唯一 Java 边界。该包统一封装注册、配置、事件、网络、库存与能量能力、物品数据、配方 Codec、方块实体生命周期，以及 GUI 和渲染 API。条件注释全部放在此包中。

业务目录继续承担原有职责：

- `registry`：物品、方块、实体、菜单、粒子和配方注册声明，使用平台注册接口。
- `block/custom` 与 `block/entity`：机器交互、加工流程与反应堆逻辑。
- `item`：物品行为；需要 GeckoLib 版本适配的两个武器保留公开入口类，并委托平台基类。
- `data/recipes`：四类机器配方模型和校验，通过平台层适配 Container／RecipeInput、RecipeHolder 和序列化。
- `menu`、`screen`、`client`：菜单数据、界面布局、提示与渲染；加载器注册入口集中在平台层。
- `platform/datagen`：Forge 数据生成器。

新增跨版本 API 时，先在平台包提供稳定接口，再让业务类调用；避免向业务类加入加载器导入和条件注释。Forge 是当前 Stonecutter 活跃源码版本，NeoForge 生成代码位于其 `build/generated/stonecutter/`，不要直接修改生成代码。

## 数据资源

Forge 数据生成结果 `src/generated/resources` 是统一来源，仍可用 `:1.20.1-forge:runData` 更新。NeoForge `processResources` 对打包副本执行以下转换，不改写来源：

- 1.21.1 的单数数据目录、`pack_format=34` 和原版物品 ID 改名。
- 公共标签从 `forge` 迁到 `c`，并映射 cobblestones／gravels。
- 原版配方结果的 `item` 字段迁到 `id`，条件配方迁到 `neoforge:conditions`。
- NBT 物品条件迁到组件；中子条件通过 `PlatformNbtIngredient` 保持部分 NBT 匹配。
- Patchouli 书籍配方使用 `patchouli:book` 组件。

`placeholder` 的原配方产出 2 个、物品默认不可堆叠。NeoForge 对该配方结果附加 `minecraft:max_stack_size=2`，保留产出数量并满足新版本的结果校验。

新增旧版 NBT 条件时，需要在构建脚本中显式补充组件映射；不支持的格式会在构建阶段报错。

## 自动服务器检查

`platform/PortSmokeChecks.java` 默认从编译和发行包排除。添加 `'-Pdustandash.smokeChecks'` 后，它在服务器启动时检查四类共 123 条机器配方及网络往返、物品数据与大整数计数、中子条件、不可损坏组件、8 类机器能力和库存存档恢复、反应堆能量能力，然后自动保存并停止服务器。

在独立目录准备 `eula.txt` 与 `server.properties` 后执行：

```powershell
.\gradlew.bat :1.20.1-forge:runServer '-Pdustandash.runDir=build/smoke/forge' '-Pdustandash.smokeChecks'
.\gradlew.bat :1.21.1-neoforge:runServer '-Pdustandash.runDir=build/smoke/neoforge' '-Pdustandash.smokeChecks'
```

运行目录中的 `port-smoke-result.txt` 必须为 `PASS`，日志必须出现 `PORT_SMOKE_PASS`，并检查资源解析错误；不能仅凭 Gradle 返回成功判断运行验证通过。最终发行构建不要带 `dustandash.smokeChecks` 参数。

服务器检查不覆盖客户端画面、实际玩家联机和第三方模组组合；发布前仍需对机器 GUI、GeckoLib 武器动画、Curios 效果、HUD 与网络交互进行实机检查。
