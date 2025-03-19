package cf.fhshkl.blockracing.skill;

import java.util.Random;

import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigSkillEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameScoreboard;
import cf.fhshkl.blockracing.core.GameTeam;
import cf.fhshkl.blockracing.core.TargetBlock;

public class SkillReblock extends SimpleSkill {
    public SkillReblock(){
        super(
            ConfigSkillEnum.ReBlock.get()
        );
    }

    @Override
    public void excuteSkill(Player player) {
        Random rand = new Random();
        int teamNum = Game.teamList.size();
        int otherTeamId = rand.nextInt( teamNum-1 );
        GameTeam team = Game.getTeamByPlayer(player);
        if(otherTeamId == team.id){
            otherTeamId = (otherTeamId +1)%teamNum;
        }

        GameTeam otherTeam = Game.teamList.get(otherTeamId);
        int swpNum = Integer.min(
            team.targetBlocks.size(),
            otherTeam.targetBlocks.size()
        );
        swpNum = Integer.min(
            swpNum,
            Game.configGame.scoreboardBlockNum
        );

        for(int i=0;i<swpNum;i++){
            TargetBlock blk = team.targetBlocks.get(i);
            TargetBlock blk2 = otherTeam.targetBlocks.get(i);

            team.targetBlocks.set(i, blk2);
            otherTeam.targetBlocks.set(i, blk);
        }
        
        GameScoreboard.repaint();
    }
}
