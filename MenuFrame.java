import java.awt.FlowLayout;

import javax.swing.*;

public class MenuFrame extends JFrame {
	public MenuFrame() {

        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.PAGE_AXIS));

        JTextField textField = new JTextField(20);
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
