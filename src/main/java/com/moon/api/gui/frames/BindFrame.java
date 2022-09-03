package com.moon.api.gui.frames;

import com.moon.Moon;
import com.moon.api.gui.Frame;
import com.moon.api.gui.Gui;
import com.moon.api.gui.ModuleFrame;
import com.moon.api.module.Module;
import com.moon.api.setting.settings.BindSetting;
import com.moon.api.utils.render.RenderUtils2D;
import com.moon.impl.modules.client.ClickGui;
import org.lwjgl.input.Keyboard;

public class BindFrame extends Frame {
    private String name;
    private boolean isHovered;
    private boolean isBinding;
    private ModuleFrame parent;
    private BindSetting setting;
    private int offset;
    private int x;
    private int y;
    private boolean normal;

    private Module module;

    public BindFrame(ModuleFrame button, int offset) {
        this.parent = button;
        this.name = "Bind";
        this.offset = offset;
        this.normal = true;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        setVisible(true);
    }

    public BindFrame(BindSetting setting, ModuleFrame button, int offset) {
        this.parent = button;
        this.setting = setting;
        this.name = setting.getName();
        this.offset = offset;
        this.normal = false;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        setVisible(true);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if(!isVisible())return;
        if (normal) {
            module = this.parent.mod;
            RenderUtils2D.drawRectMC(parent.parent.getX() + Gui.SettingOffset, parent.parent.getY() + offset + Gui.ModuleOffset, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset, this.isHovered ? Gui.GuiHoveredColor() : Gui.GuiColor());
            if (module.getBind() == -1) {
                if (ClickGui.Instance.customFont.getValue()) {
                    Moon.FontManager.drawString(isBinding ? "Listening..." : (name + " - " + "NONE"), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue());
                } else {
                    mc.fontRenderer.drawStringWithShadow(isBinding ? "Listening..." : (name + " - " + "NONE"), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue().hashCode());
                }
            } else {
                if (ClickGui.Instance.customFont.getValue()) {
                    Moon.FontManager.drawString(isBinding ? "Listening..." : (name + " - " + getRenderKey()), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue());
                } else {
                    mc.fontRenderer.drawStringWithShadow(isBinding ? "Listening..." : (name + " - " + getRenderKey()), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue().hashCode());
                }
            }

        } else {
            RenderUtils2D.drawRectMC(parent.parent.getX() + Gui.SettingOffset, parent.parent.getY() + offset + Gui.ModuleOffset, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset, this.isHovered ? Gui.GuiHoveredColor() : Gui.GuiColor());
            if (setting.getKey() == -1) {
                if (ClickGui.Instance.customFont.getValue()) {
                    Moon.FontManager.drawString(isBinding ? "Listening..." : (name + " - " + "NONE"), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue());
                } else {
                    mc.fontRenderer.drawStringWithShadow(isBinding ? "Listening..." : (name + " - " + "NONE"), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue().hashCode());
                }
            } else {
                if (ClickGui.Instance.customFont.getValue()) {
                    Moon.FontManager.drawString(isBinding ? "Listening..." : (name + " - " + getRenderKey()), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue());
                } else {
                    mc.fontRenderer.drawStringWithShadow(isBinding ? "Listening..." : (name + " - " + getRenderKey()), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue().hashCode());
                }
            }
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
        boolean old = isVisible();
        //setVisible(this.setting.isVisible());
        if(old != isVisible()){
            this.parent.init(parent.mod, parent.parent, parent.offset, true);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX() + Gui.SettingOffset && x < this.parent.parent.getX() + Gui.Width - Gui.SettingOffset && y > this.parent.parent.getY() + offset + Gui.ModuleOffset && y < this.parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(!isVisible())return;
        if (this.isBinding) {
            this.isBinding = false;
            if (normal) {
                switch (button) {
                    case 0:
                        module.setBind(-2);
                        break;
                    case 1:
                        module.setBind(-3);
                        break;
                    case 2:
                        module.setBind(-4);
                        break;
                    case 3:
                        module.setBind(-5);
                        break;
                    case 4:
                        module.setBind(-6);
                        break;

                }
            } else {
                Moon.Logger.info(button);
                switch (button) {
                    case 0:
                        setting.setKey(-2);
                        break;
                    case 1:
                        setting.setKey(-3);
                        break;
                    case 2:
                        setting.setKey(-4);
                        break;
                    case 3:
                        setting.setKey(-5);
                        break;
                    case 4:
                        setting.setKey(-6);
                        break;
                }
            }
            return;
        }
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
            this.isBinding = !this.isBinding;
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if(!isVisible())return;
        if (this.isBinding) {
            if (this.normal) {
                module.setBind(key);
                if (key == Keyboard.KEY_DELETE) {
                    module.setBind(-1);
                }
            } else {
                setting.setKey(key);
                if (key == Keyboard.KEY_DELETE) {
                    setting.setKey(-1);
                }
            }
            this.isBinding = false;
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

    private String getRenderKey() {
        if (normal) {
            if (module == null) return "NONE";
            switch (module.getBind()) {
                case -2:
                    return "M0";
                case -3:
                    return "M1";
                case -4:
                    return "M2";
                case -5:
                    return "M3";
                case -6:
                    return "M4";
                default:
                    return Keyboard.getKeyName(module.getBind());
            }
        } else {
            switch (setting.getKey()) {
                case -2:
                    return "M0";
                case -3:
                    return "M1";
                case -4:
                    return "M2";
                case -5:
                    return "M3";
                case -6:
                    return "M4";
                default:
                    return Keyboard.getKeyName(setting.getKey());
            }
        }
    }
}