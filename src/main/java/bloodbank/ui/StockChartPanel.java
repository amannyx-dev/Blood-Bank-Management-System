package bloodbank.ui;

import bloodbank.service.StockService;
import bloodbank.core.BloodGroup;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class StockChartPanel extends JPanel {
    private Map<BloodGroup,Integer> stock=Map.of();
    public StockChartPanel(){setBackground(Theme.BACKGROUND);try{stock=new StockService().getStock();}catch(Exception ignored){}}
    protected void paintComponent(Graphics graphics){super.paintComponent(graphics);Graphics2D g=(Graphics2D)graphics.create();g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);int max=Math.max(1,stock.values().stream().mapToInt(Integer::intValue).max().orElse(1));int width=Math.max(1,(getWidth()-100)/8);int index=0;for(BloodGroup group:BloodGroup.values()){int value=stock.getOrDefault(group,0);int height=(int)((getHeight()-100)*value/(double)max);int x=45+index*width;int y=getHeight()-55-height;g.setColor(Theme.ACCENT);g.fillRoundRect(x,y,width-18,height,8,8);g.setColor(Theme.TEXT);g.drawString(group.toString(),x,getHeight()-30);g.drawString(String.valueOf(value),x,y-8);index++;}g.dispose();}
}
