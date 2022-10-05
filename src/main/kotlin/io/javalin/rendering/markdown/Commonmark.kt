/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.rendering.markdown

import io.javalin.http.Context
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.JavalinRenderer
import io.javalin.rendering.util.RenderingDependency
import io.javalin.rendering.util.Util
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

class JavalinCommonmark(
    private var renderer: HtmlRenderer,
    private var parser: Parser
) : FileRenderer {

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context?): String {
        val fileContent = JavalinCommonmark::class.java.getResource(filePath).readText()
        return renderer.render(parser.parse(fileContent))
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        fun init(htmlRenderer: HtmlRenderer? = null, parser: Parser? = null) {
            Util.throwIfNotAvailable(RenderingDependency.COMMONMARK)
            val fileRenderer = JavalinCommonmark(htmlRenderer ?: defaultRenderer(), parser ?: defaultParser())
            JavalinRenderer.register(fileRenderer, ".md", ".markdown")
        }

        fun defaultRenderer(): HtmlRenderer = HtmlRenderer.builder().build()
        fun defaultParser(): Parser = Parser.builder().build()
    }

}
