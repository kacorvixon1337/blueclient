package pl.kacorvixon.blue.command;

import pl.kacorvixon.blue.command.impl.*;
import pl.kacorvixon.blue.util.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public void init() {//
        commands.add(new CommandBind());
        commands.add(new CommandToggle());
        commands.add(new CommandConfig());
        commands.add(new CommandHide());
        commands.add(new CommandHelp());
    }

    public void stop() {

    }

    public List<Command> getCommands() {
        return commands;
    }

    public boolean onSendMessage(final String message) {
        if (message.startsWith(".")) {
            String withoutPrefix = message.substring(1);
            String[] args = withoutPrefix.split(" ");
            if (!withoutPrefix.isEmpty() && args.length > 0) {
                String commmand = withoutPrefix.substring(args[0].length()).trim();
                for (Command command : commands) {
                        if (command.getAliases().equalsIgnoreCase(args[0])) {
                            command.execute(commmand, args);
                            return true;
                        }
                }
                Logger.logChat("'" + args[0] + "' is not a command.");
            } else {
                Logger.logChat("No arguments provided!");
            }
            return true;
        }
        return false;
    }
}
