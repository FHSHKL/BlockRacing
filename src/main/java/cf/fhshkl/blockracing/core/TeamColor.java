package cf.fhshkl.blockracing.core;

import org.bukkit.ChatColor;
import org.mineacademy.fo.remain.CompMaterial;

public class TeamColor {
    public static ChatColor[] colorList = {
        ChatColor.DARK_AQUA,
        ChatColor.DARK_BLUE,
        ChatColor.DARK_GRAY,
        ChatColor.DARK_GREEN,
        ChatColor.DARK_PURPLE,
        ChatColor.DARK_RED,
        ChatColor.GOLD,
        ChatColor.WHITE,
    };

    public static CompMaterial[] cWoolList = {
        CompMaterial.CYAN_WOOL,
        CompMaterial.BLUE_WOOL,
        CompMaterial.GRAY_WOOL,
        CompMaterial.GREEN_WOOL,
        CompMaterial.PURPLE_WOOL,
        CompMaterial.RED_WOOL,
        CompMaterial.ORANGE_WOOL,
        CompMaterial.WHITE_WOOL,
    };

    public static CompMaterial[] cBannerList = {
        CompMaterial.CYAN_BANNER,
        CompMaterial.BLUE_BANNER,
        CompMaterial.GRAY_BANNER,
        CompMaterial.GREEN_BANNER,
        CompMaterial.PURPLE_BANNER,
        CompMaterial.RED_BANNER,
        CompMaterial.ORANGE_BANNER,
        CompMaterial.WHITE_BANNER,
    };

    public static int size(){
        return colorList.length;
    }
}
