package bloodbank.ui;

import bloodbank.core.BloodGroup;
import bloodbank.core.Request;
import bloodbank.service.RequestService;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class RequestPanel extends JPanel {
    private final RequestService service = new RequestService();
    private final JTextField hospital = new JTextField();
    private final JTextField patient = new JTextField();
    private final JTextField requestId = new JTextField(7);
    private final JComboBox<BloodGroup> group = new JComboBox<>(BloodGroup.values());
    private final JComboBox<Request.Priority> priority = new JComboBox<>(Request.Priority.values());
    private final JSpinner units = new JSpinner(new SpinnerNumberModel(1,1,20,1));
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Hospital","Patient","Group","Units","Priority","Status","Created"},0) { public boolean isCellEditable(int row,int column) { return false; } };
    private final JTable table = new JTable(model);

    public RequestPanel() {
        setLayout(new BorderLayout(12,12)); setBackground(Theme.BACKGROUND); setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JPanel form = new JPanel(new GridLayout(0,2,9,9)); form.setBackground(Theme.PANEL); form.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        form.add(new JLabel("Hospital name")); form.add(hospital); form.add(new JLabel("Patient name")); form.add(patient); form.add(new JLabel("Required blood group")); form.add(group); form.add(new JLabel("Units")); form.add(units); form.add(new JLabel("Priority")); form.add(priority);
        JButton create = new JButton("Create request"); create.addActionListener(event -> createRequest()); form.add(create);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT)); actions.setBackground(Theme.BACKGROUND); actions.add(new JLabel("Request ID")); actions.add(requestId);
        JButton approve = new JButton("Approve selected / ID"); approve.addActionListener(event -> process(true)); actions.add(approve);
        JButton reject = new JButton("Reject selected / ID"); reject.addActionListener(event -> process(false)); actions.add(reject);
        JButton refresh = new JButton("Refresh"); refresh.addActionListener(event -> load()); actions.add(refresh);
        table.setFillsViewportHeight(true); table.getSelectionModel().addListSelectionListener(event -> { if (!event.getValueIsAdjusting() && table.getSelectedRow() >= 0) { int row = table.convertRowIndexToModel(table.getSelectedRow()); requestId.setText(model.getValueAt(row,0).toString()); group.setSelectedItem(BloodGroup.fromLabel(model.getValueAt(row,3).toString())); units.setValue(Integer.parseInt(model.getValueAt(row,4).toString())); } });
        JPanel north = new JPanel(new BorderLayout(8,8)); north.setBackground(Theme.BACKGROUND); north.add(form,BorderLayout.CENTER); north.add(actions,BorderLayout.SOUTH); add(north,BorderLayout.NORTH); add(new JScrollPane(table),BorderLayout.CENTER); load();
    }

    private void createRequest() {
        try { int id = service.create(new Request(0,hospital.getText().trim(),patient.getText().trim(),(BloodGroup)group.getSelectedItem(),(int)units.getValue(),(Request.Priority)priority.getSelectedItem())); requestId.setText(String.valueOf(id)); hospital.setText(""); patient.setText(""); load(); JOptionPane.showMessageDialog(this,"Request #"+id+" saved."); }
        catch (Exception exception) { showError(exception); }
    }

    private void process(boolean approve) {
        try { int id = Integer.parseInt(requestId.getText().trim()); if (approve) service.approve(id); else service.reject(id); load(); JOptionPane.showMessageDialog(this,approve?"Request approved and stock issued.":"Request rejected."); }
        catch (Exception exception) { showError(exception); }
    }

    private void load() {
        try { model.setRowCount(0); for (String[] row : service.history()) model.addRow(row); }
        catch (Exception exception) { showError(exception); }
    }

    private void showError(Exception exception) { JOptionPane.showMessageDialog(this,exception.getMessage(),"Request processing",JOptionPane.ERROR_MESSAGE); }
}
