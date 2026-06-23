# 阿撒托斯 Boss 战剩余任务队列

规则：每完成 1-3 个任务后暂停，交给用户检查与测试。

## 已实现，待用户测试

- [ ] 任务 1：实现打字全局冷却，防止连续提交拼写。
- [ ] 任务 2：实现聊天输入框逐字符着色，已有字母白色，缺失/非法字符暗红。
- [ ] 任务 3：实现 `VOID` / `VITAL` 的基础真实效果，并接入力场效果判断。
- [ ] 任务 4：制作几种小怪的 `cthulhu_render` NBT 特殊渲染，并新增项目所需 Shader/后处理资源骨架。
- [ ] 任务 5：实现 `SHIELD` / `BREAK` 的基础状态与第二阶段柱子交互。
- [ ] 任务 6：实现四柱专属机制：饥荒、归零、绝境、侵蚀。
- [ ] 任务 7：实现第一阶段 Boss 本体与攻击循环。
- [ ] 任务 8：实现小怪生成与 NBT 特殊渲染标记。
- [ ] 任务 9：实现第三阶段 Boss、弹幕、死亡剧本和 `EXIST` 解除无敌。
- [ ] 任务 10：实现最终倒计时、最终导弹雨和多人共同真言。
- [ ] 任务 11：实现阶段 Shader、UI 隐藏、音乐控制。
- [ ] 任务 12：实现战斗期间方块破坏/放置限制与战斗清理。
- [ ] 任务 13：将 `CthulhuConfig` 接入 Forge 配置文件。

说明：以上内容代码已接入并通过编译，但用户当前无法进游戏验证，因此暂不标记为已完成验收。任务 4 的小怪特效与 shader 资源也一并保持待测试状态。

## 待完成任务

当前原任务队列中的核心条目均已实现，等待用户进游戏测试验收。

## 任务 1-3 测试范围

### 打字冷却

- 聊天栏输入一个可拼写单词后，立即再次输入应收到冷却提示。
- `/cthulhu spell <word>` 同样受冷却限制。

### 输入框着色

- 拥有的字母显示为白色。
- 缺失字母、空格以外的非法字符显示为暗红色。
- 未持有字母时，聊天栏保持普通显示。

### VOID / VITAL

- `VOID`：10 秒内免疫 `VOID` 与 `STASIS` 力场效果。
- `VITAL`：清除 `VitalityDrain`，并稳定饱食度。

## 任务 4 测试范围

### 小怪特殊渲染

使用原版实体的 `ForgeData.cthulhu_render` 标记测试：

```mcfunction
/summon minecraft:zombie ~ ~ ~ {ForgeData:{cthulhu_render:"noise"}}
/summon minecraft:spider ~ ~ ~ {ForgeData:{cthulhu_render:"wireframe"}}
/summon minecraft:skeleton ~ ~ ~ {ForgeData:{cthulhu_render:"text_static"}}
```

预期：

- `noise`：实体表面出现青绿色脉冲噪声叠加。
- `wireframe`：实体周围出现线框包围盒。
- `text_static`：实体上方显示 `CTHULHU//STATIC`。

### Shader 资源骨架

已新增资源：

```text
assets/dustandash/shaders/core/cthulhu_phase.json
assets/dustandash/shaders/core/cthulhu_phase.vsh
assets/dustandash/shaders/core/cthulhu_phase.fsh
assets/dustandash/shaders/program/cthulhu_noise.json
assets/dustandash/shaders/program/cthulhu_noise.fsh
assets/dustandash/shaders/post/cthulhu_phase.json
```

当前这些 shader 资源尚未由 `ShaderHelper` 主动加载，先作为后续阶段视觉退化的资源基础。

## 任务 5-6、8 测试范围（待测试）

### SHIELD / BREAK

先启动战斗并进入第二阶段：

```mcfunction
/cthulhu start
/cthulhu phase PHASE_2
```

给自己发字母并拼写：

```mcfunction
/cthulhu giveletter S
/cthulhu giveletter H
/cthulhu giveletter I
/cthulhu giveletter E
/cthulhu giveletter L
/cthulhu giveletter D
/cthulhu spell SHIELD
```

预期：

