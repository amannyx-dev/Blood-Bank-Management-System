package bloodbank.ui;

import bloodbank.service.ReportService;
import bloodbank.util.CsvExporter;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ReportPanel extends JPanel {
    public ReportPanel(){setLayout(new FlowLayout(FlowLayout.LEFT,20,25));setBackground(Theme.BACKGROUND);JSpinner threshold=new JSpinner(new SpinnerNumberModel(5,0,100,1));add(new JLabel("Low stock threshold"));add(threshold);JButton export=new JButton("Export low stock CSV");export.addActionListener(e->{try{var low=new ReportService().lowStock((int)threshold.getValue());JFileChooser chooser=new JFileChooser();chooser.setSelectedFile(new File("low-stock-report.csv"));if(chooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){new CsvExporter().exportStock(low,chooser.getSelectedFile());JOptionPane.showMessageDialog(this,"Report exported");}}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}});add(export);}
}
