package com.moon.api.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.moon.Moon;
import com.moon.api.event.events.Render2DEvent;
import com.moon.api.event.events.Render3DEvent;
import com.moon.api.priorities.Priorities;
import com.moon.api.setting.Setting;
import com.moon.api.utils.IMinecraft;
import com.moon.api.utils.NullUtils;
import com.moon.api.utils.misc.MessageUtils;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class Module implements IMinecraft, NullUtils {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface registration {
        String name();
        String description();
        Category category();
        Priorities priority() default Priorities.Normal;
        boolean isListening() default false;
        int bind() default Keyboard.KEY_NONE;
        boolean enabled() default false;
        boolean visible() default true;
    }

    private registration getModule(){
        return getClass().getAnnotation(registration.class);
    }

    private final String name = getModule().name();
    private final String description = getModule().description();
    private final Category category = getModule().category();
    private final Priorities priority = getModule().priority();
    private int bind = getModule().bind();
    private boolean notification = getModule().visible();
    private boolean isEnabled = getModule().enabled();
    private int isListening = (getModule().isListening() ? 0 : 1);

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onLoad() {
    }

    public void onTick() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public void onUnload() {
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void stopListening() {
        this.isListening = -1;
    }

    public void enable() {
        this.isEnabled = true;
        this.onEnable();
        if (this.isEnabled() && this.isListening()) {
            MinecraftForge.EVENT_BUS.register(this);
            Moon.getEventProcessor().addEventListener(this);
        }
        if (this.notification) MessageUtils.sendNotificationMessage(this, true);
    }

    public void disable() {
        if (this.isListening != 0) {
            MinecraftForge.EVENT_BUS.unregister(this);
            Moon.getEventProcessor().removeEventListener(this);
        }
        this.isEnabled = false;
        this.onDisable();
        if (this.notification) MessageUtils.sendNotificationMessage(this, false);
    }

    public void toggle() {
        if (this.isEnabled()) {
            this.disable();
        } else {
            this.enable();
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled && !this.isEnabled()) {
            this.enable();
        }
        if (!enabled && this.isEnabled()) {
            this.disable();
        }
    }

    public boolean getVisible() {
        return this.notification;
    }

    public void setVisible(boolean visible) {
        this.notification = visible;
    }

    public boolean isListening() {
        return isListening >= -1;
    }


    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDisplayInfo() {
        return null;
    }

    public int getBind() {
        return this.bind;
    }

    public boolean isNotification() {
        return this.notification;
    }

    public String getBindName() {
        return Keyboard.getKeyName(this.bind);
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public Category getCategory() {
        return this.category;
    }

    public List<Setting> getSettings() {
        List<Setting> settings = new ArrayList<>();
        for (Setting setting : Moon.Settings.getSettings()) {
            if (setting.getParent() == this) {
                settings.add(setting);
            }
        }
        return settings;
    }

    public String getArrayString() {
        return this.getDisplayInfo() != null ? ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.GRAY + "]" : "";
    }

    public Setting getSettingByName(String name) {
        for (Setting setting : this.getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }

    public enum Category {
        Combat("Combat"),
        Misc("Misc"),
        Render("Render"),
        Movement("Movement"),
        Player("Player"),
        Client("Client"),
        Hud("Hud");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public Priorities getPriority() {
        return priority;
    }
}