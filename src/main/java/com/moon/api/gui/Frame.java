package com.moon.api.gui;

import com.moon.api.utils.IMinecraft;

public abstract class Frame implements IMinecraft {
    private boolean visible;

    public void render(int mouseX, int mouseY) {
    }


    public void update(int mouseX, int mouseY) {
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public void keyTyped(char typedChar, int key) {
    }

    public void setOff(int newOff) {
    }

    public int getHeight() {
        return 0;
    }

    public ModuleFrame getParent() {
        return null;
    }

    public int getOffset() {
        return 0;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean setVisible(boolean visible) {
        this.visible = visible;
        return this.visible;
    }

}