package pl.kacorvixon.blue.module.impl.render;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.ColorProperty;
import pl.kacorvixon.blue.util.render.RenderUtil;

import java.awt.*;

public class Chams extends Module {
    public ColorProperty chamscolor = new ColorProperty("Chams color", Color.RED);
    public Chams() {
        super("Chams","Chams", Category.Render,0);
        addProperties(chamscolor);
    }
    @Override
    public void onRender3D(net.weavemc.loader.api.event.RenderLivingEvent event) {

    }
}
