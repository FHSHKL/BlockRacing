package cf.fhshkl.blockracing.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import cf.fhshkl.blockracing.core.Game;

public class Config {
    public String folderPath;
    public Config(String _folderPath){
        this.folderPath = _folderPath;
    }

    public File getConfigFile(String configAbsolutePath){
        Path configPath = Path.of(
            this.folderPath,
            configAbsolutePath
        );
        File fle = configPath.toFile();
        File parent = fle.getParentFile();
        if(!parent.exists()){
            parent.mkdir();
        }
        return fle;
    }

    public ObjectMapper JSON = new JsonMapper();
    public ObjectMapper YAML = new YAMLMapper();

    public void copyResourceFile(String configAbsolutePath) throws IOException{
        File dstFile = this.getConfigFile(configAbsolutePath);
        InputStream ips = Game.main.getResource(configAbsolutePath);
        FileOutputStream ops = new FileOutputStream(dstFile);
        ops.write(ips.readAllBytes());
        ips.close();
        ops.close();
    }
}
