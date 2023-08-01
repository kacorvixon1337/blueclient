package pl.kacorvixon.blue.util.log;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Logger {
    public static void logChat(final Object message) {
        if (Minecraft.getMinecraft().thePlayer != null)
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "Blue" + EnumChatFormatting.DARK_GRAY + ": " + message));
    }

}
