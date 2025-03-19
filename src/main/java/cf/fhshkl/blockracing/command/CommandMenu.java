package cf.fhshkl.blockracing.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameState;
import cf.fhshkl.blockracing.menu.MenuInGame;
import cf.fhshkl.blockracing.menu.MenuPrepare;

public class CommandMenu implements CommandExecutor, TabCompleter {
    public CommandMenu(){
        Bukkit.getPluginCommand("menu").setExecutor(this);
        Bukkit.getPluginCommand("menu").setTabCompleter(this);
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
        if(Game.gameState == GameState.PREPARE){
            new MenuPrepare(player);
        }
        if(Game.gameState == GameState.INGAME){
            new MenuInGame(player);
        }
        return true;
    }
}
