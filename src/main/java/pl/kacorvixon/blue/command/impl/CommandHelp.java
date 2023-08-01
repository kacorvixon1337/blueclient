package pl.kacorvixon.blue.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.command.Command;
import pl.kacorvixon.blue.util.log.Logger;

public class CommandHelp extends Command {
    public CommandHelp() {
        super("help","Shows list of commands");
    }

    @Override
    public void execute(String command, String[] args) {
        for (Command cmd : Blue.getInstance().commandManager.getCommands()) {
            Logger.logChat(EnumChatFormatting.GOLD + cmd.getAliases() + EnumChatFormatting.RED + " " + cmd.getUsage() );
        }
    }
}
