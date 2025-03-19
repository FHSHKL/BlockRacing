package cf.fhshkl.blockracing.config;

import org.mineacademy.fo.remain.CompMaterial;

import cf.fhshkl.blockracing.core.Game;

public enum ConfigSkillEnum {
    AddBlock    (new ConfigSkill(CompMaterial.HOPPER,"AddBlock"           ,25)),
    ReBlock     (new ConfigSkill(CompMaterial.ENDER_CHEST,"ReBlock"       ,20)),
    RollBlock   (new ConfigSkill(CompMaterial.EGG,"RollBlock"             ,20,3)),
    RePlace     (new ConfigSkill(CompMaterial.ENDER_PEARL,"RePlace"       ,20)),
    RandomTp    (new ConfigSkill(CompMaterial.CHORUS_FRUIT,"RandomTp"     ,20)),
    Locate      (new ConfigSkill(CompMaterial.ENDER_EYE,"Locate"          ,25)),
    TeamChest   (new ConfigSkill(CompMaterial.CHEST,"TeamChest"           ,15,18,3)),
    TeamWaypoint(new ConfigSkill(CompMaterial.FILLED_MAP,"TeamWaypoint"   ,15,18,3));

    public ConfigSkill def;

    ConfigSkillEnum(ConfigSkill _def){
        this.def = _def;
    }

    public ConfigSkill get(){
        return Game.configGame.configSkillMap.get(this);
    }
}