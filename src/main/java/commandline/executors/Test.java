package commandline.executors;

import commandline.Executor;

public class Test implements Executor{

	@Override
	public void run() {
		System.out.println("Testing ...");
	}

	@Override
	public void param(String... params) {
		//do nothing
	}

	@Override
	public void helper() {
		System.out.println("This command just do nothing! :)");
	}

}
