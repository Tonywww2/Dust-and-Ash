# NuclearCraft-Neoteric Forge 1.20.1 Shader/Renderer Reference for AI

> 本文件面向后续 AI 编码使用。所有结论均来自已检查的源码、资源文件、Forge 1.20.x 源码注释、Forge 1.20.1 文档或当前工作区文件。没有源码证据的内容用“未验证”标记，不作为实现依据。

## 0. 已验证范围

### 0.1 目标版本

- 当前工作区 `gradle.properties`：
  - `minecraft_version=1.20.1`
  - `forge_version=47.4.0`
  - `mapping_channel=official`
  - `mapping_version=1.20.1`
- 当前工作区 `build.gradle`：
  - 使用 `net.minecraftforge.gradle` `[6.0,6.2)`
  - Java toolchain 为 Java 17
  - 主 mod id 为 `dustandash`

### 0.2 NuclearCraft-Neoteric 源

- 仓库：<https://github.com/igentuman/NuclearCraft-Neoteric>
- 分支：`1.20`
- 检查到的分支 HEAD：`5b68d17d8fdc32da7d0e092724800d84e865fe56`
- 用户给定目录：<https://github.com/igentuman/NuclearCraft-Neoteric/tree/1.20/src/main/resources/assets/minecraft/shaders>
- 用户给定 Java 文件：<https://github.com/igentuman/NuclearCraft-Neoteric/blob/1.20/src/main/java/igentuman/nc/client/renderer/BlackholeRenderer.java>

### 0.3 Forge 资料源

- Forge 1.20.1 文档首页：<https://docs.minecraftforge.net/en/1.20.1/>
- Forge events 文档：<https://docs.minecraftforge.net/en/1.20.1/concepts/events/>
- Forge block entity renderer 文档：<https://docs.minecraftforge.net/en/1.20.1/blockentities/ber/>
- Forge 1.20.x `RegisterShadersEvent` 源码：<https://github.com/MinecraftForge/MinecraftForge/blob/1.20.x/src/main/java/net/minecraftforge/client/event/RegisterShadersEvent.java>
- Forge 1.20.x `RenderLevelStageEvent` 源码：<https://github.com/MinecraftForge/MinecraftForge/blob/1.20.x/src/main/java/net/minecraftforge/client/event/RenderLevelStageEvent.java>
- Forge 1.20.x `EntityRenderersEvent` 源码：<https://github.com/MinecraftForge/MinecraftForge/blob/1.20.x/src/main/java/net/minecraftforge/client/event/EntityRenderersEvent.java>
- Forge 1.20.x `FMLClientSetupEvent` 源码：<https://github.com/MinecraftForge/MinecraftForge/blob/1.20.x/src/main/java/net/minecraftforge/fml/event/lifecycle/FMLClientSetupEvent.java>

### 0.4 已检查的 NuclearCraft-Neoteric 文件

核心 Java：

- `src/main/java/igentuman/nc/client/renderer/BlackholeRenderer.java`
- `src/main/java/igentuman/nc/client/renderer/BillboardingEffectRenderer.java`
- `src/main/java/igentuman/nc/util/CustomEffect.java`
- `src/main/java/igentuman/nc/client/renderer/NCShaders.java`
- `src/main/java/igentuman/nc/client/renderer/NCRenderType.java`
- `src/main/java/igentuman/nc/client/renderer/DistortShader.java`
- `src/main/java/igentuman/nc/client/renderer/NukeRenderer.java`
- `src/main/java/igentuman/nc/client/renderer/Q36FlashShader.java`
- `src/main/java/igentuman/nc/handler/event/client/TickHandler.java`
- `src/main/java/igentuman/nc/mixin/LevelRendererCloudsMixin.java`
- `src/main/java/igentuman/nc/setup/ClientSetup.java`
- `src/main/java/igentuman/nc/client/setup/EntityRenderHandler.java`
- `src/main/resources/nuclearcraft.mixins.json`

Shader/后处理资源：

- `src/main/resources/assets/minecraft/shaders/core/rendertype_clouds.json`
- `src/main/resources/assets/minecraft/shaders/core/rendertype_clouds.vsh`
- `src/main/resources/assets/minecraft/shaders/core/rendertype_clouds.fsh`
- `src/main/resources/assets/minecraft/shaders/program/black_hole.json`
- `src/main/resources/assets/minecraft/shaders/program/black_hole.vsh`
- `src/main/resources/assets/minecraft/shaders/program/black_hole.fsh`
- `src/main/resources/assets/minecraft/shaders/program/nuke.json`
- `src/main/resources/assets/minecraft/shaders/program/nuke.vsh`
- `src/main/resources/assets/minecraft/shaders/program/nuke.fsh`
- `src/main/resources/assets/minecraft/shaders/program/q36_flash.json`
- `src/main/resources/assets/minecraft/shaders/program/q36_flash.vsh`
- `src/main/resources/assets/minecraft/shaders/program/q36_flash.fsh`
- `src/main/resources/assets/nuclearcraft/shaders/core/rendertype_blackhole.json`
- `src/main/resources/assets/nuclearcraft/shaders/core/rendertype_blackhole.vsh`
- `src/main/resources/assets/nuclearcraft/shaders/core/rendertype_blackhole.fsh`
- `src/main/resources/assets/nuclearcraft/shaders/core/rendertype_nuke.json`
- `src/main/resources/assets/nuclearcraft/shaders/core/rendertype_nuke.vsh`
- `src/main/resources/assets/nuclearcraft/shaders/core/rendertype_nuke.fsh`
- `src/main/resources/assets/nuclearcraft/shaders/core/rendertype_nuke_smoke.json`
- `src/main/resources/assets/nuclearcraft/shaders/post/black_hole.json`
- `src/main/resources/assets/nuclearcraft/shaders/post/nuke.json`
- `src/main/resources/assets/nuclearcraft/shaders/post/q36_flash.json`

## 1. Forge 1.20.1 事件与客户端侧约束

### 1.1 `RegisterShadersEvent`

Forge 1.20.x 源码注释说明：

- `RegisterShadersEvent` 在 mod-specific event bus 上触发。
- 只在 logical client 上触发。
- 事件实现 `IModBusEvent`。
- 事件提供 `registerShader(ShaderInstance shaderInstance, Consumer<ShaderInstance> onLoaded)`。
- 事件提供 `getResourceProvider()`，可传给 `new ShaderInstance(...)`。

NuclearCraft-Neoteric 使用方式：

```java
@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NCShaders {
    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        registerShader(event, rl("rendertype_blackhole"), DefaultVertexFormat.POSITION_COLOR_TEX, BLACKHOLE_COLOR);
    }
}
```

关键点：

