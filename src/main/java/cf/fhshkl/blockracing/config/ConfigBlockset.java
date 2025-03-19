package cf.fhshkl.blockracing.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.mineacademy.fo.remain.CompMaterial;

import com.fasterxml.jackson.core.exc.StreamReadException;

import cf.fhshkl.blockracing.core.GameScoreboard;

public class ConfigBlockset {
    public String name;
    public int point;
    public ArrayList<String> blocks;
    public boolean enabled = true;
    public int weight = 1;
    public ChatColor color = ChatColor.RESET;
    public CompMaterial icon = CompMaterial.GRASS_BLOCK;

    public static LinkedList<ConfigBlockset> loadBlockset(Config config,String blocksetPath) throws StreamReadException, IOException{
        LinkedList<ConfigBlockset> res = new LinkedList<>();
        File blocksetDir = new File(config.folderPath, blocksetPath);
        String[] blocksetNameList = blocksetDir.list();
        for(String blocksetName : blocksetNameList){
            
            Path pth = Path.of(blocksetPath, blocksetName);
            File fle = config.getConfigFile(pth.toString());
            ConfigBlockset blockset = config.YAML.readValue(fle,ConfigBlockset.class);

            res.add(blockset);
            
        }
        return res;
    }

    public ConfigBlockset clone(){
        ConfigBlockset res = new ConfigBlockset();
        res.name = this.name;
        res.point = this.point;
        res.blocks = new ArrayList<>();
        res.blocks.addAll(this.blocks);
        res.enabled = this.enabled;
        res.weight = this.weight;
        res.color = this.color;
        return res;
    }

    public void toggle(){
        this.enabled = !this.enabled;
        GameScoreboard.repaint();
    }
}
