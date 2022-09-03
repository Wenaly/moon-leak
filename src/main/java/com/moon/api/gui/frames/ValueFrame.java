package com.moon.api.gui.frames;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.moon.Moon;
import com.moon.api.gui.Frame;
import com.moon.api.gui.Gui;
import com.moon.api.gui.ModuleFrame;
import com.moon.api.setting.settings.DoubleSetting;
import com.moon.api.setting.settings.FloatSetting;
import com.moon.api.setting.settings.IntegerSetting;
import com.moon.api.utils.math.MathUtils;
import com.moon.api.utils.render.RenderUtils2D;
import com.moon.impl.modules.client.ClickGui;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ValueFrame extends Frame {
    private boolean hovered;

    private final ModuleFrame parent;
    private DoubleSetting setD = null;
    private IntegerSetting setI = null;
    private FloatSetting setF = null;
    private int offset;
    private int x;
    private int y;
    private boolean dragging = false;

    private double renderWidth;

    public ValueFrame(DoubleSetting value, ModuleFrame button, int offset) {
        this.setD = value;
        this.parent = button;
        this.offset = offset;

        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        setVisible(true);
    }

    public ValueFrame(FloatSetting value, ModuleFrame button, int offset) {
        this.setF = value;
        this.parent = button;
        this.offset = offset;

        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        setVisible(true);
    }

    public ValueFrame(IntegerSetting value, ModuleFrame button, int offset) {
        this.setI = value;
        this.parent = button;
        this.offset = offset;

        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        setVisible(true);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (!isVisible()) return;
        net.minecraft.client.gui.Gui.drawRect(parent.parent.getX() + Gui.SettingOffset, parent.parent.getY() + offset + Gui.ModuleOffset, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset, this.hovered ? Gui.GuiHoveredColor() : Gui.GuiColor());
        RenderUtils2D.drawGradientRect(parent.parent.getX() + Gui.SettingOffset, parent.parent.getY() + offset + Gui.ModuleOffset, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset,
                (ClickGui.Instance.buttonColor.getValue().hashCode()),
                (ClickGui.Instance.buttonColor.getValue().hashCode()), hovered);
        if (ClickGui.Instance.customFont.getValue()) {
            Moon.FontManager.drawString(setI != null ? this.setI.getName() + ": " + ChatFormatting.GRAY + this.setI.getValue() : setF != null ? this.setF.getName() + ": " + ChatFormatting.GRAY + this.setF.getValue() : this.setD.getName() + ": " + ChatFormatting.GRAY + this.setD.getValue(), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue());
        } else {
            mc.fontRenderer.drawStringWithShadow(setI != null ? this.setI.getName() + ": " + ChatFormatting.GRAY + this.setI.getValue() : setF != null ? this.setF.getName() + ": " + ChatFormatting.GRAY + this.setF.getValue() : this.setD.getName() + ": " + ChatFormatting.GRAY + this.setD.getValue(), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue().hashCode());
        }
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        boolean old = isVisible();
        if (setD != null) {
            setVisible(setD.isVisible());
        }
        if (setF != null) {
            setVisible(setF.isVisible());
        }
        if (setI != null) {
            setVisible(setI.isVisible());
        }
        if(old != isVisible()){
            this.parent.init(parent.mod, parent.parent, parent.offset, true);
        }
        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();

        int widthTest = Gui.Width - (Gui.SettingOffset * 2);
        double diff = Math.min(widthTest, Math.max(0, mouseX - this.x));
        if (setI != null) {
            int min = setI.getMin();
            int max = setI.getMax();

            renderWidth = (widthTest) * (float)(setI.getValue() - min) / (max - min) + Gui.SettingOffset;

            if (dragging) {
                if (diff == 0) {
                    setI.setValue(setI.getMin());
                } else {
                    int newValue = (int) roundToPlace(((diff / widthTest) * (max - min) + min), 2);
                    setI.setValue(newValue);
                }
            }
        }
        if (setD != null) {

            double min = setD.getMin();
            double max = setD.getMax();

            renderWidth = (widthTest) * (setD.getValue() - min) / (max - min) + Gui.SettingOffset;

            if (dragging) {
                if (diff == 0) {
                    setD.setValue(setD.getMin());
                } else {
                    double newValue = roundToPlace(((diff / widthTest) * (max - min) + min), 2);
                    setD.setValue(newValue);
                }
            }
        }
        if (setF != null) {
            float min = setF.getMin();
            float max = setF.getMax();

            renderWidth = (widthTest) * (setF.getValue() - min) / (max - min) + Gui.SettingOffset;

            if (dragging) {
                if (diff == 0) {
                    setF.setValue(setF.getMin());
                } else {
                    float newValue = (float) roundToPlace(((diff / widthTest) * (max - min) + min), 2);
                    setF.setValue(newValue);
                }
            }
        }
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(!isVisible())return;
        if (isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
            dragging = true;
        }
        if (isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        dragging = false;
    }


    private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }



    public boolean isMouseOnButtonD(int x, int y) {
        return x > this.x + Gui.SettingOffset && x < this.x + (parent.parent.getWidth() / 2 + 1) - Gui.SettingOffset && y > this.y && y < this.y + Gui.Height;
    }

    public boolean isMouseOnButtonI(int x, int y) {
        return x > this.x + parent.parent.getWidth() / 2 + Gui.SettingOffset && x < this.x + parent.parent.getWidth() - Gui.SettingOffset && y > this.y && y < this.y + Gui.Height;
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