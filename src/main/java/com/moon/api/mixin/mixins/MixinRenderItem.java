package com.moon.api.mixin.mixins;

import com.moon.impl.modules.render.Animations;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem {

    private static final ResourceLocation RESOURCE = new ResourceLocation("textures/rainbow.png");

    @Redirect(method = "renderEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V", ordinal = 0))
    public void bindHook(TextureManager textureManager, ResourceLocation resource)
    {
        if (Animations.INSTANCE.rainbowEnchant.getValue())
        {
            textureManager.bindTexture(RESOURCE);
        } else {
            textureManager.bindTexture(resource);
        }
    }

}