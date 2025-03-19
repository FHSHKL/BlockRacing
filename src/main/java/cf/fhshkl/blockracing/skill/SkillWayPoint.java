package cf.fhshkl.blockracing.skill;

import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.config.ConfigSkillEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameTeam;
import cf.fhshkl.blockracing.core.WayPoint;
import cf.fhshkl.blockracing.menu.MenuInGame;

public class SkillWayPoint extends SimpleSkill {
    public SkillWayPoint(){
        super(
            ConfigSkillEnum.TeamWaypoint.get()
        );
    }

    public static void addWaypoint(GameTeam team){
        team.teamWaypoint.add( new WayPoint() );
    }

    @Override
    public void onInit(GameTeam team){
        for(int i = 0; i < this.curNum; i++){
            SkillWayPoint.addWaypoint(team);
        }
    }

    @Override
    public void excuteSkill(Player player){
        GameTeam team = Game.getTeamByPlayer(player);
        SkillWayPoint.addWaypoint(team);

        new MenuInGame(player);
    }
}
