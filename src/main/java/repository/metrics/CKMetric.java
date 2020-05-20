package repository.metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNotifier;
import com.github.mauricioaniche.ck.MetricsExecutor;
import com.github.mauricioaniche.ck.util.MetricsFinder;

import repository.commit.File;
import repository.Commit;
import repository.GitFactory;
import repository.Repository;

public class CKMetric extends CK {

	private Repository repository;

	CKMetric(Repository repo) {
		super();

		this.repository = repo;
	}

	private List<File> obtainingFiles(
		GitFactory factory, 
		Commit commit, 
		String fileExtension) {
		
		var list = new ArrayList<File>();
		try {
			factory.treeStart(commit.id(), walk -> {
				var file = new File(
					commit,
					walk.getPathString(), 
					walk.getObjectId(0)
				);

				if (file.fullPath().endsWith(fileExtension))
					list.add(file);

			});
		} catch (IOException e) {

			System.err.println(e.getMessage());
		}
		
		return list;
	}
	
	private MetricsExecutor executor(CKNotifier notifier) {
		
		MetricsFinder finder = new MetricsFinder();
		
		return new MetricsExecutor(
			() -> finder.allClassLevelMetrics(),
			() -> finder.allMethodLevelMetrics(), 
			notifier
		);
	}
	
	
	public void calculate(
		Commit commit, 
		CKNotifier notifier) {

		var factory = new GitFactory(this.repository);
		var folder = factory.folder();

		var storage = executor(notifier);

		for (var file : obtainingFiles(factory, commit, ".java")) {
			ASTParser parser = ASTParser.newParser(AST.JLS11);
			parser.setSource(file.get().toCharArray());

			Map<String, String> options = JavaCore.getOptions();
			parser.setCompilerOptions(options);
			parser.setUnitName("/" + folder + "/" + file.fullPath());
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

			storage.acceptAST(factory.absoluteFolder() + file.fullPath(), unit);
		}

	}
}
