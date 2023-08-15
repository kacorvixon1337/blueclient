package pl.kacorvixon.blue.module.impl.combat;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import pl.kacorvixon.blue.module.Category;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.impl.BooleanProperty;
import pl.kacorvixon.blue.property.impl.NumberProperty;
import pl.kacorvixon.blue.util.PlayerUtil;
import pl.kacorvixon.blue.util.math.MathUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AimAssist extends Module {

    public static NumberProperty range = new NumberProperty("AimAssist Range",4,3,6,1);
    public static NumberProperty fov = new NumberProperty("Aim fov",75,15,360,1);

    public static NumberProperty speed_yaw = new NumberProperty("Horizontal speed",1.2,1.0,5.0,0.1);
    public static BooleanProperty aim_pitch = new BooleanProperty("Aim vertically", true);
    public static NumberProperty speed_pitch = new NumberProperty("Vertical speed",1.2,1.0,5.0,0.1);

    //public static BooleanProperty click_aim = new BooleanProperty("Click Aim", true);
    public static BooleanProperty thruwalls = new BooleanProperty("Aim thru walls", false);
    public static BooleanProperty block_break = new BooleanProperty("Check block break", false);
    public static BooleanProperty weapon_only = new BooleanProperty("Swords only", false);

    public static BooleanProperty aimlock = new BooleanProperty("Aim lock", false);
    //public static BooleanProperty add_smoothnes = new BooleanProperty("Add smoothnes", false);





    /*

    //OK SO BASICALLY I JSUT SKIDDED IT TEMPORARILY CUZ I CANT GET THIS SHIT TO WORK
    //IL ITERALLY TRIED EVERYTHING BUT THE AIMASSIST JUST KEEP GLITCHING U CAN MAKE PR OR I WILL JUS TRECODE IT LATER
    //I WANT TO RELEASE IT ASAP SO DONT HATE OK OK OK?

    //OK SO BASICALLY I JSUT SKIDDED IT TEMPORARILY CUZ I CANT GET THIS SHIT TO WORK
    //IL ITERALLY TRIED EVERYTHING BUT THE AIMASSIST JUST KEEP GLITCHING U CAN MAKE PR OR I WILL JUS TRECODE IT LATER
    //I WANT TO RELEASE IT ASAP SO DONT HATE OK OK OK?


    //OK SO BASICALLY I JSUT SKIDDED IT TEMPORARILY CUZ I CANT GET THIS SHIT TO WORK
    //IL ITERALLY TRIED EVERYTHING BUT THE AIMASSIST JUST KEEP GLITCHING U CAN MAKE PR OR I WILL JUS TRECODE IT LATER
    //I WANT TO RELEASE IT ASAP SO DONT HATE OK OK OK?


       pls trust

     */











    public AimAssist() {
        super("AimAssist", "AimAssist", Category.Combat, 0);
        addProperties(range, fov, speed_yaw, aim_pitch, speed_pitch, thruwalls, block_break, weapon_only, aimlock);
    }
    @Override
    public void onTick(net.weavemc.loader.api.event.TickEvent event){
        if (Mouse.isButtonDown(0)) {
            Entity target = getTarget();

            if (target != null) {
                float[] rotations = this.getRotations(target);
                if (this.aim_pitch.getValue())
                    this.mc.thePlayer.rotationPitch = rotations[1];

                this.mc.thePlayer.rotationYaw = rotations[0];
            }
        }
    }
//    private Entity getTarget() {
//        for(Entity nigga : mc.theWorld.getLoadedEntityList()){
//            if(isValid(nigga)){
//                return nigga;
//            }
//        }
//        return null;
//    }

//    private boolean isValid(Entity entity) {
//        if(entity == null) return false;
//        if(!(entity instanceof EntityPlayer)) return false;
//        if(entity == mc.thePlayer) return false;
//        if(entity.isDead) return false;
//        if(entity.isInvisible()) return false;
//        if(entity.getDistanceToEntity(mc.thePlayer) > range.getValue()) return false;
//        if(entity.isDead) return false;
//        if(entity.isInvisible()) return false;
//        if(!thruwalls.getValue() && !this.mc.thePlayer.canEntityBeSeen(entity)) return false;
//        if(weapon_only.getValue() && !PlayerUtil.isHoldingWeapon()) return false;
//        if(!isInFov(entity, fov.getValue().floatValue())) return false;
//        if(block_break.getValue() && PlayerUtil.isBreakingBlock()) return false;
//
//        return true;
//    }

    private float[] getRotations(Entity target) {
        if(target != null) {
            float[] rotations = getTargetRotations(target);
            float sens = getSensitivity();

            if (!this.aimlock.getValue()) {
                float speedY = 40.0F * (this.speed_yaw.getValue().floatValue() / 20.0F);
                float speedP = 10.0F * (this.speed_pitch.getValue().floatValue() / 20.0F);

                //if (this.add_smoothnes.getValue()) {
                    if (fovToEntity(target) / 4.0F > this.fov.getValue().floatValue() / 4.0F) {
                        speedP /= 4.0F;
                    }

                    if (fovToEntity(target) / 4.0F > this.fov.getValue().floatValue() / 8.0F) {
                        speedY /= 2.0F;
                        speedP /= 8.0F;
                    }
                //}

                rotations[0] = smoothRotation(this.mc.thePlayer.rotationYaw, rotations[0], speedY);
                rotations[1] = smoothRotation(this.mc.thePlayer.rotationPitch, rotations[1], speedP);

                rotations[0] = (float) Math.round(rotations[0] / sens) * sens;
                rotations[1] = (float) Math.round(rotations[1] / sens) * sens;
            }

            return new float[]{rotations[0], rotations[1]};
        }
        return new float[]{0, 0};
    }
    public static float[] getTargetRotations(Entity target) {
        if (target == null) {
            return null;
        } else {
            double diffX = target.posX - mc.thePlayer.posX;
            double diffY;
            if (target instanceof EntityLivingBase) {
                EntityLivingBase x = (EntityLivingBase) target;
                diffY = x.posY + (double) x.getEyeHeight() * 0.9 - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
            } else {
                diffY = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
            }

            double diffZ = target.posZ - mc.thePlayer.posZ;
            float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592655897) - 90.0F;
            float pitch = (float) (-(Math.atan2(diffY, (double) MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ)) * 180.0 / 3.141592655897));

            return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)};
        }
    }
    public float getSensitivity() {
        float sensitivity = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return sensitivity * sensitivity * sensitivity * 8.0F * 0.15F;
    }
    public static float fovToEntity(Entity target) {
        double x = target.posX - mc.thePlayer.posX;
        double z = target.posZ - mc.thePlayer.posZ;
        double w = Math.atan2(x, z) * 57.2957795;
        return (float) (w * -1.0);
    }
    public float smoothRotation(float from, float to, float speed) {
        float angle = MathUtil.wrapAngleToCustom_float(to - from, 180.0F);
        if (angle > speed) {
            angle = speed;
        }

        if (angle < -speed) {
            angle = -speed;
        }

        return from + angle;
    }
    public static boolean isInFov(Entity target, float fov) {
        fov = (float) ((double) fov * 0.5);
        double v = ((double) (mc.thePlayer.rotationYaw - fovToEntity(target)) % 360.0 + 540.0) % 360.0 - 180.0;
        return v > 0.0 && v < (double) fov || (double) (-fov) < v && v < 0.0;
    }













