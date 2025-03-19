package cf.fhshkl.blockracing.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import cf.fhshkl.blockracing.config.ConfigRuleEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameScoreboard;
import cf.fhshkl.blockracing.core.GameState;
import cf.fhshkl.blockracing.menu.MenuInGame;
import cf.fhshkl.blockracing.menu.MenuJoinTeam;
import cf.fhshkl.blockracing.menu.MenuPrepare;

public class playerManager implements Listener {
    
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Game.initPlayer(player);
        new MenuJoinTeam(player);
        GameScoreboard.repaint();
    }

    @EventHandler
    private void onPlayerQuit(PlayerJoinEvent event){
    }

    @EventHandler
    private void onPlayerSwapHand(PlayerSwapHandItemsEvent event){
        if(!event.getPlayer().isSneaking()){
            return;
        }

        Player player = event.getPlayer();

        event.setCancelled(true);
        switch(Game.gameState){
            case GameState.PREPARE:
                new MenuPrepare(player);
                break;
            case GameState.INGAME:
                new MenuInGame(player);
                break;
            case GameState.END:
                break;
        }
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        // TODO: send msg spawn protect
        Player player = event.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, -1, 1, false, false));
        if(!ConfigRuleEnum.Swift.get()){
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, -1, 4, false, false));
        
    }
}
