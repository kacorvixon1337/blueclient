package com.example.mod.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({RendererLivingEntity.class})
public abstract class MixinRenderPlayer<T extends EntityLivingBase> extends Render<T> {
   @Shadow
   protected ModelBase mainModel;
   @Shadow
   protected boolean renderOutlines;
   @Shadow
   @Final
   private static Logger logger;

   @Shadow
   protected abstract float getSwingProgress(T var1, float var2);

   @Shadow
   protected abstract float interpolateRotation(float var1, float var2, float var3);

   @Shadow
   protected abstract void renderLivingAt(T var1, double var2, double var4, double var6);

   @Shadow
   protected abstract float handleRotationFloat(T var1, float var2);

   @Shadow
   protected abstract void rotateCorpse(T var1, float var2, float var3, float var4);

   @Shadow
   protected abstract void preRenderCallback(T var1, float var2);

   @Shadow
   protected abstract boolean setScoreTeamColor(T var1);

   @Shadow
   protected abstract void renderModel(T var1, float var2, float var3, float var4, float var5, float var6, float var7);

   @Shadow
   protected abstract void unsetScoreTeamColor();

   @Shadow
   protected abstract boolean setDoRenderBrightness(T var1, float var2);

   @Shadow
   protected abstract void unsetBrightness();

