package com.moon.api.gui;

import com.moon.Moon;
import com.moon.api.gui.frames.CategoryFrame;
import com.moon.api.module.Module;
import com.moon.api.utils.render.ColorUtils;
import com.moon.impl.modules.client.ClickGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class Gui extends GuiScreen {

    /** @author DriftyDev, ijese shit
    * TODO: Finish gui.
    */

    public static Gui Instance = new Gui();

    public static int Height = ClickGui.Instance.height.getValue();
    public static int Width = ClickGui.Instance.width.getValue();

    public static int ModuleOffset = 0;
    public static int SettingOffset = 5;

    public static int ModuleWidht = 5;

    public boolean shouldShow;

    public static int GuiColor() {
        return new ColorUtils(1, 1, 1, 255).hashCode();
    }

    public static int GuiHoveredColor() {
        return new ColorUtils(1, 1, 1, 255).hashCode();
    }

    public static int GuiModuleColor() {
        return new ColorUtils(1, 1, 1, 255).hashCode();
    }

    private boolean flag = false;
    public static ArrayList<CategoryFrame> categoryComponents;

    public Gui() {
        categoryComponents = new ArrayList<>();
        int x = 10;
        for (Module.Category category : Moon.Modules.getCategories()) {
            CategoryFrame categoryComponent = new CategoryFrame(category);
            categoryComponent.setX(x);
            categoryComponents.add(categoryComponent);
            x += categoryComponent.getWidth() + 10;
            flag = false;
        }
    }

    @Override
    public void initGui() {
        Moon.getEventProcessor().addEventListener(this);
        shouldShow = false;
        flag = false;
        for (CategoryFrame c : categoryComponents){
            c.animationValue = 0;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Height = ClickGui.Instance.height.getValue();
        Width = ClickGui.Instance.width.getValue();
        scrollWheelCheck();
        ScaledResolution sr = new ScaledResolution(mc);
        for(CategoryFrame categoryComponent : categoryComponents){
            categoryComponent.renderFrame(mouseX, mouseY);
            categoryComponent.updatePosition(mouseX, mouseY);
            for (Frame comp : categoryComponent.getComponents()) {
                comp.update(mouseX, mouseY);
            }
        }

        for (CategoryFrame categoryComponent : categoryComponents) {
            for (Frame component : categoryComponent.getComponents()) {
                //component.renderToolTip(mouseX, mouseY);
            }
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (CategoryFrame categoryComponent : categoryComponents) {
            if (categoryComponent.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                categoryComponent.setDrag(true);
                categoryComponent.dragX = mouseX - categoryComponent.getX();
                categoryComponent.dragY = mouseY - categoryComponent.getY();
            }
            if (categoryComponent.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                categoryComponent.setOpen(!categoryComponent.isOpen());
            }
            if (categoryComponent.isOpen()) {
                if (!categoryComponent.getComponents().isEmpty()) {
                    for (Frame component : categoryComponent.getComponents()) {
                        component.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        for (CategoryFrame categoryComponent : categoryComponents) {
            if (categoryComponent.isOpen() && keyCode != 1) {
                if (!categoryComponent.getComponents().isEmpty()) {
                    for (Frame component : categoryComponent.getComponents()) {
                        component.keyTyped(typedChar, keyCode);
                    }
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (CategoryFrame categoryComponent : categoryComponents) {
            categoryComponent.setDrag(false);
        }
        for (CategoryFrame categoryComponent : categoryComponents) {
            if (categoryComponent.isOpen()) {
                if (!categoryComponent.getComponents().isEmpty()) {
                    for (Frame component : categoryComponent.getComponents()) {
                        component.mouseReleased(mouseX, mouseY, state);
                    }
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        Moon.ConfigurationManager.save();
        Moon.getEventProcessor().removeEventListener(this);
    }

    private void scrollWheelCheck() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0){
            for(CategoryFrame categoryComponent : categoryComponents){
                categoryComponent.setY(categoryComponent.getY() - 15);
            }
        } else if (dWheel > 0){
            for(CategoryFrame categoryComponent : categoryComponents){
                categoryComponent.setY(categoryComponent.getY() + 15);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static ArrayList<CategoryFrame> getCategories() {
        return categoryComponents;
    }

}