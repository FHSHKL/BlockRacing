package cf.fhshkl.blockracing.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameState;
import cf.fhshkl.blockracing.core.TeamColor;

public class MenuJoinTeam extends Menu {
    public MenuJoinTeam(Player player){
        String playerName = player.getName();
        int teamNum = Game.teamList.size();

        int menuSize = UtilsMenu.getSizeFromItemNum(teamNum+1);
        setSize(menuSize);
        setTitle(ConfigLangEnum.MenuJoinTeamTitle.fmt());
        
        for(int i = 0; i <= teamNum; i ++ ){
            final int teamId = i;
            if(teamId == teamNum){
                if(teamId >= TeamColor.colorList.length){
                    continue;
                }
                if(Game.gameState != GameState.PREPARE){
                    continue;
                }
            }
            Button teamButton = new Button(0*9+teamId){
                @Override
                public void onClickedInMenu(Player arg0,Menu menu,ClickType click){
                    switch (click) {
                        case ClickType.RIGHT:
                            Game.removeTeam(teamId);
                            break;
                        default:
                            Game.joinTeam(teamId,player);
                            break;
                    }
                    new MenuJoinTeam(player);
                    // refresh menu
                    for(Player noTeamPlayer : Bukkit.getOnlinePlayers()){
                        if(Game.getTeamByPlayer(noTeamPlayer) != null){
                            continue;
                        }
                        new MenuJoinTeam(noTeamPlayer);
                    }
                }

                @Override
                public ItemStack getItem(){
                    if(teamId == teamNum){
                        return ItemCreator.of(
                            CompMaterial.TORCH,
                            "Create Team"
                        ).make();
                    }
                    if(Game.teamList.get(teamId).team.hasEntry(playerName)){
                        return ItemCreator.of(
                            TeamColor.cBannerList[teamId],
                            "u r in this team"
                        ).make();
                    }
                    else{
                        return ItemCreator.of(
                            TeamColor.cWoolList[teamId],
                            "click to join / right click to remove team"
                        ).make();
                    }
                }
            };

            this.registerButton(teamButton);

        }
        this.displayTo(player);
    }
}
