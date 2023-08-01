package pl.kacorvixon.blue.command.impl;

import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.command.Command;
import pl.kacorvixon.blue.util.log.Logger;

public class CommandToggle extends Command {
    public CommandToggle() {
        super("t","Toggles a module");
    }

    @Override
    public void execute(String command, String[] args) {
        if (args.length < 2) {
            return;
        }
        if (args.length >= 1) {
            if (Blue.getInstance().moduleAdministration.getModuleByName(args[1]) != null) {
                Blue.getInstance().moduleAdministration.getModuleByName(args[1]).enabled = (!Blue.getInstance().moduleAdministration.getModuleByName(args[1]).isEnabled());
                Logger.logChat(Blue.getInstance().moduleAdministration.getModuleByName(args[1]).name + " has been toggled!");
            } else {
                Logger.logChat("Please provide a valid module!");
            }
        }
    }
}
