package com.example.mod.mixins;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.module.impl.combat.Reach;

import java.util.List;

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
