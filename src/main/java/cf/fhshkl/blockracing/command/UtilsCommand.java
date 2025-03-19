package cf.fhshkl.blockracing.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.config.ConfigRuleEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameState;
import cf.fhshkl.blockracing.core.GameTeam;

public class UtilsCommand {
    public static boolean isNotPlayer(CommandSender sender){
        boolean res = !(sender instanceof Player);
        if(res) Bukkit.getLogger().warning(ConfigLangEnum.CommandFailIsNotPlayer.fmt());
        return res;
    }
    public static boolean isNotInTeam(CommandSender sender){
        Player player = (Player)sender;
        GameTeam team = Game.getTeamByPlayer(player);
        boolean res = team == null;
        if(res) ConfigLangEnum.CommandFailIsNotInTeam.send((Player)sender);
        return res;
    }
    public static boolean isNotGameState(CommandSender sender,GameState state){
        boolean res = Game.gameState != state;
        if(res) ConfigLangEnum.CommandFailIsNotGameState.send((Player)sender,Game.gameState,state);
        return res;
    }
    public static boolean isNotArgNum(CommandSender sender,String[] args,int num){
        boolean res = args.length != num;
        if(res) ConfigLangEnum.CommandFailIsNotArgNum.send((Player)sender, args.length,num);
        return res;
    }
    public static boolean isNotInRange(CommandSender sender,String val,List<String> args){
        boolean res = !args.contains(val);
        if(res) ConfigLangEnum.CommandFailIsNotInStringRange.send((Player)sender,val,args);
        return res;
    }
    public static boolean isNotInRange(CommandSender sender,int val,int min,int max){
        boolean res = val < min || val > max;
        if(res) ConfigLangEnum.CommandFailIsNotInIntegerRange.send((Player)sender,val,min,max);
        return res;
    }
    public static boolean isRuleFalse(CommandSender sender,ConfigRuleEnum rule){
        boolean res = rule.get();
        if(res) ConfigLangEnum.CommandFailRuleNotEnabled.send((Player)sender,rule);
        return res;
    }
}
