package cf.fhshkl.blockracing.skill;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigSkillEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameTeam;
public class SkillReplace extends SimpleSkill {
    public SkillReplace(){
        super(
            ConfigSkillEnum.RePlace.get()
        );
    }

    @Override
    public void excuteSkill(Player player) {
        Random rand = new Random();
        GameTeam team = Game.getTeamByPlayer(player);
        
        ArrayList<String> playerNames = new ArrayList<>();
        for(GameTeam otherTeam : Game.teamList){
            if(otherTeam.id == team.id){
                continue;
            }
            playerNames.addAll(otherTeam.team.getEntries());
        }
        int playerId = rand.nextInt(playerNames.size());
        String player2Name = playerNames.get(playerId);
        Player player2 = Bukkit.getPlayer(player2Name);
        
        Location loc = player.getLocation();
        Location loc2 = player2.getLocation();

        player.teleport(loc2);
        player2.teleport(loc);

        // TODO: send msg to 2 player
    }
}
