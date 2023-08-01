package pl.kacorvixon.blue.util.file.configs;

import pl.kacorvixon.blue.Blue;

import java.io.File;

public class ClientConfig {
    private String configName;

    private File configFile;

    public ClientConfig(final String configName) {
        this.configName = configName;
        this.configFile = new File(Blue.getInstance().configDir + "/" + getConfigName() + ".json");
    }

    public File getConfigFile() {
        return configFile;
    }

    public String getConfigName() {
        return configName;
    }
}