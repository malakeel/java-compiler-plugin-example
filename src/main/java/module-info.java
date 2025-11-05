module MyModule {

	requires jdk.compiler;
	requires java.compiler;
	requires java.base;

	uses com.sun.source.util.Plugin;

	provides com.sun.source.util.Plugin with com.example.JavacPlugin;

	opens com.example;
	exports com.example;
}
