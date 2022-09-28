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

class JavalinMustache(private var mustacheFactory: MustacheFactory) : FileRenderer {

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context?): String {
        val stringWriter = StringWriter()
        mustacheFactory.compile(filePath).execute(stringWriter, model).close()
        return stringWriter.toString()
    }

    companion object {
        @JvmStatic
        fun init(mustacheFactory: MustacheFactory? = null) {
            Util.throwIfNotAvailable(RenderingDependency.MUSTACHE)
            JavalinRenderer.register(JavalinMustache(mustacheFactory ?: defaultMustacheFactory()), ".mustache")
        }

        fun defaultMustacheFactory(): MustacheFactory = DefaultMustacheFactory("./")
    }

}
