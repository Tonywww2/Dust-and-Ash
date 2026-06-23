# 阿撒托斯 Boss 战工作记录

本文档用于对照原始《阿撒托斯，痴愚之侧》设计文档，记录当前已完成内容、部分完成内容、未完成内容，以及当前实现与原始设定存在差异的地方。

当前实现基于 Forge `1.20.1`，包路径为：

```text
src/main/java/com/tonywww/dustandash/cthulhu/
```

## 当前总体状态

当前已完成的是 Boss 战的服务端生命周期、字素系统核心闭环、死亡驱逐、生命上限蚕食、第一阶段法则力场雏形、第二阶段四柱雏形、聊天栏拼写交互、屏幕提示、基础持久化和调试命令。

尚未完成的是完整视觉退化、Boss 本体、第三阶段弹幕与元游戏解谜、完整小怪体系、抽象高级渲染、音乐控制、最终倒计时演出，以及若干词语效果的真实战斗逻辑。

## 已完成内容

### 1. 基础架构

已新增 `cthulhu` 模块结构：

```text
api/
client/
command/
config/
data/
entity/
event/
fight/
grapheme/
mixin/
network/
```

已实现主要类：

- `BossFightAPI`
- `GraphemeAPI`
- `HealthDrainAPI`
- `UIHintAPI`
- `ShaderHelper`
- `CthulhuConfig`
- `BossFightManager`
- `BossFightInstance`
- `FightPhase`
- `BossFightSavedData`

已修正 `PacketHandler` 的包 ID 注册方式，改为递增 ID，避免后续多个网络包冲突。

### 2. Boss 战生命周期

已完成：

- 可通过命令启动 Boss 战。
- 每个维度可维护独立 Boss 战实例。
- 服务端 tick 驱动战斗实例。
- 玩家进入战斗维度会自动加入战斗。
- 玩家离开无战斗维度时会清空客户端字母 HUD，避免跨世界显示错误。
- 可终止当前维度或全部维度战斗。
- 全部参与者被驱逐后自动终止战斗。
- 阶段切换支持：
  - `PHASE_1`
  - `PHASE_2`
  - `PHASE_3`
  - `FINAL_TRUTH`
  - `TERMINATED`

相关命令：

```mcfunction
/cthulhu start
/cthulhu stop
/cthulhu stop current
/cthulhu status
/cthulhu phase PHASE_1
/cthulhu phase PHASE_2
/cthulhu phase PHASE_3
/cthulhu phase FINAL_TRUTH
```

### 3. 持久化

已实现 `BossFightSavedData`，当前保存：

- active
- dimensionId
- corePos
- phase
- participants
- banishedPlayers
- spawnedEntities
- deathCounts
- letterInventories
- bossHealth

已实现服务器启动时从 SavedData 恢复活跃战斗实例。

当前限制：

- 尚未完整恢复实体血量、柱子精确状态和 Boss 本体状态。
- 当前实体 UUID 有记录，但实体重建逻辑仍是基础版本。

### 4. 字素系统

已完成：

- 玩家字母库存由服务端权威管理。
- 字母不进入背包。
- 字母通过 `GraphemeUpdatePacket` 全量同步到客户端。
- 客户端 HUD 显示字母环。
- 字母 HUD 已修复跨世界残留问题。
- 拾取单个字母不会再异常增加大量字母。
- 支持服务端查看和清空字母库存。
- 支持拼写单词和短语。
- 支持聊天栏输入拼写。
- 支持命令方式拼写。
- 成功拼写后扣除对应字母。
- 字母不足或词典不存在时提示失败。

相关命令：

```mcfunction
/cthulhu giveletter A
/cthulhu letters status
/cthulhu letters clear
/cthulhu spell VOID
/cthulhu spell REALITY EXISTS
```

当前词典：

```text
VOID
VITAL
SHIELD
BREAK
DELETE
MODIFY
EXIST
REALITY
REALITY EXISTS
```

当前实现差异：

- 原设计要求字母库存通过 Capability 存储；当前实现存储在 `BossFightInstance` 的 `letterInventories` 中。
- 原设计要求输入框逐字符着色；当前尚未实现 `EditBoxMixin` 字符着色。
- 原设计中 `EntityGrapheme` 会缓慢向玩家移动；根据后续需求，当前已改为停留原地，玩家靠近后拾取。

