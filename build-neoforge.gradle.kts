import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

plugins {
    id("dev.isxander.modstitch.base") version "0.8.5"
    id("maven-publish")
}

val modId = property("mod.id").toString()
val mcVersion = property("deps.minecraft").toString()
val targetNeoForgeVersion = property("deps.neoforge").toString()
val artifactVersion = "${property("mod.version")}+$mcVersion"

group = property("mod.group").toString()
version = artifactVersion
base.archivesName = "$modId-neoforge"

modstitch {
    minecraftVersion = mcVersion
    moddevgradle {
        neoForgeVersion = targetNeoForgeVersion
        defaultRuns()
    }
}

extensions.configure<net.neoforged.moddevgradle.dsl.NeoForgeExtension> {
    runs.configureEach {
        gameDirectory.set(rootProject.file(providers.gradleProperty("dustandash.runDir").getOrElse("run/neoforge")))
        if (name == "server") programArgument("--nogui")
    }
}

repositories {
    mavenCentral()
    maven("https://maven.neoforged.net/releases/")
    exclusiveContent {
        forRepository { maven("https://maven.theillusivec4.top/") }
        filter { includeGroup("top.theillusivec4.curios") }
    }
    exclusiveContent {
        forRepository { maven("https://maven.blamejared.com/") }
        filter { includeGroup("mezz.jei"); includeGroup("net.mezzdev.config"); includeGroup("vazkii.patchouli") }
    }
    exclusiveContent {
        forRepository { maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") }
        filter { includeGroupByRegex("software\\.bernie.*") }
    }
}

dependencies {
    implementation("software.bernie.geckolib:geckolib-neoforge-$mcVersion:${property("deps.geckolib")}")
    implementation("top.theillusivec4.curios:curios-neoforge:${property("deps.curios")}")
    compileOnly("mezz.jei:jei-$mcVersion-neoforge-api:${property("deps.jei")}")
    runtimeOnly("mezz.jei:jei-$mcVersion-neoforge:${property("deps.jei")}") { isTransitive = false }
    runtimeOnly("vazkii.patchouli:Patchouli:$mcVersion-${property("deps.patchouli")}")
}

sourceSets.main {
    resources.srcDir(rootProject.file("src/generated/resources"))
    if (!providers.gradleProperty("dustandash.smokeChecks").isPresent) java.exclude("**/PortSmokeChecks.java")
}

fun neoNbtComponents(nbt: String): Map<String, Any> = when {
    nbt.matches(Regex("\\{Damage:[0-9]+}")) -> mapOf("minecraft:damage" to nbt.removePrefix("{Damage:").removeSuffix("}").toInt())
    nbt.startsWith("{Potion:\"") -> mapOf("minecraft:potion_contents" to mapOf("potion" to nbt.removePrefix("{Potion:\"").removeSuffix("\"}")))
    else -> error("Add an explicit component migration for legacy NBT: $nbt")
}

// Forge datagen is the canonical source. Convert a copy when packaging 1.21.1.
fun neoResource(value: Any?): Any? {
return when (value) {
    is String -> when {
        value == "minecraft:grass" -> "minecraft:short_grass"
        value == "minecraft:scute" -> "minecraft:turtle_scute"
        value == "#forge:cobblestone" -> "#c:cobblestones"
        value == "#forge:gravel" -> "#c:gravels"
        value.startsWith("forge:") -> "c:" + value.removePrefix("forge:")
        value.startsWith("#forge:") -> "#c:" + value.removePrefix("#forge:")
        else -> value
    }
    is List<*> -> value.map(::neoResource)
    is Map<*, *> -> {
        val json = value.entries.associate { it.key.toString() to it.value }.toMutableMap()
        if (json["type"] == "forge:partial_nbt") {
            return mapOf("type" to "dustandash:custom_data", "base" to neoResource(mapOf("item" to json["item"])), "nbt" to json["nbt"])
        }
        if (json["type"] == "forge:nbt") {
            return mapOf("type" to "neoforge:components", "items" to listOf(neoResource(json["item"])), "components" to neoNbtComponents(json["nbt"].toString()), "strict" to true)
        }
        if (json["nbt"] is String) {
            json["components"] = neoNbtComponents(json.remove("nbt").toString())
        }
        if (json["type"] == "forge:conditional") {
            val alternatives = json["recipes"] as List<*>
            require(alternatives.size == 1) { "Multiple conditional recipe alternatives need an explicit NeoForge conversion" }
            val entry = alternatives.single() as Map<*, *>
            val recipe = (entry["recipe"] as Map<*, *>).entries.associate { it.key.toString() to it.value }.toMutableMap()
            recipe["neoforge:conditions"] = (entry["conditions"] as List<*>).map { condition ->
                (condition as Map<*, *>).entries.associate { (key, v) ->
                    key.toString() to if (key == "type") v.toString().replace("forge:", "neoforge:") else v
                }
            }
            return neoResource(recipe)
        }
        val type = json["type"]?.toString().orEmpty()
        if (type == "patchouli:shapeless_book_recipe") {
            json["type"] = "minecraft:crafting_shapeless"
            json["result"] = mapOf("id" to "patchouli:guide_book", "components" to mapOf("patchouli:book" to json.remove("book")))
        }
        if (type.startsWith("minecraft:") || type == "patchouli:shapeless_book_recipe") {
            when (val result = json["result"]) {
                is String -> json["result"] = mapOf("id" to result)
                is Map<*, *> -> if (result.containsKey("item")) {
                    json["result"] = result.entries.associate { (key, v) -> (if (key == "item") "id" else key.toString()) to v }
                }
            }
        }
        val result = json["result"] as? Map<*, *>
        if (result?.get("id") == "dustandash:placeholder") {
            json["result"] = result.entries.associate { it.key.toString() to it.value } + ("components" to mapOf("minecraft:max_stack_size" to 2))
        }
        json.mapValues { neoResource(it.value) }
    }
    else -> value
}
}

tasks.processResources {
    dependsOn("stonecutterGenerate")
    val props = mapOf(
        "mod_id" to modId,
        "mod_name" to project.property("mod.name"),
        "mod_version" to project.property("mod.version"),
        "mod_authors" to project.property("mod.authors"),
        "mod_description" to project.property("mod.description"),
        "mod_license" to project.property("mod.license"),
        "loader_version_range" to project.property("vers.loaderRange"),
        "minecraft_version_range" to project.property("vers.minecraftRange"),
        "neoforge_version_range" to project.property("vers.neoForgeRange"),
        "curios_version_range" to project.property("vers.curiosRange"),
    )
    inputs.properties(props)
    filesMatching("META-INF/neoforge.mods.toml") { expand(props) }
    exclude("META-INF/mods.toml")
    exclude(".cache/**")
    includeEmptyDirs = false
    eachFile {
        if (path.startsWith("data/")) {
            val segments = path.split('/').toMutableList()
            if (segments[1] == "forge") segments[1] = "c"
            val names = mapOf("recipes" to "recipe", "loot_tables" to "loot_table", "advancements" to "advancement", "functions" to "function")
            segments[2] = names[segments[2]] ?: segments[2]
            if (segments[2] == "tags" && segments.size > 3) {
                val tags = mapOf("items" to "item", "blocks" to "block", "entity_types" to "entity_type", "fluids" to "fluid", "game_events" to "game_event", "functions" to "function")
                segments[3] = tags[segments[3]] ?: segments[3]
            }
            path = segments.joinToString("/")
        }
    }
    doLast {
        destinationDir.walkTopDown().filter { it.isFile && (it.extension == "json" || it.name == "pack.mcmeta") }.forEach { file ->
            val json = neoResource(JsonSlurper().parse(file))
            if (file.name == "pack.mcmeta") {
                @Suppress("UNCHECKED_CAST")
                ((json as MutableMap<String, Any?>)["pack"] as MutableMap<String, Any?>)["pack_format"] = 34
            }
            file.writeText(JsonOutput.prettyPrint(JsonOutput.toJson(json)), Charsets.UTF_8)
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn("stonecutterGenerate")
    options.encoding = "UTF-8"
    options.release.set(21)
    options.compilerArgs.addAll(listOf("-Xmaxerrs", "1000"))
}
tasks.withType<Jar>().configureEach { archiveVersion.set(artifactVersion) }
java { withSourcesJar(); toolchain.languageVersion.set(JavaLanguageVersion.of(21)) }

tasks.withType<JavaExec>().configureEach { standardInput = System.`in` }
