# CurseForge 自动发布

目标项目为 **972493**。脚本沿用 StrongerWanderer 的使用方式，自动构建并发布以下两个正式 JAR：

| Minecraft | 加载器 | 游戏 Java | 构建任务 |
| --- | --- | --- | --- |
| 1.20.1 | Forge | 17 | `:1.20.1-forge:build` |
| 1.21.1 | NeoForge | 21 | `:1.21.1-neoforge:build` |

脚本适用于 Windows PowerShell 5.1 和 PowerShell 7。Gradle 使用 Java 25；脚本自动检查 `JAVA_HOME`、Gradle JDK 缓存和 PATH，也可以传入 `-JavaHome <JDK25目录>`。

## 配置

`gradle.properties` 中的 `mod.version` 控制版本号，`curseforge.projectId=972493` 指定项目，`curseforge.releaseType` 可选 `release`、`beta`、`alpha`。每次发布前更新 `CHANGELOG.md`；临时的 `CHANGELOG` 环境变量可覆盖其全文。

Token 通过当前进程的 `CURSEFORGE_TOKEN` 环境变量或用户级 Gradle 属性 `curseforge.token` 读取，环境变量优先。用户级文件位于 `%GRADLE_USER_HOME%\gradle.properties`；未设置该变量时为 `%USERPROFILE%\.gradle\gradle.properties`。不要将 Token 写入项目文件。环境检查只显示是否已配置，试运行使用占位 Token。

```powershell
# 检查 JDK、项目 ID、更新日志和凭据状态
.\scripts\setup-publish.ps1

# 构建、检查正式 JAR，并演练发布；不会上传，也不需要 Token
.\scripts\dryrun.ps1

# 依赖已缓存时可离线演练
.\scripts\dryrun.ps1 -Offline

# 构建并列出待发布文件，确认后上传两个版本
.\scripts\publish.ps1

# 用于已配置凭据的无人值守调用，直接上传
.\scripts\publish.ps1 -Yes
```

若需修改公开项目 ID，运行 `setup-publish.ps1 -ProjectId <数字ID>`。脚本会保留 Java 环境变量的原值，并在结束后恢复。

## Gradle 入口

```powershell
.\gradlew.bat buildAllVersions
.\gradlew.bat publishAllVersions '-Ppublish.dryRun=true'
.\gradlew.bat publishAllVersions
```

也可单独调用 `:1.20.1-forge:publishCurseforge` 或 `:1.21.1-neoforge:publishCurseforge`，它们同样执行构建和发布校验。直接运行 Gradle 发布任务会上传，不会交互确认；演练时必须带 `-Ppublish.dryRun=true`。

Forge 上传 Loom 的 `remapJar`，NeoForge 上传正式 `jar`，文件名为 `dustandash-<加载器>-<版本号>+<Minecraft版本>.jar`。不会选择旧版本包或 sources JAR。发布会校验加载器元数据，并拒绝包含 `PortSmokeChecks` 的测试包和带 `dustandash.smokeChecks` 的构建。两个上传任务顺序执行；若部分上传失败，先检查 CurseForge 上已有文件，再单独重试失败的目标，避免重复上传。

发布元数据标注客户端与服务端、对应 Minecraft / Java / 加载器版本，以及必需依赖 GeckoLib、Curios，推荐安装的可选依赖 JEI、Patchouli。依赖关系采用 [Mod Publish Plugin 的 CurseForge 配置](https://modmuss50.github.io/mod-publish-plugin/platforms/curseforge/)。
