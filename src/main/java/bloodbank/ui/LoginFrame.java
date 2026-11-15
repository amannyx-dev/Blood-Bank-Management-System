package bloodbank.ui;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import bloodbank.db.DBConnection;

public class LoginFrame extends JFrame {
    private final JTextField username=new JTextField(18);
    private final JPasswordField password=new JPasswordField(18);
    public LoginFrame(){setTitle("Blood Bank | Admin Login");setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(390,280);setLocationRelativeTo(null);getContentPane().setBackground(Theme.BACKGROUND);JPanel form=new JPanel(new GridLayout(0,1,10,10));form.setBorder(BorderFactory.createEmptyBorder(28,35,28,35));form.setBackground(Theme.PANEL);JLabel title=new JLabel("BLOOD BANK ADMIN",SwingConstants.CENTER);title.setFont(Theme.TITLE);title.setForeground(Theme.TEXT);form.add(title);form.add(new JLabel("Username"));form.add(username);form.add(new JLabel("Password"));form.add(password);JButton login=new JButton("Sign in");login.addActionListener(e->authenticate());form.add(login);add(form);setVisible(true);}
    private void authenticate(){String user=username.getText().trim();String pass=new String(password.getPassword());try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement("SELECT password_hash FROM admins WHERE username=? AND active=1")){s.setString(1,user);try(ResultSet r=s.executeQuery()){if(r.next()&&MessageDigest.isEqual(hash(pass),r.getString(1).getBytes(StandardCharsets.UTF_8))){dispose();new DashboardFrame(user);}else JOptionPane.showMessageDialog(this,"Invalid username or password");}}catch(Exception exception){JOptionPane.showMessageDialog(this,"Database connection failed: "+exception.getMessage());}}
    private byte[] hash(String value)throws Exception{return java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))).getBytes(StandardCharsets.UTF_8);}
}
