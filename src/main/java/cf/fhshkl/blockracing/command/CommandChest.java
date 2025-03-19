package cf.fhshkl.blockracing.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameState;
import cf.fhshkl.blockracing.core.GameTeam;

public class CommandChest implements CommandExecutor, TabCompleter {
    public CommandChest(){
        Bukkit.getPluginCommand("chest").setExecutor(this);
        Bukkit.getPluginCommand("chest").setTabCompleter(this);
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
            int teamChestNum = team.teamChest.size();
            for(int i = 1;i<=teamChestNum;i++){
                completions.add(String.format("%02d", i));
            }
            return completions;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(
            UtilsCommand.isNotPlayer(sender)||
            UtilsCommand.isNotInTeam(sender)||
            UtilsCommand.isNotGameState(sender,GameState.INGAME)||
            UtilsCommand.isNotArgNum(sender,args, 1)
        ){
            return true;
        }
        Player player = (Player) sender;
        GameTeam team = Game.getTeamByPlayer(player);
        int chestId = Integer.parseInt(args[0]);
        if(
            UtilsCommand.isNotInRange(sender,chestId, 1, team.teamChest.size() )
        ){
            return true;
        }

        Inventory chest = team.teamChest.get(chestId - 1);
        player.openInventory(chest);
        return true;
    }
}
