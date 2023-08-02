package pl.kacorvixon.blue.mixins;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.module.impl.combat.Reach;

import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    //ok so basically i didnt want to overwrite it but i was fucking with it 1 hour how to get double d0,d1 and flag bbool from that function bruh
    Minecraft mc = Minecraft.getMinecraft();
    Entity pointedEntity = Minecraft.getMinecraft().pointedEntity;
    @Overwrite
    public void getMouseOver(float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        if (entity == null) return;
        if (this.mc.theWorld == null) return;
        this.mc.mcProfiler.startSection("pick");
        this.mc.pointedEntity = null;
        double d0 = this.mc.playerController.getBlockReachDistance();
        this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
        double d1 = d0;
        Vec3 vec3 = entity.getPositionEyes(partialTicks);
        boolean flag = false;
        int i = 3;
        if (this.mc.playerController.extendedReach()) {
            d0 = 6.0;
            d1 = 6.0;
        } else if(Blue.getInstance().moduleAdministration.getModule(Reach.class).isEnabled()){
            d0 = Reach.range.getValue();
            d1 = Reach.range.getValue();
        } else if (d0 > 3.0 && !Blue.getInstance().moduleAdministration.getModule(Reach.class).isEnabled()) {
            d0 = 3.0;
            d1 = 3.0;
            flag = true;
        }
        if (this.mc.objectMouseOver != null) {
            d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
        }
        Vec3 vec31 = entity.getLook(partialTicks);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
        this.pointedEntity = null;
        Vec3 vec33 = null;
        float f = 1.0f;
        List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d2 = d1;
        for (int j = 0; j < list.size(); ++j) {
            double d3;
            Entity entity1 = list.get(j);
            float f1 = entity1.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
            if (axisalignedbb.isVecInside(vec3)) {
                if (!(d2 >= 0.0)) continue;
                this.pointedEntity = entity1;
                vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                d2 = 0.0;
                continue;
            }
            if (movingobjectposition == null || !((d3 = vec3.distanceTo(movingobjectposition.hitVec)) < d2) && d2 != 0.0) continue;
            boolean flag1 = false;
            if (!flag1 && entity1 == entity.ridingEntity) {
                if (d2 != 0.0) continue;
                this.pointedEntity = entity1;
                vec33 = movingobjectposition.hitVec;
                continue;
            }
            this.pointedEntity = entity1;
            vec33 = movingobjectposition.hitVec;
            d2 = d3;
        }
        if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > d0) {
            this.pointedEntity = null;
            this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
        }
        if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
            this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);
            if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                this.mc.pointedEntity = this.pointedEntity;
            }
        }
        this.mc.mcProfiler.endSection();
    }
}
