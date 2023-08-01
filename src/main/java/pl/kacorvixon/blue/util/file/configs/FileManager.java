package pl.kacorvixon.blue.util.file.configs;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import pl.kacorvixon.blue.Blue;
import pl.kacorvixon.blue.module.Module;
import pl.kacorvixon.blue.property.Property;
import pl.kacorvixon.blue.property.impl.*;
import pl.kacorvixon.blue.util.log.Logger;
import pl.kacorvixon.blue.util.render.ColorUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileManager {

    private final Minecraft mc = Minecraft.getMinecraft();

    private final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

    private final JsonParser jsonParser = new JsonParser();

    private final File directory = new File(String.format("%s%s%s%s", mc.mcDataDir, File.separator, "Blue", File.separator));


    public void init() {
        directory.mkdirs();
        Blue.getInstance().configDir.mkdirs();

        if (!Blue.getInstance().configDir.exists() && Blue.getInstance().configDir.listFiles() != null)
            loadConfigs();

    }

    public void stop() {
        saveConfig(new ClientConfig("modules"));
    }

    private ArrayList<ClientConfig> configList = new ArrayList<>();

    public void saveConfig(ClientConfig clientConfig) {
        JsonObject jsonObject = new JsonObject();
        for (Module module : Blue.getInstance().moduleAdministration.moduleList) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("name", module.name);
            moduleObject.addProperty("bind", module.keybind);
            moduleObject.addProperty("enabled", module.enabled);

            module.propertyList.forEach(value -> moduleObject.addProperty(value.name, value instanceof ColorProperty ?  String.valueOf(((ColorProperty) value).getValue().getRGB()) : String.valueOf(value.getValue())));

            jsonObject.add(module.name, moduleObject);
        }

        try {
            FileWriter fileWriter = new FileWriter(clientConfig.getConfigFile());
            fileWriter.write(new GsonBuilder().create().toJson(jsonObject));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        configList.add(clientConfig);
    }


    public java.util.List<ClientConfig> getContents() {
        return this.configList;
    }

    public void loadConfig(ClientConfig clientConfig) {
        if (!clientConfig.getConfigFile().exists() && !clientConfig.getConfigName().equalsIgnoreCase("modules")) {
            Logger.logChat("Config not found.");
            return;
        }
        String content = readFile(clientConfig.getConfigFile());

        JsonObject configurationObject = new GsonBuilder().create().fromJson(content, JsonObject.class);

        for (Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
            if (entry.getValue() instanceof JsonObject) {
                JsonObject moduleObject = (JsonObject) entry.getValue();

                for (Module module : Blue.getInstance().moduleAdministration.moduleList) {
                    if (module.name.equalsIgnoreCase(moduleObject.get("name").getAsString())) {
                        module.setKeybind(moduleObject.get("bind").getAsInt());
                        if (moduleObject.get("enabled").getAsBoolean()) {
                            module.setEnabled(!module.isEnabled());
                        }

                        for (Property value : module.propertyList) {
                            if (moduleObject.get(value.name) != null) {
                                if (value instanceof NumberProperty) {
                                    if (value.value instanceof Double) {
                                        value.value = (moduleObject.get(value.name).getAsDouble());
                                    }
                                    if (value.value instanceof Integer) {
                                        value.value = (moduleObject.get(value.name).getAsInt());
                                    }
                                    if (value.value instanceof Float) {
                                        value.value = (moduleObject.get(value.name).getAsFloat());
                                    }
                                    if (value.value instanceof Short) {
                                        value.value = (moduleObject.get(value.name).getAsShort());
                                    }
                                    if (value.value instanceof Byte) {
                                        value.value = (moduleObject.get(value.name).getAsByte());
                                    }
                                    if (value.value instanceof Long) {
                                        value.value = (moduleObject.get(value.name).getAsLong());
                                    }
                                }
                                //TODO FIX THIS
//                                if (value instanceof ColorProperty) {
//                                    Color color = ColorUtil.getColorFromInt(moduleObject.get(value.name).getAsInt());
//                                    value.value = (color.getRed() + ":" + color.getGreen() + ":" + color.getBlue());
//                                }
                                if (value instanceof BooleanProperty) {
                                    value.value = (moduleObject.get(value.name).getAsBoolean());
                                }
                                if (value instanceof EnumProperty) {
                                    for (int i = 0; i < ((EnumProperty) value).getValues().length; i++) {
                                        if (((EnumProperty) value).getValues()[i].name().equalsIgnoreCase(moduleObject.get(value.name).getAsString())) {
                                            value.value = (((EnumProperty) value).getValues()[i]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public void loadConfigs() {
        if (configList != null) {
            List<String> searchResult = new ArrayList<>();
            if (Blue.getInstance().configDir.exists())
                search(".*\\.json", Blue.getInstance().configDir, searchResult);
            for (String configName : searchResult) {
                this.configList.add(new ClientConfig(configName.replace(".json", "")));
            }
        }
    }

    public void deleteConfig(ClientConfig clientConfig) {
        if (clientConfig.getConfigFile() != null) {
            if (clientConfig.getConfigFile().delete()) {
                this.deleteConfig(clientConfig);
                this.configList.remove(clientConfig);
                this.loadConfigs();
            } else {
                //emtpy else
            }
        }
    }


    //read file better than above
    public String readFile(File path) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public void writeFile(File file, String text) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String readClipboard() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (HeadlessException | IOException | UnsupportedFlavorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void writeClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public void search(final String pattern, final File folder, java.util.List<String> result) {

        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getName().replace(".json", ""));
                }
            }
        }
    }
}