- 注册 core shader 必须在 mod event bus，不是 Forge runtime event bus。
- `ShaderInstance` 的 `ResourceLocation` 为 `nuclearcraft:rendertype_blackhole` 时，对应资源是 `assets/nuclearcraft/shaders/core/rendertype_blackhole.json`。
- `VertexFormat` 必须与 shader JSON 的 `attributes` 对齐。例如 `DefaultVertexFormat.POSITION_COLOR_TEX` 对应 `Position`、`Color`、`UV0`。

### 1.2 `RenderLevelStageEvent`

Forge 1.20.x 源码注释说明：

- `RenderLevelStageEvent` 只在 logical client 触发。
- 该事件不是 `IModBusEvent`，NuclearCraft-Neoteric 在 Forge event bus 上订阅。
- NuclearCraft-Neoteric 使用到：
  - `RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS`
  - `RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS`

已验证用法：

- `DistortShader`：在 `AFTER_CUTOUT_BLOCKS` 执行黑洞后处理。
- `Q36FlashShader`：在 `AFTER_CUTOUT_BLOCKS` 执行 Q36 flash 后处理。
- `NukeRenderer`：在 `AFTER_TRANSLUCENT_BLOCKS` 绘制核爆 billboard，并执行核爆 shockwave 后处理。
- `TickHandler`：在 `AFTER_TRANSLUCENT_BLOCKS` 批量绘制延迟透明 billboard。

### 1.3 Block entity renderer 注册

Forge 1.20.x `EntityRenderersEvent` 源码说明：

- `EntityRenderersEvent` 系列事件在 mod-specific event bus 上触发。
- 只在 logical client 上触发。
- `EntityRenderersEvent.RegisterRenderers` 提供：
  - `registerEntityRenderer(EntityType<? extends T>, EntityRendererProvider<T>)`
  - `registerBlockEntityRenderer(BlockEntityType<? extends T>, BlockEntityRendererProvider<T>)`

NuclearCraft-Neoteric 同时存在两种注册风格：

- `EntityRenderHandler` 使用 `EntityRenderersEvent.RegisterRenderers` 注册普通 entity renderer。
- `ClientSetup` 在 `FMLClientSetupEvent` 的 `event.enqueueWork` 里直接调用 `BlockEntityRenderers.register(...)` 注册多个 BER，其中包括黑洞：

```java
BlockEntityRenderers.register(KUGELBLITZ_BE.get("black_hole").get(), BlackholeRenderer::new);
```

AI 实现时的保守准则：

- 如果要贴近 Forge 1.20.x 事件 API，优先使用 `EntityRenderersEvent.RegisterRenderers` 注册 BER。
- 如果要按 NuclearCraft-Neoteric 原样迁移，可在 `FMLClientSetupEvent.enqueueWork` 中使用 `BlockEntityRenderers.register`。这不是推测；这是该项目当前代码里的做法。

## 2. Shader 资源路径规则（按已验证项目行为）

### 2.1 Core shader

Core shader 用于 `ShaderInstance` + `RenderType` 绘制世界几何或自定义顶点。

路径格式：

```text
assets/<namespace>/shaders/core/<shader_id>.json
assets/<namespace>/shaders/core/<shader_id>.vsh
assets/<namespace>/shaders/core/<shader_id>.fsh
```

示例：

- `new ShaderInstance(provider, nuclearcraft:rendertype_blackhole, POSITION_COLOR_TEX)`
- 加载 `assets/nuclearcraft/shaders/core/rendertype_blackhole.json`
- JSON 内：
  - `"vertex": "nuclearcraft:rendertype_blackhole"`
  - `"fragment": "nuclearcraft:rendertype_blackhole"`
- 加载：
  - `assets/nuclearcraft/shaders/core/rendertype_blackhole.vsh`
  - `assets/nuclearcraft/shaders/core/rendertype_blackhole.fsh`

### 2.2 Post-chain program shader

Post-chain shader 用于 `PostChain`。它的 post 文件在 `shaders/post`，program 文件在 `shaders/program`。

路径格式：

```text
assets/<namespace>/shaders/post/<post_id>.json
assets/<namespace>/shaders/program/<program_id>.json
assets/<namespace>/shaders/program/<program_id>.vsh
assets/<namespace>/shaders/program/<program_id>.fsh
```

已验证两种命名：

- NuclearCraft-Neoteric 的 post pass 写 `"name": "black_hole"`，没有 namespace，所以 program 放在 `assets/minecraft/shaders/program/black_hole.*`。
- 当前 Dust-and-Ash 的 post pass 写 `"name": "dustandash:cthulhu_noise"`，program 放在 `assets/dustandash/shaders/program/cthulhu_noise.*`。

### 2.3 覆盖 vanilla `minecraft` namespace shader

NuclearCraft-Neoteric 在 `assets/minecraft/shaders/core/rendertype_clouds.*` 放置文件。这是对 `minecraft` namespace 下 `rendertype_clouds` core shader 的替换。

该替换配合 `LevelRendererCloudsMixin` 使用：

- mixin 注入 `LevelRenderer.renderClouds(...)`
- 在 `VertexBuffer.drawWithShader(...)` 调用前取得 `RenderSystem.getShader()`
- 设置新增 uniform：
  - `CloudOriginWorld`
  - `Shockwave0`
  - `Shockwave1`
  - `Shockwave2`
  - `Shockwave3`
  - `ShockwaveCount`
  - `ShockwaveSoft`

## 3. NuclearCraft-Neoteric 黑洞视觉系统总览

黑洞效果由两条独立链路组成：

1. 可见黑洞核心 billboard：
   - `BlackholeRenderer`
   - `BillboardingEffectRenderer`
   - `TickHandler`
   - `NCRenderType.BLACKHOLE`
   - `NCShaders.BLACKHOLE_COLOR`
   - `assets/nuclearcraft/shaders/core/rendertype_blackhole.*`
   - texture：`assets/nuclearcraft/textures/particle/blackhole_glow.png`

2. 屏幕空间扭曲后处理：
   - `DistortShader`
   - `NCShaders.blackholePostEffect`
   - `assets/nuclearcraft/shaders/post/black_hole.json`
   - `assets/minecraft/shaders/program/black_hole.*`

这两条链路都存在并被调用。`BlackholeRenderer` 只负责 billboard 核心；后处理扭曲由 `DistortShader` 执行。

## 4. 黑洞 billboard 核心：完整调用链

### 4.1 `BlackholeRenderer`

类声明：

```java
public class BlackholeRenderer implements BlockEntityRenderer<BlockEntity>
```

字段：

- `CORE`：
  - 类型：`CustomEffect`
  - texture：`rl("textures/particle/blackhole_glow.png")`
  - 实际资源：`nuclearcraft:textures/particle/blackhole_glow.png`
