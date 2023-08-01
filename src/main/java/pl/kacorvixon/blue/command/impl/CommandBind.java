package pl.kacorvixon.blue.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.command.Command;
import pl.kacorvixon.blue.util.log.Logger;

import java.util.Objects;

public class CommandBind extends Command {

    public CommandBind() {
        super("bind","binds module to a key");
    }

    @Override
    public void execute(String command, String[] args) {
        if (args.length == 2) {
            if (Objects.nonNull(Blue.getInstance().moduleAdministration.getModuleByName(args[1]))) {
                int key = Blue.getInstance().moduleAdministration.getModuleByName(args[1]).keybind;
                Logger.logChat(ChatFormatting.GRAY + Blue.getInstance().moduleAdministration.getModuleByName(args[1]).name + ChatFormatting.RESET + " is currently bound to " + ChatFormatting.GRAY + Keyboard.getKeyName(key));
            } else {
                Logger.logChat("Please provide a valid module to bind a key to!");
            }
        } else if (args.length >= 3) {
            if (Objects.nonNull(Blue.getInstance().moduleAdministration.getModuleByName(args[1]))) {
                if (args[2] != null) {
                    int key = Keyboard.getKeyIndex(args[2].toUpperCase());
                    Blue.getInstance().moduleAdministration.getModuleByName(args[1]).setKeybind(key);
                    Logger.logChat(ChatFormatting.GRAY + Blue.getInstance().moduleAdministration.getModuleByName(args[1]).name + ChatFormatting.RESET + " has been bound to " + ChatFormatting.GRAY + args[2].toUpperCase());
                } else {
                    Logger.logChat("Please provide a key to bind the module to!");
                }
            } else {
                Logger.logChat("Please provide a valid module to bind a key to!");
            }
        }
    }
}
