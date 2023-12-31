package pl.kacorvixon.blue.mixins;

import pl.kacorvixon.blue.listener.RenderListener;
import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.kacorvixon.blue.Blue;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(
            method = {"startGame"},
            at = {@At("TAIL")}
    )
    public void startGame(CallbackInfo ci) {

        Blue.getInstance().init();
        EventBus.subscribe(new RenderListener());
        //EventBus.subscribe(new Hud());
        //EventBus.subscribe(new ESP());
        //EventBus.subscribe(new TickListener());

    }
    @Inject(
            method = {"shutdownMinecraftApplet"},
            at = {@At("HEAD")}
    )
    public void shutdownMinecraftApplet(CallbackInfo ci) {

        Blue.getInstance().stop();

    }
}