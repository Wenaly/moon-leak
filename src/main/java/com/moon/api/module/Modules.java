package com.moon.api.module;

import com.moon.Moon;
import com.moon.api.event.events.Render2DEvent;
import com.moon.api.event.events.Render3DEvent;
import com.moon.api.gui.Gui;
import com.moon.api.utils.IMinecraft;
import com.moon.api.utils.misc.ReflectionUtils;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Modules implements IMinecraft {

    private final List<Module> Modules = new ArrayList<>();
    private final List<Module> VisibleModules = new ArrayList<>();

    public Modules() {

        try {
            ArrayList<Class<?>> modClasses = ReflectionUtils.getClassesForPackage("com.moon.impl.modules");
            modClasses.spliterator().forEachRemaining(aClass -> {
                if (Module.class.isAssignableFrom(aClass)) {
                    try {
                        Module module = (Module) aClass.getConstructor().newInstance();
                        Modules.add(module);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Modules.sort(Comparator.comparing(Module::getPriority));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public final List<Module> getModules() {
        return this.Modules;
    }

    public final List<Module> getVisibleModules() {
        return this.VisibleModules;
    }

    public final boolean isVisible(Module module) {
        return this.VisibleModules.contains(module);
    }

    public final void addVisibleModule(Module module) {
        this.VisibleModules.add(module);
    }

    public final void removeVisibleModule(Module module) {
        if (!isVisible(module)) return;
        this.VisibleModules.remove(module);
    }

    public final Module getModuleByName(String name) {
        for (Module hack : this.Modules) {
            if (hack.getName().equalsIgnoreCase(name)) {
                return hack;
            }
        }
        return null;
    }

    public final void enableModule(String name) {
        this.getModuleByName(name).enable();
    }

    public final void disableModule(String name) {
        this.getModuleByName(name).disable();
    }

    public final boolean isModuleEnabled(String name) {
        try {
            return this.getModuleByName(name).isEnabled();
        } catch (NullPointerException error) {
            return false;
        }
    }

    public final void onUpdate() {
        this.Modules.stream().filter(Module::isEnabled).spliterator().forEachRemaining(Module::onUpdate);
    }


    public final void onTick() {
        this.Modules.stream().filter(Module::isEnabled).spliterator().forEachRemaining(Module::onTick);
    }

    public final void onRender2D(Render2DEvent event) {
        this.Modules.stream().filter(Module::isEnabled).spliterator().forEachRemaining(module -> module.onRender2D(event));
    }

    public final void onRender3D(Render3DEvent event) {
        this.Modules.stream().filter(Module::isEnabled).spliterator().forEachRemaining(module -> module.onRender3D(event));
    }

    public final void onLogout() {
        this.Modules.spliterator().forEachRemaining(Module::onLogout);
    }

    public final void onLogin() {
        this.Modules.spliterator().forEachRemaining(Module::onLogin);
    }

    public final void onUnload() {
        this.Modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.Modules.forEach(Moon.getEventProcessor()::removeEventListener);
        this.Modules.forEach(Module::onUnload);
    }

    public final void onKeyDown(int key) {
        if (key <= 0 || mc.currentScreen instanceof Gui) {
            return;
        }
        for (Module module : Modules) {
            if (module.getBind() == key) {
                module.toggle();
            }
        }
    }

    public final void unloadAll() {
        for (Module module : this.Modules) {
            module.disable();
        }
    }

    public final List<Module.Category> getCategories() {
        List<Module.Category> cats = new ArrayList<>();
        for (Module.Category category : Module.Category.values()) {
            if (category.getName().equalsIgnoreCase("hud")) continue;
            cats.add(category);
        }
        return cats;
    }

    public final List<Module> getModulesByCategory(Module.Category cat) {
        List<Module> modules = new ArrayList<>();
        for (Module module : this.Modules) {
            if (module.getCategory() == cat) {
                modules.add(module);
            }
        }
        modules.sort(Comparator.comparing(Module::getName));
        return modules;
    }

    public final List<Module> getEnabledModules() {
        List<Module> modules = new ArrayList<>();
        for (Module module : this.Modules) {
            if (module.isEnabled()) {
                modules.add(module);
            }
        }
        return modules;
    }

}