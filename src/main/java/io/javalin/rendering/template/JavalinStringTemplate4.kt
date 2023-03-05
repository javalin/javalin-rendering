package io.javalin.rendering.template

import io.javalin.http.Context
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.JavalinRenderer
import io.javalin.rendering.JavalinRenderer.register
import io.javalin.rendering.util.RenderingDependency.STRING_TEMPLATE_4
import io.javalin.rendering.util.Util.throwIfNotAvailable
import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STRawGroupDir
import java.util.function.Consumer

class JavalinStringTemplate4 : FileRenderer {

    override fun render(filePath: String, model: Map<String, Any?>, ctx: Context): String {
        if (reloadOnRender) {
            group!!.unload()
            group!!.load()
        }
        val template = group!!.getInstanceOf(cleanFilePath(filePath))
        model.forEach(template::add)
        return template.render()
    }


    private fun cleanFilePath(filePath: String): String {
        return if (filePath.endsWith(".st")) filePath.substring(0, filePath.length - 3) else filePath
    }

    class JavalinStringTemplate4Configuration {
        var templateDir = defaultTemplateDir
        var devMode = false;
        var delimiter = defaultDelimiter

        fun templateDir(templateDir: String) {
            this.templateDir = templateDir
        }

        fun devMode(devMode: Boolean) {
            this.devMode = devMode;
        }

        fun delimiter(delimiter: Char) {
            this.delimiter = delimiter
        }


    }

    companion object {
        private const val defaultDelimiter = '$'
        private const val defaultTemplateDir = "templates/st"
        private var reloadOnRender = false
        private var verbose = false
        private var group: STRawGroupDir? = null

        val extensions = arrayOf(".st")

        @JvmStatic
        @JvmOverloads
        fun init(consumer: Consumer<JavalinStringTemplate4Configuration>? = null) {
            throwIfNotAvailable(STRING_TEMPLATE_4)
            register(JavalinStringTemplate4(), *extensions)
            val config = JavalinStringTemplate4Configuration()
            consumer?.accept(config)
            verbose = config.devMode
            reloadOnRender = config.devMode
            STGroup.verbose = verbose
            group = STRawGroupDir(config.templateDir, "UTF-8", config.delimiter, config.delimiter)
            group!!.load()
        }
    }

    class Loader : JavalinRenderer.FileRendererLoader {
        override fun load() = if (!JavalinRenderer.hasRenderer(*extensions) && STRING_TEMPLATE_4.exists()) init() else Unit
    }

}
