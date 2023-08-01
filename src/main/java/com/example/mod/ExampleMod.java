package com.example.mod;

import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.command.CommandBus;
import net.weavemc.loader.api.event.*;
import com.example.mod.command.TestCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;
import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.ui.kot.ClickGui;
import pl.kacorvixon.blue.ui.kotedit.ClickGuiEdit;

public class ExampleMod implements ModInitializer {
    @Override
    public void preInit() {
        System.out.println("initializing blue...");
        //CommandBus.register(new TestCommand());
        EventBus.subscribe(KeyboardEvent.class, e -> {
//            if (Minecraft.getMinecraft().currentScreen == null && e.getKeyState()) {
//                Minecraft.getMinecraft().thePlayer.addChatMessage(
//                        new ChatComponentText("Key Pressed: " + Keyboard.getKeyName(e.getKeyCode()))
//                );
//            }
            if(Keyboard.isKeyDown(Keyboard.KEY_INSERT)){
                ClickGuiEdit.getInstance().display();
                //RoundedUtil.roundedShader = new ShaderUtil("roundedRect");
            }
            Blue.getInstance().moduleAdministration.moduleList.forEach(mod -> {
                if (e.getKeyCode() == mod.keybind && e.getKeyState()) {
                    mod.setEnabled(!mod.enabled);
                }
            });
        });
        //EventBus.subscribe(new RenderGameOverlayListener());













    }
}