plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.20.1-forge"

stonecutter parameters {
    val loader = current.project.substringAfterLast('-')
    constants {
        match(loader, "forge", "neoforge")
    }
}

tasks.register("validateCurseForgePublish") {
    group = "publishing"
    description = "Checks release configuration without uploading."
    doLast {
        check(!providers.gradleProperty("dustandash.smokeChecks").isPresent) {
            "Smoke-check builds cannot be published. Use the default build."
        }
        val dryRun = providers.gradleProperty("publish.dryRun").map { it.toBooleanStrict() }.getOrElse(false)
        val id = providers.gradleProperty("curseforge.projectId").getOrElse("").trim()
        check(id.matches(Regex("[1-9][0-9]*"))) { "Set a numeric curseforge.projectId in gradle.properties." }
        check(providers.gradleProperty("curseforge.releaseType").getOrElse("release") in listOf("release", "beta", "alpha")) {
            "curseforge.releaseType must be release, beta or alpha."
        }
        if (!dryRun) {
            val token = providers.environmentVariable("CURSEFORGE_TOKEN").filter { it.isNotBlank() }
                .orElse(providers.gradleProperty("curseforge.token").filter { it.isNotBlank() })
            check(token.isPresent) { "Set CURSEFORGE_TOKEN or curseforge.token in user-level Gradle properties." }
        }
        val changelog = providers.environmentVariable("CHANGELOG").getOrNull()?.takeIf { it.isNotBlank() }
            ?: file("CHANGELOG.md").takeIf { it.isFile }?.readText().orEmpty()
        check(changelog.isNotBlank()) { "Provide nonempty CHANGELOG.md or the CHANGELOG environment variable." }
        logger.lifecycle("CurseForge project: $id; dry run: $dryRun")
    }
}

tasks.register("buildAllVersions") {
    group = "build"
    description = "Builds the Forge and NeoForge release JARs."
    dependsOn(stonecutter.tasks.named("build").map { it.values })
}
tasks.register("publishAllVersions") {
    group = "publishing"
    description = "Publishes the Forge and NeoForge release JARs to CurseForge."
    dependsOn(stonecutter.tasks.named("publishMods").map { it.values })
}
stonecutter.tasks.order("publishCurseforge")
