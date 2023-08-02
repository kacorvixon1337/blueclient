package pl.kacorvixon.blue.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import pl.kacorvixon.blue.Blue;

@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin {
    @Overwrite
    public void sendChatMessage(String message) {
        if (!Blue.getInstance().commandManager.onSendMessage(message))
        {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
        }
    }
}
