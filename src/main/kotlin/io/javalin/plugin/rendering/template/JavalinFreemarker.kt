/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.plugin.rendering.template

import freemarker.template.Configuration
import freemarker.template.Version
import io.javalin.http.Context

import io.javalin.plugin.rendering.RenderingDependency
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.JavalinRenderer
import io.javalin.util.DependencyUtil
import java.io.StringWriter

object JavalinFreemarker : FileRenderer {

    fun init() {
        JavalinRenderer.register(JavalinFreemarker, ".ftl")
    }

    private var configuration: Configuration? = null
    private val defaultConfiguration: Configuration by lazy { defaultFreemarkerEngine() }

    @JvmStatic
    fun configure(staticConfiguration: Configuration) {
        configuration = staticConfiguration
    }

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context?): String {
        DependencyUtil.ensurePresence(RenderingDependency.FREEMARKER)
        val stringWriter = StringWriter()
        (configuration ?: defaultConfiguration).getTemplate(filePath).process(model, stringWriter)
        return stringWriter.toString()
    }

    private fun defaultFreemarkerEngine() = Configuration(Version(2, 3, 26)).apply {
        setClassForTemplateLoading(JavalinFreemarker::class.java, "/")
    }

}
