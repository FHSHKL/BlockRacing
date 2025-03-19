package cf.fhshkl.blockracing.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import cf.fhshkl.blockracing.config.ConfigBlockset;
import cf.fhshkl.blockracing.config.ConfigLangEnum;
import cf.fhshkl.blockracing.config.ConfigRuleEnum;

public class GameScoreboard {
    public static Scoreboard coreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
    public static GameScoreboard prepareBoard = new GameScoreboard();

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    Objective objective;

    int curLine = 0;

    public static void repaint(){
        switch(Game.gameState){
            case GameState.PREPARE:
                prepareBoard.repaintPrepareBoard();
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.setScoreboard(prepareBoard.scoreboard);
                }
                break;
            case GameState.INGAME:
                for(GameTeam team : Game.teamList){
                    team.gameScoreboard.resetIngameScoreboard();
                }
                for(GameTeam team : Game.teamList){
                    team.gameScoreboard.repaintIngame(team);
                }
                
                for(GameTeam team : Game.teamList){
                    for(String playerName : team.team.getEntries()){
                        Player player = Bukkit.getPlayer(playerName);
                        if(player == null){
                            continue;
                        }
                        player.setScoreboard(team.gameScoreboard.scoreboard);
                    }
                }
                break;
            case GameState.END:
                break;
        }
    }

    public void repaintPrepareBoard(){
        if(objective != null){
            objective.unregister();
        }
        objective = scoreboard.registerNewObjective("prepareSidebar", Criteria.DUMMY, ConfigLangEnum.ScoreboardPrepareTitle.fmt());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.curLine = 0;

        this.addLine( ConfigLangEnum.ScoreboardPrepareOpenMenu.fmt());
        this.addLine( "+");
        for(ConfigRuleEnum rule : ConfigRuleEnum.values()){
            boolean ruleVal = rule.get();
            ChatColor ruleColor = ( ruleVal ? ChatColor.GREEN : ChatColor.RED );
            this.addLine(ConfigLangEnum.ScoreboardPrepareRule.fmt(rule,ruleColor,ruleVal));
        }
        this.addLine( "-");
        for(ConfigBlockset blockset : Game.configBlocksets){
            boolean enabled = blockset.enabled;
            ChatColor color = ( enabled ? ChatColor.GREEN : ChatColor.RED );
            this.addLine(ConfigLangEnum.ScoreboardPrepareBlockSet.fmt( blockset.name,color,enabled));
        }
        this.addLine( "*");
        this.addLine( ConfigLangEnum.ScoreboardPrepareMadeBy.fmt());
    }

    public void resetIngameScoreboard(){
        if(objective != null){
            objective.unregister();
        }
        objective = scoreboard.registerNewObjective("ingameSidebar", Criteria.DUMMY, ConfigLangEnum.ScoreboardPrepareTitle.fmt());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        curLine = 0;
    }

    public void repaintIngame(GameTeam team){
        if(!Game.configGame.scoreboardEnemyVisiablity){
            this.addLine(ConfigLangEnum.ScoreboardIngameTeam.fmt(team,team.point,team.targetBlocks.size()));
            // this.addLine("Team"+team.id + "-P" + team.point + "-B"+team.targetBlocks.size());
            for(TargetBlock block : team.getOnboardBlockList()){
                this.addLine(ConfigLangEnum.ScoreboardIngameTargetBlock.fmt(block.blockSetName,team.color,block.blockSetColor,block.getLocalName()));
            }
        }
        else{
            for(GameTeam gtm : Game.teamList){
                gtm.gameScoreboard.addLine(ConfigLangEnum.ScoreboardIngameTeam.fmt(team,team.point,team.targetBlocks.size()));
                for(TargetBlock block : team.getOnboardBlockList()){
                    gtm.gameScoreboard.addLine(ConfigLangEnum.ScoreboardIngameTargetBlock.fmt(block.blockSetName,team.color,block.blockSetColor,block.getLocalName()));
                }
            }
        }
    }

    public void addLine(String str){
        this.setLine(curLine, str);
        this.curLine++;
    }

    public void setLine(int solt,String str){
        this.objective.getScore(str).setScore(99-solt);
    }
}
