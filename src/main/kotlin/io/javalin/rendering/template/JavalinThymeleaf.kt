/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.rendering.template

import io.javalin.http.Context
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.JavalinRenderer
import io.javalin.rendering.util.RenderingDependency
import io.javalin.rendering.util.Util
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.WebContext
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.web.servlet.JakartaServletWebApplication

class JavalinThymeleaf(private var templateEngine: TemplateEngine) : FileRenderer {

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context): String {
        // ctx.req.servletContext that is passed to buildApplication has to match ctx.req.servletContext passed into
        // buildExchange. (application.servletContext === ctx.req.servletContext)
        val application = JakartaServletWebApplication.buildApplication(ctx.req().servletContext)
        val webExchange = application.buildExchange(ctx.req(), ctx.res())
        val context = WebContext(webExchange, webExchange.locale, model)
        return templateEngine.process(filePath, context)
    }

    companion object {
        @JvmStatic
        fun init(templateEngine: TemplateEngine? = null) {
            Util.throwIfNotAvailable(RenderingDependency.THYMELEAF)
            JavalinRenderer.register(JavalinThymeleaf(templateEngine ?: defaultThymeLeafEngine()), ".html", ".tl", ".thyme", ".thymeleaf")
        }

        private fun defaultThymeLeafEngine() = TemplateEngine().apply {
            setTemplateResolver(ClassLoaderTemplateResolver().apply {
                templateMode = TemplateMode.HTML
            })
        }
    }

}
