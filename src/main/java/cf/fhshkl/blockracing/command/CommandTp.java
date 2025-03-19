package cf.fhshkl.blockracing.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.config.ConfigRuleEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameState;
import cf.fhshkl.blockracing.core.GameTeam;

public class CommandTp implements CommandExecutor, TabCompleter {
    public CommandTp(){
        Bukkit.getPluginCommand("tp").setExecutor(this);
        Bukkit.getPluginCommand("tp").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (
            UtilsCommand.isNotPlayer(sender)||
            UtilsCommand.isNotInTeam(sender)||
            UtilsCommand.isNotGameState(sender,GameState.INGAME)
        ) {
            return null;
        }
        Player player = (Player) sender;
        GameTeam team = Game.getTeamByPlayer(player);
        List<String> completions = new ArrayList<>();
        if(args.length == 1){
            completions.addAll(team.team.getEntries());
            return completions;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(
            UtilsCommand.isNotPlayer(sender)||
            UtilsCommand.isRuleFalse(sender, ConfigRuleEnum.TeamTp)||
            UtilsCommand.isNotInTeam(sender)||
            UtilsCommand.isNotGameState(sender,GameState.INGAME)||
            UtilsCommand.isNotArgNum(sender,args, 1)
        ){
            return true;
        }
        Player player = (Player) sender;
        GameTeam team = Game.getTeamByPlayer(player);
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if(
            !team.team.hasEntry(args[0])
        ){
            ConfigLangEnum.TpFailNotYourTeammate.send((Player)sender,args[0]);
            return true;
        }
        if(
            targetPlayer == null
        ){
            ConfigLangEnum.TpFailNoSuchPlayer.send((Player)sender,args[0]);
            return true;
        }
        Location loc = targetPlayer.getLocation();
        player.teleport(loc);

        return true;
    }
}
