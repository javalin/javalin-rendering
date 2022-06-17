/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.plugin.rendering.markdown

import io.javalin.core.util.DependencyUtil
import io.javalin.http.Context
import io.javalin.plugin.rendering.FileRenderer
import io.javalin.plugin.rendering.JavalinRenderer
import io.javalin.plugin.rendering.RenderingDependency
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

object JavalinCommonmark : FileRenderer {

    init {
        JavalinRenderer.register(JavalinCommonmark, ".md", ".markdown")
    }

    private var renderer: HtmlRenderer? = null
    private val defaultRenderer: HtmlRenderer by lazy {
        HtmlRenderer.builder().build()
    }

    private var parser: Parser? = null
    private val defaultParser: Parser by lazy {
        Parser.builder().build()
    }

    @JvmStatic
    fun configure(staticHtmlRenderer: HtmlRenderer, staticMarkdownParser: Parser) {
        renderer = staticHtmlRenderer
        parser = staticMarkdownParser
    }

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context?): String {
        DependencyUtil.ensurePresence(RenderingDependency.COMMONMARK)
        val fileContent = JavalinCommonmark::class.java.getResource(filePath).readText()
        return (renderer ?: defaultRenderer).render((parser ?: defaultParser).parse(fileContent))
    }

}
