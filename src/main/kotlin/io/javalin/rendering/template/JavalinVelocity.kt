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
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import java.io.StringWriter
import java.nio.charset.StandardCharsets

class JavalinVelocity @JvmOverloads constructor(
    private var velocityEngine: VelocityEngine = defaultVelocityEngine()
) : FileRenderer {

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context): String {
        val stringWriter = StringWriter()
        velocityEngine.getTemplate(filePath, StandardCharsets.UTF_8.name()).merge(
            VelocityContext(model.toMutableMap()), stringWriter
        )
        return stringWriter.toString()
    }

    companion object {
        val extensions = arrayOf(".vm", ".vtl")

        @JvmStatic
        @JvmOverloads
        fun init(velocityEngine: VelocityEngine? = null) {
            Util.throwIfNotAvailable(RenderingDependency.VELOCITY)
            JavalinRenderer.register(JavalinVelocity(velocityEngine ?: defaultVelocityEngine()), *extensions)
        }

        fun defaultVelocityEngine() = VelocityEngine().apply {
            setProperty("resource.loaders", "class")
            setProperty("resource.loader.class.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
        }
    }

    class Loader : JavalinRenderer.FileRendererLoader {
        override fun load() = if (!JavalinRenderer.hasRenderer(*extensions)) init() else Unit
    }

}