- `MIN_SCALE = 0.1F`
- `MAX_SCALE = 4F`
- 静态初始化设置白色：
  - `CORE.setColor(Color.rgbai(255, 255, 255, 255))`

`render(...)` 行为：

1. 将传入 `BlockEntity` 强转为 `BlackHoleBE`。
2. 读取 `((BlackHoleBE) tile).getBlackholeScale()`。
3. 调用 `getBoundedScale(energyScale, MIN_SCALE, MAX_SCALE)`。
4. 调用 `BillboardingEffectRenderer.render(CORE.getTexture(), "nc.blackhole", Supplier<CustomEffect>)`。
5. 在 supplier 内：
   - 计算中心点：`Vec3.atCenterOf(tile.getBlockPos())`
   - `CORE.setPos(center)`
   - `CORE.setScale(scale)`
   - 返回 `CORE`

比例公式：

```java
private static float getBoundedScale(float scale, float min, float max) {
    return min + scale * (max - min);
}
```

注意：

- 函数名叫 `getBoundedScale`，但源码中没有 clamp。输入 `scale` 如果超过 `[0,1]`，输出会超过 `[min,max]`。
- `BlackHoleBE.getBlackholeScale()` 在目标源码中直接返回字段 `scale`。

### 4.2 `CustomEffect`

`CustomEffect` 保存 billboard 所需状态：

- `ResourceLocation texture`
- `int GRID_SIZE`
- `Vec3 pos`
- `Color color`
- `float scale`

构造：

- `new CustomEffect(texture)` 默认 `gridSize=4`
- `new CustomEffect(texture, gridSize)` 可显式设置 atlas 网格数量

方法：

- `setPos(Vec3)`
- `setScale(float)`
- `setColor(Color)`
- `getPos(float partialTick)`
- `getScale()`
- `getTexture()`
- `getTextureGridSize()`

### 4.3 `BillboardingEffectRenderer`

对外入口：

```java
public static void render(ResourceLocation texture, String profilerSection, Supplier<CustomEffect> lazyEffect)
```

行为：

1. 调用 `TickHandler.addTransparentRenderer(...)`。
2. RenderType 使用 `NCRenderType.BLACKHOLE.apply(texture)`。
3. 传入一个 `TickHandler.LazyRender`。
4. 真正绘制发生在 `LazyRender.render(...)` 中。

内部绘制：

- `gridSize = effect.getTextureGridSize()`
- `tick = renderTick % (gridSize * gridSize)`
- `yIndex = tick % gridSize`
- `xIndex = tick / gridSize`
- `spriteSize = 1F / gridSize`
- 使用 `camera.rotation()` 获取 billboard 朝向。
- 初始四个顶点：
  - `(-1,  1, 0)`
  - `( 1,  1, 0)`
  - `( 1, -1, 0)`
  - `(-1, -1, 0)`
- 每个顶点：
  - 先用 camera quaternion 旋转
  - 再乘以 `effect.getScale()`
  - 再加上 `effect.getPos(partialTick)`
- UV：
  - `minU = xIndex * spriteSize`
  - `maxU = minU + spriteSize`
  - `minV = yIndex * spriteSize`
  - `maxV = minV + spriteSize`
- Vertex format 实际写入：
  - position
  - color
  - UV

顶点写入顺序：

```java
buffer.vertex(matrix, v0).color(...).uv(minU, maxV).endVertex();
buffer.vertex(matrix, v1).color(...).uv(maxU, maxV).endVertex();
buffer.vertex(matrix, v2).color(...).uv(maxU, minV).endVertex();
buffer.vertex(matrix, v3).color(...).uv(minU, minV).endVertex();
```

### 4.4 `TickHandler` 延迟透明渲染

队列：

```java
private static final Map<RenderType, List<TickHandler.LazyRender>> transparentRenderers = new HashMap<>();
```

添加：

```java
public static void addTransparentRenderer(RenderType renderType, LazyRender render)
```

渲染事件：

- 订阅 `RenderLevelStageEvent`
- 只在 `AFTER_TRANSLUCENT_BLOCKS` 执行

绘制前矩阵处理：

```java
Vec3 camVec = camera.getPosition();
matrix.translate(-camVec.x, -camVec.y, -camVec.z);
```

渲染流程：

1. 从 `Minecraft.renderBuffers().bufferSource()` 取得 `MultiBufferSource.BufferSource`。
2. 对每个 RenderType 分组调用 `renderer.getBuffer(renderType)`。
3. 执行每个 `LazyRender.render(...)`。
4. 调用 `renderer.endBatch(renderType)`。
5. 清空 `transparentRenderers`。

透明排序：

- 如果只有一个 RenderType 分组，直接绘制。
- 如果多个 RenderType 分组，计算每组内最近渲染点到 camera 的距离平方。
- 使用 `Comparator.comparingDouble(info -> -info.closest)` 排序，即距离大的组先绘制。

## 5. `NCRenderType.BLACKHOLE`

`NCRenderType` 继承 `RenderType`，构造器私有，只通过静态函数创建。

`BLACKHOLE` 类型：

```java
public static final Function<ResourceLocation, RenderType> BLACKHOLE = Util.memoize(resourceLocation -> {
    RenderType.CompositeState state = RenderType.CompositeState.builder()
            .setShaderState(NCShaders.BLACKHOLE_COLOR.shard)
            .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .setOutputState(RenderType.MAIN_TARGET)
            .setLightmapState(NO_LIGHTMAP)
            .setOverlayState(NO_OVERLAY)
            .createCompositeState(true);
    return create("nc_blackhole", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, true, true, state);
});
```

细节：

- `Util.memoize`：同一个 texture `ResourceLocation` 复用同一个 RenderType。
- shader：`NCShaders.BLACKHOLE_COLOR.shard`
- texture：传入的 `resourceLocation`
- transparency：`TRANSLUCENT_TRANSPARENCY`
- output：`MAIN_TARGET`
- lightmap：`NO_LIGHTMAP`
- overlay：`NO_OVERLAY`
- composite sort 参数：`createCompositeState(true)`
- vertex format：`POSITION_COLOR_TEX`
- draw mode：`QUADS`
- buffer size：`256`
- affects crumbling：`true`
- sort on upload：`true`

## 6. Core shader：`nuclearcraft:rendertype_blackhole`

### 6.1 JSON

路径：

```text
assets/nuclearcraft/shaders/core/rendertype_blackhole.json
```

字段：

- `blend`：
  - `func`: `add`
  - `srcrgb`: `srcalpha`
  - `dstrgb`: `1-srcalpha`
- `vertex`: `nuclearcraft:rendertype_blackhole`
- `fragment`: `nuclearcraft:rendertype_blackhole`
- `attributes`：
  - `Position`
  - `Color`
  - `UV0`
