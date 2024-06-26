import de.undercouch.gradle.tasks.download.Download

object Properties {
    const val MOD_VERSION = "3.0"
    const val MOD_NAME = "Nanobass-Chat"
    const val MODID = "nanobass_chat"
    const val MAVEN_GROUP = "com.example.examplemod"
    const val COSMIC_REACH_VERSION = "0.1.21"
    const val LOADER_VERSION = "0.15.7"
    const val FLUX_VERSION = "0.5.4"
}

val modJarName = "${Properties.MOD_NAME}_${Properties.MOD_VERSION}-CR_${Properties.COSMIC_REACH_VERSION}-all"
val modJarPath = "$projectDir/build/libs/$modJarName.jar"

println("Mod JAR path: $modJarPath")

plugins {
    id("java")
    id("de.undercouch.download") version "5.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()

    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.quiltmc.org/repository/release")

}

// Required Fabric Dependencies
dependencies {
    implementation("com.google.guava:guava:33.0.0-jre")
    implementation("com.google.code.gson:gson:2.9.1")

    shadow("net.fabricmc:fabric-loader:0.15.7")
    shadow("net.fabricmc:tiny-mappings-parser:0.2.2.14")
    shadow("net.fabricmc:access-widener:2.1.0")
    shadow("net.fabricmc:sponge-mixin:0.12.5+mixin.0.8.5")

    shadow("org.ow2.asm:asm:9.6")
    shadow("org.ow2.asm:asm-util:9.6")
    shadow("org.ow2.asm:asm-tree:9.6")
    shadow("org.ow2.asm:asm-analysis:9.6")
    shadow("org.ow2.asm:asm-commons:9.6")
    shadow("io.github.llamalad7:mixinextras-fabric:0.3.5")

    shadow(files("$projectDir/run/cosmic-reach.jar"))
    shadow(files("$projectDir/run/loader.jar"))
}

// Embedded | Project Dependencies
dependencies {
    shadow ("dev.crmodders:FluxAPI:${Properties.FLUX_VERSION}")
}

base.archivesName = modJarName

val properties = mapOf(
    "version" to Properties.MOD_VERSION,
    "loader_version" to Properties.LOADER_VERSION,
    "cosmic_reach_version" to Properties.COSMIC_REACH_VERSION,
    "flux_version" to Properties.FLUX_VERSION,
    "mod_name" to Properties.MOD_NAME,
    "modid" to Properties.MODID,
)

tasks.processResources {
    inputs.properties(properties)

    filesMatching("fabric.mod.json") {
        expand(properties)
    }
}

tasks.register("setupEnvironment") {
    group = "crmodders"

    dependsOn("downloadCosmicReach")
    dependsOn("downloadLoader")
}

tasks.register<Download>("downloadCosmicReach") {
    group = "crmodders"

    src("https://cosmic-archive.netlify.app/Cosmic%20Reach-${Properties.COSMIC_REACH_VERSION}.jar")
    acceptAnyCertificate(true)
    dest("$projectDir/run/cosmic-reach.jar")
}

tasks.register<Download>("downloadLoader") {
    group = "crmodders"

    src("https://github.com/GalacticLoader/GalacticLoader/releases/download/${Properties.LOADER_VERSION}/GalacticLoader-${Properties.LOADER_VERSION}.jar")
    acceptAnyCertificate(true)
    dest("$projectDir/run/loader.jar")
}

tasks.register("clearCache") {
    group = "crmodders"

    if (project.file(path).exists())
        project.file(path).delete()
}

tasks.register("runClient") {
    group = "crmodders"

    dependsOn("clearCache")
    dependsOn("shadowJar")

    doLast{
        var betterClasspath = listOf<File>()
        betterClasspath = betterClasspath.plus(sourceSets.main.get().compileClasspath)
        betterClasspath = betterClasspath.plus(file(modJarPath))

        javaexec {
            workingDir("$projectDir/run")
            jvmArgs("-Dfabric.skipMcProvider=true","-Dfabric.development=false", "-Dfabric.addMods=C:\\Users\\mwsch\\IdeaProjects\\ChatMod\\build\\libs\\Nanobass-Chat_3.0-CR_0.1.21-all-all.jar")
            classpath(betterClasspath)
            mainClass = "net.fabricmc.loader.impl.launch.knot.KnotClient"
        }
    }
}
