package com.moon.api.utils.render;

import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class ColorUtils extends Color {

    private static int factor = 0;

    public ColorUtils(int r, int g, int b) {
        super(r, g, b);
    }

    public ColorUtils(int rgb) {
        super(rgb);
    }

    public ColorUtils(int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
    }

    public ColorUtils(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public ColorUtils(Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public ColorUtils(ColorUtils color, int a) {
        super(color.getRed(), color.getGreen(), color.getBlue(), a);
    }

    public static ColorUtils fromHSB(float hue, float saturation, float brightness) {
        return new ColorUtils(Color.getHSBColor(hue, saturation, brightness));
    }

    public float getHue() {
        return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[0];
    }

    public float getSaturation() {
        return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[1];
    }

    public float getBrightness() {
        return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[2];
    }

    public void glColor() {
        GlStateManager.color(getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f, getAlpha() / 255.0f);
    }

    public static int shadeColour(int color, int precent) {
        int r = (((color & 0xFF0000) >> 16) * (100 + precent) / 100);
        int g = (((color & 0xFF00) >> 8) * (100 + precent) / 100);
        int b = ((color & 0xFF) * (100 + precent) / 100);
        return new Color(r,g,b).hashCode();
    }

    public static Color getRainbow(int offset) {
        return fromHSB((System.currentTimeMillis() + offset % (360 * 32)) / (360f * 32), 1, 1);
    }

    public static Color getWave(Color color, int offset) {
        if (factor > 255) factor = 0;
        factor += (15 + offset);

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        float[] hsb = Color.RGBtoHSB(r, g, b, null);

        Color factorized = Color.getHSBColor( hsb[0], hsb[1], 0.5f * ((factor % 80) + hsb[2]));

        r = factorized.getRed();
        g = factorized.getGreen();
        b = factorized.getBlue();

        return new Color(r, g , b, a);
    }

    static int steps = 100;
    static int step = 0;
    static boolean revert = false;

    public static void setMaxFadeStep(int maxFadeStep) {
        steps = maxFadeStep;
    }

    public static void update() {
        step += 1;
    }

    public static Color fade(Color a, Color b, int alpha) {

        final Color oldColor = revert ? b : a;
        final Color newColor = revert ? a : b;
        final int dRed = newColor.getRed() - oldColor.getRed();
        final int dGreen = newColor.getGreen() - oldColor.getGreen();
        final int dBlue = newColor.getBlue() - oldColor.getBlue();
        // No point if no difference.
        if (dRed != 0 || dGreen != 0 || dBlue != 0) {
            // Do it in n steps.
            final Color c = new Color(
                        oldColor.getRed() + ((dRed * step) / steps),
                        oldColor.getGreen() + ((dGreen * step) / steps),
                        oldColor.getBlue() + ((dBlue * step) / steps), alpha);
            if (step >= steps) {
                revert = true;
            }
            return c;
        } else {
            revert = true;
        }
        return new Color(0, 0,0, alpha);
    }

}