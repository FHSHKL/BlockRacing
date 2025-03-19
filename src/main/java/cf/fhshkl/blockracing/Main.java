package cf.fhshkl.blockracing;

import org.mineacademy.fo.plugin.SimplePlugin;

import cf.fhshkl.blockracing.core.Game;

public class Main extends SimplePlugin {
    @Override
    protected void onPluginStart() {
        try{
            Game.main = this;
            Game.init();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPluginStop(){
        super.onPluginStop();
    }
}