### 5. 聊天栏交互

已完成：

- 新增 `ChatScreenMixin`。
- 玩家拥有字母时，聊天栏输入会被拼写系统接管。
- Enter 发送 `WordSpellPacket`。
- Tab 补全被禁用。
- 上箭头历史被禁用。
- 支持 `REALITY EXISTS` 这类带空格短语。

当前限制：

- 仅在客户端字母缓存非空时接管聊天栏。
- 尚未实现输入框字母颜色反馈。
- 尚未实现全局打字冷却。

### 6. 生命上限蚕食

已完成：

- `HealthDrainAPI.applyVitalityDrain`
- `HealthDrainAPI.applySoulWither`
- `HealthDrainAPI.clearAllDrain`
- `HealthDrainAPI.clearVitalityDrain`
- `HealthDrainAPI.recoverSoulWither`
- `HealthDrainAPI.reapply`

当前实现方式：

- 使用玩家 PersistentData 保存百分比。
- 使用固定 UUID 的 `MAX_HEALTH` AttributeModifier 实现生命上限变化。
- `VitalityDrain` 和 `SoulWither` 分别使用独立 modifier，乘算叠加。
- 睡眠后恢复 `SoulWither`。
- 驱逐时清除 `VitalityDrain`，保留 `SoulWither`。

调试命令：

```mcfunction
/cthulhu drain vitality
/cthulhu drain soul
/cthulhu drain clear
/cthulhu drain status
```

当前实现差异：

- 原设计要求自定义药水效果 `VitalityDrainEffect` 和 `SoulWitherEffect`；当前未注册药水效果，而是直接使用 AttributeModifier 和 PersistentData。

### 7. 死亡与驱逐

已完成：

- 监听玩家死亡。
- 记录战斗内死亡次数。
- 默认死亡 3 次后驱逐。
- 驱逐后禁止回到战斗维度。
- 被驱逐时清除生命力吞噬，保留灵魂枯竭。
- 全员驱逐后终止战斗。
- 驱逐到下界时 Y 坐标为 `130`。
- 玩家死亡后，当前持有字母会掉落在死亡位置。
- 死亡掉落字母有拾取延迟，防止死亡瞬间被重新捡回。

当前实现差异：

- 原设计中驱逐目标描述为“下界基岩层”；当前根据测试需求改为下界 `Y=130`。

### 8. 第一阶段：法则力场

已完成基础版本：

- 新增 `CthulhuLawFieldEntity`。
- 第一阶段会周期性在参与者附近生成法则力场。
- 当前最多保留 6 个力场。
- 力场可被攻击并击碎。
- 击碎后掉落一个字母，来源于 `VOIDVITAL`。
- 力场加入 `cthulhu_boss` 队伍。
- 力场有基础效果：
  - `SILENCE`：让玩家物品进入短冷却。
  - `EVAPORATION`：击退玩家。
  - `VOID`：造成魔法伤害。
  - `STASIS`：给予减速和挖掘减速。

调试命令：

```mcfunction
/cthulhu spawn field SILENCE V
/cthulhu spawn field EVAPORATION O
/cthulhu spawn field VOID I
/cthulhu spawn field STASIS D
```

当前实现差异：

- 原设计中的力场视觉形态尚未完成。
- `EVAPORATION` 仍是即时击退效果，未实现“黑色线性轨道，延迟后爆发”。
- `VOID` 未实现“下坠空洞，关闭前可击碎”的完整行为。
- `STASIS` 未真正清空攻速/蓄力，只使用了减速与挖掘减速替代。
- `VOID` 和 `VITAL` 的真实反制效果目前仍是提示性效果，未完整接入力场免疫/净化区。

### 9. 第二阶段：四柱

已完成基础版本：

- 新增 `CthulhuPillarEntity`。
- 切换到 `PHASE_2` 时生成四根柱子：
  - 北：`AMIN`
  - 东：`RLTY`
  - 南：`EXIS`
  - 西：`DLTE`
- 柱子可被攻击。
- 柱子死亡后掉落对应字母。
- 实现 3 tick 输出窗口。
- 输出窗口结束后进入可配置无敌。
- 四柱全部死亡后：
  - 清除参与者 `VitalityDrain`
  - 自动推进到 `PHASE_3`
  - 自动补发 `REALITY`
