package cf.fhshkl.blockracing.config;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameTeam;
import cf.fhshkl.blockracing.core.HexColorUtils;

public enum ConfigLangEnum {
    CommandFailIsNotPlayer("[ERR]not a player"),
    CommandFailIsNotGameState("[ERR]gameState:%s != %s"),
    CommandFailIsNotArgNum("[ERR]argNum:%d != %d"),
    CommandFailIsNotInStringRange("[ERR]arg:%s NOT IN %s"),
    CommandFailIsNotInIntegerRange("[ERR]arg:%d NOT IN [%d,%d]"),
    CommandFailIsNotInTeam("[ERR]you are not in team"),
    CommandFailRuleNotEnabled("[ERR]Rule:%s is not enabled"),

    TpFailNoSuchPlayer("No such player called %s"),
    TpFailNotYourTeammate("%s is not your teammate"),
    TpFailRuleNotEnabled("[Rule]teamTp is not enabled"),

    CommandLocateFailNotBought("you need to buy locate first"),

    MenuPrepareTitle("Prepare Menu"),
    MenuJoinTeamTitle("Join Team Menu"),
    MenuInGameTitle("In Game Menu"),

    ScoreboardPrepareTitle("{#BOLD}BlockRacing{#RESET}"),
    ScoreboardPrepareOpenMenu("Use {#RED}SHIFT+F{#RESET} to open menu"),
    ScoreboardPrepareRule("[rule]%s:%s%b"),
    ScoreboardPrepareBlockSet("[blockSet]%s:%s%b"),
    ScoreboardPrepareMadeBy("Made By : LQSnow/xiaojiuwo233/FHSHKL"),

    ScoreboardIngameTitle("{#BOLD}BlockRacing{#RESET}"),
    ScoreboardIngameTeam("%s {#BOLD}P{#RESET}:%d {#BOLD}B{#RESET}:%d"),
    ScoreboardIngameTargetBlock("%s %s|{#RESET} %s%s{#RESET}"),

    CollectBlock("%s collected %s ( task from %s )"),
    GiveBlockFailNoEmpty("no empty place"),

    TeamChestTitle("%s-Chest%d"),
    TeamWayPointTitle("%s-WayPoint%d"),
    WayPointLocation("%.1f %.1f %.1f"),
    WayPointBiome("Biome:%s"),
    WayPointRightClickDelete("right click :rm / confirm remove"),
    WayPointLeftClickTp("left click : tp & concel remove"),

    SkillCost("Cost : %d"),
    SkillNum("UsedNum/MaxNum : %d / %d"),
    
    SkillBuy("[Skill-%s]Bought by %s%s{#RESET} , cost {#RED}%d{#RESET} point , {#GREEN}%d{#RESET} point left"),
    SkillFailMaxNum("[Skill-%s]UsedNum/OwnedNum:%d >= MaxNum:%d"),
    SkillFailTooExpensive("[Skill-%s]Too expensive TeamPoint:%d < Cost:%d"),
    SkillFailAlreadyBought("[Skill-%s]already bought"),

    MsgTeamAdd("Add %s"),
    MsgTeamAddFail("Add team failed , max team number = %d"),

    MsgTeamRemove("Remove %s"),
    MsgTeamRemoveIdChange("teamId change %s => %s"),
    MsgTeamRemoveFailOutOfRange("Remove team failed , teamId=%d not in range [%d,%d)"),
    MsgTeamRemoveFailNotEmpty("Remove team failed , %s not empty"),

    MsgGameWin("%s win!!!!!!"),
    MsgGameStartFailPlayerNotReady("%s is not ready!"),
    MsgGameStartFailAtLeastTwoTeam("at least 2 teams to start!"),
    
    MsgPlayerJoinTeam("Player %s join %s");

    private String getFmt(){
        String res = this.def;
        if(Game.configGame.configLangMap.containsKey(this)){
            res = Game.configGame.configLangMap.get(this);
        }
        else{
            Game.configGame.configLangMap.put(this,this.def);
        }
        return res;
    }
    public String setFmt(String str){
        return Game.configGame.configLangMap.put(this,str);
    }

    public String def;
    ConfigLangEnum(String _def){
        this.def = _def;
    }

    public String fmt(Object ...args){
        String res = String.format(this.getFmt(), args);
        return HexColorUtils.translate(res);
    }

    public void send(Player player,Object ...args){
        player.sendMessage(this.fmt(args));
    }

    public void sendTeam(GameTeam team,Object ...args){
        for(String playerName : team.team.getEntries()){
            Player player = Bukkit.getPlayer(playerName);
            this.send(player, args);
        }
    }

    public void sendAll(Object ...args){
        for(GameTeam team : Game.teamList){
            this.sendTeam(team, args);
        }
    }

    public void sendAllWithNoTeam(Object ...args){
        for(Player player : Bukkit.getOnlinePlayers()){
            this.send(player, args);
        }
    }
}
