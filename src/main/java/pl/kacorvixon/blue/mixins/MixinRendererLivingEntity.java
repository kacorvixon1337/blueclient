package pl.kacorvixon.blue.mixins;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(RendererLivingEntity.class)
public class MixinRendererLivingEntity {

//    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
//    public void doRender(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
//        if (Minecraft.getMinecraft().theWorld != null) {
//            EventBus.callEvent(new Render2DLivingEvent((RendererLivingEntity<EntityLivingBase>) (Object) this, entity, x, y, z, partialTicks));
//        }
//    }

}