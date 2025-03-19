package cf.fhshkl.blockracing.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.config.ConfigSkillEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameTeam;
import cf.fhshkl.blockracing.core.WayPoint;
import cf.fhshkl.blockracing.skill.SimpleSkill;

public class MenuInGame extends Menu {
    public MenuInGame(Player player){
        setSize(5*9);
        setTitle(ConfigLangEnum.MenuInGameTitle.fmt());

        GameTeam team = Game.getTeamByPlayer(player);

        for(int i = 0;i<team.teamChest.size();i++){
            final int chestId = i;
            Button chestButton = new Button(0*9+i) {
                @Override
                public void onClickedInMenu(Player arg0, Menu arg1, ClickType arg2) {
                    Inventory chest = team.teamChest.get(chestId);
                    player.openInventory(chest);
                }
                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(
                        CompMaterial.CHEST,
                        ConfigLangEnum.TeamChestTitle.fmt(team,chestId+1)
                    ).make();
                }
            };
            this.registerButton(chestButton);
        }

        for(int i = 0;i<team.teamWaypoint.size();i++){
            final int wayPointId = i;
            WayPoint wayPoint = team.teamWaypoint.get(i);
            Button wayPointButton = new Button(2*9+i) {
                @Override
                public void onClickedInMenu(Player arg0, Menu arg1, ClickType clickType) {
                    switch(clickType){
                        case ClickType.RIGHT:
                            if(wayPoint.loc == null){
                                break;
                            }
                            if(wayPoint.waitConfirmToDelete){
                                wayPoint.loc = null;
                            }
                            else{
                                wayPoint.waitConfirmToDelete = true;
                            }
                            
                            break;
                        default:
                            if(wayPoint.loc == null){
                                wayPoint.init(player.getLocation());
                            }
                            else{
                                wayPoint.waitConfirmToDelete = false;
                                player.teleport(wayPoint.loc);
                                return;
                            }
                        break;
                    }
                    new MenuInGame(player);
                }
                @Override
                public ItemStack getItem() {
                    if(wayPoint.loc == null){
                        return ItemCreator.of(
                            CompMaterial.MAP,
                            ConfigLangEnum.TeamWayPointTitle.fmt(team,wayPointId+1)
                        ).make();
                    }
                    else{
                        return ItemCreator.of(
                            wayPoint.icon,
                            ConfigLangEnum.WayPointLocation.fmt(wayPoint.loc.getX(),wayPoint.loc.getY(),wayPoint.loc.getZ()),
                            ConfigLangEnum.WayPointBiome.fmt(wayPoint.biome),
                            ConfigLangEnum.WayPointLeftClickTp.fmt(),
                            ConfigLangEnum.WayPointRightClickDelete.fmt()
                        ).make();
                    }
                }
            };
            this.registerButton(wayPointButton);
        }

        int skillButtonSolt = 4*9+0;
        for(ConfigSkillEnum skillEnum : ConfigSkillEnum.values()){
            SimpleSkill skill = team.skillList.get(skillEnum);
            Button skillButton = new Button(skillButtonSolt++) {
                @Override
                public void onClickedInMenu(Player arg0, Menu arg1, ClickType arg2) {
                    skill.tryBuyAndUse(player);
                }
                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(
                        skill.icon,
                        skill.name,
                        ConfigLangEnum.SkillCost.fmt(skill.cost),
                        ConfigLangEnum.SkillNum.fmt(skill.curNum,skill.maxNum)
                    ).make();
                }
            };
            this.registerButton(skillButton);
        }

        this.displayTo(player);
    }
}
