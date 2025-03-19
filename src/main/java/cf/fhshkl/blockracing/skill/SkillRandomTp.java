package cf.fhshkl.blockracing.skill;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigSkillEnum;

public class SkillRandomTp extends SimpleSkill {

    public SkillRandomTp(){
        super(
            ConfigSkillEnum.RandomTp.get()
        );
    }

    public static void randomTp(Player player){
        Random random = new Random();
        World playerWorld = player.getWorld();
        Location loc = player.getLocation();
        while(true){
            int xOffset = random.nextInt(20000) - 10000;
            int zOffset = random.nextInt(20000) - 10000;

            loc.add(xOffset, 0, zOffset);
            Block blk = playerWorld.getHighestBlockAt(loc);
            if(playerWorld.getEnvironment() == World.Environment.NETHER){
                while(blk.getType() == Material.BEDROCK){
                    blk = loc.add(0, -1, 0).getBlock();
                }
            }
            if(blk.isEmpty() || blk.isLiquid()){
                continue;
            }
            loc = blk.getLocation();
            break;
        }

        loc.add(0, 1, 0);
        Block tar = loc.getBlock();
        tar.getRelative(0,1,0).setBlockData(Bukkit.createBlockData(Material.AIR));
        tar.getRelative(0,2,0).setBlockData(Bukkit.createBlockData(Material.AIR));
        player.teleport(loc);
    }
    
    @Override
    public void excuteSkill(Player player){
        SkillRandomTp.randomTp(player);
    }
}
