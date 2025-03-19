package cf.fhshkl.blockracing.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import cf.fhshkl.blockracing.config.ConfigBlockset;
import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.config.ConfigRuleEnum;
import cf.fhshkl.blockracing.config.ConfigSkillEnum;
import cf.fhshkl.blockracing.skill.SimpleSkill;
import cf.fhshkl.blockracing.skill.SkillAddBlock;
import cf.fhshkl.blockracing.skill.SkillLocate;
import cf.fhshkl.blockracing.skill.SkillRandomTp;
import cf.fhshkl.blockracing.skill.SkillReblock;
import cf.fhshkl.blockracing.skill.SkillReplace;
import cf.fhshkl.blockracing.skill.SkillRoll;
import cf.fhshkl.blockracing.skill.SkillTeamChest;
import cf.fhshkl.blockracing.skill.SkillWayPoint;

public class GameTeam {
    
    public int id;
    public ChatColor color;
    public Team team;
    public List<TargetBlock> targetBlocks = new LinkedList<>();
    public List<TargetBlock> rollBlocks = new LinkedList<>();

    public GameScoreboard gameScoreboard = new GameScoreboard();

    public Set<String> rollPlayerSet = new HashSet<>();

    public List<Inventory> teamChest = new LinkedList<>();
    public List<WayPoint> teamWaypoint = new LinkedList<>();

    public Map<ConfigSkillEnum,SimpleSkill> skillList = new HashMap<>();

    public boolean locateEnabled = false;

    public int point = 0;

    GameTeam(int _id){
        this.id = _id;
        this.color = TeamColor.colorList[_id];

        team = GameScoreboard.coreBoard.getTeam("Team"+_id);
        if(team != null){
            team.unregister();
        }
        team = GameScoreboard.coreBoard.registerNewTeam("Team"+_id);
        team.setDisplayName("Team"+_id);
        team.setPrefix("[Team"+_id+"]");
        team.setColor(this.color);

        skillList.put(ConfigSkillEnum.AddBlock,new SkillAddBlock());
        skillList.put(ConfigSkillEnum.Locate,new SkillLocate());
        skillList.put(ConfigSkillEnum.RandomTp,new SkillRandomTp());
        skillList.put(ConfigSkillEnum.ReBlock,new SkillReblock());
        skillList.put(ConfigSkillEnum.RePlace,new SkillReplace());
        skillList.put(ConfigSkillEnum.RollBlock,new SkillRoll());
        skillList.put(ConfigSkillEnum.TeamChest,new SkillTeamChest());
        skillList.put(ConfigSkillEnum.TeamWaypoint,new SkillWayPoint());

        for(ConfigSkillEnum skill : ConfigSkillEnum.values()){
            skillList.get(skill).onInit(this);
        }

    }

    public void initBlockList(){
        if(ConfigRuleEnum.Racing.get() && this.id != 0){
            this.targetBlocks.addAll(Game.teamList.get(0).targetBlocks);
            this.rollBlocks.addAll(Game.teamList.get(0).rollBlocks);
            return;
        }

        Random rand = new Random();

        int gameBlockSetNum = 0;
        int maxBlockSetNameLen = 0;
        List<ConfigBlockset> blocksets = new ArrayList<>();        

        for(ConfigBlockset configBlockset : Game.configBlocksets){
            if(!configBlockset.enabled){
                continue;
            }
            // - all enabled block
            blocksets.add(configBlockset.clone());
            gameBlockSetNum += configBlockset.blocks.size();
            // - fix for blockset name
            maxBlockSetNameLen = Integer.max(
                maxBlockSetNameLen,
                configBlockset.name.length()
            );
        }

        if(gameBlockSetNum < Game.configGame.blockAmount){
            Bukkit.getLogger().info("2 much target block " + gameBlockSetNum + " < " +Game.configGame.blockAmount);
            // ERROR
            return;
        }

        // random shuffle
        for(ConfigBlockset configBlockset : blocksets){
            for(int i = configBlockset.blocks.size() - 1 ; i > 0 ; i --){
                int randNum =  rand.nextInt(i+1);

                String swp1 = configBlockset.blocks.get(i);
                String swp2 = configBlockset.blocks.get(randNum);
                configBlockset.blocks.set(i, swp2);
                configBlockset.blocks.set(randNum, swp1);
            }
        }
        
        
        while(targetBlocks.size() < Game.configGame.blockAmount){
            int weightMax = 0;
            for(ConfigBlockset configBlockset : blocksets){
                if(configBlockset.blocks.isEmpty()){
                    continue;
                }
                weightMax += configBlockset.weight;
            }
            int randNum = rand.nextInt(weightMax);
            for(ConfigBlockset configBlockset : blocksets){
                if(configBlockset.blocks.isEmpty()){
                    continue;
                }
                randNum -= configBlockset.weight;
                if(randNum < 0){
                    String blockName = configBlockset.blocks.remove(0);
                    targetBlocks.add(new TargetBlock(blockName, configBlockset,maxBlockSetNameLen));
                }
            }
        }

        for(ConfigBlockset configBlockset : blocksets){
            if(configBlockset.blocks.isEmpty()){
                continue;
            }
            for(String blockName : configBlockset.blocks){
                rollBlocks.add(new TargetBlock(blockName, configBlockset,maxBlockSetNameLen));
            }
        }
    }

