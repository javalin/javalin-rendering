package io.javalin.plugin.rendering.template;

import io.javalin.http.Context;
import io.javalin.rendering.FileRenderer;
import io.javalin.rendering.JavalinRenderer;
import org.jetbrains.annotations.NotNull;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STRawGroupDir;

import java.util.Map;
import java.util.function.Consumer;

public class JavalinStringTemplate4 implements FileRenderer {

    private static final char defaultDelimiter = '$';
    private static final String defaultTemplateDir = "templates/st";
    private static final String defaultModelAccessor = "m";
    private static String modelAccessor = "m";
    private static STRawGroupDir group;

    public static void init(Consumer<JavalinStringTemplate4Configuration> consumer) {
        JavalinRenderer.register(new JavalinStringTemplate4(), ".st", ".html.st");
        JavalinStringTemplate4Configuration config = new JavalinStringTemplate4Configuration();
        consumer.accept(config);
        STGroup.verbose = config.verbose;
        group = new STRawGroupDir(config.templateDir, "UTF-8", config.delimiter, config.delimiter);
        group.load();
        modelAccessor = config.modelAccessor;
    }

    @Override
    public String render(@NotNull String filePath, @NotNull Map<String, Object> model, Context context) throws Exception {
        ST template = group.getInstanceOf(cleanFilePath(filePath));
        template.add(modelAccessor, model);
        return template.render();
    }

    private String cleanFilePath(String filePath) {
        return filePath.endsWith(".st") ? filePath.substring(0, filePath.length() - 3) : filePath;
    }

    public static class JavalinStringTemplate4Configuration {

        private String templateDir = defaultTemplateDir;
        private String modelAccessor = defaultModelAccessor;
        private char delimiter = defaultDelimiter;
        private boolean verbose = false;

        public void templateDir(String templateDir) {
            this.templateDir = templateDir;
        }

        public void modelAccessor(String modelAccessor) {
            this.modelAccessor = modelAccessor;
        }

        public void delimiter(char delimiter) {
            this.delimiter = delimiter;
        }

        public void verbose(boolean verbose) {
            this.verbose = verbose;
        }
    }

}
