package com.moon.api.gui;


import com.moon.Moon;
import com.moon.api.gui.frames.CategoryFrame;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.*;
import com.moon.api.gui.frames.*;
import com.moon.api.setting.settings.BooleanSetting;
import com.moon.api.utils.render.RenderUtils2D;
import com.moon.impl.modules.client.ClickGui;

import java.util.ArrayList;

public class ModuleFrame extends Frame {

    public Module mod;
    public CategoryFrame parent;
    private ArrayList<Frame> subcomponents;
    private ArrayList<Setting> notInitSettings;
    private ArrayList<Boolean> oldVals;
    public boolean isOpen;
    private boolean isHovered;
    public int offset;
    public int subCompLength = 0;
    public int opY;

    public ArrayList<Frame> getChildren() {
        ArrayList<Frame> children = new ArrayList<>();
        for (Frame component : this.subcomponents) {
            if (component.getParent() == this) {
                children.add(component);
            }
        }
        return children;
    }


    public void init(Module mod, CategoryFrame parent, int offset, boolean update) {
        if (!update) {
            this.mod = mod;
            this.parent = parent;
            this.offset = offset;
        }

        this.subcomponents = new ArrayList<>();
        this.notInitSettings = new ArrayList<>();
        this.oldVals = new ArrayList<>();
        if (!update) {
            this.isOpen = false;
        }
        opY = offset + Gui.Instance.Height + Gui.Instance.ModuleOffset;
        if (Moon.Settings.getSettingFromModule(mod) != null) {
            for (Setting s : Moon.Settings.getSettingFromModule(mod)) {
                if (s instanceof BooleanSetting) {
                    if (!((BooleanSetting) s).isVisible()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new BooleanFrame((BooleanSetting) s, this, opY));
                    opY +=Gui.Instance.Height + Gui.Instance.ModuleOffset;
                } else if (s instanceof EnumSetting) {
                    if (!((EnumSetting) s).isVisible()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new EnumFrame((EnumSetting) s, this, mod, opY));
                    opY += Gui.Instance.Height + Gui.Instance.ModuleOffset;
                } else if (s instanceof IntegerSetting) {
                    if (!((IntegerSetting) s).isVisible()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new ValueFrame((IntegerSetting) s, this, opY));
                    opY += Gui.Instance.Height + Gui.Instance.ModuleOffset;
                } else if (s instanceof FloatSetting) {
                    if (!((FloatSetting) s).isVisible()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new ValueFrame((FloatSetting) s, this, opY));
                    opY += Gui.Instance.Height + Gui.Instance.ModuleOffset;
                } else if (s instanceof DoubleSetting) {
                    if (!((DoubleSetting) s).isVisible()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new ValueFrame((DoubleSetting) s, this, opY));
                    opY += Gui.Instance.Height + Gui.Instance.ModuleOffset;
                } else if (s instanceof ColorSetting) {
                    if (!((ColorSetting) s).isVisible()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new ColorFrame((ColorSetting) s, this, opY));
                    opY += Gui.Instance.Height + Gui.Instance.ModuleOffset;
                }
            }
            this.subcomponents.add(new BindFrame(this, opY));
            opY += Gui.Instance.Height + Gui.Instance.ModuleOffset;
            this.subcomponents.add(new VisibleFrame(this, opY));
            if(update){
                parent.refresh();
            }
        }
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
        int opY = offset + Gui.Instance.Height + Gui.Instance.ModuleOffset;
        for (Frame comp : this.subcomponents) {
            comp.setOff(opY);
            opY += Gui.Instance.Height + Gui.Instance.ModuleOffset;
        }
    }


