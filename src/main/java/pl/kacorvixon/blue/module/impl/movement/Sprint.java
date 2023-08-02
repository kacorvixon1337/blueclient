package pl.kacorvixon.blue.module.impl.movement;

import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.NumberProperty;
import pl.kacorvixon.blue.util.log.Logger;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Sprint", Category.Movement, 0);
    }

    @Override
    public void onTick(net.weavemc.loader.api.event.TickEvent event){

        mc.gameSettings.keyBindSprint.pressed = true;

    }
}

