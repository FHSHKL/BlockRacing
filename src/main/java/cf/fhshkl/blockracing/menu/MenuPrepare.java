package cf.fhshkl.blockracing.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import cf.fhshkl.blockracing.config.ConfigBlockset;
import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.config.ConfigRuleEnum;
import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameTeam;

public class MenuPrepare extends Menu {
    public MenuPrepare(Player player){
        int blockSetNum = Game.configBlocksets.size();
        int menuItemNum = 9 + blockSetNum;
        int menuSize = UtilsMenu.getSizeFromItemNum(menuItemNum);

        setSize(menuSize);
        setTitle(ConfigLangEnum.MenuPrepareTitle.fmt());

        int ithRule = 0;
        for(ConfigRuleEnum rule : ConfigRuleEnum.values()){
            Button ruleButton = new Button(menuSize - 9 + ithRule) {
                @Override
                public void onClickedInMenu(Player arg0,Menu menu,ClickType click){
                    rule.toggle();
                    new MenuPrepare(player);
                }
    
                @Override
                public ItemStack getItem(){
                    CompMaterial wool = rule.get() ? CompMaterial.GREEN_WOOL : CompMaterial.RED_WOOL;
                    String enabledStatus = rule.get() ? "enabled" : "disabled";
                    
                    return ItemCreator.of(
                        wool,
                        String.format("Rule %s", rule),
                        enabledStatus
                    ).make();
                }
            };
            ithRule++;

            this.registerButton(ruleButton);
        }

        for(int i=0;i<blockSetNum;i++){
            final int blocksetId = i;
            final ConfigBlockset configBlockset = Game.configBlocksets.get(blocksetId);
            Button blocksetButton = new Button(0*9+i){
                @Override
                public void onClickedInMenu(Player player,Menu menu,ClickType click){
                    configBlockset.toggle();
                    new MenuPrepare(player);
                }

                @Override
                public ItemStack getItem(){
                    CompMaterial wool = configBlockset.enabled ? CompMaterial.GREEN_WOOL : CompMaterial.RED_WOOL;
                    String enabledStatus = configBlockset.enabled ? "enabled" : "disabled";
                    
                    return ItemCreator.of(
                        wool,
                        String.format("Blockset %s", configBlockset.name),
                        enabledStatus
                    ).make();
                }
            };
            this.registerButton(blocksetButton);
        }

        Button chooseTeamButton = new Button(menuSize - 9 + 6) {
            @Override
            public void onClickedInMenu(Player player,Menu menu,ClickType click){
                new MenuJoinTeam(player);
            }

            @Override
            public ItemStack getItem(){
                return ItemCreator.of(
                    // TODO: change color of wool / change tip message
                    CompMaterial.TOTEM_OF_UNDYING,
                    "select team"
                ).make();
            }
        };
        this.registerButton(chooseTeamButton);

        Button readyButton = new Button(menuSize - 9 + 7) {
            @Override
            public void onClickedInMenu(Player player,Menu menu,ClickType click){
                String playerName = player.getName();
                GameTeam team = Game.getTeamByPlayer(player);
                if(team == null){
                    new MenuJoinTeam(player);
                    Game.inGamePlayer.remove(playerName);
                    return;
                }
                if(Game.inGamePlayer.contains(playerName)){
                    Game.inGamePlayer.remove(playerName);
                }
                else{
                    Game.inGamePlayer.add(playerName);
                }
                // TODO: send message : plz join a team first
                new MenuPrepare(player);
            }

            @Override
            public ItemStack getItem(){
                String playerName = player.getName();
                if(Game.inGamePlayer.contains(playerName)){
                    return ItemCreator.of(
                        // TODO: change color of wool / change tip message
                        CompMaterial.EMERALD_BLOCK,
                        "already ready"
                    ).make();
                }
                else{
                    return ItemCreator.of(
                        // TODO: change color of wool / change tip message
                        CompMaterial.EMERALD,
                        "ready"
                    ).make();
                }
            }
        };
        this.registerButton(readyButton);

        Button startButton = new Button(menuSize - 9 + 8) {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if(Game.checkStartDemands()){
                    Game.gameStart();
                }
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(
                    // TODO: change color of wool / change tip message
                    CompMaterial.DIAMOND,
                    "start"
                ).make();
            }
        };
        this.registerButton(startButton);

        this.displayTo(player);
    }
}
