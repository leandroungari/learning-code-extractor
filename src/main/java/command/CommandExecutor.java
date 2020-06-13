package command;

import java.util.HashMap;

import com.beust.jcommander.JCommander;

public class CommandExecutor {

  private HashMap<String, Command> commands;
  private String[] args;

  public CommandExecutor(String[] args) {
    this.commands = new HashMap<>();
    this.args = args;
  }

  public void add(String name, Command cmd) {
    this.commands.put(name, cmd);
  }
  
  public void execute() {

    var builder = JCommander.newBuilder();
    for(var entry: this.commands.entrySet()) {
      builder.addCommand(entry.getKey(), entry.getValue());
    }
    
    var cmd = builder.build();
    cmd.parse(args);

    commands.get(cmd.getParsedCommand()).run();
    System.out.println("> Comando executado.");
  }
}