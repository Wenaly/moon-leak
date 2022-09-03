package com.moon.api.gui.frames;

import com.moon.Moon;
import com.moon.api.gui.Frame;
import com.moon.api.gui.Gui;
import com.moon.api.gui.ModuleFrame;
import com.moon.api.module.Module;
import com.moon.api.utils.IMinecraft;
import com.moon.api.utils.render.ColorUtils;
import com.moon.api.utils.render.RenderUtils2D;
import com.moon.impl.modules.client.ClickGui;

import java.awt.*;
import java.util.ArrayList;

public class CategoryFrame implements IMinecraft {
    public ArrayList<Frame> components;
    public Module.Category category;
    private final int width;
    private final int height;
    public int x;
    public int y;
    public boolean isOpen;
    private boolean isDragging;
    public int dragX;
    public int dragY;

    public CategoryFrame(Module.Category cat) {
        this.category = cat;
        this.components = new ArrayList<>();
        this.width = Gui.Width;
        this.height = Gui.Height;
        this.x = 5;
        this.y = 5;
        this.dragX = 0;
        this.isOpen = true;
        this.isDragging = false;

        int tY = this.height;

        for (Module mod : Moon.Modules.getModules()) {
            if (mod.getCategory().equals(category)) {
                ModuleFrame moduleButton = new ModuleFrame();
                moduleButton.init(mod, this, tY, false);
                this.components.add(moduleButton);
                tY += Gui.Height + Gui.ModuleOffset;
            }
        }
    }

    public float animationValue = 0;

    public ArrayList<Frame> getComponents() {
        return components;
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public void renderFrame(int mouseX, int mouseY) {
        ColorUtils.update();
        RenderUtils2D.drawGradientRect(this.x + 4, this.y, this.x + width, this.y + height, ClickGui.Instance.headButtonColor.hashCode(),  ClickGui.Instance.headButtonColor2.hashCode(), false);
        if (ClickGui.Instance.customFont.getValue()) {
            Moon.FontManager.drawString(category.getName().toUpperCase(), this.x + ((this.width - Moon.getFontManager().getStringWidth(category.getName().toUpperCase())) / 2f), this.y - ((this.height - Moon.getFontManager().getHeight()) / 2f), ClickGui.Instance.fontColor.getValue());
        } else {
            mc.fontRenderer.drawStringWithShadow(category.getName().toUpperCase(), this.x + ((this.width - mc.fontRenderer.getStringWidth(category.getName().toUpperCase())) / 2f), this.y - ((this.height - mc.fontRenderer.FONT_HEIGHT) / 2f), ClickGui.Instance.fontColor.getValue().hashCode());
        }
        if (this.isOpen) {
            if (!this.components.isEmpty()) {
                int x = 0;
                for (Frame component : components) {
                    component.render(mouseX, mouseY);
                    x++;
                    if (component instanceof ModuleFrame) {
                        if (((ModuleFrame) component).isOpen) {
                            x += ((ModuleFrame) component).subCompLength;
                        }
                    }
                }
                x *= Gui.Height + Gui.ModuleOffset;
            }
        }
    }

    public void refresh() {
        int off = this.height;
        for (Frame comp : components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public int getY() {
        return y;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public int getWidth() {
        return width;
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - dragX);
            this.setY(mouseY - dragY);
        }
    }

    public boolean isWithinHeader(int x, int y) {
        if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
            return true;
        }
        return false;
    }
}