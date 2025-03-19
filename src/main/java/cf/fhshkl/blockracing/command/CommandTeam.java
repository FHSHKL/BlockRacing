package cf.fhshkl.blockracing.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.menu.MenuJoinTeam;

public class CommandTeam implements CommandExecutor, TabCompleter {
    public CommandTeam(){
        Bukkit.getPluginCommand("team").setExecutor(this);
        Bukkit.getPluginCommand("team").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // - init check
        if(
            UtilsCommand.isNotPlayer(sender)
        ){
            return null;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // - init check
        if(
            UtilsCommand.isNotPlayer(sender)||
            UtilsCommand.isNotArgNum(sender,args, 0)
        ){
            return true;
        }

        Player player = (Player) sender;
        new MenuJoinTeam(player);
        return true;
    }
}
