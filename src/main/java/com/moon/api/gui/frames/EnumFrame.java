package com.moon.api.gui.frames;

import com.moon.Moon;
import com.moon.api.gui.Frame;
import com.moon.api.gui.Gui;
import com.moon.api.gui.ModuleFrame;
import com.moon.api.module.Module;
import com.moon.api.setting.settings.EnumSetting;
import com.moon.api.utils.render.RenderUtils2D;
import com.moon.impl.modules.client.ClickGui;

public class EnumFrame extends Frame {

    private boolean hovered;
    private ModuleFrame parent;
    private EnumSetting set;
    private int offset;
    private int x;
    private int y;
    private Module mod;

    private int modeIndex;

    public EnumFrame(EnumSetting set, ModuleFrame button, Module mod, int offset){
        this.set = set;
        this.parent = button;
        this.mod = mod;
        this.offset = offset;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.modeIndex = 0;
        setVisible(true);
    }
    @Override
    public void render(int mouseX, int mouseY) {
        if(!isVisible())return;
        RenderUtils2D.drawRectMC(parent.parent.getX() + Gui.SettingOffset, parent.parent.getY() + offset + Gui.ModuleOffset, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset, this.hovered ? Gui.GuiHoveredColor() : Gui.GuiColor());
        if (ClickGui.Instance.customFont.getValue()) {
            Moon.FontManager.drawString(set.getName() + ": " + set.getValue(), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue());
        } else {
            mc.fontRenderer.drawStringWithShadow(set.getName() + ": " + set.getValue(), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue().hashCode());
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
        setVisible(this.set.isVisible());
        if(old != isVisible()){
            this.parent.init(parent.mod, parent.parent, parent.offset, true);
        }
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(!isVisible())return;
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
            int maxIndex = set.getModes().size();

            if (modeIndex + 1 > maxIndex)
                modeIndex = 0;
            else
                modeIndex++;

            try {
                set.setValue(set.getModes().get(modeIndex));
            } catch (Exception e) {
                modeIndex = 0;
                set.setValue(set.getModes().get(modeIndex));
            }
        }

        if (isMouseOnButton(mouseX, mouseY) && button == 1 && this.parent.isOpen) {
            int maxIndex = set.getModes().size();

            if (modeIndex == 0)
                modeIndex = maxIndex - 1;
            else
                modeIndex--;

            try {
                set.setValue(set.getModes().get(modeIndex));
            } catch (Exception e) {
                modeIndex = 0;
                set.setValue(set.getModes().get(modeIndex));
            }
        }

    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX() + Gui.SettingOffset && x < this.parent.parent.getX() + Gui.Width - Gui.SettingOffset && y > this.parent.parent.getY() + offset + Gui.ModuleOffset && y < this.parent.parent.getY() + offset + Gui.Height +Gui.ModuleOffset;
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