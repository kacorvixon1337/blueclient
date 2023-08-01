package com.example.mod.listener;

import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.event.RenderLivingEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import pl.kacorvixon.blue.Blue;

public class RenderListener {

   @SubscribeEvent
   public void onRender3D(RenderLivingEvent e) {
      Blue.getInstance().moduleAdministration.moduleList.forEach(module -> {

         if(module.enabled){
            if(Minecraft.getMinecraft().theWorld != null)
               module.onRender3D(e);
         }
      });
   }

   @SubscribeEvent
   public void onRender2D(RenderGameOverlayEvent e) {
      Blue.getInstance().moduleAdministration.moduleList.forEach(module -> {
         if(module.enabled){
            if(Minecraft.getMinecraft().theWorld != null)
               module.onRender2D(e);
         }
      });
   }

   @SubscribeEvent
   public void onTick(TickEvent e) {
      Blue.getInstance().moduleAdministration.moduleList.forEach(module -> {
         if(module.enabled){
            if(Minecraft.getMinecraft().theWorld != null)
               module.onTick(e);
         }
      });
   }




}