- `samplers`：
  - `Sampler0`
- `uniforms`：
  - `ModelViewMat` `matrix4x4` count `16`
  - `ProjMat` `matrix4x4` count `16`
  - `IViewRotMat` `matrix3x3` count `9`
  - `ColorModulator` `float` count `4`
  - `FogStart` `float` count `1`
  - `FogEnd` `float` count `1`
  - `FogShape` `int` count `1`

### 6.2 Vertex shader

路径：

```text
assets/nuclearcraft/shaders/core/rendertype_blackhole.vsh
```

关键内容：

- `#version 150`
- `#moj_import <fog.glsl>`
- 输入：
  - `in vec3 Position`
  - `in vec4 Color`
  - `in vec2 UV0`
- uniform：
  - `ModelViewMat`
  - `ProjMat`
  - `IViewRotMat`
  - `FogShape`
- 输出：
  - `vertexDistance`
  - `vertexColor`
  - `texCoord0`
- `gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0)`
- `vertexDistance = fog_distance(ModelViewMat, IViewRotMat * Position, FogShape)`
- `vertexColor = Color`
- `texCoord0 = UV0`

### 6.3 Fragment shader

路径：

```text
assets/nuclearcraft/shaders/core/rendertype_blackhole.fsh
```

关键内容：

- `#version 150`
- `#moj_import <fog.glsl>`
- sampler：
  - `Sampler0`
- uniform：
  - `ColorModulator`
  - `FogStart`
  - `FogEnd`
- 输入：
  - `vertexDistance`
  - `vertexColor`
  - `texCoord0`
- 输出：
  - `fragColor`
- 颜色公式：
  - `texture(Sampler0, texCoord0) * vertexColor`
  - 如果 alpha `< 0.1`，`discard`
  - 最终乘 `ColorModulator` 和 `linear_fog_fade(...)`

## 7. 黑洞后处理链：`DistortShader`

### 7.1 注册

`ClientSetup` 在 `FMLClientSetupEvent.enqueueWork` 中调用：

```java
DistortShader.register();
```

`DistortShader.register()`：

```java
MinecraftForge.EVENT_BUS.register(DistortShader.class);
```

事件类注解：

```java
@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class DistortShader
```

### 7.2 黑洞位置注册表

`DistortShader` 内部有：

```java
public static final BlackholeRegistry blackhole = new BlackholeRegistry();
```

`BlackholeRegistry`：

- `Set<BlockPos> positions`
- `contains(BlockPos)`
- `add(BlockPos)`
- `remove(BlockPos)`
- `getPositions()` 返回不可修改视图

后处理只遍历该 registry 内的 `BlockPos`。AI 实现时必须确认 block entity 创建/移除时把位置加入/移出 registry；否则 post-chain 不会处理任何黑洞。

### 7.3 执行阶段

`onRenderTick(RenderLevelStageEvent event)`：

- stage 必须是 `AFTER_CUTOUT_BLOCKS`
- `Minecraft.getInstance()`
- 配置开关：`KUGELBLITZ_CONFIG.BLACKHOLE_SHADER.get()` 为 false 时直接返回
- `blackholePostEffect != null` 才执行

resize：

```java
if (currentSize != mc.getWindow().getWidth() + mc.getWindow().getHeight()) {
    currentSize = mc.getWindow().getWidth() + mc.getWindow().getHeight();
    blackholePostEffect.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
    effect.getUniform("BlurDir").set(0.2f, 0.0f);
}
```

渲染状态：

- `RenderSystem.enableDepthTest()`
- `RenderSystem.depthMask(false)`
- 对每个黑洞位置：
  - `processBlackHole(...)`
  - 成功则 `blackholePostEffect.process(mc.getFrameTime())`
- 绑定主 framebuffer 写入：
  - `mc.getMainRenderTarget().bindWrite(false)`
- 绑定 post-chain 最后一 pass 输出为读：
  - `blackholePostEffect.passes.get(blackholePostEffect.passes.size()-1).outTarget.bindRead()`
- depth func：
  - `RenderSystem.depthFunc(515)`
- blend：
  - `enableBlend`
  - `blendFuncSeparate(SRC_ALPHA, ONE_MINUS_SRC_ALPHA, ONE, ZERO)`
- 绘制全屏 quad：
  - `VertexFormat.Mode.QUADS`
  - `DefaultVertexFormat.POSITION_TEX`
  - 四个顶点使用 main render target width/height
- 恢复：
  - `depthFunc(515)`
  - `depthMask(true)`
  - `disableBlend()`

### 7.4 `processBlackHole(...)`

输入：

- `Minecraft mc`
- `RenderLevelStageEvent event`
- `EffectInstance effect`
- `BlockPos pos`

前置检查：

- 源码写的是 `if (mc.level == null && mc.player == null) return false;`
- 只要二者不是同时 null 就继续。注意这是源码原样；如果要修改，必须单独测试。
- 玩家到黑洞中心距离大于 `64` 时返回 false。
- `mc.level.getExistingBlockEntity(pos)` 必须是 `BlackHoleBE`，否则返回 false。

scale：

```java
float scaleMult = 0.3f / blackHoleBE.scale;
if (scaleMult != 1) {
    scaleMult = (float) Math.pow(scaleMult + 0.375f, 5);
}
```

世界坐标投影到屏幕 UV：

1. 取 view matrix：`event.getPoseStack().last().pose()`
2. 取 projection matrix：`RenderSystem.getProjectionMatrix()`
3. 取 camera position：`mc.gameRenderer.getMainCamera().getPosition()`
4. 黑洞中心减 camera：
   - `pos.getX() + 0.5 - cameraPos.x()`
   - `pos.getY() + 0.5 - cameraPos.y()`
   - `pos.getZ() + 0.5 - cameraPos.z()`
5. `Vector4f(posX, posY, posZ, 1.0f)`
6. 先乘 view matrix，再乘 projection matrix
7. 如果 `w != 0`，进行 perspective divide：
   - `x /= w`
   - `y /= w`
   - `z /= w`
8. depth：
   - `normalizedDepth = (z + 1.0f) * 0.5f`
9. 若 `z` 在 `(-1, 1)`：
   - `blurX = x * 0.5f + 0.5f`
   - `blurY = y * 0.5f + 0.5f`
10. 屏幕边缘 margin：
    - `0.1f`
    - `blurX`/`blurY` 在 `[-0.1, 1.1]` 内才认为可见

radius/magnification：

```java
distanceFactor = 7f / distance;
baseRadius = blackholeVisible ? 150.0f : 0.0f;
radius = baseRadius * distanceFactor * scaleMult;
baseMagnification = blackholeVisible ? 5.8f : 0.1f;
```

设置 uniforms：

