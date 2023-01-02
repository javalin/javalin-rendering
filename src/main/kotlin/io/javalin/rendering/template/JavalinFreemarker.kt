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
import io.javalin.rendering.util.RenderingDependency.FREEMARKER
import io.javalin.rendering.util.Util
import java.io.StringWriter

class JavalinFreemarker @JvmOverloads constructor(
    private val configuration: Configuration = defaultFreemarkerEngine()
) : FileRenderer {

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context): String {
        val stringWriter = StringWriter()
        configuration.getTemplate(filePath).process(model, stringWriter)
        return stringWriter.toString()
    }

    companion object {
        val extensions = arrayOf(".ftl")

        @JvmStatic
        @JvmOverloads
        fun init(configuration: Configuration? = null) {
            Util.throwIfNotAvailable(FREEMARKER)
            val fileRenderer = JavalinFreemarker(configuration ?: defaultFreemarkerEngine())
            JavalinRenderer.register(fileRenderer, *extensions)
        }

        fun defaultFreemarkerEngine() = Configuration(Version(2, 3, 26)).apply {
            setClassForTemplateLoading(JavalinFreemarker::class.java, "/")
        }
    }

    class Loader : JavalinRenderer.FileRendererLoader {
        override fun load() = if (!JavalinRenderer.hasRenderer(*extensions) && FREEMARKER.exists()) init() else Unit
    }

}
