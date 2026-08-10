import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    id("dev.architectury.loom") version "1.11.458"
    id("eclipse")
    id("idea")
    id("maven-publish")
}

val modId = property("mod.id").toString()
val modName = property("mod.name").toString()
val modVersion = property("mod.version").toString()
val modGroup = property("mod.group").toString()
val modAuthors = property("mod.authors").toString()
val modDescription = property("mod.description").toString()
val modLicense = property("mod.license").toString()

val minecraftVersion = property("vers.mcVersion").toString()
val forgeVersion = property("vers.deps.fml").toString()
val jeiVersion = property("deps.jei").toString()
val geckolibVersion = property("deps.geckolib").toString()
val patchouliVersion = property("deps.patchouli").toString()

group = modGroup
version = "$modVersion+$minecraftVersion"

base {
    archivesName.set("$modId-forge")
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

loom {
    silentMojangMappingsLicense()
    if (stonecutter.current.isActive) {
        runConfigs.all {
            ideConfigGenerated(true)
            runDir("../../run")
        }
    }
}

sourceSets {
    main {
        resources.srcDir(rootProject.file("src/generated/resources"))
    }
}

repositories {
    mavenCentral()
    maven {
        name = "Progwml6"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        name = "BlameJared"
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        name = "ModMaven"
        url = uri("https://modmaven.dev/")
    }
    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        content {
            includeGroupByRegex("software\\.bernie.*")
            includeGroup("com.eliotlash.mclib")
        }
    }
}

dependencies {
    "minecraft"("com.mojang:minecraft:$minecraftVersion")
    "mappings"(loom.officialMojangMappings())
    "forge"("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")

    "modCompileOnly"("mezz.jei:jei-$minecraftVersion-common-api:$jeiVersion")
    "modCompileOnly"("mezz.jei:jei-$minecraftVersion-forge-api:$jeiVersion")
    "modRuntimeOnly"("mezz.jei:jei-$minecraftVersion-forge:$jeiVersion")

    "modImplementation"("software.bernie.geckolib:geckolib-forge-$minecraftVersion:$geckolibVersion")
    "forgeRuntimeLibrary"("com.eliotlash.mclib:mclib:20")

    "modCompileOnly"("vazkii.patchouli:Patchouli:$minecraftVersion-$patchouliVersion:api")
    "modRuntimeOnly"("vazkii.patchouli:Patchouli:$minecraftVersion-$patchouliVersion")
}

tasks.configureEach {
    if (name == "createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn("stonecutterGenerate")
    options.encoding = "UTF-8"
    options.release.set(17)
}

tasks.named<ProcessResources>("processResources") {
    dependsOn("stonecutterGenerate")

    val replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to project.property("vers.minecraftRange"),
        "forge_version" to forgeVersion,
        "forge_version_range" to project.property("vers.forgeRange"),
        "loader_version_range" to project.property("vers.loaderRange"),
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "mod_authors" to modAuthors,
        "mod_description" to modDescription,
    )
    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(replaceProperties + mapOf("project" to project))
    }
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes(
            "Specification-Title" to modId,
            "Specification-Vendor" to modAuthors,
            "Specification-Version" to "1",
            "Implementation-Title" to project.name,
            "Implementation-Version" to archiveVersion.get(),
            "Implementation-Vendor" to modAuthors,
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri(rootProject.layout.projectDirectory.dir("mcmodsrepo"))
        }
    }
}