package pl.kacorvixon.blue;

import net.minecraft.client.Minecraft;
import pl.kacorvixon.blue.command.CommandManager;
import pl.kacorvixon.blue.font.FontManager;
import pl.kacorvixon.blue.module.ModuleAdministration;
import pl.kacorvixon.blue.module.impl.render.Hud;
import pl.kacorvixon.blue.util.file.configs.ClientConfig;
import pl.kacorvixon.blue.util.file.configs.FileManager;
import java.io.File;

public class Blue {

    //TODO
    //new eventbus and much more events
    //maybe try to implement somehow shaders (u cant rn cuz lunar is gay)f
    //fix aimassist cuz it sucks
    //Velocity
    //Chams
    //Noslow

    private static Blue INSTANCE;
    public final String client_name = "blue", version = "1.0", build = "080123";

    public final ModuleAdministration moduleAdministration;
    public final CommandManager commandManager;
    public final FontManager fontManager;
    public final FileManager fileManager;

    public File dir = new File(Minecraft.getMinecraft().mcDataDir, "Blue"); //.minecraft/Blue
    public File configDir = new File(dir, "configs");

    Blue(){
        this.moduleAdministration = new ModuleAdministration();
        this.commandManager = new CommandManager();
        this.fontManager = new FontManager();
        this.fileManager = new FileManager();
    }

    public void init() {
        moduleAdministration.init();
        commandManager.init();
        fontManager.init();
        fileManager.init();

        fileManager.loadConfig(new ClientConfig("default"));
        moduleAdministration.autoenable(moduleAdministration.getModule(Hud.class));
    }
    public void stop() {
        fileManager.stop();
    }

    public static Blue getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Blue();
        }
        return INSTANCE;
    }
}
