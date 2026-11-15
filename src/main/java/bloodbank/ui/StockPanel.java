package bloodbank.ui;

import bloodbank.core.BloodGroup;
import bloodbank.service.StockService;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Map;

public class StockPanel extends JPanel {
    private final StockService service = new StockService();
    private final JPanel cards = new JPanel(new GridLayout(2,4,12,12));
    private final JTextField donorId = new JTextField(6);
    private final JComboBox<BloodGroup> group = new JComboBox<>(BloodGroup.values());
    private final JSpinner units = new JSpinner(new SpinnerNumberModel(1,1,3,1));

    public StockPanel() {
        setLayout(new BorderLayout(10,10));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        cards.setBackground(Theme.BACKGROUND);
        add(cards,BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setBackground(Theme.BACKGROUND);
        actions.add(new JLabel("Donor ID")); actions.add(donorId);
        actions.add(new JLabel("Blood group")); actions.add(group);
        actions.add(new JLabel("Units (max 3)")); actions.add(units);
        JButton addDonation = new JButton("Record donation and add stock");
        addDonation.addActionListener(event -> recordDonation());
        actions.add(addDonation);
        JButton refresh = new JButton("Refresh stock"); refresh.addActionListener(event -> refresh()); actions.add(refresh);
        add(actions,BorderLayout.SOUTH);
        refresh();
    }

    private void recordDonation() {
        try {
            service.recordDonation(Integer.parseInt(donorId.getText().trim()),(BloodGroup)group.getSelectedItem(),(int)units.getValue());
            donorId.setText("");
            refresh();
            JOptionPane.showMessageDialog(this,"Donation recorded and stock updated.");
        } catch (Exception exception) { JOptionPane.showMessageDialog(this,exception.getMessage(),"Donation",JOptionPane.ERROR_MESSAGE); }
    }

    public void refresh() {
        cards.removeAll();
        try {
            Map<BloodGroup,Integer> stock = service.getStock();
            for (BloodGroup bloodGroup : BloodGroup.values()) {
                int count = stock.getOrDefault(bloodGroup,0);
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createEmptyBorder(20,10,20,10));
                card.setBackground(count < 3 ? Theme.RED : count < 8 ? Theme.ORANGE : Theme.GREEN);
                JLabel title = new JLabel(bloodGroup.toString(),JLabel.CENTER); title.setFont(Theme.TITLE);
                JLabel amount = new JLabel(count + " units",JLabel.CENTER); amount.setFont(new Font("Segoe UI",Font.BOLD,20));
                card.add(title,BorderLayout.NORTH); card.add(amount,BorderLayout.CENTER); cards.add(card);
            }
        } catch (Exception exception) { cards.add(new JLabel("Unable to load stock: " + exception.getMessage())); }
        cards.revalidate(); cards.repaint();
    }
}
