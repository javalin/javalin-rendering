/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.plugin.rendering.template

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.javalin.core.util.Util
import io.javalin.http.Context
import io.javalin.plugin.rendering.FileRenderer
import io.javalin.plugin.rendering.JavalinRenderer
import io.javalin.plugin.rendering.OptionalDependency
import io.javalin.plugin.rendering.ensureDependencyPresent
import java.io.StringWriter

object JavalinPebble : FileRenderer {

    init {
        JavalinRenderer.register(JavalinPebble, ".peb", ".pebble")
    }

    private var pebbleEngine: PebbleEngine? = null
    private val defaultPebbleEngine: PebbleEngine by lazy { defaultPebbleEngine() }

    @JvmStatic
    fun configure(staticPebbleEngine: PebbleEngine) {
        pebbleEngine = staticPebbleEngine
    }

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context?): String {
        ensureDependencyPresent(OptionalDependency.PEBBLE)
        val compiledTemplate = (pebbleEngine ?: defaultPebbleEngine).getTemplate(filePath)
        val stringWriter = StringWriter()
        compiledTemplate.evaluate(stringWriter, model)
        return stringWriter.toString()
    }

    private fun defaultPebbleEngine() = PebbleEngine.Builder()
        .loader(ClasspathLoader())
        .strictVariables(false)
        .build()

}
