package com.example;

import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.ToolProvider;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.VariableTree;

import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

//import com.sun.tools.javac.api.BasicJavacTask;
//import com.sun.tools.javac.util.Context;
//import com.sun.tools.javac.util.Log;
//import static io.malakeel.Constants.*;
//import java.lang.System.Logger.Level;

public class JavacPlugin implements Plugin {

	@Override
	public String getName() {
		return "ExamplePlugin";
	}

	@Override
	public void init(JavacTask task, String... args) {

		System.out.println("I am running from init in JavacPlugin");

		for (String a : args) {
			System.out.println("Plugin initialized with arg: " + a);
		}

		System.out.println("Task types" + task.getTypes().toString());

		task.addTaskListener(new PostAnalyzeTaskListener(task));

		javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

//		Context context = ((BasicJavacTask) task).getContext();
//
//		Log logger = Log.instance(context);

//		 logger.warning(Warning.of(null, module, module, args));
		// ToolProvider.

	}

	static class PostAnalyzeTaskListener implements TaskListener {

		private final ShowTypeTreeVisitor visitor;

		PostAnalyzeTaskListener(JavacTask task) {
			visitor = new ShowTypeTreeVisitor(task);
		}

		@Override
		public void started(TaskEvent taskEvent) {
//			logger.log(Level.ALL, "Logging :" + taskEvent);

		}

		@Override
		public void finished(TaskEvent taskEvent) {

			if (taskEvent.getKind().equals(TaskEvent.Kind.ANALYZE)) {
				CompilationUnitTree compilationUnit = taskEvent.getCompilationUnit();

				visitor.scan(compilationUnit, null);

//				DataDefTreeVisitor data = new DataDefTreeVisitor();
//				compilationUnit.accept(data, null);

			}

			System.out.println("Completed Anaysis: " + taskEvent.toString());

		}

		static class ShowTypeTreeVisitor extends TreePathScanner<Void, Void> {

			private final Trees trees;

			private CompilationUnitTree currentCompilationUnit;

			ShowTypeTreeVisitor(JavacTask task) {
				trees = Trees.instance(task);
				;
			}

			@Override
			public Void visitCompilationUnit(CompilationUnitTree tree, Void aVoid) {
				currentCompilationUnit = tree;

				return super.visitCompilationUnit(tree, aVoid);
			}

			@Override
			public Void visitVariable(VariableTree tree, Void aVoid) {

				TypeMirror type = trees.getTypeMirror(getCurrentPath());

				trees.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "type is " + type, tree, currentCompilationUnit);

//				trees.printMessage(Diagnostic.Kind.ERROR, "Test error message ", tree, currentCompilationUnit);

				return super.visitVariable(tree, aVoid);
			}
		}
	}

}
