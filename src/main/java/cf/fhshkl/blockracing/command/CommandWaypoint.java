package cf.fhshkl.blockracing.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameState;
import cf.fhshkl.blockracing.core.GameTeam;
import cf.fhshkl.blockracing.core.WayPoint;

public class CommandWaypoint implements CommandExecutor, TabCompleter {
    public CommandWaypoint(){
        Bukkit.getPluginCommand("waypoint").setExecutor(this);
        Bukkit.getPluginCommand("waypoint").setTabCompleter(this);
    }
    
    public static List<String> arg0Range = Arrays.asList("rm","use");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(
            UtilsCommand.isNotPlayer(sender)||
            UtilsCommand.isNotInTeam(sender)
        ){
            return null;
        }

        Player player = (Player) sender;
        GameTeam team = Game.getTeamByPlayer(player);
        List<String> completions = new ArrayList<>();

        if(args.length == 0){
            return arg0Range;
        }

        if(args.length == 1){
            if(UtilsCommand.isNotInRange(sender,args[0], arg0Range)){
                return null;
            }
            int teamWayPointSize = team.teamWaypoint.size();
            for(int i = 1;i<=teamWayPointSize;i++){
                completions.add(Integer.toString(i));
            }
            return completions;
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
            UtilsCommand.isNotArgNum(sender,args, 2)||
            UtilsCommand.isNotInRange(sender,args[0], arg0Range)
        ){
            return true;
        }

        Player player = (Player) sender;
        GameTeam team = Game.getTeamByPlayer(player);
        int waypointId = Integer.parseInt(args[1]);
        // - chceck arg1
        if(
            UtilsCommand.isNotInRange(sender,waypointId, 1, team.teamWaypoint.size() )
        ){
            return true;
        }
        // - execute
        WayPoint wayPoint = team.teamWaypoint.get(waypointId - 1);
        if(args[0] == "rm"){
            wayPoint.loc = null;
        }
        if(args[0] == "use"){
            wayPoint.tryUse(player);
        }
        return true;
    }
}
