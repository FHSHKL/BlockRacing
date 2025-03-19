package cf.fhshkl.blockracing.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;

import cf.fhshkl.blockracing.Main;
import cf.fhshkl.blockracing.command.CommandChest;
import cf.fhshkl.blockracing.command.CommandLocate;
import cf.fhshkl.blockracing.command.CommandMenu;
import cf.fhshkl.blockracing.command.CommandSkill;
import cf.fhshkl.blockracing.command.CommandTeam;
import cf.fhshkl.blockracing.command.CommandTp;
import cf.fhshkl.blockracing.command.CommandWaypoint;
import cf.fhshkl.blockracing.config.Config;
import cf.fhshkl.blockracing.config.ConfigBlockset;
import cf.fhshkl.blockracing.config.ConfigGame;
import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.config.ConfigRuleEnum;
import cf.fhshkl.blockracing.cron.CollectChecker;
import cf.fhshkl.blockracing.cron.Potion;
import cf.fhshkl.blockracing.listener.playerManager;
import cf.fhshkl.blockracing.skill.SkillRandomTp;

public class Game {
    public static Main main;
    public static GameState gameState = GameState.PREPARE;

    public static Config config;
    public static ConfigGame configGame = new ConfigGame();
    public static LinkedList<ConfigBlockset> configBlocksets;

    public static List<GameTeam> teamList = new LinkedList<>();
    public static GameScoreboard gameScoreboard = new GameScoreboard();

    public static Set<String> inGamePlayer = new HashSet<>();

    public static Map<String,String> localBlockNameMap = new HashMap<>();

    public static void initConfig() throws StreamReadException, IOException{
        String _dataFolderAbsolutePath = main.getDataFolder().getAbsolutePath();
        // main.getResource("blockset-default/default.yml");
        config = new Config(_dataFolderAbsolutePath);

        File fle = config.getConfigFile("conf.yml");
        if(fle.exists()){
            configGame = config.YAML.readValue(fle,ConfigGame.class);
        }
        else{
            // - conf from default constructor
            
            File conf0 = config.getConfigFile("conf.yml");
            configGame = new ConfigGame();
            config.YAML.writeValue(conf0, configGame);
            

            config.copyResourceFile("blockset-debug/debug1.yml");
            config.copyResourceFile("blockset-debug/debug2.yml");
            config.copyResourceFile("blockset-debug/debug3.yml");

            config.copyResourceFile("blockset-default-en/1.easy.yml");
            config.copyResourceFile("blockset-default-en/2.medium.yml");
            config.copyResourceFile("blockset-default-en/3.hard.yml");
            config.copyResourceFile("blockset-default-en/4.end.yml");
            config.copyResourceFile("blockset-default-en/5.dyed.yml");
/*
            config.copyResourceFile("blockset-default-zh/1.easy.yml");
            config.copyResourceFile("blockset-default-zh/2.medium.yml");
            config.copyResourceFile("blockset-default-zh/3.hard.yml");
            config.copyResourceFile("blockset-default-zh/4.end.yml");
            config.copyResourceFile("blockset-default-zh/5.dyed.yml");
*/
            config.copyResourceFile("lang/zh_cn.json");
            config.copyResourceFile("lang/en_us.json");

            //config.copyResourceFile("conf-zh_cn.yml");
            config.copyResourceFile("conf-en_us.yml");
        }
        // - fk
        configBlocksets = ConfigBlockset.loadBlockset(config,"blockset-"+configGame.blockSet+"/");

        File blockNameFile = config.getConfigFile("lang/"+configGame.lang+".json");
        localBlockNameMap = config.JSON.readValue(blockNameFile,new TypeReference< HashMap<String,String> >(){});
    }

    public static void init() throws IOException{
        initConfig();

        Bukkit.getPluginManager().registerEvents(new playerManager(), main);

        new CommandChest();
        new CommandLocate();
        new CommandMenu();
        new CommandSkill();
        new CommandTeam();
        new CommandWaypoint();
        new CommandTp();

        for(World wd : Bukkit.getWorlds()){
            wd.setDifficulty(Difficulty.PEACEFUL);
            wd.setGameRule(GameRule.KEEP_INVENTORY, true);
        }

    }

    // Team Handler
    public static void addTeam(){
        int id = teamList.size();
        if(id >= TeamColor.size()){
            ConfigLangEnum.MsgTeamAddFail.sendAllWithNoTeam(TeamColor.size());
            return;
        }
        GameTeam newTeam = new GameTeam(id);
        teamList.add(newTeam);
        ConfigLangEnum.MsgTeamAdd.sendAllWithNoTeam(newTeam);
    }

