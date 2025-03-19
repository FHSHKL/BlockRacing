package cf.fhshkl.blockracing.skill;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.config.ConfigSkillEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameTeam;
import cf.fhshkl.blockracing.menu.MenuInGame;

public class SkillTeamChest extends SimpleSkill {
    public SkillTeamChest(){
        super(
            ConfigSkillEnum.TeamChest.get()
        );
    }

    public static void addChest(GameTeam team){
        int id = team.teamChest.size();
        String chestTitle = ConfigLangEnum.TeamChestTitle.fmt(team,id + 1);
        Inventory inventory = Bukkit.createInventory(null, 6*9,chestTitle);
        team.teamChest.add(inventory);
    }

    @Override
    public void onInit(GameTeam team){
        for(int i = 0; i < this.curNum; i++){
            SkillTeamChest.addChest(team);
        }
    }

    @Override
    public void excuteSkill(Player player){
        GameTeam team = Game.getTeamByPlayer(player);
        SkillTeamChest.addChest(team);

        new MenuInGame(player);
    }
}
