/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.plugin.rendering.template

import io.javalin.http.Context
import io.javalin.plugin.rendering.FileRenderer
import io.javalin.plugin.rendering.JavalinRenderer
import io.javalin.plugin.rendering.OptionalDependency
import io.javalin.plugin.rendering.ensureDependencyPresent
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.WebContext
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

object JavalinThymeleaf : FileRenderer {

    init {
        JavalinRenderer.register(JavalinThymeleaf, ".html", ".tl", ".thyme", ".thymeleaf")
    }

    private var templateEngine: TemplateEngine? = null
    private val defaultTemplateEngine: TemplateEngine by lazy { defaultThymeLeafEngine() }

    @JvmStatic
    fun configure(staticTemplateEngine: TemplateEngine) {
        templateEngine = staticTemplateEngine
    }

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context): String {
        ensureDependencyPresent(OptionalDependency.THYMELEAF)
        val context = WebContext(ctx.req, ctx.res, ctx.req.servletContext)
        context.setVariables(model)
        return (templateEngine ?: defaultTemplateEngine).process(filePath, context)
    }

    private fun defaultThymeLeafEngine() = TemplateEngine().apply {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            templateMode = TemplateMode.HTML
        })
    }

}