    public static void removeTeam(int id){
        if(id < 0 || id >= teamList.size()){
            ConfigLangEnum.MsgTeamRemoveFailOutOfRange.sendAllWithNoTeam(id,0,teamList.size());
            return;
        }
        GameTeam toBeRemoved = teamList.get(id);
        if(!toBeRemoved.team.getEntries().isEmpty()){
            ConfigLangEnum.MsgTeamRemoveFailNotEmpty.sendAllWithNoTeam(toBeRemoved);
            return;
        }
        teamList.remove(id);
        toBeRemoved.team.unregister();
        ConfigLangEnum.MsgTeamRemove.sendAllWithNoTeam(toBeRemoved);

        int teamNum = teamList.size();
        for(int i=id;i<teamNum;i++){
            GameTeam team = teamList.get(i);
            if(team.id != i){
                String originTeamString = team.toString();
                team.id = i;
                String newTeamString = team.toString();
                ConfigLangEnum.MsgTeamRemoveIdChange.sendAll(
                    originTeamString,
                    newTeamString
                );
                
            }
        }
    }

    public static void joinTeam(int id,Player player){
        if(id > teamList.size()){
            // TODO:this should never happen
            return;
        }
        if(id == teamList.size()){
            Game.addTeam();
        }
        String playerName = player.getName();
        for(GameTeam team : teamList){
            if(team.team.hasEntry(playerName)){
                team.team.removeEntry(playerName);
            }
        }
        GameTeam team = teamList.get(id);
        team.team.addEntry(playerName);

        ConfigLangEnum.MsgPlayerJoinTeam.sendAllWithNoTeam(playerName,team);
    }

    public static GameTeam getTeamByPlayer(Player player){
        String playerName = player.getName();
        for(GameTeam team : teamList){
            if(team.team.hasEntry(playerName)){
                return team;
            }
        }
        return null;
    }

    public static boolean checkStartDemands(){
        for(Player player : Bukkit.getOnlinePlayers()){
            String playerName = player.getName();
            if(!inGamePlayer.contains(playerName)){
                ConfigLangEnum.MsgGameStartFailPlayerNotReady.sendAllWithNoTeam(playerName);
                return false;
            }
        }
        int teamNum = 0;
        for(GameTeam team : teamList){
            if(team.team.getSize()>0){
                teamNum ++;
            }
        }
        if(teamNum <= 1){
            ConfigLangEnum.MsgGameStartFailAtLeastTwoTeam.sendAllWithNoTeam();
            return false;
        }
        // send all GAME START !!!
        return true;
    }

    public static void gameStart(){
        gameState = GameState.INGAME;
        // close menu
        for(Player player : Bukkit.getOnlinePlayers()){
            player.closeInventory();
        }
        // generate block list
        for(GameTeam  team : teamList){
            team.initBlockList();
        }

        // set world
        World world = Bukkit.getWorlds().get(0);
        world.setDifficulty(Difficulty.EASY);
        world.setTime(1000);
        world.setStorm(false);
        world.setThundering(false);
        // rm : remove all entity
        // rm : set world border
        for(World wd : Bukkit.getWorlds()){
            wd.setDifficulty(Difficulty.EASY);
            wd.setGameRule(GameRule.KEEP_INVENTORY, true);
        }

        // todo: SPECTATOR

        for(String playerName : inGamePlayer){
            Player player = Bukkit.getPlayer(playerName);
            Game.initPlayer(player);
        }

        new CollectChecker().runTaskTimer(main, 0, 5);
        new Potion().runTaskTimer(main, 0, 5);

        GameScoreboard.repaint();
    }

    public static void initPlayer(Player player){
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setHealth(20);
        player.setExp(0);
        player.setLevel(0);
        player.setFoodLevel(20);
        player.setSaturation(10);
        player.setGameMode(GameMode.SURVIVAL);


        if(Game.gameState == GameState.PREPARE){
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 1200, 4, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 1200, 4, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 4, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false));
        }
        

        if(Game.gameState != GameState.INGAME){
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false));

        SkillRandomTp.randomTp(player);

        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.addItem(ItemCreator.of(CompMaterial.STONE_PICKAXE).amount(1).make());
        inventory.addItem(ItemCreator.of(CompMaterial.STONE_AXE    ).amount(1).make());
        inventory.addItem(ItemCreator.of(CompMaterial.STONE_SHOVEL ).amount(1).make());

        if(!ConfigRuleEnum.Swift.get()){
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, -1, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, -1, 1, false, false));

        inventory.addItem(ItemCreator.of(CompMaterial.IRON_PICKAXE).enchant(Enchantment.SILK_TOUCH, 1).make());
        inventory.addItem(ItemCreator.of(CompMaterial.GOLDEN_CARROT).amount(64).make());

        ItemStack damagedElytra = new ItemStack(Material.ELYTRA);
        ItemMeta elytraMeta = damagedElytra.getItemMeta();
        Repairable repairable = (Repairable) elytraMeta;
        repairable.setRepairCost(15);
        Damageable damageable = (Damageable) elytraMeta;
        damageable.setDamage( Material.ELYTRA.getMaxDurability()-1 );
        damagedElytra.setItemMeta(elytraMeta);
        inventory.addItem(damagedElytra);

        ItemStack xpBook = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) xpBook.getItemMeta();
        meta.addStoredEnchant(Enchantment.MENDING, 1, true);
        xpBook.setItemMeta(meta);
        inventory.addItem(xpBook);
    }
}
