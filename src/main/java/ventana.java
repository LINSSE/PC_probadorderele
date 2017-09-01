import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;

public class ventana  extends JFrame{



	public ventana()
	{
		this.setTitle("Monitor - Probrador de Reles");
		Dimension UserScreen = Toolkit.getDefaultToolkit().getScreenSize();
		int ScreenWidth = (int) UserScreen.getWidth();
		int ScreenHeight = (int) UserScreen.getHeight();
		this.setSize(ScreenWidth, ScreenHeight);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		/**/
		/**/
		/**/
	}
}