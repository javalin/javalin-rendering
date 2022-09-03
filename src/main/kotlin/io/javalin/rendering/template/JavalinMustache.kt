/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.rendering.template

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import io.javalin.http.Context
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.JavalinRenderer
import io.javalin.rendering.util.RenderingDependency
import io.javalin.rendering.util.Util
import java.io.StringWriter

object JavalinMustache : FileRenderer {

    fun init() {
        Util.throwIfNotAvailable(RenderingDependency.MUSTACHE)
        JavalinRenderer.register(JavalinMustache, ".mustache")
    }

    private var mustacheFactory: MustacheFactory? = null
    private val defaultMustacheFactory: MustacheFactory by lazy { DefaultMustacheFactory("./") }

    @JvmStatic
    fun configure(staticMustacheFactory: MustacheFactory) {
        mustacheFactory = staticMustacheFactory
    }

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context?): String {
        val stringWriter = StringWriter()
        (mustacheFactory ?: defaultMustacheFactory).compile(filePath).execute(stringWriter, model).close()
        return stringWriter.toString()
    }

}
