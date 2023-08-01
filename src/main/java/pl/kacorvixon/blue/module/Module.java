package pl.kacorvixon.blue.module;

import net.minecraft.client.Minecraft;
import pl.kacorvixon.blue.property.Property;
import pl.kacorvixon.blue.util.render.animation.PosAnimation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {
    public final String name;
    public final String rendername;
    public final Category category;
    protected static Minecraft mc;
    public int keybind;
    public boolean enabled;
    public String suffix;
    public final List<Property<?>> propertyList;
    public final PosAnimation animation;
    public boolean hidden;
    //theres no point in using annotations :shrug:
    public Module(final String name,final String renderName, final Category category, final Integer keybind) {
        mc = Minecraft.getMinecraft();
        this.name = name;
        this.category = category;
        this.keybind = keybind;
        this.propertyList = new ArrayList<>();
        this.rendername = renderName;
        animation = new PosAnimation();
    }


    public void addProperties(final Property<?>... properties) {
        propertyList.addAll(Arrays.asList(properties));
    }

    public final void setKeybind(final int keybind) {
        this.keybind = keybind;
    }

    public final String getDisplayName() {
        if (this.suffix == null) {
            return this.rendername;
        } else {
            return String.format("%s \2477%s", this.name, this.suffix);
        }
    }

    public void setSuffix(final String suffix) {
        this.suffix = suffix;
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                this.onEnable();
            } else {
                this.onDisable();
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void onEnable() {

    }
    public void onDisable() {

    }




    public void onRender3D(net.weavemc.loader.api.event.RenderLivingEvent event) {

    }
    public void onRender2D(net.weavemc.loader.api.event.RenderGameOverlayEvent event) {

    }
    public void onTick(net.weavemc.loader.api.event.TickEvent event) {

    }







}