- 柱子加入 `cthulhu_boss` 队伍，关闭友伤。

调试命令：

```mcfunction
/cthulhu spawn pillar AMIN
```

当前实现差异：

- 四柱当前是通用柱子实体，没有区分饥荒、归零、绝境、侵蚀四类专属机制。
- 尚未实现柱子跨空间联动。
- 尚未实现饥荒柱的饱食度流失、字弹、小怪召唤。
- 尚未实现归零柱“越近无敌帧越长”。
- 尚未实现绝境柱“远距离物理免疫，近战全吃”。
- 尚未实现侵蚀柱“每 15 秒扣除 2% 最大生命值”。
- `SHIELD` 和 `BREAK` 当前只有提示性效果，没有真实战斗逻辑。

### 10. 第三阶段与最终真言

已完成基础版本：

- 切换到 `PHASE_3` 时自动补发 `REALITY`。
- 支持 `FINAL_TRUTH` 阶段。
- 支持在聊天栏或命令中输入：

```text
REALITY EXISTS
```

- 在 `FINAL_TRUTH` 阶段成功拼写 `REALITY EXISTS` 会终止战斗。

当前实现差异：

- 尚未实现 The Grid 空间。
- 尚未实现文字风暴巨像。
- 尚未实现 `SLASH` 字刃风暴。
- 尚未实现 `BULLET` 字符导弹。
- 尚未实现 `SUFFER` 领域生命上限扣除。
- 尚未实现死亡剧本写入聊天栏。
- 尚未实现 `DELETE` / `MODIFY` 的真实秒杀反制。
- 尚未实现 `EXIST` 解除绝对防御。
- 尚未实现血书写、魂献祭。
- 尚未实现最终 10 秒倒计时和最后导弹雨补字。

### 11. 屏幕提示系统

已完成：

- 新增 `ClientHintManager`。
- 新增 `HintMessagePacket`。
- 新增 `CthulhuHintOverlay`。
- 阶段切换时显示中央提示。
- 提示有渐入渐出 alpha。

当前实现差异：

- 文案为英文临时文案。
- 尚未实现原设计中更强烈的阶段演出。

### 12. 网络通信

已完成数据包：

| 数据包 | 方向 | 状态 |
|--------|------|------|
| `GraphemeUpdatePacket` | S to C | 已完成 |
| `WordSpellPacket` | C to S | 已完成 |
| `HintMessagePacket` | S to C | 已完成 |

未完成数据包：

| 数据包 | 方向 | 状态 |
|--------|------|------|
| `PhaseUpdatePacket` | S to C | 未单独实现，当前阶段提示和服务端状态分散处理 |
| `BanishPacket` | S to C | 未实现，当前直接服务端传送并聊天提示 |
| `LetterPickupPacket` | S to C | 未实现，当前用全量库存同步代替 |

## 未完成内容汇总

### 1. 世界退化视觉

未完成：

- 阶段一全局饱和度降低 50%。
- 阶段二黑白灰与材质溶解。
- 阶段三 The Grid。
- 原版 UI 隐藏，仅保留快捷栏与聊天栏。
- Shader 管理实际逻辑。

当前仅保留了 `ShaderHelper` 空 API。

### 2. 音乐与音效

未完成：

- 停止原版音乐。
- 循环打字机音效。
- 阶段三特殊音效。

### 3. Boss 本体

未完成：

- 第一阶段阿撒托斯混沌灰色墨迹实体。
- Boss 血量、免伤、阶段推进条件。
- 第三阶段文字风暴巨像。
- Boss 攻击 AI。

### 4. 小怪体系

未完成：

- 噪点僵尸。
- 线框蜘蛛。
- 字模骷髅。
- 乱码末影人。
- 原版实体 + NBT 渲染控制。
- 小怪与 Boss 队伍完整管理。

### 5. 抽象实体渲染

部分完成：

- 当前有 `CthulhuTextEntityRenderer`，以文字显示柱子、字母、力场。

未完成：

- Boss 墨迹动态纹理。
- 文本柱像素化长方体。
- 字符导弹立体文字。
- 字刃字符刀光。
- 小怪 NBT 特殊渲染。
- 真正“隐形实体 + 抽象渲染”的完整美术表现。

