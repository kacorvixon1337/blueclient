package pl.kacorvixon.blue;

import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.*;
import org.lwjgl.input.Keyboard;
import pl.kacorvixon.blue.ui.kotedit.ClickGuiEdit;

public class Initializer implements ModInitializer {
    @Override
    public void preInit() {
        System.out.println("initializing blue...");
        EventBus.subscribe(KeyboardEvent.class, e -> {
            if(Keyboard.isKeyDown(Keyboard.KEY_INSERT)){
                ClickGuiEdit.getInstance().display();
                Blue.getInstance().moduleAdministration.moduleList.forEach(mod -> {
                    if (e.getKeyCode() == mod.keybind && e.getKeyState()) {
                        mod.setEnabled(!mod.enabled);
                    }
                });
            };
        });
    }}