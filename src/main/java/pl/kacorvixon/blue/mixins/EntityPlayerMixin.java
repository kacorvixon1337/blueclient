package pl.kacorvixon.blue.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.module.impl.combat.KeepSprint;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin {


    @Overwrite
    public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
        if (targetEntity.canAttackWithItem()) {
            if (!targetEntity.hitByEntity(Minecraft.getMinecraft().thePlayer)) {
                float f = (float) Minecraft.getMinecraft().thePlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
                int i = 0;
                float f1 = 0.0F;

                if (targetEntity instanceof EntityLivingBase) {
                    f1 = EnchantmentHelper.getModifierForCreature(Minecraft.getMinecraft().thePlayer.getHeldItem(), ((EntityLivingBase) targetEntity).getCreatureAttribute());
                } else {
                    f1 = EnchantmentHelper.getModifierForCreature(Minecraft.getMinecraft().thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
                }

                i = i + EnchantmentHelper.getKnockbackModifier(Minecraft.getMinecraft().thePlayer);

                if (Minecraft.getMinecraft().thePlayer.isSprinting()) {
                    ++i;
                }

                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = Minecraft.getMinecraft().thePlayer.fallDistance > 0.0F && !Minecraft.getMinecraft().thePlayer.onGround && !Minecraft.getMinecraft().thePlayer.isOnLadder() && !Minecraft.getMinecraft().thePlayer.isInWater() && !Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.blindness) && Minecraft.getMinecraft().thePlayer.ridingEntity == null && targetEntity instanceof EntityLivingBase;

                    if (flag && f > 0.0F) {
                        f *= 1.5F;
                    }

                    f = f + f1;
                    boolean flag1 = false;
                    int j = EnchantmentHelper.getFireAspectModifier(Minecraft.getMinecraft().thePlayer);

                    if (targetEntity instanceof EntityLivingBase && j > 0 && !targetEntity.isBurning()) {
                        flag1 = true;
                        targetEntity.setFire(1);
                    }

                    double d0 = targetEntity.motionX;
                    double d1 = targetEntity.motionY;
                    double d2 = targetEntity.motionZ;
                    boolean flag2 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(Minecraft.getMinecraft().thePlayer), f);

                    if (flag2) {
                        if (i > 0) {
                            targetEntity.addVelocity((double) (-MathHelper.sin(Minecraft.getMinecraft().thePlayer.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(Minecraft.getMinecraft().thePlayer.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F));
                            if(!Blue.getInstance().moduleAdministration.getModule(KeepSprint.class).isEnabled()) {
                                Minecraft.getMinecraft().thePlayer.motionX *= 0.6D;
                                Minecraft.getMinecraft().thePlayer.motionZ *= 0.6D;
                                Minecraft.getMinecraft().thePlayer.setSprinting(false);
                            }
                        }

                        if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
                            ((EntityPlayerMP) targetEntity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(targetEntity));
                            targetEntity.velocityChanged = false;
                            targetEntity.motionX = d0;
                            targetEntity.motionY = d1;
                            targetEntity.motionZ = d2;
                        }

                        if (flag) {
                            Minecraft.getMinecraft().thePlayer.onCriticalHit(targetEntity);
                        }

                        if (f1 > 0.0F) {
                            Minecraft.getMinecraft().thePlayer.onEnchantmentCritical(targetEntity);
                        }

                        if (f >= 18.0F) {
                            Minecraft.getMinecraft().thePlayer.triggerAchievement(AchievementList.overkill);
                        }

                        Minecraft.getMinecraft().thePlayer.setLastAttacker(targetEntity);

                        if (targetEntity instanceof EntityLivingBase) {
                            EnchantmentHelper.applyThornEnchantments((EntityLivingBase) targetEntity, Minecraft.getMinecraft().thePlayer);
                        }

                        EnchantmentHelper.applyArthropodEnchantments(Minecraft.getMinecraft().thePlayer, targetEntity);
                        ItemStack itemstack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
                        Entity entity = targetEntity;

                        if (targetEntity instanceof EntityDragonPart) {
                            IEntityMultiPart ientitymultipart = ((EntityDragonPart) targetEntity).entityDragonObj;

                            if (ientitymultipart instanceof EntityLivingBase) {
                                entity = (EntityLivingBase) ientitymultipart;
                            }
                        }

                        if (itemstack != null && entity instanceof EntityLivingBase) {
                            itemstack.hitEntity((EntityLivingBase) entity, Minecraft.getMinecraft().thePlayer);

                            if (itemstack.stackSize <= 0) {
                                Minecraft.getMinecraft().thePlayer.destroyCurrentEquippedItem();
                            }
                        }

                        if (targetEntity instanceof EntityLivingBase) {
                            Minecraft.getMinecraft().thePlayer.addStat(StatList.damageDealtStat, Math.round(f * 10.0F));

                            if (j > 0) {
                                targetEntity.setFire(j * 4);
                            }
                        }

                        Minecraft.getMinecraft().thePlayer.addExhaustion(0.3F);
                    } else if (flag1) {
                        targetEntity.extinguish();
                    }
                }
            }
        }
    }
}