### 6. 词语效果

部分完成：

- 词典、拼写、扣字母、失败提示已完成。

未完成真实效果：

- `VOID`：免疫虚无与静止 10 秒。
- `VITAL`：创造白色净化区并恢复生命上限。
- `SHIELD`：抵挡邻近柱飞弹。
- `BREAK`：切断柱子联动 20 秒。
- `DELETE` / `MODIFY`：反制死亡剧本。
- `EXIST`：解除绝对防御。

### 7. 输入框视觉反馈

未完成：

- 已拥有字母白色。
- 缺失字母和非字母暗红色。
- 补齐字母浅红色。

### 8. 真实伤害与弹幕

未完成：

- 自定义伤害类型区分。
- 物理弹幕。
- 真实伤害惩罚。
- 字符导弹。
- 字刃风暴。
- 导弹核心字母获取。

### 9. 方块交互限制

未完成：

- 战斗期间禁止维度内方块破坏。
- 战斗期间禁止维度内方块放置。

### 10. 配置文件化

部分完成：

- 已有 `CthulhuConfig` 常量类。

未完成：

- 尚未接入 Forge config spec。
- 当前配置不是外部配置文件驱动。

## 当前可用测试命令

### 战斗生命周期

```mcfunction
/cthulhu start
/cthulhu stop
/cthulhu stop current
/cthulhu status
/cthulhu phase PHASE_1
/cthulhu phase PHASE_2
/cthulhu phase PHASE_3
/cthulhu phase FINAL_TRUTH
```

### 字母系统

```mcfunction
/cthulhu giveletter A
/cthulhu letters status
/cthulhu letters clear
/cthulhu spawn grapheme A
/cthulhu spell VOID
/cthulhu spell REALITY EXISTS
```

### 生命上限

```mcfunction
/cthulhu drain vitality
/cthulhu drain soul
/cthulhu drain clear
/cthulhu drain status
```

### 实体调试

```mcfunction
/cthulhu spawn pillar AMIN
/cthulhu spawn field SILENCE V
/cthulhu spawn field EVAPORATION O
/cthulhu spawn field VOID I
/cthulhu spawn field STASIS D
```

## 当前建议测试流程

### 1. 字母拾取与同步

```mcfunction
/cthulhu stop
/cthulhu start
/cthulhu letters clear
/cthulhu spawn grapheme A
```

靠近拾取后执行：

```mcfunction
/cthulhu letters status
```

预期：

```text
Graphemes: {A=1}
```

### 2. 跨世界同步

```mcfunction
/cthulhu start
/cthulhu giveletter V
```

切换到其他维度。

预期：

- 非战斗维度 HUD 清空。
- 回到战斗维度后重新同步正确库存。

### 3. 死亡掉字母

```mcfunction
/cthulhu start
/cthulhu letters clear
/cthulhu giveletter V
/cthulhu giveletter O
/cthulhu giveletter I
/cthulhu giveletter D
/kill @s
```

预期：

- 死亡位置掉落全部 4 个字母。
- 复活后 HUD 清空。
- 等待约 3 秒后靠近可重新拾取。

### 4. 第二阶段四柱

```mcfunction
/cthulhu start
/cthulhu phase PHASE_2
```

击杀四柱。

预期：

- 四柱掉落各自字母。
- 四柱全毁后自动进入 `PHASE_3`。
- 自动补发 `REALITY`。

### 5. 最终真言

```mcfunction
/cthulhu phase FINAL_TRUTH
```

确保拥有 `REALITY EXISTS` 所需字母后，在聊天栏输入：

```text
REALITY EXISTS
```

预期：

- 字母被消耗。
- 战斗终止。

## 下一步开发建议

建议按以下顺序继续：

1. 实现 `EditBoxMixin` 字符着色。
2. 给 `VOID`、`VITAL`、`SHIELD`、`BREAK` 接入真实效果。
3. 完成四柱专属机制。
4. 实现小怪 NBT 标记和特殊渲染。
5. 实现 Boss 本体与第一阶段攻击循环。
6. 实现第三阶段 The Grid 与弹幕。
7. 接入 Shader、UI 隐藏和音乐控制。
8. 将 `CthulhuConfig` 接入 Forge 配置文件。

