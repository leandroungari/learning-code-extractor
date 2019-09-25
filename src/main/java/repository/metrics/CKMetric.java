package repository.metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jgit.lib.ObjectId;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNotifier;
import com.github.mauricioaniche.ck.MetricsExecutor;
import com.github.mauricioaniche.ck.util.MetricsFinder;

import repository.commit.File;
import repository.GitFactory;

public class CKMetric extends CK {

	public CKMetric() {
		super();
	}

	private List<File> obtainingFiles(
		GitFactory factory, 
		ObjectId commitId, 
		String fileExtension) {
		
		var list = new ArrayList<File>();
		try {
			factory.treeStart(commitId, walk -> {
				var file = new File(
					walk.getPathString(), 
					walk.getObjectId(0)
				);

				if (file.name().endsWith(fileExtension))
					list.add(file);

			});
		} catch (IOException e) {

			System.err.println(e.getMessage());
		}
		
		return list;
	}
	
	public MetricsExecutor executor(CKNotifier notifier) {
		
		MetricsFinder finder = new MetricsFinder();
		
		return new MetricsExecutor(
			() -> finder.allClassLevelMetrics(),
			() -> finder.allMethodLevelMetrics(), 
			notifier
		);
	}
	
	
	public void calculate(
		ObjectId commitId, 
		CKNotifier notifier) {

		var factory = new GitFactory();
		var folder = factory.folder();

		var storage = executor(notifier);

		for (var file : obtainingFiles(factory, commitId, ".java")) {
			ASTParser parser = ASTParser.newParser(AST.JLS11);
			parser.setSource(file.get().toCharArray());

			Map<String, String> options = JavaCore.getOptions();
			parser.setCompilerOptions(options);
			parser.setUnitName("/" + folder + "/" + file.name());
			parser.setEnvironment(
				new String[] { factory.absoluteFolder() }, 
				new String[] { "" }, 
				new String[] { "UTF-8" },
				true
			);

			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);

			JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
			var unit = (CompilationUnit) parser.createAST(null);

			storage.acceptAST(factory.absoluteFolder() + file.name(), unit);
		}

	}
}
