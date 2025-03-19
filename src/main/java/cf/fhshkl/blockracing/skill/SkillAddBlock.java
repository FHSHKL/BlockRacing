package cf.fhshkl.blockracing.skill;

import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigSkillEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameScoreboard;
import cf.fhshkl.blockracing.core.GameTeam;

public class SkillAddBlock extends SimpleSkill {
    public SkillAddBlock(){
        super(
            ConfigSkillEnum.AddBlock.get()
        );
    }

    @Override
    public void excuteSkill(Player player) {
        String playerName = player.getName();
        for(GameTeam team : Game.teamList){
            if(team.team.hasEntry(playerName)){
                continue;
            }

            int addNum = Integer.min(
                team.rollBlocks.size(),
                5
            );

            team.targetBlocks.addAll(
                team.rollBlocks.subList(0, addNum)
            );

            team.rollBlocks = team.rollBlocks.subList(addNum, team.rollBlocks.size());
        }

        GameScoreboard.repaint();
    }
}