    public List<TargetBlock> getOnboardBlockList(){
        int curNum = Integer.min(Game.configGame.scoreboardBlockNum,targetBlocks.size());
        List<TargetBlock> res = new ArrayList<>();
        res.addAll(this.targetBlocks.subList(0, curNum));
        return res;
    }

    public void win(){
        Game.gameState = GameState.END;
        for(Player player : Bukkit.getOnlinePlayers()){
            player.closeInventory();
            player.setGameMode(GameMode.SPECTATOR);
        }
        ConfigLangEnum.MsgGameWin.sendAllWithNoTeam(this);
        // play sound
        // send title
    }

    public void tryPutInChest(TargetBlock block){
        CompMaterial compMaterial = CompMaterial.valueOf(block.name);
        int lastEmpty = -1;
        for(Inventory chest : this.teamChest.reversed()){
            lastEmpty = chest.getSize() - 1;
            while(lastEmpty >= 0 && chest.getItem(lastEmpty) != null){
                lastEmpty --;
            }
            if(lastEmpty != -1){
                chest.setItem(lastEmpty,ItemCreator.of(compMaterial).amount(64).make());
                break;
            }
        }
        if(lastEmpty < 0){
            ConfigLangEnum.GiveBlockFailNoEmpty.sendTeam(this);
            return;
        }
    }

    public void tryCollectBlock(GameTeam tarTeam ,Inventory inventory){
        List<TargetBlock> otherTeamTarget = tarTeam.getOnboardBlockList();
        for(TargetBlock block : otherTeamTarget){
            this.tryCollectBlock(tarTeam, inventory, block);
        }
    }

    public void tryCollectBlock(GameTeam tarTeam ,Inventory inventory,TargetBlock block){
        Material material = Material.valueOf(block.name);

        if(!inventory.contains(material)){
            return;
        }

        if(!ConfigRuleEnum.Seize.get() && tarTeam.id != this.id){
            return;
        }

        ConfigLangEnum.CollectBlock.sendAll(this,block.getLocalName(),tarTeam);
/*
        Bukkit.getLogger().info("Tid-"+tarTeam.id);
        Bukkit.getLogger().info("B-"+block);
        Bukkit.getLogger().info("T-"+tarTeam.getOnboardBlockList());
*/
        if(ConfigRuleEnum.Seize.get() && tarTeam.id != this.id){
            TargetBlock newBlock = tarTeam.rollBlocks.remove(0);
            tarTeam.targetBlocks.add(newBlock);
        }

        if(ConfigRuleEnum.Give.get()){
            for(GameTeam team : Game.teamList){
                if(tarTeam.id == team.id){
                    continue;
                }
                team.tryPutInChest(block);
            }
        }

        int idx = tarTeam.targetBlocks.indexOf(block);
        if(idx != -1 && idx < Game.configGame.scoreboardBlockNum){
            tarTeam.targetBlocks.remove(idx);
        }

        this.point += block.point;
    }

    public void checkInventory(){
        for(GameTeam otherTeam : Game.teamList){
            for(String playerName : this.team.getEntries()){
                Player player = Bukkit.getPlayer(playerName);
                if(player == null){
                    continue;
                }
                Inventory inventory = player.getInventory();
                this.tryCollectBlock(otherTeam, inventory);
            }
            for(Inventory inventory : this.teamChest){
                this.tryCollectBlock(otherTeam, inventory);
            }
        }

        if(this.targetBlocks.isEmpty()){
            this.win();
        }
    }

    public String toString(){
        return String.format("%steam%d%s", TeamColor.colorList[this.id],this.id,ChatColor.RESET);
    }
}