```java
effect.getUniform("BlurPos").set(blurX, blurY);
effect.getUniform("Radius").set(radius, baseMagnification / scaleMult);
effect.getUniform("BlackHoleDepth").set(normalizedDepth);
```

## 8. 后处理资源：`black_hole`

### 8.1 Post-chain JSON

路径：

```text
assets/nuclearcraft/shaders/post/black_hole.json
```

结构：

- `targets`: `["swap"]`
- `passes` 两个：
  1. `name: "black_hole"`
     - `intarget: "minecraft:main"`
     - `outtarget: "swap"`
     - aux target：`DepthSampler` -> `minecraft:main:depth`
     - uniforms：
       - `BlurPos`
       - `BlurDir`
       - `Radius`
       - `BlackHoleDepth`
  2. `name: "black_hole"`
     - `intarget: "swap"`
     - `outtarget: "minecraft:main"`
     - aux target：`DepthSampler` -> `minecraft:main:depth`
     - uniforms：
       - `BlurDir`
       - `BlurPos`
       - `Radius`
       - `BlackHoleDepth`

因为 pass `name` 没有 namespace，program 文件位于：

```text
assets/minecraft/shaders/program/black_hole.json
assets/minecraft/shaders/program/black_hole.vsh
assets/minecraft/shaders/program/black_hole.fsh
```

### 8.2 Program JSON

路径：

```text
assets/minecraft/shaders/program/black_hole.json
```

字段：

- `blend`：
  - `func`: `add`
  - `srcrgb`: `one`
  - `dstrgb`: `zero`
- `vertex`: `black_hole`
- `fragment`: `black_hole`
- `attributes`：
  - `Position`
- `samplers`：
  - `DiffuseSampler`
  - `DepthSampler`
- `uniforms`：
  - `ProjMat`
  - `InSize`
  - `OutSize`
  - `BlurPos`
  - `BlurDir`
  - `Radius`
  - `BlackHoleDepth`

### 8.3 Vertex shader

路径：

```text
assets/minecraft/shaders/program/black_hole.vsh
```

输入/输出：

- `in vec4 Position`
- uniforms：
  - `ProjMat`
  - `OutSize`
  - `InSize`
  - `BlurPos`
  - `BlurDir`
  - `Radius`
  - `ScreenSize`
- outputs：
  - `texCoord`
  - `oneTexel`
  - `dummyOut`

核心：

- `outPos = ProjMat * vec4(Position.xy, 0.0, 1.0)`
- `gl_Position = vec4(outPos.xy, 0.2, 1.0)`
- `oneTexel = 1.0 / InSize`
- `texCoord = Position.xy / OutSize`
- 源码特意用 `dummyOut` 引用 `BlurPos`、`BlurDir`、`Radius`，注释说明目的为避免编译器优化掉这些 uniform。

### 8.4 Fragment shader

路径：

```text
assets/minecraft/shaders/program/black_hole.fsh
```

输入：

- `DiffuseSampler`
- `DepthSampler`
- `texCoord`
- `oneTexel`

uniform：

- `InSize`
- `BlurPos`
- `Radius`
- `BlurDir`
- `BlackHoleDepth`

含义按源码注释与实际公式：

- `Radius.x`：lens radius in pixels
- `Radius.y`：magnification factor，最低用 `max(1.1, Radius.y)`
- `BlurPos`：屏幕 UV 中心
- `BlurDir.x`：被用于 feather 计算，`featherAmount = clamp(BlurDir.x * 1.5, 0.2, 0.65)`
- `BlackHoleDepth`：黑洞位置投影深度

关键行为：

- 读取原像素颜色：`texture(DiffuseSampler, texCoord)`
- 读取原深度：`texture(DepthSampler, texCoord).r`
- 半径基准：
  - `normalizedRadius = Radius.x / 2160.0`
  - `lensRadiusPixels = normalizedRadius * InSize.y`
  - `lensRadiusUV = lensRadiusPixels / InSize.y`
- aspect correction：
  - `aspectRatio = InSize.x / InSize.y`
  - 距离用 `vec2((texCoord.x - BlurPos.x) * aspectRatio, texCoord.y - BlurPos.y)`
- 影响范围：
  - `outerRadius = lensRadiusUV * aspectRatio`
  - `innerRadius = outerRadius * (1.0 - featherAmount)`
  - `glowRadius = outerRadius * 1.2`
- 太远直接返回原颜色。
- 如果 `originalDepth < BlackHoleDepth`，直接返回原颜色。不要在未测试前改写此比较。
- 畸变：
  - 旋转强度固定 `0.7`
  - 越靠中心旋转越强
  - magnification 影响采样点
  - 中心区域变暗
  - 边缘平滑混合回原颜色
- glow：
  - 紫色 `vec3(0.5, 0.0, 0.7)`
  - 强度 `0.1`

## 9. `NCShaders` 注册与 `ShaderTracker`

`NCShaders` 订阅 mod event bus：

```java
@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NCShaders
```

静态字段：

- `ShaderTracker BLACKHOLE_COLOR`
- `ShaderTracker NUKE_CORE`
- `ShaderTracker NUKE_SMOKE`
- `PostChain blackholePostEffect`
- `PostChain nukePostEffect`
- `PostChain q36FlashPostEffect`

`shaderRegistry(RegisterShadersEvent event)`：

1. 注册 `nuclearcraft:rendertype_blackhole`，format `POSITION_COLOR_TEX`。
2. 注册 `nuclearcraft:rendertype_nuke`，format `POSITION_COLOR_TEX`。
3. 注册 `nuclearcraft:rendertype_nuke_smoke`，format `POSITION_COLOR_TEX`。
4. 创建并 resize 三个 `PostChain`：
   - `nuclearcraft:shaders/post/nuke.json`
   - `nuclearcraft:shaders/post/black_hole.json`
   - `nuclearcraft:shaders/post/q36_flash.json`

`registerShader(...)`：

```java
event.registerShader(
    new ShaderInstance(event.getResourceProvider(), shaderLocation, vertexFormat),
    tracker::setInstance
);
```

`ShaderTracker`：

- 实现 `Supplier<ShaderInstance>`
- 保存 `ShaderInstance instance`
- 暴露 `RenderStateShard.ShaderStateShard shard = new RenderStateShard.ShaderStateShard(this)`
- `NCRenderType` 直接把 `shard` 放入 RenderType state。

## 10. `assets/minecraft/shaders` 文件清单与细节

### 10.1 `core/rendertype_clouds.*`

用途：

- 替换 vanilla `minecraft:rendertype_clouds` core shader。
- 配合 `LevelRendererCloudsMixin` 在云层上应用核爆 shockwave wipe。

JSON：

