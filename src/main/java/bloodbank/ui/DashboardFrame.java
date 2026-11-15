package bloodbank.ui;

import bloodbank.service.StockService;
import bloodbank.thread.ExpiryMonitor;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private final JPanel content=new JPanel(new BorderLayout());
    private final JLabel status=new JLabel("Ready");
    private final ExpiryMonitor monitor;
    public DashboardFrame(String admin){setTitle("Blood Bank Management System");setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(1100,720);setLocationRelativeTo(null);getContentPane().setBackground(Theme.BACKGROUND);JPanel root=new JPanel(new BorderLayout());root.setBackground(Theme.BACKGROUND);JPanel sidebar=new JPanel(new GridLayout(0,1,6,6));sidebar.setBackground(Theme.PANEL);sidebar.setBorder(BorderFactory.createEmptyBorder(18,12,18,12));String[] names={"Overview","Donors","Stock","Requests","History","Reports"};for(String name:names){JButton button=new JButton(name);button.addActionListener(e->showPanel(name));sidebar.add(button);}JLabel heading=new JLabel("  BLOOD BANK  |  "+admin);heading.setFont(Theme.TITLE);heading.setForeground(Theme.TEXT);content.setBackground(Theme.BACKGROUND);root.add(sidebar,BorderLayout.WEST);root.add(content,BorderLayout.CENTER);root.add(status,BorderLayout.SOUTH);add(root);showPanel("Overview");monitor=new ExpiryMonitor(new StockService(),message->status.setText(message));monitor.start();addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosing(java.awt.event.WindowEvent e){monitor.stopMonitoring();}});setVisible(true);}
    private void showPanel(String name){content.removeAll();JPanel panel=switch(name){case "Donors"->new DonorPanel();case "Stock"->new StockPanel();case "Requests"->new RequestPanel();case "History"->new HistoryPanel();case "Reports"->new ReportPanel();default->new JPanel(new BorderLayout());};if(name.equals("Overview")){JLabel welcome=new JLabel("Blood Bank Management",SwingConstants.CENTER);welcome.setFont(new Font("Segoe UI",Font.BOLD,30));welcome.setForeground(Theme.TEXT);panel.add(welcome,BorderLayout.NORTH);panel.add(new StockChartPanel(),BorderLayout.CENTER);}content.add(panel);content.revalidate();content.repaint();}
}
