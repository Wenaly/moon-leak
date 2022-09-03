package com.moon.api.managers;

import com.google.gson.*;
import com.moon.Moon;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.ColorSetting;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ConfigurationManager {

    private final String mainFolder = "Moon/";
    private final String configFolder = mainFolder + "configs/";

    public void load() {
        try {
            this.loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() throws IOException {
        for (Module module : Moon.Modules.getModules()) {

            String stringPath = configFolder + module.getName() + ".json";
            Path path = Paths.get(stringPath);
            if (!Files.exists(path)) {
                return;
            }
            InputStream stream = Files.newInputStream(path);
            JsonObject jsonObject = new JsonObject();

            try {
                jsonObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
            } catch (IllegalStateException e) {
                Moon.Logger.error("Bad Config File for: " + module.getName() + ". Resetting.");

            }

            stream.close();

            for (Map.Entry entry : jsonObject.entrySet()) {
                String settingName = (String) entry.getKey();
                JsonElement element = (JsonElement) entry.getValue();

                if (settingName.equalsIgnoreCase("enabled")) {
                    if (module.getName().equalsIgnoreCase("clickgui"))
                    {
                        module.setEnabled(false);
                        continue;
                    }
                    module.setEnabled(element.getAsBoolean());
                    continue;
                }

                if (settingName.equalsIgnoreCase("bind")) {
                    module.setBind(element.getAsInt());
                    continue;
                }

                Setting setting = module.getSettingByName(settingName);

                if (setting == null) continue;

                switch (setting.getType()) {
                    case "Boolean":
                        setting.setValue(element.getAsBoolean());
                        break;
                    case "Color":
                        try {
                            ColorSetting colorValue = (ColorSetting) setting;
                            int r = Integer.parseInt(element.getAsJsonArray().get(0).toString());
                            int g = Integer.parseInt(element.getAsJsonArray().get(1).toString());
                            int b = Integer.parseInt(element.getAsJsonArray().get(2).toString());
                            int alpha = Integer.parseInt(element.getAsJsonArray().get(3).toString());
                            boolean rainbow = Boolean.parseBoolean(element.getAsJsonArray().get(4).toString());

                            colorValue.setValue(r, g, b, alpha);
                            colorValue.setRainbow(rainbow);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Enum":
                        setting.setValue(element.toString());
                        break;
                    case "Int":
                        setting.setValue(Integer.parseInt(element.toString()));
                        break;
                    case "Double":
                        setting.setValue(Double.parseDouble(element.toString()));
                        break;
                    default:
                        Moon.Logger.error("Unknown Setting type for " + module.getName() + " : " + setting.getName());
                        break;
                }
            }
        }
    }

    private void saveConfig() throws IOException {
        for (Module module : Moon.Modules.getModules()) {
            Path outputFile;
            File directory = new File(configFolder);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            if (!Files.exists(outputFile = Paths.get(configFolder + module.getName() + ".json"))) {
                Files.createFile(outputFile);
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this.writeSettings(module));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));
            writer.write(json);
            writer.close();
        }
    }

    public JsonObject writeSettings(Module module) {
        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();
        object.add("enabled", jp.parse(new Boolean(module.isEnabled()).toString()));
        for (Setting setting : module.getSettings()) {
            try {
                if (setting instanceof ColorSetting) {
                    ColorSetting colorValue = (ColorSetting) setting;

                    JsonArray json = new JsonArray();

                    json.add(colorValue.getColor().getRed());
                    json.add(colorValue.getColor().getGreen());
                    json.add(colorValue.getColor().getBlue());
                    json.add(colorValue.getColor().getAlpha());
                    json.add(colorValue.getRainbow());

                    object.add(setting.getName(), json);
                    continue;
                }
                object.add(setting.getName(), jp.parse(setting.getValue().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        object.add("bind", jp.parse(new Integer(module.getBind()).toString()));
        return object;
    }

}
