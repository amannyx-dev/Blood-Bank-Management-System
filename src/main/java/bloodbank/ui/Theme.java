package bloodbank.ui;

import java.awt.*;

public final class Theme {
    public static final Color BACKGROUND=new Color(18,25,38),PANEL=new Color(29,39,56),ACCENT=new Color(220,65,78),TEXT=new Color(235,240,247),MUTED=new Color(150,164,182),GREEN=new Color(46,160,112),ORANGE=new Color(230,151,55),RED=new Color(205,65,75);
    public static final Font NORMAL=new Font("Segoe UI",Font.PLAIN,14),TITLE=new Font("Segoe UI",Font.BOLD,22);
    private Theme(){}
    public static void style(Component component){component.setFont(NORMAL);component.setForeground(TEXT);}
}
