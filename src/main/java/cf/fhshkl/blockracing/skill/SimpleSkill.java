package cf.fhshkl.blockracing.skill;

import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.config.ConfigSkill;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameTeam;

public class SimpleSkill extends ConfigSkill {
    public SimpleSkill(ConfigSkill configSkill){
        super(
            configSkill.icon,
            configSkill.name,
            configSkill.cost,
            configSkill.maxNum,
            configSkill.curNum
        );
    }

    public boolean isAbleToBuy(Player player){
        if(curNum >= maxNum){
            ConfigLangEnum.SkillFailMaxNum.send(player,name,curNum,maxNum);
            return false;
        }

        GameTeam team = Game.getTeamByPlayer(player);
        if(team.point < this.cost){
            ConfigLangEnum.SkillFailTooExpensive.send(player,name,team.point,cost);
            return false;
        }
        return true;
    }

    public void buy(Player player){
        GameTeam team = Game.getTeamByPlayer(player);
        team.point -= this.cost;
        curNum++;

        ConfigLangEnum.SkillBuy.sendTeam(team,name,team.color,player.getName(),cost,team.point);
    }

    public void excuteSkill(Player player) {
        throw new Error("this should be override");
    }

    public void onInit(GameTeam team){
    }

    public void tryBuyAndUse(Player player){
        if(this.isAbleToBuy(player)){
            this.buy(player);
            this.excuteSkill(player);
        }
    }
}
