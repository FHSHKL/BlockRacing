package cf.fhshkl.blockracing.config;

import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameScoreboard;

public enum ConfigRuleEnum {
    Give(true),
    Racing(false),
    Seize(true),
    Swift(false),
    TeamTp(true);

    public boolean def;
    ConfigRuleEnum(boolean _def){
        this.def = _def;
    }

    public boolean get(){
        return Game.configGame.configRuleMap.get(this);
    }

    public void set(boolean _val){
        Game.configGame.configRuleMap.put(this,_val);
        GameScoreboard.repaint();
    }

    public void toggle(){
        boolean val = this.get();
        val = !val;
        this.set(val);
    }
}
