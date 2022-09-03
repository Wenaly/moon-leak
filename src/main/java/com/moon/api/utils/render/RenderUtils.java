package com.moon.api.utils.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {

    public static void drawRoundedRect(float x, float y, float width, float height, float radius) {


        GL11.glEnable(GL11.GL_BLEND);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        drawArc((x + width - radius), (y + height - radius), radius, 0, 90, 16);
        drawArc((x + radius), (y + height - radius), radius, 90, 180, 16);
        drawArc(x + radius, y + radius, radius, 180, 270, 16);
        drawArc((x + width - radius), (y + radius), radius, 270, 360, 16);

        // GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);


        GL11.glBegin(GL11.GL_TRIANGLES);
        {
            addVertex(x + width - radius, y);
            addVertex(x + radius, y);

            addVertex(x + width - radius, y + radius);
            addVertex(x + width - radius, y + radius);

            addVertex(x + radius, y);
            addVertex(x + radius, y + radius);

            addVertex(x + width, y + radius);
            addVertex(x, y + radius);

            addVertex(x, y + height - radius);
            addVertex(x + width, y + radius);

            addVertex(x, y + height-radius);
            addVertex(x + width, y + height - radius);

            addVertex(x + width - radius, y + height - radius);
            addVertex(x + radius, y + height - radius);

            addVertex(x + width - radius, y + height);
            addVertex(x + width - radius, y + height);

            addVertex(x + radius, y + height - radius);
            addVertex(x + radius, y + height);
        }



        GL11.glEnd();

    }

    public static void drawLine(final double x, final double y, final double x1, final double y1, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public static void drawOutlineLine(double left, double top, double right, double bottom, double width, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth((float) width);

        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, bottom, 0.0D).color(r, g, b, a).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).color(r, g, b, a).endVertex();
        bufferbuilder.pos(right, top, 0.0D).color(r, g, b, a).endVertex();
        bufferbuilder.pos(left, top, 0.0D).color(r, g, b, a).endVertex();
        bufferbuilder.pos(left, bottom, 0.0D).color(r, g, b, a).endVertex();
        tessellator.draw();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        //GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void color(float r, float g, float b, float a) {
        GL11.glColor4f((float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
    }

    public static void color(double r, double g, double b, double a) {
        GL11.glColor4f((float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
    }

    public static void color(int r, int g, int b, int a) {
        GL11.glColor4f((float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
    }

    public static void color(float r, float g, float b) {
        GL11.glColor3f((float) r / 255, (float) g / 255, (float) b / 255);
    }

    public static void color(double r, double g, double b) {
        GL11.glColor3f((float) r / 255, (float) g / 255, (float) b / 255);
    }

    public static void color(int r, int g, int b) {
        GL11.glColor3f((float) r / 255, (float) g / 255, (float) b / 255);
    }

    public static void sewTexture(float s, float t, float r) {
        GL11.glTexCoord3f(s, t, r);
    }

    public static void sewTexture(float s, float t) {
        GL11.glTexCoord2f(s, t);
    }

    public static void sewTexture(float s) {
        GL11.glTexCoord1f(s);
    }

    public static void sewTexture(double s, double t, double r) {
        sewTexture((float) s, (float) t, (float) r);
    }

    public static void sewTexture(double s, double t) {
        sewTexture((float) s, (float) t);
    }

    public static void sewTexture(double s) {
        sewTexture((float) s);
    }

    public static void sewTexture(int s, int t, int r) {
        sewTexture((float) s, (float) t, (float) r);
    }

    public static void sewTexture(int s, int t) {
        sewTexture((float) s, (float) t);
    }

    public static void sewTexture(int s) {
        sewTexture((float) s);
    }

    public static void addVertex(float x, float y, float z) {
        sewTexture(x, y, z);
    }

    public static void addVertex(float x, float y) {
        GL11.glVertex2f(x, y);
    }

    public static void addVertex(double x, double y, double z) {
        addVertex((float) x, (float) y, (float) z);
    }

    public static void addVertex(double x, double y) {
        addVertex((float) x, (float) y);
    }

    public static void addVertex(int x, int y, int z) {
        addVertex((float) x, (float) y, (float) z);
    }

    public static void addVertex(int x, int y) {
        addVertex((float) x, (float) y);
    }

    public static void drawArc(float cx, float cy, float r, float start_angle, float end_angle, float num_segments) {
        GL11.glEnable(GL11.GL_TRIANGLES);

        for (int i = (int) (num_segments / (360 / start_angle)) + 1; i <= num_segments / (360 / end_angle); i++) {
            double previousAngle = 2 * Math.PI * (i - 1) / num_segments;
            double angle = 2 * Math.PI * i / num_segments;

            addVertex(cx, cy);
            addVertex(cx + Math.cos(angle) * r, (cy + Math.sin(angle) * r));
            addVertex(cx + Math.cos(previousAngle) * r, cy + Math.sin(previousAngle) * r);
        }

        GL11.glEnd();
    }

    public static boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height)
    {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }

}