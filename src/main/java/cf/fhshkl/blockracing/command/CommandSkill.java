package cf.fhshkl.blockracing.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigSkillEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameState;
import cf.fhshkl.blockracing.core.GameTeam;

public class CommandSkill implements CommandExecutor, TabCompleter  {
    public CommandSkill(){
        Bukkit.getPluginCommand("skill").setExecutor(this);
        Bukkit.getPluginCommand("skill").setTabCompleter(this);
    }

    public static List<String> arg0Range = new ArrayList<>();
    {{
        for(ConfigSkillEnum skill : ConfigSkillEnum.values()){
            arg0Range.add(skill.get().name);
        }
    }}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // - init check
        if(
            UtilsCommand.isNotPlayer(sender)||
            UtilsCommand.isNotInTeam(sender)||
            UtilsCommand.isNotGameState(sender,GameState.INGAME)
        ){
            return null;
        }
        if(args.length == 0){
            return arg0Range;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // - init check
        if(
            UtilsCommand.isNotPlayer(sender)||
            UtilsCommand.isNotInTeam(sender)||
            UtilsCommand.isNotGameState(sender,GameState.INGAME)||
            UtilsCommand.isNotArgNum(sender,args, 1)||
            UtilsCommand.isNotInRange(sender,args[0], arg0Range )
        ){
            return true;
        }

        ConfigSkillEnum skill = ConfigSkillEnum.valueOf(args[0]);
        Player player = (Player) sender;
        GameTeam team = Game.getTeamByPlayer(player);

        // TODO: send msg try buy skill and use
        team.skillList.get(skill).tryBuyAndUse(player);
        return true;
    }
    
}
