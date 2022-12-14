package com.moon.api.mixin.mixins.access;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketEntityVelocity.class})
public interface ISPacketEntityVelocity {
    @Accessor(value="motionX")
    public void setMotionX(int var1);

    @Accessor(value="motionY")
    public void setMotionY(int var1);

    @Accessor(value="motionZ")
    public void setMotionZ(int var1);
}
