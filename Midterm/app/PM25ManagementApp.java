package app;
import management.PM25Management;

import javax.swing.*;
import java.awt.*;

public class PM25ManagementApp extends JFrame {
    public PM25ManagementApp() {
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JButton enterProgramButton = new JButton("Enter Program");
        enterProgramButton.setFont(new Font("Sans Serif", Font.BOLD, 14));
        enterProgramButton.setBackground(Color.GREEN);
        enterProgramButton.setForeground(Color.WHITE);
        enterProgramButton.setToolTipText("Click to enter the PM2.5 management program");
        enterProgramButton.addActionListener(e -> {
            new PM25Management().setVisible(true);
            this.dispose();
        });

        JButton viewDevelopersButton = new JButton("View Developers");
        viewDevelopersButton.setFont(new Font("Sans Serif", Font.BOLD, 14));
        viewDevelopersButton.setBackground(Color.BLUE);
        viewDevelopersButton.setForeground(Color.WHITE);
        viewDevelopersButton.setToolTipText("Click to view developer information");
        viewDevelopersButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Developed by Your Name", "Developers", JOptionPane.INFORMATION_MESSAGE);
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(enterProgramButton, gbc);

        gbc.gridy = 1;
        panel.add(viewDevelopersButton, gbc);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PM25ManagementApp().setVisible(true));
    }
}
