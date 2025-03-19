package cf.fhshkl.blockracing.skill;

import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.config.ConfigSkillEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameTeam;

public class SkillLocate extends SimpleSkill {
    public SkillLocate(){
        super(
            ConfigSkillEnum.Locate.get()
        );
    }

    @Override
    public boolean isAbleToBuy(Player player){
        GameTeam team = Game.getTeamByPlayer(player);
        if(team.locateEnabled){
            ConfigLangEnum.SkillFailAlreadyBought.send(player,name);
            return false;
        }
        if(!super.isAbleToBuy(player)){
            return false;
        }
        return true;
    }

    @Override
    public void excuteSkill(Player player){
        GameTeam team = Game.getTeamByPlayer(player);
        team.locateEnabled = true;
    }
}
