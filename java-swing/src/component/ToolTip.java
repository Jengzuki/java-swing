package component;

import java.awt.Color;

import javax.swing.*;

public class ToolTip {
	 private static void createAndShowGUI() {
	        JFrame frame = new JFrame("HelloWorldSwing");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        JLabel label = new JLabel("Hello World");
	        JToolTip tool = new JToolTip();
	        tool.setBackground(new Color(0, 0, 4));
	        
	        frame.getContentPane().add(label);
	 
	        frame.pack();
	        frame.setVisible(true);
	    }
	 
	    public static void main(String[] args) {
	         javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI();
	            }
	        });
	    }
}
