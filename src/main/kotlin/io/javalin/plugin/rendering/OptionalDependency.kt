/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.plugin.rendering

import io.javalin.core.util.JavalinLogger
import io.javalin.core.util.Util
import io.javalin.http.InternalServerErrorResponse
import java.net.URLEncoder
import java.util.HashMap

enum class OptionalDependency(val displayName: String, val testClass: String, val groupId: String, val artifactId: String, val version: String) {
    JTE("jte", "gg.jte.TemplateEngine", "gg.jte", "jte", "1.12.0"),
    JTE_KOTLIN("jte-kotlin", "gg.jte.compiler.kotlin.KotlinClassCompiler", "gg.jte", "jte-kotlin", "1.12.0"),
    VELOCITY("Velocity", "org.apache.velocity.app.VelocityEngine", "org.apache.velocity", "velocity-engine-core", "2.3"),
    FREEMARKER("Freemarker", "freemarker.template.Configuration", "org.freemarker", "freemarker", "2.3.30"),
    THYMELEAF("Thymeleaf", "org.thymeleaf.TemplateEngine", "org.thymeleaf", "thymeleaf", "3.0.12.RELEASE"),
    MUSTACHE("Mustache", "com.github.mustachejava.MustacheFactory", "com.github.spullara.mustache.java", "compiler", "0.9.7"),
    PEBBLE("Pebble", "com.mitchellbosecke.pebble.PebbleEngine", "io.pebbletemplates", "pebble", "3.1.5"),
    COMMONMARK("Commonmark", "org.commonmark.renderer.html.HtmlRenderer", "org.commonmark", "commonmark", "0.17.1"),
}

private val dependencyCheckCache = HashMap<String, Boolean>()

fun ensureDependencyPresent(dependency: OptionalDependency, startupCheck: Boolean = false) {
    if (dependencyCheckCache[dependency.testClass] == true) {
        return
    }
    if (!Util.classExists(dependency.testClass)) {
        val message = missingDependencyMessage(dependency)
        if (startupCheck) {
            throw IllegalStateException(message)
        } else {
            JavalinLogger.warn(message)
            throw InternalServerErrorResponse(message)
        }
    }
    dependencyCheckCache[dependency.testClass] = true
}

internal fun missingDependencyMessage(dependency: OptionalDependency) = """|
    |-------------------------------------------------------------------
    |Missing dependency '${dependency.displayName}'. Add the dependency.
    |
    |pom.xml:
    |<dependency>
    |    <groupId>${dependency.groupId}</groupId>
    |    <artifactId>${dependency.artifactId}</artifactId>
    |    <version>${dependency.version}</version>
    |</dependency>
    |
    |build.gradle:
    |implementation group: '${dependency.groupId}', name: '${dependency.artifactId}', version: '${dependency.version}'
    |
    |Find the latest version here:
    |https://search.maven.org/search?q=${URLEncoder.encode("g:" + dependency.groupId + " AND a:" + dependency.artifactId, "UTF-8")}
    |-------------------------------------------------------------------""".trimMargin()