   @Shadow
   protected abstract void renderLayers(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

   protected MixinRenderPlayer(RenderManager renderManager) {
      super(renderManager);
   }

   @Overwrite
   public void doRender(T player, double posX, double posY, double posZ, float yaw, float partialTicks) {
      if (player == Minecraft.getMinecraft().thePlayer) {
         GlStateManager.pushMatrix();
         GlStateManager.disableCull();
         this.mainModel.swingProgress = this.getSwingProgress(player, partialTicks);
         this.mainModel.isRiding = player.isRiding();
         this.mainModel.isChild = player.isChild();

         try {
            float var10 = this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
            float var11 = this.interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, partialTicks);
            float var12 = var11 - var10;
            if (player.isRiding() && player.ridingEntity instanceof EntityLivingBase) {
               EntityLivingBase var13 = (EntityLivingBase) player.ridingEntity;
               var10 = this.interpolateRotation(var13.prevRenderYawOffset, var13.renderYawOffset, partialTicks);
               var12 = var11 - var10;
               float var14 = MathHelper.wrapAngleTo180_float(var12);
               if (var14 < -85.0F) {
                  var14 = -85.0F;
               }

               if (var14 >= 85.0F) {
                  var14 = 85.0F;
               }

               var10 = var11 - var14;
               if (var14 * var14 > 2500.0F) {
                  var10 += var14 * 0.2F;
               }
            }

            float pitch = player.rotationPitch;
            float prevPitch = player.prevRotationPitch;
            float var20 = prevPitch + (pitch - prevPitch) * partialTicks;
            this.renderLivingAt(player, posX, posY, posZ);
            float var14 = this.handleRotationFloat(player, partialTicks);
            this.rotateCorpse(player, var14, var10, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(player, partialTicks);
            GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
            float var16 = player.prevLimbSwingAmount + (player.limbSwingAmount - player.prevLimbSwingAmount) * partialTicks;
            float var17 = player.limbSwing - player.limbSwingAmount * (1.0F - partialTicks);
            if (player.isChild()) {
               var17 *= 3.0F;
            }

            if (var16 > 1.0F) {
               var16 = 1.0F;
            }

            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations(player, var17, var16, partialTicks);
            this.mainModel.setRotationAngles(var17, var16, var14, var12, var20, 0.0625F, player);
            if (this.renderOutlines) {
               boolean var18 = this.setScoreTeamColor(player);
               this.renderModel(player, var17, var16, var14, var12, var20, 0.0625F);
               if (var18) {
                  this.unsetScoreTeamColor();
               }
            } else {
               boolean var18 = this.setDoRenderBrightness(player, partialTicks);
               this.renderModel(player, var17, var16, var14, var12, var20, 0.0625F);
               if (var18) {
                  this.unsetBrightness();
               }

               GlStateManager.depthMask(true);
               if (!(player instanceof EntityPlayer) || !((EntityPlayer) player).isSpectator()) {
                  this.renderLayers(player, var17, var16, partialTicks, var14, var12, var20, 0.0625F);
               }
            }

            GlStateManager.disableRescaleNormal();
         } catch (Exception var201) {
            logger.error("Couldn't render entity", var201);
         }

         GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GlStateManager.enableTexture2D();
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
         GlStateManager.enableCull();
         GlStateManager.popMatrix();
         if (!this.renderOutlines) {
            super.doRender(player, posX, posY, posZ, -yaw, partialTicks);
         }
      } else {
         GlStateManager.pushMatrix();
         GlStateManager.disableCull();
         this.mainModel.swingProgress = this.getSwingProgress(player, partialTicks);
         this.mainModel.isRiding = player.isRiding();
         this.mainModel.isChild = player.isChild();

         try {
            float var10 = this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
            float var11 = this.interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, partialTicks);
            float var12 = var11 - var10;
            if (player.isRiding() && player.ridingEntity instanceof EntityLivingBase) {
               EntityLivingBase var13 = (EntityLivingBase) player.ridingEntity;
               var10 = this.interpolateRotation(var13.prevRenderYawOffset, var13.renderYawOffset, partialTicks);
               var12 = var11 - var10;
               float var14 = MathHelper.wrapAngleTo180_float(var12);
               if (var14 < -85.0F) {
                  var14 = -85.0F;
               }

               if (var14 >= 85.0F) {
                  var14 = 85.0F;
               }

               var10 = var11 - var14;
               if (var14 * var14 > 2500.0F) {
                  var10 += var14 * 0.2F;
               }
            }

            float var20 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
            this.renderLivingAt(player, posX, posY, posZ);
            float var14 = this.handleRotationFloat(player, partialTicks);
            this.rotateCorpse(player, var14, var10, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(player, partialTicks);
            GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
            float var16 = player.prevLimbSwingAmount + (player.limbSwingAmount - player.prevLimbSwingAmount) * partialTicks;
            float var17 = player.limbSwing - player.limbSwingAmount * (1.0F - partialTicks);
            if (player.isChild()) {
               var17 *= 3.0F;
            }

            if (var16 > 1.0F) {
               var16 = 1.0F;
            }

            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations(player, var17, var16, partialTicks);
            this.mainModel.setRotationAngles(var17, var16, var14, var12, var20, 0.0625F, player);
            if (this.renderOutlines) {
               boolean var18 = this.setScoreTeamColor(player);
               this.renderModel(player, var17, var16, var14, var12, var20, 0.0625F);
               if (var18) {
                  this.unsetScoreTeamColor();
               }
            } else {
               boolean var18 = this.setDoRenderBrightness(player, partialTicks);
               this.renderModel(player, var17, var16, var14, var12, var20, 0.0625F);
               if (var18) {
                  this.unsetBrightness();
               }

               GlStateManager.depthMask(true);
               if (!(player instanceof EntityPlayer) || !((EntityPlayer) player).isSpectator()) {
                  this.renderLayers(player, var17, var16, partialTicks, var14, var12, var20, 0.0625F);
               }
            }

            GlStateManager.disableRescaleNormal();
         } catch (Exception var21) {
            logger.error("Couldn't render entity", var21);
         }

         GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GlStateManager.enableTexture2D();
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
         GlStateManager.enableCull();
         GlStateManager.popMatrix();
         if (!this.renderOutlines) {
            super.doRender(player, posX, posY, posZ, yaw, partialTicks);
         }
      }

      //Blue.getInstance().call(new Render2DLivingEvent(e));
   }

}