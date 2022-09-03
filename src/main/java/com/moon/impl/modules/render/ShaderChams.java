package com.moon.impl.modules.render;

import com.moon.api.event.events.RenderEntityModelEvent;
import com.moon.api.event.handler.EventHandler;
import com.moon.api.module.Module;
import com.moon.api.utils.render.GLShader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec2f;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@Module.registration(name = "ShaderChams", description = "", category = Module.Category.Render)
public class ShaderChams extends Module {

    protected final long initTime = System.currentTimeMillis();
    protected final GLShader waterShader = GLShader.createShader("water");

    @EventHandler
    public void onRenderEntityModel(RenderEntityModelEvent event) {
        if (nullCheck()) return;
        if (!(event.entity instanceof EntityPlayer)) return;
        event.setCancelled(true);
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glPushMatrix();
        Color color = new Color(255, 255, 255, 255);
        waterShader.bind();
        waterShader.set("time", (System.currentTimeMillis() - initTime) / 2000.0f);
        waterShader.set("resolution", new Vec2f((mc.displayWidth * 2) /*/ 20.0f*/, (mc.displayHeight * 2) /*/ 20.0f*/));
        waterShader.set("tex", 0);

        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, color.getAlpha());
        waterShader.set("alpha", color.getAlpha() / 255.0f);
        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(1.0f, -2000000f);
        event.modelBase.render(event.entity,
                event.limbSwing,
                event.limbSwingAmount,
                event.age,
                event.headYaw,
                event.headPitch,
                event.scale);
        glDisable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(1.0f, 2000000f);
        GlStateManager.popMatrix();
        waterShader.unbind();
        glPopMatrix();
        glPopAttrib();
    }

}
