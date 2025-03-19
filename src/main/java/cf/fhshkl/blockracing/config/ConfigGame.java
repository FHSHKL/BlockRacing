package cf.fhshkl.blockracing.config;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigGame {
    public String blockSet = "default-en";
    public int blockAmount = 50;

    public Map<ConfigRuleEnum,Boolean> configRuleMap = new HashMap<>();
    {{
        for(ConfigRuleEnum rule : ConfigRuleEnum.values()){
            configRuleMap.put(rule, rule.def);
        }
    }}

    public int scoreboardBlockNum = 3;
    public boolean scoreboardEnemyVisiablity = true;

    public Map<ConfigSkillEnum,ConfigSkill> configSkillMap = new HashMap<>();
    {{
        for(ConfigSkillEnum skill : ConfigSkillEnum.values()){
            configSkillMap.put(skill, skill.def);
        }
    }}

    public Map<ConfigLangEnum,String> configLangMap = new HashMap<>();
    {{
        for(ConfigLangEnum lang : ConfigLangEnum.values()){
            configLangMap.put(lang,lang.def);
        }
    }}

    public String configVersion = "4.0";
    public String lang = "en_us";
}