//    private boolean isValid(Entity entity) {
//        if(entity == null) return false;
//        if(entity == mc.thePlayer) return false;
//        if(entity.isDead) return false;
//        if(entity.isInvisible()) return false;
//        if(!thruwalls.getValue() && !this.mc.thePlayer.canEntityBeSeen(entity)) return false;
//        if(entity.getDistanceToEntity(mc.thePlayer) > range.getValue()) return false;
//        if(weapon_only.getValue() && !PlayerUtil.isHoldingWeapon()) return false;
//        if(!isInFov(entity, fov.getValue().floatValue())) return false;
//        if(block_break.getValue() && PlayerUtil.isBreakingBlock()) return false;
//
//        return true;
//    }

    public static Entity getTarget() {
        Iterator<EntityPlayer> players = mc.theWorld.playerEntities.iterator();
        EntityPlayer target;
        do {
            do {
                do {
                    do {
                        do {
                            do {
                                do {
                                    if (!players.hasNext())
                                        return null;

                                    target = players.next();
                                } while (target == mc.thePlayer);
                            } while (target.isDead);
                        } while (!AimAssist.thruwalls.getValue() && !mc.thePlayer.canEntityBeSeen(target));
                    } while (target.isInvisible());
                } while (mc.thePlayer.isOnSameTeam(target));
            } while (target.getDistanceToEntity(mc.thePlayer) > range.getValue());
        } while (!aimlock.getValue() && !isInFov(target, (float) fov.getValue().floatValue()));
        return target;
    }
}