- `vertex`: `rendertype_clouds`
- `fragment`: `rendertype_clouds`
- `attributes`：
  - `Position`
  - `Color`
  - `Normal`
- `samplers`: 空
- `uniforms`：
  - `ModelViewMat`
  - `ProjMat`
  - `ColorModulator`
  - `FogStart`
  - `FogEnd`
  - `FogColor`
  - `CloudOriginWorld`
  - `ShockwaveCount`
  - `Shockwave0`
  - `Shockwave1`
  - `Shockwave2`
  - `Shockwave3`
  - `ShockwaveSoft`

VSH：

- 输入 `Position`、`Color`、`Normal`
- 计算：
  - `gl_Position`
  - `vertexDistance = fog_distance(ModelViewMat, Position, 0)`
  - `vertexColor = Color * ColorModulator`
  - `worldXZ = CloudOriginWorld.xz + Position.xz`

FSH：

- 对每个 shockwave：
  - `vec4 sw` 的 `x,z` 是中心，`w` 是半径
  - `sw.w <= 0` 时保留 alpha
  - `distance(worldXZ, sw.xz)` 与半径比较
  - 在 `r - soft` 内完全 discard，在 `r + soft` 外完全保留，中间 smoothstep
- 最多处理 4 个 shockwave。
- `ShockwaveCount` 决定处理几个。
- `keep <= 0.001` 时 discard。
- 最终 `color.a *= keep`，再 `linear_fog(...)`。

Mixin 设置：

- `CloudOriginWorld` 由 cloud render 的 camera/cloud 偏移推导。
- `Shockwave0..3` 来自 `BombFxManager.active()` 中最多 4 个 `ActiveBomb`。
- `sw` 设置为：
  - x：`b.epicenter.getX() + 0.5f`
  - y：`cloudHeight`
  - z：`b.epicenter.getZ() + 0.5f`
  - w：`b.cloudWipeRadius(partialTick)`
- `ShockwaveSoft` 固定 `24.0f`。

### 10.2 `program/black_hole.*`

用途：

- 黑洞屏幕空间畸变。
- 被 `assets/nuclearcraft/shaders/post/black_hole.json` 调用。
- Java 调用方：`DistortShader`。

字段与行为见第 8 章。

### 10.3 `program/nuke.*`

用途：

- 核爆 shockwave 屏幕空间畸变。
- 被 `assets/nuclearcraft/shaders/post/nuke.json` 调用。
- Java 调用方：`NukeRenderer.onPostFx(...)`。

JSON：

- `vertex`: `nuke`
- `fragment`: `nuke`
- `attributes`: `Position`
- `samplers`：
  - `DiffuseSampler`
  - `DepthSampler`
- `uniforms`：
  - `ProjMat`
  - `InSize`
  - `OutSize`
  - `BlurPos`
  - `BlurDir`
  - `Radius`
  - `BlackHoleDepth`

VSH：

- 与 `black_hole.vsh` 结构相同。
- 使用 `dummyOut` 引用 `BlurPos`、`BlurDir`、`Radius`，防止被优化掉。

FSH：

- uniform 注释：
  - `BlurPos`: screen-space ring center in UV
  - `Radius.x`: ring radius
  - `Radius.y`: ring thickness
  - `BlurDir.x`: distortion strength
  - `BlurDir.y`: brightening multiplier
  - `BlackHoleDepth`: detonation point depth
- 读取 `DiffuseSampler` 与 `DepthSampler`。
- 如果像素在 detonation point 前方，不畸变。
- 对环带附近像素做径向折射、轻微色散和亮化。

Java uniforms：

- `NukeRenderer.setShockwaveUniforms(...)` 设置：
  - `BlurPos`
  - `Radius`
  - `BlurDir`
  - `BlackHoleDepth`
- `BlurPos` 与 depth 通过 world-to-screen 投影计算。
- `Radius.x` 是屏幕 UV 下的环半径。
- `Radius.y` 是 thickness。
- `BlurDir.x` 是 strength。
- `BlurDir.y` 是 brighten。

### 10.4 `program/q36_flash.*`

用途：

- Q36 能量 flash 的屏幕空间畸变、glow、bolt/rim 效果。
- 被 `assets/nuclearcraft/shaders/post/q36_flash.json` 调用。
- Java 调用方：`Q36FlashShader`。

JSON：

- `vertex`: `q36_flash`
- `fragment`: `q36_flash`
- `attributes`: `Position`
- `samplers`：
  - `DiffuseSampler`
  - `DepthSampler`
- `uniforms`：
  - `ProjMat`
  - `InSize`
  - `OutSize`
  - `BlurPos`
  - `BlurDir`
  - `Radius`
  - `BlackHoleDepth`
  - `Time`

VSH：

- 与 `black_hole.vsh`/`nuke.vsh` 同类结构。
- `dummyOut` 保留 `BlurPos`、`BlurDir`、`Radius`。

FSH：

- 继承黑洞类 lens distortion 基础。
- 增加 `Time`。
- 增加 jagged radial bolt、rim pulse 等 flash 视觉。

Java uniforms：

- `Q36FlashShader.processFlash(...)` 设置：
  - `BlurPos`
  - `Radius`
  - `BlackHoleDepth`
  - `Time`
- resize 时设置：
  - `BlurDir = (0.2f, 0.0f)`

## 11. `assets/nuclearcraft/shaders` 文件清单与细节

### 11.1 `core/rendertype_blackhole.*`

用途：

- 黑洞核心 billboard 的 RenderType shader。
- Java 调用方：
  - `NCShaders.BLACKHOLE_COLOR`
  - `NCRenderType.BLACKHOLE`
  - `BillboardingEffectRenderer`
  - `BlackholeRenderer`

字段见第 6 章。

### 11.2 `core/rendertype_nuke.*`

用途：

- 核爆 fireball、shockwave、smoke 等 billboard/ground quad 的 core shader。
- Java 调用方：`NukeRenderer`。

JSON：

- `vertex`: `nuclearcraft:rendertype_nuke`
- `fragment`: `nuclearcraft:rendertype_nuke`
- `attributes`：
  - `Position`
  - `Color`
  - `UV0`
- `samplers`：
  - `Sampler0`
  - `Sampler1`
  - `Sampler2`
  - `Sampler3`
- `uniforms`：
  - `ModelViewMat`
  - `ProjMat`
  - `ColorModulator`
  - `GameTime`
  - `NukeData`
  - `SpriteRect`

VSH：

- 输入 `Position`、`Color`、`UV0`
- 输出 `vertexColor`、`texCoord0`
- `gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0)`

FSH：

- `Sampler0`: 核爆主 atlas texture
- `Sampler1..3`: 三张噪声 texture
- `NukeData`: Java 侧写入 `phase`、`progress`、`yieldN`、`alpha`
- `SpriteRect`: 当前 atlas 子图矩形
- shader 内按 phase 处理不同效果：
  - fireball
  - shockwave
  - smoke
  - stem
  - ground
  - secondary white shockwave

