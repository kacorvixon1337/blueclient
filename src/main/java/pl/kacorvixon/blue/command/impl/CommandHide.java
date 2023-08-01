package pl.kacorvixon.blue.command.impl;

import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.command.Command;
import pl.kacorvixon.blue.util.log.Logger;

public class CommandHide extends Command {
    public CommandHide() {
        super("hide","Hides module from arraylist");
    }

    @Override
    public void execute(String command, String[] args) {
        if (args.length >1) {
            Blue.getInstance().moduleAdministration.getModuleByName(args[1]).hidden = !Blue.getInstance().moduleAdministration.getModuleByName(args[1]).hidden;
        } else {
            Logger.logChat("Wrong Usage!");
        }
    }
}
