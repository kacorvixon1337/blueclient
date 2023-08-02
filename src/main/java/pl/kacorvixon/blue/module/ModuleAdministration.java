package pl.kacorvixon.blue.module;

import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.module.impl.combat.*;
import pl.kacorvixon.blue.module.impl.misc.ChestStealer;
import pl.kacorvixon.blue.module.impl.movement.Eagle;
import pl.kacorvixon.blue.module.impl.movement.Sprint;
import pl.kacorvixon.blue.module.impl.render.ChestESP;
import pl.kacorvixon.blue.module.impl.render.ESP;
import pl.kacorvixon.blue.module.impl.render.Hud;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ModuleAdministration {
    public final List<Module> moduleList;

    public ModuleAdministration() {
        this.moduleList = new ArrayList<>();
    }

    public void init() {
        moduleList.add(new Hud());
        //moduleList.add(new Animations()); i will implement it in the future
        moduleList.add(new ESP());
        moduleList.add(new LeftClicker());
        moduleList.add(new Reach());
        moduleList.add(new KeepSprint());
        moduleList.add(new Eagle());
        moduleList.add(new RightClicker());
        moduleList.add(new ChestESP());
        moduleList.add(new ChestStealer());
        moduleList.add(new Sprint());
        moduleList.add(new AimAssist());
        //moduleList.add(new GlowESP()); shaders not possible cuz lunar is gay
    }


    public void autoenable(Module nig){
        nig.setEnabled(true);
    }



    public static <M extends Module> M getInstance(final Class<M> was) {
        return (M) Blue.getInstance().moduleAdministration.getModule(was);
    }

    public void onKey(final int key) {
        moduleList.stream().filter(module -> module.keybind == key).forEach(module -> module.setEnabled(!module.enabled));
    }

    public List<Module> getModulesByCategory(final Category category) {
        return moduleList.stream().filter(module -> module.category == category).collect(Collectors.toList());
    }

    public Module getModule(final Class<?> moduleClass) {
        return moduleList.stream().filter(module -> module.getClass() == moduleClass).findFirst().orElse(null);
    }

    public Module getModuleByName(final String name) {
        return moduleList.stream().filter(module -> module.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