Java 侧 texture 绑定：

```java
RenderSystem.setShaderTexture(0, TEXTURE);
RenderSystem.setShaderTexture(1, NOISE_0);
RenderSystem.setShaderTexture(2, NOISE_1);
RenderSystem.setShaderTexture(3, NOISE_2);
```

### 11.3 `core/rendertype_nuke_smoke.json`

该 JSON：

- 使用同一个 vertex：`nuclearcraft:rendertype_nuke`
- 使用同一个 fragment：`nuclearcraft:rendertype_nuke`
- attributes、samplers、uniforms 与 `rendertype_nuke.json` 对齐

差别不在 JSON 片段中直接体现；实际 pass 区分由 Java 的 `NCShaders.NUKE_SMOKE` 和 RenderSystem blend/state 调用路径决定。

### 11.4 `post/nuke.json`

结构：

- `targets`: `["swap"]`
- 单 pass：
  - `name`: `nuke`
  - `intarget`: `minecraft:main`
  - `outtarget`: `swap`
  - aux target：`DepthSampler` -> `minecraft:main:depth`
  - uniforms：
    - `BlurPos`
    - `BlurDir`
    - `Radius`
    - `BlackHoleDepth`

Java composite：

- `NukeRenderer.onPostFx(...)`
- `post.process(mc.getFrameTime())`
- 将最后 pass 的 `outTarget` 读回并绘制到 main framebuffer。

### 11.5 `post/q36_flash.json`

结构：

- `targets`: `["swap"]`
- 两个 pass：
  1. `q36_flash`: `minecraft:main -> swap`
  2. `q36_flash`: `swap -> minecraft:main`
- 每个 pass 都带 `DepthSampler` aux target。
- uniforms：
  - `BlurPos`
  - `BlurDir`
  - `Radius`
  - `BlackHoleDepth`
  - `Time`

Java composite：

- `Q36FlashShader.onRenderTick(...)`
- 遍历 `mc.level.entitiesForRendering()`
- 只处理 `Q36EnergyFlash`
- 距离超过 `64` 不处理
- 生命周期结束不处理
- 对每个有效 flash 调用 `q36FlashPostEffect.process(mc.getFrameTime())`

## 12. 当前 Dust-and-Ash 项目现状

当前工作区已有 shader 资源：

```text
src/main/resources/assets/dustandash/shaders/core/cthulhu_phase.json
src/main/resources/assets/dustandash/shaders/core/cthulhu_phase.vsh
src/main/resources/assets/dustandash/shaders/core/cthulhu_phase.fsh
src/main/resources/assets/dustandash/shaders/post/cthulhu_phase.json
src/main/resources/assets/dustandash/shaders/program/cthulhu_noise.json
src/main/resources/assets/dustandash/shaders/program/cthulhu_noise.fsh
```

`cthulhu_phase` core shader：

- JSON：
  - `vertex`: `dustandash:cthulhu_phase`
  - `fragment`: `dustandash:cthulhu_phase`
  - `attributes`: `Position`, `Color`
  - uniforms:
    - `ModelViewMat`
    - `ProjMat`
    - `PhaseTint`
- VSH：
  - 输入 `Position`、`Color`
  - 输出 `vertexColor`
- FSH：
  - `fragColor = vertexColor * PhaseTint`

`cthulhu_phase` post-chain：

- `targets`: `swap`
- pass 1：
  - `name`: `dustandash:cthulhu_noise`
  - `intarget`: `minecraft:main`
  - `outtarget`: `swap`
  - uniform `Time`
- pass 2：
  - `name`: `blit`
  - `intarget`: `swap`
  - `outtarget`: `minecraft:main`

`cthulhu_noise` program：

- JSON：
  - `vertex`: `sobel`
  - `fragment`: `dustandash:cthulhu_noise`
  - `attributes`: `Position`
  - sampler: `DiffuseSampler`
  - uniforms:
    - `ProjMat`
    - `OutSize`
    - `Time`
- FSH：
  - 读取 `DiffuseSampler`
  - 使用 `OutSize` 和 `Time` 生成 scan/grain
  - 给原 RGB 加青绿色 tint

当前 Java 搜索结果：

- 未发现 `ShaderInstance`
- 未发现 `RegisterShadersEvent`
- 未发现 `PostChain`
- 未发现 Java 引用 `cthulhu_phase` 或 `cthulhu_noise`
- 已有 `RenderLevelStageEvent` 用在 `CthulhuClientRenderEvents`
- 已有 `RenderType` 用在 `CthulhuClientRenderEvents`

因此当前资源文件本身存在，但缺少已验证的 Java 注册和执行链路。

## 13. 迁移/实现清单：core shader + RenderType + BER

如果要在 Dust-and-Ash 中实现类似黑洞核心 billboard，最小链路如下。

### 13.1 资源

创建或复用：

```text
assets/dustandash/shaders/core/<shader_id>.json
assets/dustandash/shaders/core/<shader_id>.vsh
assets/dustandash/shaders/core/<shader_id>.fsh
assets/dustandash/textures/particle/<texture>.png
```

如果使用 `DefaultVertexFormat.POSITION_COLOR_TEX`，JSON attributes 必须包含：

```json
["Position", "Color", "UV0"]
```

### 13.2 注册 shader

创建 client-only shader holder，订阅 mod bus：

```java
@Mod.EventBusSubscriber(modid = DustAndAsh.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModShaders {
    public static final ShaderTracker EFFECT = new ShaderTracker();

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
            new ShaderInstance(event.getResourceProvider(),
                new ResourceLocation(DustAndAsh.MODID, "<shader_id>"),
                DefaultVertexFormat.POSITION_COLOR_TEX),
            EFFECT::setInstance
        );
    }
}
```

`ShaderTracker` 必须能被 `RenderStateShard.ShaderStateShard` 作为 `Supplier<ShaderInstance>` 使用。

### 13.3 定义 RenderType

按 `NCRenderType.BLACKHOLE`：

- shader state 使用 `ModShaders.EFFECT.shard`
- texture state 传入 texture
- transparency 用 `TRANSLUCENT_TRANSPARENCY`
- output 用 `MAIN_TARGET`
- lightmap/overlay 按需要选择；NuclearCraft 黑洞使用 `NO_LIGHTMAP` 和 `NO_OVERLAY`
- format 用 `POSITION_COLOR_TEX`
- mode 用 `QUADS`

### 13.4 渲染队列

如果在 BER 内直接画透明 quad，排序和 buffer flush 容易与世界透明 pass 冲突。NuclearCraft 的做法是：

