package com.moon.api.mixin.mixins;

import com.moon.Moon;
import com.moon.api.event.events.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.MoverType;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityPlayerSP.class}, priority=9998)
public abstract class MixinEntityPlayerSP
        extends AbstractClientPlayer {

    private MotionUpdateEvent motionEvent;

    public MixinEntityPlayerSP(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_) {
        super(p_i47378_2_, p_i47378_3_.getGameProfile());
    }

    @Inject(method={"sendChatMessage"}, at={@At(value="HEAD")}, cancellable=true)
    public void sendChatMessage(String message, CallbackInfo callback) {
        ChatEvent chatEvent = new ChatEvent(message);
        Moon.getEventProcessor().postEvent(chatEvent);
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void pushOutOfBlocksHook(double x, double y, double z, CallbackInfoReturnable<Boolean> info) {
        EntityPushEvent event = new EntityPushEvent(1);
        Moon.getEventProcessor().postEvent(event);
        if (event.isCancelled()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="HEAD")}, cancellable=true)
    private void preMotion(CallbackInfo info) {
        UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
        Moon.getEventProcessor().postEvent(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="RETURN")})
    private void postMotion(CallbackInfo info) {
        UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);
        Moon.getEventProcessor().postEvent(event);
    }

    @Redirect(method={"move"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(AbstractClientPlayer player, MoverType moverType, double x, double y, double z) {
        MoveEvent event = new MoveEvent(0, moverType, x, y, z);
        Moon.getEventProcessor().postEvent(event);
        if (!event.isCancelled()) {
            super.move(event.getType(), event.getX(), event.getY(), event.getZ());
        }
    }

    @Inject(method = "onUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V",
                    shift = At.Shift.BEFORE))
    private void onUpdateWalkingPlayerPre(CallbackInfo ci)
    {
        motionEvent = new MotionUpdateEvent(0, this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
        Moon.getEventProcessor().postEvent(motionEvent);
        if (motionEvent.isCancelled())
        {
            ci.cancel();
        }
    }

    @Inject(
            method = "onUpdateWalkingPlayer",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void onUpdateWalkingPlayer_Head(CallbackInfo callbackInfo) {
        motionEvent = new MotionUpdateEvent(0, this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
        Moon.getEventProcessor().postEvent(motionEvent);
        if (motionEvent.isCancelled())
        {
            callbackInfo.cancel();
        }
    }

    @Redirect(
            method = "onUpdateWalkingPlayer",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/client/entity/EntityPlayerSP.posX:D"))
    private double posXHook(EntityPlayerSP entityPlayerSP)
    {
        return motionEvent.getX();
    }

    @Redirect(
            method = "onUpdateWalkingPlayer",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/util/math/AxisAlignedBB.minY:D"))
    private double minYHook(AxisAlignedBB axisAlignedBB)
    {
        return motionEvent.getY();
    }

    @Redirect(
            method = "onUpdateWalkingPlayer",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/client/entity/EntityPlayerSP.posZ:D"))
    private double posZHook(EntityPlayerSP entityPlayerSP)
    {
        return motionEvent.getZ();
    }

    @Redirect(
            method = "onUpdateWalkingPlayer",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/client/entity/EntityPlayerSP.rotationYaw:F"))
    private float rotationYawHook(EntityPlayerSP entityPlayerSP)
    {
        return motionEvent.getYaw();
    }

    @Redirect(
            method = "onUpdateWalkingPlayer",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/client/entity/EntityPlayerSP.rotationPitch:F"))
    private float rotationPitchHook(EntityPlayerSP entityPlayerSP)
    {
        return motionEvent.getPitch();
    }

    @Redirect(
            method = "onUpdateWalkingPlayer",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/client/entity/EntityPlayerSP.onGround:Z"))
    private boolean onGroundHook(EntityPlayerSP entityPlayerSP)
    {
        return motionEvent.isOnGround();
    }

}