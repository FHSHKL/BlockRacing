package cf.fhshkl.blockracing.skill;

import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigSkillEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameScoreboard;
import cf.fhshkl.blockracing.core.GameTeam;
import cf.fhshkl.blockracing.core.TargetBlock;

public class SkillRoll extends SimpleSkill {
    public SkillRoll(){
        super(
            ConfigSkillEnum.RollBlock.get()
        );
    }

    @Override
    public boolean isAbleToBuy(Player player){
        GameTeam team = Game.getTeamByPlayer(player);
        team.rollPlayerSet.add(player.getName());
        if(team.rollPlayerSet.size() == team.team.getEntries().size()){
            // TODO: send all roll!
            return true;
        }
        // TODO: send to team , someone ask for roll
        return false;
    }

    @Override
    public void excuteSkill(Player player){
        GameTeam team = Game.getTeamByPlayer(player);
        for(int i=0;i<Game.configGame.scoreboardBlockNum && i<team.targetBlocks.size();i++){
            TargetBlock blk = team.targetBlocks.remove(0);
            team.rollBlocks.add(blk);

            TargetBlock roolBlk = team.rollBlocks.remove(0);
            team.targetBlocks.add(roolBlk);
        }
        GameScoreboard.repaint();
    }

    @Override
    public void onInit(GameTeam team){
        team.rollPlayerSet.clear();
    }
}