    @Override
    public void render(int MouseX, int MouseY) {
        subCompLength = 0;
        if (mod.isEnabled()) {
            RenderUtils2D.drawGradientRect(parent.getX() + 4, this.parent.getY() + this.offset + Gui.ModuleOffset,
                    parent.getX() + parent.getWidth(), this.parent.getY() + Gui.Instance.Height + this.offset + Gui.ModuleOffset,
                    (ClickGui.Instance.buttonColor.getValue().hashCode()),
                    (ClickGui.Instance.buttonColor.getValue().hashCode()), isHovered);
        } else {
            RenderUtils2D.drawRectMC(parent.getX() + 4, this.parent.getY() + this.offset + Gui.ModuleOffset, parent.getX() + parent.getWidth(), this.parent.getY() + Gui.Height + this.offset + Gui.ModuleOffset, this.isHovered ? Gui.GuiHoveredColor() : Gui.GuiModuleColor());
        }
        if (ClickGui.Instance.customFont.getValue()) {
            Moon.FontManager.drawString(this.mod.getName(), parent.getX() + 5, parent.getY() + this.offset + ((Gui.Height - Moon.getFontManager().getHeight() / 2f)), ClickGui.Instance.fontColor.getValue());
        } else {
            mc.fontRenderer.drawStringWithShadow(this.mod.getName(), parent.getX() + 5, parent.getY() + ((Gui.Height - mc.fontRenderer.FONT_HEIGHT / 2f)), ClickGui.Instance.fontColor.getValue().hashCode());
        }
        if (this.isOpen) {
            if (!this.subcomponents.isEmpty()) {
                for (Frame comp : this.subcomponents) {
                    if (!comp.isVisible()) continue;
                    comp.render(MouseX, MouseY);
                    if (comp instanceof ColorFrame) {
                        if (((ColorFrame) comp).isOpen()) {
                            subCompLength += 6;
                        } else {
                            subCompLength++;
                        }
                    } else {
                        subCompLength++;
                    }
                }
            }
        }
        //renderArrow();
    }

    @Override
    public int getHeight() {
        if (this.isOpen) {
            int val = 0;
            for (Frame c : subcomponents) {
                if (!c.isVisible()) {
                    val -= 1;
                }
                if (c instanceof ColorFrame) {
                    if (((ColorFrame) c).isOpen()) {
                        val += 5;
                    }
                }
            }
            return ((Gui.Instance.Height + Gui.Instance.ModuleOffset) * (this.subcomponents.size() + 1 + val));
        }
        return Gui.Instance.Height + Gui.Instance.ModuleOffset;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        this.isHovered = isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (Frame comp : this.subcomponents) {
                comp.update(mouseX, mouseY);
            }
        }
        if (!this.notInitSettings.isEmpty()) {
            if (oldVals.isEmpty()) {
                for (Setting s : this.notInitSettings) {
                    if (s instanceof BooleanSetting) {
                        this.oldVals.add(((BooleanSetting) s).isVisible());
                    } else if (s instanceof EnumSetting) {
                        this.oldVals.add(((EnumSetting) s).isVisible());
                    } else if (s instanceof IntegerSetting) {
                        this.oldVals.add(((IntegerSetting) s).isVisible());
                    } else if (s instanceof DoubleSetting) {
                        this.oldVals.add(((DoubleSetting) s).isVisible());
                    } else if (s instanceof ColorSetting) {
                        this.oldVals.add(((ColorSetting) s).isVisible());
                    }
                }
            } else {
                int index = 0;
                for (Setting s : this.notInitSettings) {
                    boolean old = oldVals.get(index);
                    boolean init = false;
                    if (s instanceof BooleanSetting) {
                        if (((BooleanSetting) s).isVisible() != old) {
                            init = true;
                        }
                    } else if (s instanceof EnumSetting) {
                        if (((EnumSetting) s).isVisible() != old) {
                            init = true;
                        }
                    } else if (s instanceof IntegerSetting) {
                        if (((IntegerSetting) s).isVisible() != old) {
                            init = true;
                        }
                    } else if (s instanceof DoubleSetting) {
                        if (((DoubleSetting) s).isVisible() != old) {
                            init = true;
                        }
                    } else if (s instanceof ColorSetting) {
                        if (((ColorSetting) s).isVisible() != old) {
                            init = true;
                        }
                    }
                    index++;
                    if (init) {
                        oldVals.clear();
                        this.init(mod, parent, offset, true);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.isOpen = !this.isOpen;
            this.parent.refresh();
            for (Frame comp : parent.getComponents()) {
                if (comp instanceof ModuleFrame) {
                    if (((ModuleFrame) comp).isOpen) {
                        if (((ModuleFrame) comp).isOpen) {
                            for (Frame comp2 : ((ModuleFrame) comp).getChildren()) {
                                if (comp2 instanceof ColorFrame) {
                                    ((ColorFrame) comp2).setOpen(false);
                                    this.parent.refresh();
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Frame comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Frame comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        for (Frame comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }


    public boolean isMouseOnButton(int x, int y) {
        if (x > parent.getX() + Gui.ModuleWidht && x < parent.getX() + parent.getWidth() - Gui.ModuleWidht && y > this.parent.getY() + this.offset && y < this.parent.getY() + Gui.Height + Gui.ModuleOffset + this.offset) {
            return true;
        }
        return false;
    }

}