- 玩家获得 1 层 `SHIELD` 拦截次数，最多累计 3 层。
- 饥荒柱发射法则飞弹时，若玩家有 `SHIELD`，应收到拦截提示并消耗 1 层，不受到该次飞弹伤害。

测试 `BREAK`：

等待 1 秒打字冷却结束后执行：

```mcfunction
/cthulhu giveletter B
/cthulhu giveletter R
/cthulhu giveletter E
/cthulhu giveletter A
/cthulhu giveletter K
/cthulhu spell BREAK
```

预期：

- 最近的 1-2 根柱子显示 `/BREAK` 和倒计时。
- 断链期间柱子不进入自定义无敌窗口。
- 断链期间饥荒/侵蚀等周期性机制暂停。

### 四柱机制

可手动生成单根柱子测试：

```mcfunction
/cthulhu spawn pillar AMIN FAMINE
/cthulhu spawn pillar RLTY ZERO
/cthulhu spawn pillar EXIS DESPAIR
/cthulhu spawn pillar DLTE EROSION
```

预期：

- `FAMINE`：周期性增加饥饿消耗，发射法则飞弹，并生成带 `cthulhu_render` 特效的小怪。
- `ZERO`：距离越近，受到伤害越低，后续自定义无敌越长；远程攻击更有效。
- `DESPAIR`：远距离物理攻击被拒绝，贴近攻击可造成伤害。
- `EROSION`：每 15 秒对参与者施加一次 `VitalityDrain`。

### 小怪生成与特殊渲染

直接生成测试：

```mcfunction
/cthulhu spawn minion zombie noise
/cthulhu spawn minion spider wireframe
/cthulhu spawn minion skeleton text_static
```

预期：

- 生成的小怪立即带有对应特殊渲染。
- 远离再靠近后，特效仍会通过追踪同步恢复。

## 任务 7 测试范围（待测试）

### 第一阶段 Boss 本体

启动战斗：

```mcfunction
/cthulhu start
```

预期：

- 核心点上方生成第一阶段 Boss，客户端显示 `AZATHOTH / 当前血量/最大血量`。
- Boss 保持悬浮，不受重力影响，不会因为玩家远离而消失。
- Boss 拥有高额物理减伤，普通攻击造成的实际血量下降应明显小于武器面板伤害。

### 第一阶段攻击循环

保持战斗在 `PHASE_1` 并观察：

```mcfunction
/cthulhu status
```

预期：

- Boss 每约 3 秒在参与者附近生成一个法则力场。
- Boss 每约 7 秒在参与者附近生成一个带特殊渲染的小怪。
- 力场被击破后仍按既有逻辑掉落字母。

### 阶段推进

击杀第一阶段 Boss，或先用高伤害手段压低血量后击杀。

预期：

- Boss 死亡后战斗自动切换到 `PHASE_2`。
- 四柱自动生成，分别为 `FAMINE`、`ZERO`、`DESPAIR`、`EROSION`。

### 独立生成测试

如果只想测试实体渲染和受击，不启动完整战斗：

```mcfunction
/cthulhu spawn boss1
```

注意：独立生成的 Boss 如果没有对应维度的活跃 `PHASE_1` 战斗，下一次服务端 tick 会自行消失。这是为了避免孤儿 Boss 留在世界中。

## 任务 9-10 测试范围（待测试）

### 第三阶段 Boss 与弹幕

启动战斗并切换第三阶段：

```mcfunction
/cthulhu start
/cthulhu phase PHASE_3
```

预期：

- 核心点上方生成 `TEXT STORM`。
- Boss 周期性对参与者造成 `SLASH/BULLET` 弹幕伤害，并发送提示。
- Boss 周期性写入死亡剧本，聊天会提示 `玩家名 · DIE`。

测试死亡剧本反制：

```mcfunction
/cthulhu giveletter D
/cthulhu giveletter E
/cthulhu giveletter L
/cthulhu giveletter E
/cthulhu giveletter T
/cthulhu giveletter E
/cthulhu spell DELETE
```

或：

```mcfunction
/cthulhu giveletter M
/cthulhu giveletter O
/cthulhu giveletter D
/cthulhu giveletter I
/cthulhu giveletter F
/cthulhu giveletter Y
/cthulhu spell MODIFY
```

