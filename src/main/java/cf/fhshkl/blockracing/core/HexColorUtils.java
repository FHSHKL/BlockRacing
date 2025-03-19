package cf.fhshkl.blockracing.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

public class HexColorUtils {
    public static String translate(String message){
        Pattern hexPattern = Pattern.compile("\\{#([A-Z|a-z|0-9|_]{3,})\\}");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            String group = matcher.group(1);
            ChatColor color = ChatColor.valueOf(group);
            String group2 = "§x";
            if(color == null){
                for(char x : group.toCharArray()){
                    group2+= '§';
                    group2+= x;
                }
            }
            else{
                group2 = "§" + color.getChar();
            }
            matcher.appendReplacement(sb, group2);
        }
        matcher.appendTail(sb);
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }
}