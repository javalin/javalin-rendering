package io.javalin.rendering.template

import io.javalin.http.Context
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.JavalinRenderer.register
import io.javalin.rendering.util.RenderingDependency
import io.javalin.rendering.util.Util.throwIfNotAvailable
import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STRawGroupDir
import java.util.function.Consumer

class JavalinStringTemplate4 : FileRenderer {

    override fun render(filePath: String, model: Map<String, Any>, context: Context): String {
        val template = group!!.getInstanceOf(cleanFilePath(filePath))
        template.add(modelAccessor, model)
        return template.render()
    }

    private fun cleanFilePath(filePath: String): String {
        return if (filePath.endsWith(".st")) filePath.substring(0, filePath.length - 3) else filePath
    }

    class JavalinStringTemplate4Configuration {
        var templateDir = defaultTemplateDir
        var modelAccessor = defaultModelAccessor
        var delimiter = defaultDelimiter
        var verbose = false
        fun templateDir(templateDir: String) {
            this.templateDir = templateDir
        }

        fun modelAccessor(modelAccessor: String) {
            this.modelAccessor = modelAccessor
        }

        fun delimiter(delimiter: Char) {
            this.delimiter = delimiter
        }

        fun verbose(verbose: Boolean) {
            this.verbose = verbose
        }
    }

    companion object {
        private const val defaultDelimiter = '$'
        private const val defaultTemplateDir = "templates/st"
        private const val defaultModelAccessor = "m"
        private var modelAccessor = "m"
        private var group: STRawGroupDir? = null

        @JvmStatic
        @JvmOverloads
        fun init(consumer: Consumer<JavalinStringTemplate4Configuration>) {
            throwIfNotAvailable(RenderingDependency.STRING_TEMPLATE_4)
            register(JavalinStringTemplate4(), ".st", ".html.st")
            val config = JavalinStringTemplate4Configuration()
            consumer.accept(config)
            STGroup.verbose = config.verbose
            group = STRawGroupDir(config.templateDir, "UTF-8", config.delimiter, config.delimiter)
            group!!.load()
            modelAccessor = config.modelAccessor
        }
    }
}