预期：

- 5 秒内输入 `DELETE` 或 `MODIFY` 会清除死亡剧本。
- 未反制则玩家受到致死伤害。

### EXIST 解除绝对防御

等待 `TEXT STORM / EXIST` 标签出现，或观察 Boss 提示 `Absolute defense: gather EXIST.`。

预期：

- 绝对防御期间攻击 Boss 不造成伤害，并提示需要 `EXIST`。
- Boss 周围生成 `E X I S T` 字母。
- 收集并输入 `EXIST` 后，绝对防御解除。

### 最终真言

击杀第三阶段 Boss，或使用高伤害手段击杀。

预期：

- 战斗进入 `FINAL_TRUTH`。
- 服务端每 2 秒提示最终倒计时，并模拟最终导弹雨：玩家受到伤害并获得 `REALITYEXISTS` 中的随机字母。
- 每个在线参与者都需要成功输入：

```mcfunction
/cthulhu spell REALITY EXISTS
```

- 每提交一名参与者，聊天显示 `REALITY EXISTS: 当前/需要人数`。
- 所有在线参与者提交后，战斗终止并清理。
- 倒计时结束前未完成，则玩家受惩罚伤害，提交记录清空，倒计时重启。

### 独立生成测试

如果只想测试第三阶段实体：

```mcfunction
/cthulhu phase PHASE_3
/cthulhu spawn storm
```

注意：独立生成的 `TEXT STORM` 如果没有对应维度的活跃 `PHASE_3` 或 `FINAL_TRUTH` 战斗，下一次服务端 tick 会自行消失。

## 任务 12-13 测试范围（待测试）

### 方块破坏/放置限制

启动战斗：

```mcfunction
/cthulhu start
```

预期：

- 在当前维度破坏方块会被取消，并收到 `Azathoth rejects block breaking during the fight.`。
- 在当前维度放置方块会被取消，并收到 `Azathoth rejects block placing during the fight.`。
- 非战斗维度不受影响。

停止战斗后测试：

```mcfunction
/cthulhu stop
```

预期：

- 当前维度恢复正常破坏/放置。
- Boss、柱子、力场、字母实体，以及战斗队伍中的特殊小怪被清理。
- 参与者客户端字母 HUD 被清空。
- `VitalityDrain` 被清除，`SoulWither` 不会被清除。

### 配置文件接入

启动一次游戏后，检查 common 配置中应出现 `Cthulhu Boss Fight` 分组，包含：

```text
maxDeathsBeforeBanish
banishAltDim
banishNetherY
phase3FallbackLetters
vitalityDrainPercent
soulWitherPerMissing
soulWitherMax
soulRecoveryPerSleep
pillarOutputWindowTicks
pillarInvulTicks
globalTypeCooldownTicks
lockBlockInteractionsDuringFight
```

建议测试：

1. 将 `lockBlockInteractionsDuringFight` 改为 `false`，重载/重启后启动战斗。
2. 破坏和放置方块应不再被 Boss 战阻止。
3. 将 `banishNetherY` 改为其他高度，触发驱逐后应使用新的下界 Y 坐标。

## 任务 11 测试范围（待测试）

### 阶段视觉同步

启动战斗并切换阶段：

```mcfunction
/cthulhu start
/cthulhu phase PHASE_1
/cthulhu phase PHASE_2
/cthulhu phase PHASE_3
```

预期：

- `PHASE_1`：屏幕出现轻微暗化/色彩剥离叠层。
- `PHASE_2`：屏幕出现更强黑白噪声横线。
- `PHASE_3`：屏幕出现黑底网格叠层。
- `/cthulhu stop` 后视觉叠层消失。

### 第三阶段 UI 隐藏

切换到第三阶段：

```mcfunction
/cthulhu phase PHASE_3
```

预期：

- 大部分原版 HUD 被隐藏。
- 热键栏、聊天栏、字母环和 Boss 战提示仍保留。

### 音乐控制

在任意 Boss 战阶段停留一段时间。

预期：

- 原版音乐会被持续停止。
- 当前实现尚未接入专属打字机循环音效，后续可替换为自定义音效事件。
