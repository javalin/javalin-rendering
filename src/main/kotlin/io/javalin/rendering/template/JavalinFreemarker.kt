/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.rendering.template

import freemarker.template.Configuration
import freemarker.template.Version
import io.javalin.http.Context
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.JavalinRenderer
import io.javalin.rendering.util.RenderingDependency
import io.javalin.rendering.util.Util
import java.io.StringWriter

class JavalinFreemarker(private val configuration: Configuration) : FileRenderer {

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context?): String {
        val stringWriter = StringWriter()
        configuration.getTemplate(filePath).process(model, stringWriter)
        return stringWriter.toString()
    }

    companion object {
        @JvmStatic
        fun init(configuration: Configuration? = null) {
            Util.throwIfNotAvailable(RenderingDependency.FREEMARKER)
            val fileRenderer = JavalinFreemarker(configuration ?: defaultFreemarkerEngine())
            JavalinRenderer.register(fileRenderer, ".ftl")
        }

        fun defaultFreemarkerEngine() = Configuration(Version(2, 3, 26)).apply {
            setClassForTemplateLoading(JavalinFreemarker::class.java, "/")
        }
    }

}
