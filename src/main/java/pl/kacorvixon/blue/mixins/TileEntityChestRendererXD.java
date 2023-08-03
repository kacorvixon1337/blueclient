package pl.kacorvixon.blue.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import pl.kacorvixon.blue.module.ModuleAdministration;
import pl.kacorvixon.blue.module.impl.render.ChestESP;
import pl.kacorvixon.blue.util.render.RenderUtil;

import static net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer.DESTROY_STAGES;
import static org.lwjgl.opengl.GL11.*;

@Mixin(TileEntityChestRenderer.class)
public abstract class TileEntityChestRendererXD {

    @Shadow
    private boolean isChristmas;
    @Shadow
    private static final ResourceLocation textureTrappedDouble = new ResourceLocation("textures/entity/chest/trapped_double.png");
    @Shadow
    private static final ResourceLocation textureChristmasDouble = new ResourceLocation("textures/entity/chest/christmas_double.png");
    @Shadow
    private static final ResourceLocation textureNormalDouble = new ResourceLocation("textures/entity/chest/normal_double.png");
    @Shadow
    private static final ResourceLocation textureTrapped = new ResourceLocation("textures/entity/chest/trapped.png");
    @Shadow
    private static final ResourceLocation textureChristmas = new ResourceLocation("textures/entity/chest/christmas.png");
    @Shadow
    private static final ResourceLocation textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
    @Shadow
    private ModelChest simpleChest = new ModelChest();
    @Shadow
    private ModelChest largeChest = new ModelLargeChest();


    @Overwrite //overwrite cuz lazy, i will recode in the future i want to release it fasttt
    public void renderTileEntityAt(TileEntityChest te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int i;

        if (!te.hasWorldObj()) {
            i = 0;
        } else {
            Block block = te.getBlockType();
            i = te.getBlockMetadata();

            if (block instanceof BlockChest && i == 0) {
                ((BlockChest) block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }

            te.checkForAdjacentChests();
        }

        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null) {
            ModelChest modelchest;

            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null) {
                modelchest = this.simpleChest;

                if (destroyStage >= 0) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4.0F, 4.0F, 1.0F);
                    GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode(5888);
                } else if (this.isChristmas) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(textureChristmas);
                } else if (te.getChestType() == 1) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(textureTrapped);
                } else {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(textureNormal);
                }
            } else {
                modelchest = this.largeChest;

                if (destroyStage >= 0) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(8.0F, 4.0F, 1.0F);
                    GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode(5888);
                } else if (this.isChristmas) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(textureChristmasDouble);
                } else if (te.getChestType() == 1) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(textureTrappedDouble);
                } else {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(textureNormalDouble);
                }
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();

            if (destroyStage < 0) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            GlStateManager.translate(0.5F, 0.5F, 0.5F);
            int j = 0;

            if (i == 2) {
                j = 180;
            }

            if (i == 3) {
                j = 0;
            }

            if (i == 4) {
                j = 90;
            }

            if (i == 5) {
                j = -90;
            }

            if (i == 2 && te.adjacentChestXPos != null) {
                GlStateManager.translate(1.0F, 0.0F, 0.0F);
            }

            if (i == 5 && te.adjacentChestZPos != null) {
                GlStateManager.translate(0.0F, 0.0F, -1.0F);
            }

            GlStateManager.rotate((float) j, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

            if (te.adjacentChestZNeg != null) {
                float f1 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;

                if (f1 > f) {
                    f = f1;
                }
            }

            if (te.adjacentChestXNeg != null) {
                float f2 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;

                if (f2 > f) {
                    f = f2;
                }
            }

            f = 1.0F - f;
            f = 1.0F - f * f * f;
            ChestESP chestESP = ModuleAdministration.getInstance(ChestESP.class);
            modelchest.chestLid.rotateAngleX = -(f * (float) Math.PI / 2.0F);
            if (chestESP.enabled && (chestESP.visible.value || chestESP.oddcluded.value)) {
                if (chestESP.oddcluded.value) {
                    glDisable(GL_TEXTURE_2D);
                    RenderUtil.glColor(chestESP.oddcludedcolor.value.getRGB());
                    glDisable(GL_DEPTH_TEST);
                    glDisable(GL_LIGHTING);
                    glDepthMask(true);
                    modelchest.renderAll();
                    glEnable(GL_LIGHTING);
                    glColor4f(1,1,1,1);
                    glEnable(GL_DEPTH_TEST);
                    glDepthMask(true);
                    glEnable(GL_TEXTURE_2D);
                }
                if (chestESP.visible.value) {
                    glDisable(GL_TEXTURE_2D);
                    RenderUtil.glColor(chestESP.visiblecolor.value.getRGB());
                    if (chestESP.flat.value) {
                        glDisable(GL_LIGHTING);
                    }
                }
                modelchest.renderAll();
                if (chestESP.visible.value) {
                    glEnable(GL_TEXTURE_2D);
                    glEnable(GL_LIGHTING);


                }

                glColor4f(1,1,1,1);
            } else {
                modelchest.renderAll();
            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }
}
