package com.moon.api.newgui;

import com.moon.api.module.Module;
import com.moon.api.newgui.component.Component;
import com.moon.api.newgui.component.impl.ColorComponent;
import com.moon.api.newgui.component.impl.KeybindComponent;
import com.moon.api.newgui.component.impl.ModuleComponent;
import com.moon.api.newgui.frame.Frame;
import com.moon.api.newgui.frame.impl.CategoryFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Click extends GuiScreen {


    private final ArrayList<Frame> frames = new ArrayList<>();
    private boolean attached = false;

    public void init() {
        if (!attached)
        {
            attached = true;
        }

        getFrames().clear();
        int x = 2;
        int y = 2;
        for (Module.Category moduleCategory : Module.Category.values()) {
            getFrames().add(new CategoryFrame(moduleCategory, x, y, 110, 16));
            if (x + 220 >= new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
                x = 2;
                y += 20;
            } else x += 112;
        }
        getFrames().forEach(Frame::init);
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        super.onResize(mcIn, w, h);
        init();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (mc.world == null)
        {
            this.drawDefaultBackground();
        }

        getFrames().forEach(frame -> frame.drawScreen(mouseX,mouseY,partialTicks));
    }

    @Override
    protected void keyTyped(char character, int keyCode) throws IOException {
        super.keyTyped(character, keyCode);
        getFrames().forEach(frame -> frame.keyTyped(character,keyCode));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        getFrames().forEach(frame -> frame.mouseClicked(mouseX,mouseY,mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        getFrames().forEach(frame -> frame.mouseReleased(mouseX,mouseY,mouseButton));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        getFrames().forEach(frame -> {
            for (Component comp : frame.getComponents()) {
                if (comp instanceof ModuleComponent) {
                    final ModuleComponent moduleComponent = (ModuleComponent) comp;
                    for (Component component : moduleComponent.getComponents()) {
                        if (component instanceof KeybindComponent) {
                            final KeybindComponent keybindComponent = (KeybindComponent) component;
                            keybindComponent.setBinding(false);
                        }
                    }
                }
            }
        });
    }

    public void onGuiOpened() {
        getFrames().forEach(frame -> {
            for (Component comp : frame.getComponents()) {
                if (comp instanceof ModuleComponent) {
                    final ModuleComponent moduleComponent = (ModuleComponent) comp;
                    for (Component component : moduleComponent.getComponents()) {
                        if (component instanceof ColorComponent) {
                            final ColorComponent colorComponent = (ColorComponent) component;
                            float[] hsb = Color.RGBtoHSB(colorComponent.getColorSetting().getRed(), colorComponent.getColorSetting().getGreen(), colorComponent.getColorSetting().getBlue(), null);
                            colorComponent.setHue(hsb[0]);
                            colorComponent.setSaturation(hsb[1]);
                            colorComponent.setBrightness(hsb[2]);
                            colorComponent.setAlpha(colorComponent.getColorSetting().getAlpha() / 255.f);
                        }
                    }
                }
            }
        });
    }

    public ArrayList<Frame> getFrames() {
        return frames;
    }
}
