/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 *
 */

package io.javalin

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import gg.jte.ContentType
import gg.jte.TemplateEngine
import io.javalin.jte.PrecompileJteTestClasses
import io.javalin.plugin.rendering.JavalinRenderer
import io.javalin.plugin.rendering.template.JavalinJte
import io.javalin.plugin.rendering.template.JavalinPebble
import io.javalin.plugin.rendering.template.JavalinVelocity
import io.javalin.plugin.rendering.template.TemplateUtil.model
import io.javalin.testtools.JavalinTest
import org.apache.velocity.app.VelocityEngine
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TestTemplates {

    private val defaultBaseModel = model("foo", "bar")

    @Test
    fun `velocity templates work`() = JavalinTest.test { app, http ->
        JavalinVelocity.configure(JavalinVelocity.defaultVelocityEngine())
        app.get("/hello") { it.render("/templates/velocity/test.vm", model("message", "Hello Velocity!")) }
        assertThat(http.get("/hello").body?.string()).isEqualTo("<h1>Hello Velocity!</h1>")
    }

    @Test
    fun `velocity template variables work`() = JavalinTest.test { app, http ->
        JavalinVelocity.configure(JavalinVelocity.defaultVelocityEngine())
        app.get("/hello") { it.render("/templates/velocity/test-set.vm") }
        assertThat(http.get("/hello").body?.string()).isEqualTo("<h1>Set works</h1>")
    }

    @Test
    fun `velocity external templates work`() = JavalinTest.test { app, http ->
        JavalinVelocity.configure(VelocityEngine().apply {
            setProperty("resource.loader.file.path", "src/test/resources/templates/velocity")
        })
        app.get("/hello") { it.render("test.vm") }
        assertThat(http.get("/hello").body?.string()).isEqualTo("<h1>\$message</h1>")
    }

    @Test
    fun `freemarker templates work`() = JavalinTest.test { app, http ->
        app.get("/hello") { it.render("/templates/freemarker/test.ftl", model("message", "Hello Freemarker!")) }
        assertThat(http.get("/hello").body?.string()).isEqualTo("<h1>Hello Freemarker!</h1>")
    }

    @Test
    fun `thymeleaf templates work`() = JavalinTest.test { app, http ->
        app.get("/hello") { it.render("/templates/thymeleaf/test.html", model("message", "Hello Thymeleaf!")) }
        assertThat(http.get("/hello").body?.string()).isEqualTo("<h1>Hello Thymeleaf!</h1>")
    }

    @Test
    fun `thymeleaf url syntax work`() = JavalinTest.test { app, http ->
        app.get("/hello") { it.render("/templates/thymeleaf/testUrls.html", model("linkParam2", "val2")) }
        assertThat(http.get("/hello").body?.string()).isEqualTo("<a href=\"/test-link?param1=val1&amp;param2=val2\">Link text</a>")
    }

    @Test
    fun `mustache templates work`() = JavalinTest.test { app, http ->
        app.get("/hello") { it.render("/templates/mustache/test.mustache", model("message", "Hello Mustache!")) }
        assertThat(http.get("/hello").body?.string()).isEqualTo("<h1>Hello Mustache!</h1>")
    }

    @Test
    fun `pebble templates work`() = JavalinTest.test { app, http ->
        app.get("/hello1") { it.render("templates/pebble/test.peb", model("message", "Hello Pebble!")) }
        assertThat(http.get("/hello1").body?.string()).isEqualTo("<h1>Hello Pebble!</h1>")
    }

    @Test
    fun `pebble empty context map work`() = JavalinTest.test { app, http ->
        app.get("/hello2") { it.render("templates/pebble/test-empty-context-map.peb") }
        assertThat(http.get("/hello2")).isNotEqualTo("Internal server error")
    }

    @Test
    fun `pebble custom engines work`() = JavalinTest.test { app, http ->
        app.get("/hello") { it.render("templates/pebble/test.peb") }
        assertThat(http.get("/hello").body?.string()).isEqualTo("<h1></h1>")
        JavalinPebble.configure(
            PebbleEngine.Builder()
                .loader(ClasspathLoader())
                .strictVariables(true)
                .build()
        )
        assertThat(http.get("/hello").body?.string()).isEqualTo("Internal server error")
    }

    @Test
    fun `jte works`() = JavalinTest.test { app, http ->
        JavalinJte.configure(TemplateEngine.createPrecompiled(null, ContentType.Html, null, PrecompileJteTestClasses.PACKAGE_NAME))
        app.get("/hello") { it.render("test.jte", model("page", JteTestPage("hello", "world"))) }
        assertThat(http.get("/hello").body?.string()).isEqualToIgnoringNewLines("<h1>hello world!</h1>")
    }

    @Test
    fun `jte multiple params work`() = JavalinTest.test { app, http ->
        JavalinJte.configure(TemplateEngine.createPrecompiled(null, ContentType.Html, null, PrecompileJteTestClasses.PACKAGE_NAME))
        app.get("/hello") { it.render("multiple-params.jte", model("one", "hello", "two", "world")) }
        assertThat(http.get("/hello").body?.string()).isEqualToIgnoringNewLines("<h1>hello world!</h1>")
    }

    @Test
    fun `jte kotlin works`() = JavalinTest.test { app, http ->
        JavalinJte.configure(TemplateEngine.createPrecompiled(null, ContentType.Html, null, PrecompileJteTestClasses.PACKAGE_NAME))
        app.get("/hello") { it.render("kte/test.kte", model("page", JteTestPage("hello", "world"))) }
        assertThat(http.get("/hello").body?.string()).isEqualToIgnoringNewLines("<h1>hello world!</h1>")
    }

    @Test
    fun `jte kotlin multiple params work`() = JavalinTest.test { app, http ->
        JavalinJte.configure(TemplateEngine.createPrecompiled(null, ContentType.Html, null, PrecompileJteTestClasses.PACKAGE_NAME))
        app.get("/hello") { it.render("kte/multiple-params.kte", model("one", "hello", "two", "world")) }
        assertThat(http.get("/hello").body?.string()).isEqualToIgnoringNewLines("<h1>hello world!</h1>")
    }

    @Test
    fun `markdown works`() = JavalinTest.test { app, http ->
        app.get("/hello") { it.render("/markdown/test.md") }
        assertThat(http.get("/hello").body?.string()).isEqualTo("<h1>Hello Markdown!</h1>\n")
    }

    @Test
    fun `unregistered extension throws exception`() = JavalinTest.test { app, http ->
        app.get("/hello") { it.render("/markdown/test.unknown") }
        assertThat(http.get("/hello").body?.string()).isEqualTo("Internal server error")
    }

    @Test
    fun `registering custom renderer works`() = JavalinTest.test { app, http ->
        JavalinRenderer.register({ _, _, _ -> "Hah." }, ".lol")
        app.get("/hello") { it.render("/markdown/test.lol") }
        assertThat(http.get("/hello").body?.string()).isEqualTo("Hah.")
    }

    @Test
    fun `base Model works`() = JavalinTest.test { app, http ->
        JavalinRenderer.baseModelFunction = { ctx -> defaultBaseModel }
        app.get("/") { it.render("/templates/freemarker/test-with-base.ftl") }
        assertThat(http.get("/").body?.string()).contains("<h3>bar</h3>")
    }

    @Test
    fun `base model overwrite works`() = JavalinTest.test { app, http ->
        JavalinRenderer.baseModelFunction = { ctx -> defaultBaseModel }
        app.get("/") { it.render("/templates/freemarker/test-with-base.ftl", model("foo", "baz")) }
        assertThat(http.get("/").body?.string()).contains("<h3>baz</h3>")
    }

    data class JteTestPage(val hello: String, val world: String)
}
