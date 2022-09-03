package com.moon.api.gui.frames;

import com.moon.Moon;
import com.moon.api.gui.Frame;
import com.moon.api.gui.Gui;
import com.moon.api.gui.ModuleFrame;
import com.moon.api.setting.settings.*;
import com.moon.api.utils.render.RenderUtils2D;
import com.moon.impl.modules.client.ClickGui;

import java.awt.*;

public class BooleanFrame extends Frame {
    private ColorFrame p;
    private boolean hovered;
    private BooleanSetting option;
    private final ModuleFrame parent;
    private ColorSetting coption;
    private int offset;
    private int x;
    private int y;

    public BooleanFrame(BooleanSetting option, ModuleFrame button, int offset) {
        this.option = option;
        this.coption = null;
        this.parent = button;
        this.offset = offset;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        setVisible(true);
    }


    @Override
    public void render(int mouseX, int mouseY) {
        if(!isVisible())return;
        Gui.drawRect(parent.parent.getX() + Gui.SettingOffset, parent.parent.getY() + offset + Gui.ModuleOffset, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset, this.hovered ? Gui.GuiHoveredColor() : Gui.GuiColor());
        RenderUtils2D.drawBorderedRect(parent.parent.getX() + Gui.SettingOffset + 85, parent.parent.getY() + offset + 3 + Gui.ModuleOffset, parent.parent.getX() + 115 - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset - 3, 1, this.coption == null ? !this.option.getValue() ? Gui.GuiColor() : ClickGui.Instance.buttonColor.getValue().hashCode() : coption.getRainbow() ? new Color(coption.getValue().getRed(), coption.getValue().getGreen(), coption.getValue().getBlue(), 255).hashCode() : Gui.GuiColor(), new Color(0, 0, 0, 200).hashCode(), this.hovered);
        RenderUtils2D.drawRectMC(parent.parent.getX() + Gui.SettingOffset + (this.coption == null ? this.option.getValue() ? 95 : 88 : this.coption.getRainbow() ? 95 : 88), parent.parent.getY() + offset + 5 + Gui.ModuleOffset, parent.parent.getX() + (this.coption == null ? this.option.getValue() ? 112 : 105 : this.coption.getRainbow() ? 112 : 105) - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset - 5, new Color(50, 50, 50, 255).hashCode());
        if (ClickGui.Instance.customFont.getValue()) {
            Moon.FontManager.drawString(this.coption == null ? this.option.getName() : "Rainbow", parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue());
        } else {
            mc.fontRenderer.drawStringWithShadow(this.coption == null ? this.option.getName() : "Rainbow", parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue().hashCode());
        }
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();
        boolean old = isVisible();
        setVisible(this.option.isVisible());
        if(old != isVisible()){
            this.parent.init(parent.mod, parent.parent, parent.offset, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(!isVisible())return;
        if (coption == null) {
            if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
                this.option.setValue(!option.getValue());
            }
        } else {
            if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen && p.isOpen()) {
                this.coption.setRainbow(!coption.getRainbow());
            }
        }

    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX() + Gui.SettingOffset && x < this.parent.parent.getX() + Gui.Width - Gui.SettingOffset && y > this.parent.parent.getY() + offset + Gui.ModuleOffset && y < this.parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset;
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