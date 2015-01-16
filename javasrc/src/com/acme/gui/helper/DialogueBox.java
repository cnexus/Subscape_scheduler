package com.acme.gui.helper;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * UNUSED
 */

public class DialogueBox{
	public static final int YES_OPTION = JOptionPane.YES_OPTION;
	public static final int NO_OPTION = JOptionPane.NO_OPTION;

	private static final int LINE_LENGTH_MAX = 70;
	public static final String WINDOWS_LF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	public static final String METAL_LF = "javax.swing.plaf.metal.MetalLookAndFeel";
	public static final String MOTIF_LF = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	private JFrame parent;

	public DialogueBox(JFrame c){
		try {
			UIManager.setLookAndFeel(WINDOWS_LF);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		parent = c;
	}

	private static String createSpacedMessage(String message){
		String[] lines = message.split("(?<=\\G.{"+LINE_LENGTH_MAX+"})");
		StringBuffer sb = new StringBuffer(message.length() + lines.length*2);
		for(String s : lines){
			if(s != null && s.length() != 0)
				sb.append(s.trim() + "\n");
		}

		return sb.toString();
	}

	public void showErrorDialogue(String message){
		int n1 = (int)(Math.random() * 8 + 1);
		int n2 = (int)(Math.random() * 8 + 1);
		int n3 = (int)(Math.random() * 8 + 1);
		String title = "Error code " + n1 + "" + n2 + "" + n3; //+ "" + n3;
		String msg = createSpacedMessage(message);
		JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.ERROR_MESSAGE);
	}

	public String showInputDialogue(String inputMessage){
		//String title = "Please enter: ";
		String msg = createSpacedMessage(inputMessage);
		String answer = JOptionPane.showInputDialog(parent, msg);
		return answer;
	}

	public void showInformationDialogue(String info){
		//String title = "Information.";
		String msg = createSpacedMessage(info);
		JOptionPane.showMessageDialog(parent, msg);
	}

	public int showConfirmDialogue(String text){
		String msg = createSpacedMessage(text);
		int result = JOptionPane.showConfirmDialog(parent, msg);
		return result;
	}

	public void showWarningDialogue(String warning){
		String msg = DialogueBox.createSpacedMessage(warning);
		JOptionPane.showMessageDialog(parent, msg, "Warning", JOptionPane.WARNING_MESSAGE);
		System.out.println(parent.getBounds());
	}

	public static void main(String[] args){
		JFrame frame = new JFrame("Sub Scheduler v1.3");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(300, 50, 700, 600);
		DialogueBox dm = new DialogueBox(frame);
		frame.setVisible(true);
		dm.showErrorDialogue("ERROR. ERROR. ERROR. ERROR. ERROR.");
		dm.showWarningDialogue("YOUR COMPUTER IS ABOUT TO CRASH");
		dm.showInformationDialogue("JESSICA IS WEIRD");
		dm.showConfirmDialogue("Do you like cats?");
		String answer = dm.showInputDialogue("What is your age?");
		System.out.println("Your age is " + answer);
	}
}

/** THE CODE BELOW WORKED BUT WAS GETTING HARDER TO IMPLEMENT
 *  I DECIDED TO GO WITH JOptionPane INSTEAD AS IT IS EASIER
 *  TO DO CREATE AN ERROR DIALOGUE WITH JOptionPane
 *  
 *  
 *  4/19/10 - Last day that code below was updated. Also day
 *  that above code was written and perfected.
 */

//public class ErrorWindow extends JFrame{
//	protected static final long serialVersionUID = 1L;
//	protected static final int LINE_LENGTH_MAX = 60; //Only 55 characters per line allowed
//	protected static final int LINE_HEIGHT = 25; //One string line takes up about 25 pixels height
//	protected static final int LINE_WIDTH = 400; //One string line takes up about 360 pixels in width
//	protected static int BUTTON_OFFSET = 75;
//	protected static int SPACER = 40;
//	private static final String windows  = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
//	private String buttonText = "Close";
//	protected JFrame parent;
//	public static Font MESSAGE_FONT = new Font("Dialog", Font.PLAIN, 13);
//	private static OptionPaneDemo pane = new OptionPaneDemo();
//	
//	
//	public ErrorWindow(String message, JFrame frame) throws Exception{
//		super("ERROR");
//		parent = frame;
//
//		UIManager.setLookAndFeel(windows);
////		int num = (int)(Math.random() * 8 + 1);
////		int num2 = (int) (Math.random() * 7 + 2);
////		this.setTitle(num + "" + num2 + "" + num);
//		
//		JFrame.setDefaultLookAndFeelDecorated(true);
//		JButton okButton = new JButton("Close");
//		JPanel ePanel = new JPanel();
//		
//		String[] lines = message.split("(?<=\\G.{"+LINE_LENGTH_MAX+"})");
//		StringBuffer all = new StringBuffer();
//		all.append("<html>");
//		for(int i = 0; i < lines.length; i++){
//			all.append(lines[i]);
//			if(i < lines.length-1){
//				all.append("<br>");
//			}else{
//				all.append("<br> <br>");
//			}
//		}
//		all.append("</html>");
//		//System.out.println("sb = \n" + all);
//		//ePanel.setLayout(new BorderLayout());
//		
//		JLabel msg = new JLabel(all.toString());
//		System.out.println("Default font = " + msg.getFont() + " style = " + msg.getFont().getFontName());
//		msg.setFont(MESSAGE_FONT);
//		okButton.addActionListener(new CustomListener(this));
//		okButton.setSize(100, 40);
//		ePanel.add(msg, BorderLayout.NORTH);
//		JPanel spacerPanel = new JPanel();
//		spacerPanel.setSize(LINE_WIDTH, SPACER);
//		spacerPanel.setBackground(Color.BLACK);
//		//ePanel.add(spacerPanel, BorderLayout.SOUTH);
//		ePanel.add(okButton, BorderLayout.SOUTH);
//		add(ePanel);
//		setBounds(100,100, LINE_WIDTH, LINE_HEIGHT*lines.length + BUTTON_OFFSET);
//		setVisible(true);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		
//		//Set the icon
//		Image icon = Toolkit.getDefaultToolkit().createImage("images/error1.gif");
//		this.setIconImage(icon);
//	}
//	
//	
//	public static void main(String [] args) throws Exception{
//		ErrorWindow ew = new ErrorWindow("You did not fill any checkboxes for Ms. Teacher and her subjects hahaha cool. RAndom words to fill this space and test out the thingy so yeah. Let's add more random words to fill this space and test out the thingy.", null);
//		//ew.setVisible(true);
//		//System.out.println("width = " + ew.getWidth());
//	}
//	
//	public void disable(){
//		setVisible(false);
//		System.exit(1);
//	}
//	
//	public String getText(){
//		return buttonText;
//	}
//}
//
//class CustomListener implements ActionListener{
//	ErrorWindow parent;
//	
//	public CustomListener(ErrorWindow f){
//		parent = f;
//	}
//	
//	public void actionPerformed(ActionEvent e) {
//		JButton src = (JButton) e.getSource();
//		if(parent.getText().equals(src.getText())){
//			parent.disable();
//		}
//	}
//}
