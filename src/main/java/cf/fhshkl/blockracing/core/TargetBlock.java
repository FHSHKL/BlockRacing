package cf.fhshkl.blockracing.core;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import cf.fhshkl.blockracing.config.ConfigBlockset;

public class TargetBlock {
    public String name;
    public int point;
    public String blockSetName;
    public ChatColor blockSetColor;

    TargetBlock(String _name,ConfigBlockset blockset,int maxBlockSetNameLen){
        this.name = _name;
        this.point = blockset.point;
        this.blockSetColor = blockset.color;

        StringBuilder stringBuilder = new StringBuilder();
        for(int i = maxBlockSetNameLen - blockset.name.length();i>=1;i--){
            stringBuilder.append(' ');
        }
        stringBuilder.append(blockset.name);
        this.blockSetName = stringBuilder.toString();
    }

    public String getLocalName(){
        String transKey = Material.valueOf(name).getTranslationKey();
        String res = Game.localBlockNameMap.get(transKey);
        return res;
    }
}
