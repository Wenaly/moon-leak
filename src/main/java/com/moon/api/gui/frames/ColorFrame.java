package com.moon.api.gui.frames;

import com.moon.Moon;
import com.moon.api.gui.Frame;
import com.moon.api.gui.Gui;
import com.moon.api.gui.ModuleFrame;
import com.moon.api.setting.settings.ColorSetting;
import com.moon.api.utils.render.RenderUtils2D;
import com.moon.impl.modules.client.ClickGui;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ColorFrame extends Frame {
    private final ColorSetting set;
    private final ModuleFrame parent;
    private Color finalColor;
    private int offset;
    private boolean isOpen;
    private boolean firstTimeOpen;
    private final int booleanButtonOffset = 80;
    boolean pickingColor = false;
    boolean pickingHue = false;
    boolean pickingAlpha = false;

    public ColorFrame(ColorSetting value, ModuleFrame button, int offset) {
        this.set = value;
        this.parent = button;
        this.offset = offset;
        this.isOpen = false;
        this.firstTimeOpen = true;
        setVisible(true);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RenderUtils2D.drawRectMC(parent.parent.getX() + Gui.SettingOffset, parent.parent.getY() + offset + Gui.ModuleOffset, parent.parent.getX() + Gui.SettingOffset + 95, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset, ClickGui.Instance.buttonColor.getValue().hashCode());
        RenderUtils2D.drawRectMC(parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset - 5, parent.parent.getY() + offset + Gui.ModuleOffset, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset,ClickGui.Instance.buttonColor.getValue().hashCode());
        RenderUtils2D.drawRectMC(parent.parent.getX() + Gui.SettingOffset + 95, parent.parent.getY() + offset + Gui.ModuleOffset, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset - 5, parent.parent.getY() + offset + Gui.ModuleOffset + 3, ClickGui.Instance.buttonColor.getValue().hashCode());
        RenderUtils2D.drawRectMC(parent.parent.getX() + Gui.SettingOffset + 95, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset - 5, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset - 3, ClickGui.Instance.buttonColor.getValue().hashCode());
        RenderUtils2D.drawBorderedRect(parent.parent.getX() + Gui.SettingOffset + 95, parent.parent.getY() + offset + Gui.ModuleOffset + 3, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset - 5, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset - 3, 1, this.set.getValue().hashCode(), Gui.GuiColor(), false);
        if (ClickGui.Instance.customFont.getValue()) {
            Moon.FontManager.drawString(set.getName(), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue());
        } else {
            mc.fontRenderer.drawStringWithShadow(set.getName(), parent.parent.getX(), parent.parent.getY() + offset + 3 + Gui.ModuleOffset, ClickGui.Instance.fontColor.getValue().hashCode());
        }
        if (this.isOpen) {
            Gui.drawRect(parent.parent.getX() + Gui.SettingOffset, parent.parent.getY() + offset + Gui.ModuleOffset + Gui.Height, parent.parent.getX() + parent.parent.getWidth() - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset + booleanButtonOffset, Gui.GuiColor());
            this.drawPicker(set, parent.parent.getX() + 7, parent.parent.getY() + offset + 19, parent.parent.getX() + 100, parent.parent.getY() + offset + 19, parent.parent.getX() + 7, parent.parent.getY() + offset + 72, mouseX, mouseY);
            set.setValue(finalColor);
            RenderUtils2D.drawBorderedRect(parent.parent.getX() + Gui.SettingOffset + 85, parent.parent.getY() + offset + 4 + Gui.ModuleOffset + booleanButtonOffset, parent.parent.getX() + 115 - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset - 2  + booleanButtonOffset, 1,this.set.getRainbow() ? new Color(set.getValue().getRed(), set.getValue().getGreen(), set.getValue().getBlue(), 255).hashCode() : Gui.GuiColor(), new Color(0, 0, 0, 200).hashCode(), false);
            RenderUtils2D.drawRectMC(parent.parent.getX() + Gui.SettingOffset + (this.set.getRainbow() ? 95 : 88), parent.parent.getY() + offset + 6 + Gui.ModuleOffset + booleanButtonOffset, parent.parent.getX() + (this.set.getRainbow() ? 112 : 105) - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset - 4 + booleanButtonOffset, new Color(50, 50, 50, 255).hashCode());
            if (ClickGui.Instance.customFont.getValue()) {
                Moon.FontManager.drawString("Rainbow", parent.parent.getX(), parent.parent.getY() + offset + 5  + Gui.ModuleOffset + booleanButtonOffset , ClickGui.Instance.fontColor.getValue());
            } else {
                mc.fontRenderer.drawStringWithShadow("Rainbow", parent.parent.getX(), parent.parent.getY() + offset + 5  + Gui.ModuleOffset + booleanButtonOffset , ClickGui.Instance.fontColor.getValue().hashCode());
            }
        }
    }


    @Override
    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(!isVisible())return;
        if (isMouseOnButton(mouseX, mouseY) && parent.isOpen && button == 1) {
            for (Frame comp : parent.parent.getComponents()) {
                if (comp instanceof ModuleFrame) {
                    if (((ModuleFrame) comp).isOpen) {
                        for (Frame comp2 : ((ModuleFrame) comp).getChildren()) {
                            if (comp2 instanceof ColorFrame) {
                                if (((ColorFrame) comp2).isOpen && comp2 != this) {
                                    ((ColorFrame) comp2).setOpen(false);
                                    this.parent.parent.refresh();
                                }
                            }
                        }
                    }
                }
            }
            setOpen(!isOpen);
            this.parent.parent.refresh();
        }
        if (!this.isOpen && !this.firstTimeOpen) {
            this.firstTimeOpen = true;
        }
        if (this.isOpen && firstTimeOpen) {
            boolean flag = false;
            for (Frame component : this.parent.getChildren()) {
                if (!flag && component == this) {
                    flag = true;
                    continue;
                }
                if (flag) {
                    component.setOff(component.getOffset() + (Gui.Height + Gui.ModuleOffset) * 5);
                }
            }
            this.firstTimeOpen = false;
        }
        if (this.isOpen && mouseOver(parent.parent.getX() + Gui.SettingOffset + 85, parent.parent.getY() + offset + 4 + Gui.ModuleOffset + booleanButtonOffset, parent.parent.getX() + 115 - Gui.SettingOffset, parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset - 2  + booleanButtonOffset, mouseX, mouseY)) {
            this.set.setRainbow(!this.set.getRainbow());
        }
    }

    public void drawPicker(ColorSetting subColor, int pickerX, int pickerY, int hueSliderX, int hueSliderY, int alphaSliderX, int alphaSliderY, int mouseX, int mouseY) {
        float[] color = new float[] {
                0, 0, 0, 0
        };

        try {
            color = new float[] {
                    Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[0], Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[1], Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[2], subColor.getColor().getAlpha() / 255f
            };
        } catch (Exception ignored) {

        }



        int pickerWidth = 90;
        int pickerHeight = 51;
        int hueSliderWidth = 10;
        int hueSliderHeight = 59;
        int alphaSliderHeight = 10;
        int alphaSliderWidth = 90;
        if (!pickingColor && !pickingHue && !pickingAlpha) {
            if (Mouse.isButtonDown(0) && mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY)) {
                pickingColor = true;
            } else if (Mouse.isButtonDown(0) && mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight, mouseX, mouseY)) {
                pickingHue = true;
            } else if (Mouse.isButtonDown(0) && mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + alphaSliderWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY))
                pickingAlpha = true;
        }

        if (pickingHue) {
            float restrictedY = (float) Math.min(Math.max(hueSliderY, mouseY), hueSliderY + hueSliderHeight);
            color[0] = (restrictedY - (float) hueSliderY) / hueSliderHeight;
            color[0] = (float) Math.min(0.97, color[0]);
        }

        if (pickingAlpha) {
            float restrictedX = (float) Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + pickerWidth);
            color[3] = 1 - (restrictedX - (float) alphaSliderX) / pickerWidth;
        }

        if (pickingColor) {
            float restrictedX = (float) Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
            float restrictedY = (float) Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);
            color[1] = (restrictedX - (float) pickerX) / pickerWidth;
            color[2] = 1 - (restrictedY - (float) pickerY) / pickerHeight;
            color[2] = (float) Math.max(0.04000002, color[2]);
            color[1] = (float) Math.max(0.022222223, color[1]);
        }

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        RenderUtils2D.drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, 255);

        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);

        RenderUtils2D.drawRectMC(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, -1);

        finalColor = alphaIntegrate(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]);

        drawAlphaSlider(alphaSliderX, alphaSliderY, pickerWidth, alphaSliderHeight, finalColor.getRed() / 255f, finalColor.getGreen() / 255f, finalColor.getBlue() / 255f, color[3]);
    }

    public static Color alphaIntegrate(Color color, float alpha) {
        float red = (float) color.getRed() / 255;
        float green = (float) color.getGreen() / 255;
        float blue = (float) color.getBlue() / 255;
        return new Color(red, green, blue, alpha);
    }

    public void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;
        if (height > width) {
            RenderUtils2D.drawRectMC(x, y, x + width, y + 4, 0xFFFF0000);
            y += 4;

            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                RenderUtils2D.drawGradientRect(x, y + step * (height / 6f), x + width, y + (step + 1) * (height / 6f), previousStep, nextStep, false);
                step++;
            }
            int sliderMinY = (int) (y + height*hue) - 4;
            RenderUtils2D.drawRectMC(x, sliderMinY - 1, x + width, sliderMinY + 1,-1);
        } else {
            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                RenderUtils2D.gradient(x + step * (width / 6), y, x + (step + 1) * (width / 6), y + height, previousStep, nextStep, true);
                step++;
            }

            int sliderMinX = (int) (x + (width * hue));
            RenderUtils2D.drawRectMC(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        }
    }

    public void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {
        boolean left = true;
        int checkerBoardSquareSize = height / 2;

        for (int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                RenderUtils2D.drawRectMC(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, 0xFFFFFFFF);
                RenderUtils2D.drawRectMC(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, 0xFF909090);

                if (squareIndex < width - checkerBoardSquareSize) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    RenderUtils2D.drawRectMC(minX, y, maxX, y + height, 0xFF909090);
                    RenderUtils2D.drawRectMC(minX, y + checkerBoardSquareSize, maxX, y + height,0xFFFFFFFF);
                }
            }

            left = !left;
        }

        RenderUtils2D.drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1).getRGB(), 0);
        int sliderMinX = (int) (x + width - (width * alpha));
        RenderUtils2D.drawRectMC(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        pickingColor = false;
        pickingHue = false;
        pickingAlpha = false;
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX() + Gui.SettingOffset && x < this.parent.parent.getX() + Gui.Width - Gui.SettingOffset && y > this.parent.parent.getY() + offset +Gui.ModuleOffset && y < this.parent.parent.getY() + offset + Gui.Height + Gui.ModuleOffset;
    }

    public void setOpen(boolean v) {
        this.isOpen = v;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public ModuleFrame getParent() {
        return parent;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        boolean old = isVisible();
        setVisible(this.set.isVisible());
        if(old != isVisible()){
            this.parent.init(parent.mod, parent.parent, parent.offset, true);
        }
    }

    public static boolean mouseOver(int minX, int minY, int maxX, int maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }
}