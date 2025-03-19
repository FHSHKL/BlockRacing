package cf.fhshkl.blockracing.config;

import org.mineacademy.fo.remain.CompMaterial;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigSkill {
    public CompMaterial icon;
    public String name;
    public int cost;
    public int maxNum;
    public int curNum;

    public ConfigSkill(CompMaterial _icon,String _name,int _cost,int _maxNum,int _curNum){
        this.icon = _icon;
        this.name = _name;
        this.maxNum = _maxNum;
        this.cost = _cost;
        this.curNum = _curNum;
    }

    public ConfigSkill(CompMaterial _icon,String _name,int _cost,int _maxNum){
        this(_icon,_name,_cost,_maxNum,0);
    }

    public ConfigSkill(CompMaterial _icon,String _name,int _cost){
        this(_icon,_name,_cost,999999,0);
    }

    public ConfigSkill(){}
}
