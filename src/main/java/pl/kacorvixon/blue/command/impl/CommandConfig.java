package pl.kacorvixon.blue.command.impl;

import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.command.Command;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.util.file.configs.ClientConfig;
import pl.kacorvixon.blue.util.log.Logger;

import java.io.File;
import java.util.Objects;

public class CommandConfig extends Command {
    public CommandConfig() {
        super("config","Loads and saves a config");
    }

    @Override
    public void execute(String command, String[] args) {
        if (args.length < 2) {
            Logger.logChat("Wrong Usage!");
        } else {
            if (args.length > 2) {
                ClientConfig clientConfig = new ClientConfig(args[2]);
                if (args[1].equalsIgnoreCase("save")) {
                    Blue.getInstance().fileManager.saveConfig(clientConfig);
                    Logger.logChat("saved " + args[2]);
                }
                if (args[1].equalsIgnoreCase("load") && clientConfig.getConfigFile().exists()) {
                    for (Module module : Blue.getInstance().moduleAdministration.moduleList) {
                        module.setEnabled(false);
                    }
                    Blue.getInstance().fileManager.loadConfig(clientConfig);
                    Logger.logChat("loaded " + args[2]);
                }
            }
            if (args[1].equalsIgnoreCase("list")) {
                File folder = Blue.getInstance().configDir;
                File[] listOfFiles = folder.listFiles();
                for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
                    if (listOfFiles[i].isFile()) {
                        Logger.logChat("" + listOfFiles[i].getName().replace(".json","") + "  §7(Local)");
                    } else if (listOfFiles[i].isDirectory()) {
                        System.out.println("error" + listOfFiles[i].getName());
                    }
                }            }
        }
    }
}
