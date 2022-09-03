package com.moon.api.gui.frames;

import com.moon.Moon;
import com.moon.api.gui.Frame;
import com.moon.api.gui.Gui;
import com.moon.api.gui.ModuleFrame;
import com.moon.api.module.Module;
import com.moon.api.utils.render.RenderUtils2D;
import com.moon.impl.modules.client.ClickGui;

public class VisibleFrame extends Frame {
    private String name;
    private boolean isHovered;
    private ModuleFrame parent;
    private int offset;
    private int x;
    private int y;

    private Module module;

    public VisibleFrame(ModuleFrame button, int offset) {
        this.parent = button;
        this.name = "Visible";
        this.offset = offset;

        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        setVisible(true);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        module = this.parent.mod;
        RenderUtils2D.drawRectMC(parent.parent.getX() + Gui.SettingOffset, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset, this.isHovered ? Gui.GuiHoveredColor() : Gui.GuiColor());
        if (ClickGui.Instance.customFont.getValue()) {
            Moon.FontManager.drawString("Visible: " + (this.module.getVisible() ? "True" : "False"), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue());
        } else {
            mc.fontRenderer.drawStringWithShadow("Visible: " + (this.module.getVisible() ? "True" : "False"), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue().hashCode());
        }
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        this.isHovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX() + Gui.SettingOffset && x < this.parent.parent.getX() + Gui.Width - Gui.SettingOffset && y > this.parent.parent.getY() + offset + Gui.ModuleOffset && y < this.parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
            this.module.setVisible(!this.module.getVisible());
        }
    }

    @Override
    public ModuleFrame getParent() {
        return parent;
    }

    @Override
    public int getOffset() {
        return offset;
    }
}