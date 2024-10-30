import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    kotlin("jvm") version "2.0.21"
    id("fabric-loom") version "1.8.+"
    id("maven-publish")
}
val versionSuffix: String = project.findProperty("version") as String? ?: "1.21.1"

val versionSpecificProperties = file("versions/$versionSuffix/gradle.properties")

if (versionSpecificProperties.exists()) {
    val props = Properties()
    versionSpecificProperties.inputStream().use { props.load(it) }
    props.forEach { (key, value) ->
        project.extensions.extraProperties[key.toString()] = value
    }
} else {
    throw GradleException("Properties file for version $versionSuffix not found.")
}

val mcVersion = property("mcVersion")!!.toString()
val mcDep = property("mcDep").toString()

version = project.property("mod_version") as String
group = "net.shade"

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    withSourcesJar()
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("potionoverlay") {
            sourceSet("main")
            sourceSet("client")
        }
    }
}

repositories {
    maven("https://maven.isxander.dev/releases") {
        name = "Xander Maven"
    }
    maven("https://maven.terraformersmc.com/") {
        name = "Terraformers"
    }
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") }
        filter { includeGroup("maven.modrinth") }
    }
    exclusiveContent {
        forRepository { maven("https://cursemaven.com") }
        filter { includeGroup("curse.maven") }
    }
}
dependencies {

    minecraft("com.mojang:minecraft:$mcVersion")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")

    modImplementation("net.fabricmc:fabric-loader:${property("loaderVersion")}")
    val fapiVersion = property("fabric_version").toString()
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:$fapiVersion") // so you can do `depends: fabric-api` in FMJ

    listOf(
        "fabric-lifecycle-events-v1",
        "fabric-key-binding-api-v1",
        "fabric-rendering-v1"
    ).forEach {
        modImplementation(fabricApi.module(it, fapiVersion))
    }

    modImplementation("net.fabricmc:fabric-language-kotlin:${property("kotlin_loader_version")}")
    optionalProp("modmenu_version") {
        modImplementation("com.terraformersmc:modmenu:$it")
    }

    modApi("dev.isxander:yet-another-config-lib:${property("yacl_version")}") {
        exclude(group = "net.fabricmc.fabric-api", module = "fabric-api")
    }
}

//tasks.processResources {
//    inputs.property("version", project.version)
//    inputs.property("minecraft_version", project.property("minecraft_version"))
//    inputs.property("loader_version", project.property("loader_version"))
//    filteringCharset = "UTF-8"
//
//    filesMatching("fabric.mod.json") {
//        expand(
//            "version" to project.version,
//            "minecraft_version" to project.property("minecraft_version"),
//            "loader_version" to project.property("loader_version"),
//            "kotlin_loader_version" to project.property("kotlin_loader_version")
//        )
//    }
//}
//
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

//tasks.jar {
//    from("LICENSE") {
//        rename { "${it}_${project.base.archivesName}" }
//    }
//}

tasks {
    processResources {
        val modId: String by project
        val modName: String by project
        val modDescription: String by project
        val githubProject: String by project
        val loaderVersion: String by project.extra
        val mcDep : String by project.extra
        val kotlin_loader_version: String by project.extra
        val yacl_version: String by project.extra
        println("Mod ID: $modId")
        println("Mod Name: $modName")
        println("Mod Description: $modDescription")
        println("Github Project: $githubProject")
        println("Loader Version: $loaderVersion")
        println("MC Version: $mcDep")
        println("Kotlin Loader Version: $kotlin_loader_version")
        println("YACL Version: $yacl_version")

        val props = mapOf(
            "id" to modId,
            "group" to project.group,
            "name" to modName,
            "description" to modDescription,
            "version" to project.version,
            "github" to githubProject,
            "mc" to mcDep,
            "loader_version" to loaderVersion,
            "kotlin_loader_version" to kotlin_loader_version,
            "minecraft_version" to mcDep,
            "yacl_version" to yacl_version
        )

        props.forEach(inputs::property)

        filesMatching("fabric.mod.json") {
            expand(props)
        }

//        register("releaseMod") {
//            group = "mod"
//
//            dependsOn("publish")
//        }
    }
//    register("releaseMod") {
//        group = "mod"
//
//        dependsOn("publish")
//    }
}
//publishMods {
//    displayName.set("Zoomify $versionWithoutMC for MC $mcVersion")
//    file.set(tasks.remapJar.get().archiveFile)
//    changelog.set(
//        rootProject.file("changelogs/${versionWithoutMC}.md")
//            .takeIf { it.exists() }
//            ?.readText()
//            ?: "No changelog provided."
//    )
//    type.set(when {
//        isAlpha -> ALPHA
//        isBeta -> BETA
//        else -> STABLE
//    })
//    modLoaders.add("fabric")
//
//    fun versionList(prop: String) = findProperty(prop)?.toString()
//        ?.split(',')
//        ?.map { it.trim() }
//        ?: emptyList()
//}
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}
fun <T> optionalProp(property: String, block: (String) -> T?) {
    findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)
}