1. BER 每帧只构造/更新 effect 数据。
2. 调用一个全局延迟透明队列。
3. 在 `RenderLevelStageEvent.AFTER_TRANSLUCENT_BLOCKS` 统一绘制并 `endBatch(renderType)`。

迁移时至少要保留这些行为：

- 使用 camera 逆平移把世界坐标放回渲染坐标系。
- billboard 顶点用 `camera.rotation()` 旋转。
- 每帧清空队列，避免重复绘制旧 effect。
- 透明对象按距离从远到近绘制。

### 13.5 注册 BER

Forge 事件 API 路线：

```java
@SubscribeEvent
public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(MOD_BLOCK_ENTITY.get(), MyRenderer::new);
}
```

NuclearCraft 原样路线：

```java
event.enqueueWork(() ->
    BlockEntityRenderers.register(MOD_BLOCK_ENTITY.get(), MyRenderer::new)
);
```

二者都需要 client-only 侧执行。

## 14. 迁移/实现清单：post-chain 屏幕空间效果

### 14.1 资源

post-chain：

```text
assets/dustandash/shaders/post/<post_id>.json
```

program：

```text
assets/dustandash/shaders/program/<program_id>.json
assets/dustandash/shaders/program/<program_id>.vsh
assets/dustandash/shaders/program/<program_id>.fsh
```

post pass 如果写：

```json
{ "name": "dustandash:<program_id>" }
```

则 program 放在 `assets/dustandash/shaders/program/<program_id>.*`。

post pass 如果写：

```json
{ "name": "<program_id>" }
```

则按 NuclearCraft 的已验证布局，program 放在 `assets/minecraft/shaders/program/<program_id>.*`。

### 14.2 创建 `PostChain`

NuclearCraft 在 `RegisterShadersEvent` 中创建：

```java
PostChain chain = new PostChain(
    mc.getTextureManager(),
    mc.getResourceManager(),
    mc.getMainRenderTarget(),
    new ResourceLocation(MODID, "shaders/post/<post_id>.json")
);
chain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
```

### 14.3 运行时 resize

NuclearCraft 用 `width + height` 做尺寸变化检测：

```java
int dim = mc.getWindow().getWidth() + mc.getWindow().getHeight();
if (currentSize != dim) {
    currentSize = dim;
    chain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
}
```

注意：`width + height` 不唯一。例如 `1000x800` 与 `900x900` 相同。源码就是这样写；如要改成 width/height 独立缓存，需要实际测试。

### 14.4 设置 uniform

从 post-chain 取 effect：

```java
EffectInstance effect = chain.passes.get(0).getEffect();
```

设置：

```java
effect.getUniform("BlurPos").set(x, y);
effect.getUniform("Radius").set(radius, magnificationOrThickness);
effect.getUniform("BlackHoleDepth").set(depth);
```

必须保证：

- program JSON `uniforms` 中声明了该 uniform。
- GLSL 中声明了该 uniform。
- GLSL 没有被编译器优化掉；NuclearCraft 的 post VSH 通过 `dummyOut` 保留某些 uniform。

### 14.5 世界坐标投影到屏幕 UV

NuclearCraft 三处后处理都使用同类公式：

```java
Matrix4f viewMatrix = event.getPoseStack().last().pose();
Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix();
Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();

Vector4f p = new Vector4f(
    (float)(worldX - cameraPos.x),
    (float)(worldY - cameraPos.y),
    (float)(worldZ - cameraPos.z),
    1.0f
);
p.mul(viewMatrix);
p.mul(projectionMatrix);
if (p.w != 0.0f) {
    p.x /= p.w;
    p.y /= p.w;
    p.z /= p.w;
}

float uvX = p.x * 0.5f + 0.5f;
float uvY = p.y * 0.5f + 0.5f;
float depth = (p.z + 1.0f) * 0.5f;
```

可见性检查：

- `p.z > -1.0f && p.z < 1.0f`
- `uvX/uvY` 在 `[-margin, 1 + margin]`

### 14.6 执行与合成

执行：

```java
chain.process(mc.getFrameTime());
```

合成回 main framebuffer：

```java
mc.getMainRenderTarget().bindWrite(false);
chain.passes.get(chain.passes.size() - 1).outTarget.bindRead();
RenderSystem.depthFunc(515);
RenderSystem.enableBlend();
RenderSystem.blendFuncSeparate(SRC_ALPHA, ONE_MINUS_SRC_ALPHA, ONE, ZERO);
// draw fullscreen quad using POSITION_TEX
```

结束恢复：

```java
RenderSystem.depthFunc(515);
RenderSystem.depthMask(true);
RenderSystem.disableBlend();
```

## 15. 常见失败点（均来自已检查代码路径）

1. 只放 shader 资源，不注册 `ShaderInstance` 或 `PostChain`：资源不会自动执行。
2. core shader JSON 的 `attributes` 与 `VertexFormat` 不匹配：顶点输入会错。
3. post pass `name` 与 program 文件 namespace 不匹配：找不到 program。
4. uniform 只在 Java 设置，但 JSON 或 GLSL 未声明：`getUniform(...)` 可能失败或返回不可用对象。
5. uniform 在 GLSL 中声明但没有实际参与输出：编译器可能优化；NuclearCraft 用 `dummyOut` 防止关键 uniform 被优化。
6. 在 BER 内直接绘制透明 quad 而不管理排序和 flush：NuclearCraft 选择延迟到 `AFTER_TRANSLUCENT_BLOCKS` 批处理。
7. post-chain 未随窗口 resize：NuclearCraft 每帧检查窗口尺寸并 resize。
8. 后处理 world-to-screen 投影没有减 camera position：NuclearCraft 所有相关代码都先把世界坐标转为相机相对坐标。
9. 忘记恢复 `depthMask(true)` 或关闭 blend：会污染后续渲染状态。
10. 黑洞后处理 registry 没有位置：`DistortShader` 只遍历 `BlackholeRegistry.positions`。

## 16. AI 实现时的硬性约束

- 不要把 `RegisterShadersEvent` 放到 Forge event bus；源码证明它是 mod event bus 事件。
- 不要把 `RenderLevelStageEvent` 放到 mod event bus；NuclearCraft 在 Forge event bus 上使用它。
- 不要把 unnamespaced post program 和 namespaced program 路径混用。
- 不要假设 `BlackholeRenderer` 自己运行后处理；它只排队 billboard，扭曲在 `DistortShader`。
- 不要假设当前 Dust-and-Ash 的 shader 已生效；当前 Java 搜索未发现注册/执行链路。
- 修改任何 depth 比较或 post-chain composite 逻辑前必须实际进游戏验证截图或日志；这些逻辑依赖 Minecraft framebuffer/depth texture 行为。

