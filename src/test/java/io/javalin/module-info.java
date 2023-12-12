module test {
	requires io.javalin.rendering;
    requires transitive kotlin.stdlib;

    requires gg.jte.runtime;
    requires org.assertj.core;
    exports io.javalin to org.assertj.core;
}
