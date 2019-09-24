package repository.metric;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jgit.lib.ObjectId;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNotifier;
import com.github.mauricioaniche.ck.MetricsExecutor;

import repository.File;

public class CKMetric extends CK {

	public CKMetric() {
		super();
	}
	
	public void calculate(ObjectId commitId,
			CKNotifier notifier) {

		var factory = new GitFactory();

		// It inits the metrics executor
		MetricsExecutor storage = new MetricsExecutor(
				classLevelMetrics, methodLevelMetrics, notifier);

		var list = new ArrayList<File>();
		try {
			factory.treeStart(commitId, walk -> {
				var file = new File(walk.getPathString(),
						walk.getObjectId(0));

				if (file.name().endsWith(".java"))
					list.add(file);

			});
		} catch (IOException e) {

			System.err.println(e.getMessage());
		}

		for (var file : list) {
			ASTParser parser = ASTParser.newParser(AST.JLS11);
			parser.setSource(file.get().toCharArray());

			Map<String, String> options = JavaCore.getOptions();
			parser.setCompilerOptions(options);
			parser.setUnitName("/" + folder + "/" + file.name());
			parser.setEnvironment(
					new String[] { factory.absoluteFolder() },
					new String[] { "" }, new String[] { "UTF-8" },
					true);

			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);

			JavaCore.setComplianceOptions(JavaCore.VERSION_11,
					options);
			var unit = (CompilationUnit) parser.createAST(null);

			storage.acceptAST(
					factory.absoluteFolder() + file.name(), unit);
		}

	}
}
