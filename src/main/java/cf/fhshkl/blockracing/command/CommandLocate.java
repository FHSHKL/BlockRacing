package cf.fhshkl.blockracing.command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameState;
import cf.fhshkl.blockracing.core.GameTeam;

public class CommandLocate implements CommandExecutor, TabCompleter {

    public CommandLocate(){
        Bukkit.getPluginCommand("locate").setExecutor(this);
        Bukkit.getPluginCommand("locate").setTabCompleter(this);
    }

    public static List<String> arg0Range = Arrays.asList("structure","biome");
    public static List<String> arg1BiomeRange = new LinkedList<>();
    {{
        for(Biome biome : Registry.BIOME){
            Bukkit.getLogger().info("Biome : "+biome.toString());
            arg1BiomeRange.add(biome.toString().toLowerCase());
        }
    }}
    public static List<String> arg1StructureRange = new LinkedList<>();
    {{
        for(Keyed structure : Registry.STRUCTURE){
            Bukkit.getLogger().info("Structure : "+structure.getKey().toString());
            arg1StructureRange.add(structure.getKey().getKey().toLowerCase());
        }
        
    }}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(
            UtilsCommand.isNotPlayer(sender)||
            UtilsCommand.isNotInTeam(sender)||
            UtilsCommand.isNotGameState(sender, GameState.INGAME)
        ){
            return null;
        }

        if(args.length == 1){
            return arg0Range;
        }

        if(args.length == 2){
            if(UtilsCommand.isNotInRange(sender,args[0], arg0Range)){
                return null;
            }
            if(args[0].equals("biome")){
                
                return arg1BiomeRange;
            }
            if(args[0].equals("structure")){
                
                return arg1StructureRange;
            }

            return null;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
        if(!team.locateEnabled){
            ConfigLangEnum.CommandLocateFailNotBought.send(player);
            return true;
        }
        if(args[0] == "structure" && UtilsCommand.isNotInRange(sender,args[1], arg1StructureRange)){
            return true;
        }
        if(args[0] == "biome" && UtilsCommand.isNotInRange(sender,args[1], arg1BiomeRange) ){
            return true;
        }
        player.addAttachment(Game.main, "minecraft.command.locate", true);
        player.performCommand("minecraft:locate "+args[0]+" "+args[1]);
        player.addAttachment(Game.main, "minecraft.command.locate", false);
        team.locateEnabled = false;
        return true;
    }
    
}
