package metrics;

import org.junit.Test;

import com.github.mauricioaniche.ck.CK;

public class ObtainingMetrics {

	
	@Test
	public void start() {
		
		
		new CK().calculate("/home/leandroungari/refactoring-toy-example", result -> {
			System.out.println(result.toString());
			System.out.println("WMC: " + result.getWmc());
			System.out.println();
		});
	}
	
}
