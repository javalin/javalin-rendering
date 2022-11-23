/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.rendering.template

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.javalin.http.Context
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.JavalinRenderer
import io.javalin.rendering.util.RenderingDependency
import io.javalin.rendering.util.Util
import java.io.StringWriter

class JavalinPebble @JvmOverloads constructor(
    private var pebbleEngine: PebbleEngine = defaultPebbleEngine()
) : FileRenderer {

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context): String {
        val compiledTemplate = pebbleEngine.getTemplate(filePath)
        val stringWriter = StringWriter()
        compiledTemplate.evaluate(stringWriter, model)
        return stringWriter.toString()
    }

    companion object {
        val extensions = arrayOf(".peb", ".pebble")

        @JvmStatic
        @JvmOverloads
        fun init(pebbleEngine: PebbleEngine? = null) {
            Util.throwIfNotAvailable(RenderingDependency.PEBBLE)
            JavalinRenderer.register(JavalinPebble(pebbleEngine ?: defaultPebbleEngine()), *extensions)
        }

        private fun defaultPebbleEngine() = PebbleEngine.Builder()
            .loader(ClasspathLoader())
            .strictVariables(false)
            .build()
    }

    class Loader : JavalinRenderer.FileRendererLoader {
        override fun load() = if (!JavalinRenderer.hasRenderer(*extensions)) init() else Unit
    }

}
