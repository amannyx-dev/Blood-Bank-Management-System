package bloodbank.ui;

import bloodbank.service.HistoryService;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;

public class HistoryPanel extends JPanel {
    private final HistoryService service = new HistoryService();
    private final JTextField filter = new JTextField(12);
    private final JTextField fromDate = new JTextField(10);
    private final JTextField toDate = new JTextField(10);
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Donor","Phone","Group","Units","Date"}, 0) { public boolean isCellEditable(int row, int column) { return false; } };

    public HistoryPanel() {
        setLayout(new BorderLayout(10,10));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.setBackground(Theme.BACKGROUND);
        controls.add(new JLabel("Name / phone / group")); controls.add(filter);
        controls.add(new JLabel("From YYYY-MM-DD")); controls.add(fromDate);
        controls.add(new JLabel("To YYYY-MM-DD")); controls.add(toDate);
        JButton search = new JButton("Apply filters"); search.addActionListener(event -> load()); controls.add(search);
        JButton clear = new JButton("Clear"); clear.addActionListener(event -> { filter.setText(""); fromDate.setText(""); toDate.setText(""); load(); }); controls.add(clear);
        add(controls, BorderLayout.NORTH);
        JTable table = new JTable(model); table.setFillsViewportHeight(true); add(new JScrollPane(table), BorderLayout.CENTER);
        load();
    }

    private void load() {
        try {
            LocalDate from = fromDate.getText().isBlank() ? null : LocalDate.parse(fromDate.getText().trim());
            LocalDate to = toDate.getText().isBlank() ? null : LocalDate.parse(toDate.getText().trim());
            model.setRowCount(0);
            for (String[] row : service.search(filter.getText(), from, to)) model.addRow(row);
        } catch (Exception exception) { JOptionPane.showMessageDialog(this, exception.getMessage(), "Donation history", JOptionPane.ERROR_MESSAGE); }
    }
}
