package com.moon.api.mixin.mixins.access;

import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketExplosion.class})
public interface ISPacketExplosion {
    @Accessor(value="motionX")
    public void setMotionX(float var1);

    @Accessor(value="motionY")
    public void setMotionY(float var1);

    @Accessor(value="motionZ")
    public void setMotionZ(float var1);
}
