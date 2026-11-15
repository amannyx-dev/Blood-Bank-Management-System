package bloodbank.ui;

import bloodbank.core.BloodGroup;
import bloodbank.core.Donor;
import bloodbank.service.DonorService;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DonorPanel extends JPanel {
    private final DonorService service = new DonorService();
    private final JTextField name = new JTextField();
    private final JTextField age = new JTextField();
    private final JTextField phone = new JTextField();
    private final JTextField weight = new JTextField();
    private final JTextField city = new JTextField();
    private final JTextField search = new JTextField(16);
    private final JComboBox<String> gender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
    private final JComboBox<BloodGroup> group = new JComboBox<>(BloodGroup.values());
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Gender", "Phone", "Group", "Weight", "City"}, 0) {
        public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);
    private int selectedDonorId;

    public DonorPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BACKGROUND);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBackground(Theme.PANEL);
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        addField(form, "Name", name);
        addField(form, "Age", age);
        addField(form, "Gender", gender);
        addField(form, "Phone", phone);
        addField(form, "Blood group", group);
        addField(form, "Weight kg", weight);
        addField(form, "City", city);
        JButton register = new JButton("Register donor");
        register.addActionListener(event -> save(false));
        form.add(register);
        JButton update = new JButton("Update selected");
        update.addActionListener(event -> save(true));
        form.add(update);
        add(form, BorderLayout.NORTH);
        table.setFillsViewportHeight(true);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 1 && table.getSelectedRow() >= 0) populateForm(table.convertRowIndexToModel(table.getSelectedRow()));
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Theme.BACKGROUND);
        bottom.add(new JLabel("Search name, phone or group"));
        bottom.add(search);
        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) { load(); }
        });
        JButton delete = new JButton("Delete selected");
        delete.addActionListener(event -> deleteSelected());
        bottom.add(delete);
        JButton clear = new JButton("Clear form");
        clear.addActionListener(event -> clearForm());
        bottom.add(clear);
        add(bottom, BorderLayout.SOUTH);
        load();
    }

    private void addField(JPanel panel, String label, java.awt.Component field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    private void populateForm(int row) {
        selectedDonorId = Integer.parseInt(model.getValueAt(row, 0).toString());
        name.setText(model.getValueAt(row, 1).toString());
        age.setText(model.getValueAt(row, 2).toString());
        gender.setSelectedItem(model.getValueAt(row, 3).toString());
        phone.setText(model.getValueAt(row, 4).toString());
        group.setSelectedItem(BloodGroup.fromLabel(model.getValueAt(row, 5).toString()));
        weight.setText(model.getValueAt(row, 6).toString());
        city.setText(model.getValueAt(row, 7).toString());
    }

    private Donor readForm() {
        return new Donor(name.getText().trim(), Integer.parseInt(age.getText().trim()), gender.getSelectedItem().toString(), phone.getText().trim(), (BloodGroup) group.getSelectedItem(), Double.parseDouble(weight.getText().trim()), city.getText().trim());
    }

    private void save(boolean update) {
        try {
            Donor donor = readForm();
            if (update) {
                if (selectedDonorId < 1) throw new IllegalArgumentException("Select a donor from the table first.");
                donor.setId(selectedDonorId);
                service.update(donor);
                JOptionPane.showMessageDialog(this, "Donor details updated.");
            } else {
                service.register(donor);
                JOptionPane.showMessageDialog(this, "Donor registered successfully.");
            }
            clearForm();
            load();
        } catch (Exception exception) { showError(exception); }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { showError(new IllegalArgumentException("Select a donor from the table first.")); return; }
        int answer = JOptionPane.showConfirmDialog(this, "Delete the selected donor?", "Confirm delete", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                service.delete(Integer.parseInt(model.getValueAt(table.convertRowIndexToModel(row), 0).toString()));
                clearForm();
                load();
            } catch (Exception exception) { showError(exception); }
        }
    }

    private void clearForm() {
        selectedDonorId = 0;
        name.setText(""); age.setText(""); phone.setText(""); weight.setText(""); city.setText("");
        gender.setSelectedIndex(0); group.setSelectedIndex(0); table.clearSelection();
    }

    private void load() {
        try {
            model.setRowCount(0);
            for (Donor donor : service.search(search.getText())) model.addRow(new Object[]{donor.getId(), donor.getName(), donor.getAge(), donor.getGender(), donor.getPhone(), donor.getBloodGroup(), donor.getWeight(), donor.getCity()});
        } catch (Exception exception) { showError(exception); }
    }

    private void showError(Exception exception) {
        JOptionPane.showMessageDialog(this, exception.getMessage(), "Blood Bank", JOptionPane.ERROR_MESSAGE);
    }
}
