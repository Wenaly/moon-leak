package com.moon;

import com.google.gson.*;
import com.moon.api.event.Events;
import com.moon.api.event.handler.EventProcessor;
import com.moon.api.gui.font.FontManager;
import com.moon.api.managers.ConfigurationManager;
import com.moon.api.managers.FriendsManager;
import com.moon.api.managers.RotationManager;
import com.moon.api.module.Modules;
import com.moon.api.newgui.NewGui;
import com.moon.api.setting.Settings;
import com.moon.api.utils.misc.IconUtils;
import com.moon.api.utils.misc.SystemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import sun.misc.Unsafe;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Mod(modid = Moon.ModId, name = Moon.ModName, version = Moon.ModVersion)
public class Moon {

    public static final String ModId = "moon";
    public static final String ModName = "Moon";
    public static final String ModVersion = "1.1";

    public static final Logger Logger = LogManager.getLogger(ModId);

    @Mod.Instance
    public static Moon Instance;

    private static EventProcessor EventProcessor;
    private static RotationManager RotationManager;
    public static Events Events;
    public static Modules Modules;
    public static Settings Settings;

    public static ConfigurationManager ConfigurationManager;
    public static FriendsManager FriendsManager;
    public static FontManager FontManager;

    public static NewGui NewGui;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent Event) {
        Logger.info("Loading " + ModName + " " + ModVersion);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent Event) throws IOException {
        String uid = "000";
        File directory = new File(System.getenv("APPDATA") + "/.moon");
        Path outputFile = Paths.get(directory.getAbsolutePath() + "/uid.json");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!Files.exists(outputFile)) {
            Files.createFile(outputFile);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject object = new JsonObject();
            JsonParser jp = new JsonParser();
            object.add("uid", jp.parse("000"));
            String json = gson.toJson(object);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));
            writer.write(json);
            writer.close();
        }

        if (!Files.exists(directory.toPath())) {
            return;
        }
        InputStream stream = Files.newInputStream(outputFile);
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
        } catch (IllegalStateException e) {
            // no
        }
        stream.close();

        for (Map.Entry entry : jsonObject.entrySet()) {
            String name = (String) entry.getKey();
            JsonElement element = (JsonElement) entry.getValue();

            if (name.equalsIgnoreCase("uid")) {
                uid = element.getAsString();
                continue;
            }
        }
        URL url = new URL("https://auth.driftydev.repl.co/uid/" + uid);
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();

        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = inputStream.read()) != -1; ) {
            sb.append((char) ch);
        }

        if (sb.toString().equals(SystemUtils.getHWID())) { // sb.toString().equals(SystemUtils.getHWID())
            this.load();
            this.getLogger().info(ModName + " " + ModVersion + " has been loaded.");
            Display.setTitle(ModName + " v" + ModVersion);
        } else {
            final Unsafe unsafe = Unsafe.getUnsafe();
            unsafe.putAddress(0, 0);
        }
    }

    private static void load() {
        setIcon();
        EventProcessor = new EventProcessor();
        RotationManager = new RotationManager();
        EventProcessor.addEventListener(RotationManager);
        Events = new Events();
        Settings = new Settings();
        Modules = new Modules();
        FontManager = new FontManager();
        FontManager.load();
        ConfigurationManager = new ConfigurationManager();
        ConfigurationManager.load();
        NewGui = new NewGui();
    }

    public static void unLoad() {
        ConfigurationManager.save();
    }

    public static EventProcessor getEventProcessor() { return EventProcessor; }

    public static RotationManager getRotationManager() { return RotationManager; }

    public static FontManager getFontManager() { return FontManager; }

    public static Logger getLogger() {
        return Logger;
    }

    public static void setIcon() {
        //Why am I skidding Zori's setWindowIcon? I don't even know.
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (InputStream inputStream16x = Minecraft.class.getResourceAsStream("/assets/moon/x16.png");
                 InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/moon/x32.png")) {
                ByteBuffer[] icons = new ByteBuffer[]{IconUtils.INSTANCE.readImageToBuffer(inputStream16x), IconUtils.INSTANCE.readImageToBuffer(inputStream32x)};
                Display.setIcon(icons);
            } catch (Exception e) {
                getLogger().error("Couldn't set Windows Icon", e);
            }
        }
    }

    }
