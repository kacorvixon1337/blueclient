package pl.kacorvixon.blue.module.impl.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.util.render.RenderUtil;
import pl.kacorvixon.blue.util.render.animation.Animation;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ESP extends Module {

    private BooleanProperty health = new BooleanProperty("Health",true);
    private BooleanProperty box = new BooleanProperty("Box",true);

    public ESP() {
        super("2DESP", "2D ESP", Category.Render, 52);
        addProperties(health,box);
    }
    private Animation animation = new Animation();
    @Override
    public void onRender2D(net.weavemc.loader.api.event.RenderGameOverlayEvent event) {
        if(mc.theWorld != null) {
            mc.theWorld.getLoadedEntityList().forEach(entity -> {
                if (entity instanceof EntityPlayer && !entity.isInvisible()) {
                    EntityLivingBase ent = (EntityLivingBase) entity;
                    if (RenderUtil.isInViewFrustrum(ent) && ent instanceof EntityPlayer && ent != mc.thePlayer) {
                        double x = RenderUtil.interpolate((float) entity.prevPosX, (float) entity.posX, event.getPartialTicks());
                        double y = RenderUtil.interpolate((float) entity.prevPosY, (float) entity.posY, event.getPartialTicks());
                        double z = RenderUtil.interpolate((float) entity.prevPosZ, (float) entity.posZ, event.getPartialTicks());
                        double width = entity.width / 1.5;
                        double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                        AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                        List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                        mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                        Vector4d position = null;
                        for (Vector3d vector : vectors) {
                            vector = RenderUtil.project(vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
                            if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                                if (position == null) {
                                    position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                                }
                                position.x = Math.min(vector.x, position.x);
                                position.y = Math.min(vector.y, position.y);
                                position.z = Math.max(vector.x, position.z);
                                position.w = Math.max(vector.y, position.w);
                            }
                        }
                        mc.entityRenderer.setupOverlayRendering();
                        if (position != null) {
                            if (box.value) {
                                RenderUtil.drawRect(position.x - 1, position.y, position.z - position.x + 3, position.w - position.y, new Color(0, 0, 0).getRGB(), false, 4f);
                                RenderUtil.drawRect(position.x - 1, position.y, position.z - position.x + 3, position.w - position.y, -1, false, 0.5f);
                            }

                            if (ent.getHealth() > 0.0f && health.value) {
                                GL11.glPushMatrix();
                                GlStateManager.enableBlend();
                                double hpPercentage = ent.getHealth() / ent.getMaxHealth();
                                if (hpPercentage > 1)
                                    hpPercentage = 1;
                                else if (hpPercentage < 0)
                                    hpPercentage = 0;

                                double offset = position.w - position.y;
                                double percentoffset = offset / ent.getMaxHealth();
                                double finalnumber = (percentoffset * ent.getHealth());
                                RenderUtil.drawRect(position.x - 3, position.y, 0.5, finalnumber, new Color(0, 0, 0).getRGB(), false, 4.0f);
                                RenderUtil.drawRect(position.x - 3, position.y, 0.5, finalnumber, getHealthColor(ent), true, 1.0f);
                                GL11.glScalef(0.7f, 0.7f, 0.7f);
                                //mc.fontRendererObj.drawStringWithShadow(roundToFirstDecimalPlace(hpPercentage * 100) + " %", (float) (position.x - 3) * 1.35f, (float) (position.y + finalnumber) * 1.35f + 10, -1);
                                GlStateManager.disableBlend();
                                GL11.glPopMatrix();
                            }

                        }
                    }

                }
            });
        }
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
    }
//
//    private double roundToFirstDecimalPlace(double value) {
//        double inc = 0.1;
//        double halfOfInc = inc / 2.0D;
//        double floored = Math.floor(value / inc) * inc;
//        if (value >= floored + halfOfInc)
//            return new BigDecimal(Math.ceil(value / inc) * inc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//        else return new BigDecimal(floored).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }
}

