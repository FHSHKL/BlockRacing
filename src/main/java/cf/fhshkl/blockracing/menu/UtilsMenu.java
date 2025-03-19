package cf.fhshkl.blockracing.menu;

public class UtilsMenu {
    /*
     * 8 => 1
     * 9 => 1
     * 10 => 2
     */
    public static int getSizeFromItemNum(int itemNum){
        return ( (itemNum - 1) / 9 + 1 ) * 9;
    }
}
