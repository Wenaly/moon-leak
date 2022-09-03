package com.moon.api.mixin.mixins;

import com.moon.Moon;
import com.moon.impl.modules.render.Animations;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityLivingBase.class})
public class MixinEntityLivingBase {

    @Inject(method={"getArmSwingAnimationEnd"}, at={@At(value="HEAD")}, cancellable=true)
    private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
        if (Moon.Modules.isModuleEnabled("Animations") && Animations.INSTANCE.changeSwing.getValue()) {
            info.setReturnValue(Animations.INSTANCE.swingDelay.getValue().intValue());
        }
    }
}
