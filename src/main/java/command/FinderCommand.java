package command;

import com.beust.jcommander.Parameter;

import controller.FinderController;

public class FinderCommand implements Command {

  @Parameter(names = { "-origin" })
  private String origin;

  @Parameter(names = { "-target" })
  private String target;

  @Override
  public void run() {
    try {
      var first = origin.split("/");
      var second = target.split("/");
      var controller = new FinderController(first[0], first[1], second[0], second[1]);
      controller.execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("> Comando finalizado");
  }
}