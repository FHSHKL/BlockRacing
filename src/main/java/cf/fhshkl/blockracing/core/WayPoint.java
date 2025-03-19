package cf.fhshkl.blockracing.core;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.CompMaterial;

public class WayPoint {
    public Location loc;
    public CompMaterial icon;
    public Biome biome;

    public boolean waitConfirmToDelete = false;

    public void init(Location _loc){
        this.loc = _loc;
        Block block = _loc.getBlock();
        while (block.isEmpty()&&block.getY()>-64) {
            block = block.getRelative(0, -1, 0);
        }
        this.icon = CompMaterial.fromBlock(block);
        if(block.isEmpty()){
            switch(block.getWorld().getEnvironment()){
                case Environment.NORMAL:icon = CompMaterial.GRASS_BLOCK;break;
                case Environment.NETHER:icon = CompMaterial.NETHERRACK;break;
                case Environment.THE_END:icon = CompMaterial.END_STONE;break;
                default:icon = CompMaterial.FILLED_MAP;break;
            }
        }
        this.biome = block.getBiome();
    }

    public void tryUse(Player player){
        if(this.loc == null){
            this.init(player.getLocation());
        }
        else{
            player.teleport(this.loc);
        }
    }
}
