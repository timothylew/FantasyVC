package utility;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

//this class contains methods to make the GUI making process easier in settings aesthetic properties of components
//many of these methods are generically typed to only accept objects that have JComponent as a super class
//this lets us pass in a combination of different JComponent child classes (buttons, labels, ect)
//thought this would be a good was to show you guys how generics can be used

//another fun fact, many of these methods take a parameter such as "JComponent... components
//this means multiple JComponents can be passed into the method, which will come in handy
public class AppearanceSettings {
	
	//JTextArea settings
	public static void setTextComponents(JTextArea... components){
		
		for (JTextArea component : components){
			component.setEditable(false);
			component.setLineWrap(true);
			component.setWrapStyleWord(true);
			component.setOpaque(true);
		}
	}
	
	//sets background color of JComponents passed in
	@SafeVarargs
	public static <T extends JComponent>void setBackground(Color backGround, T ... components ){
		
		for (T component : components) component.setBackground(backGround);
	}
	
	//sets the layout to boxlayout for each panel
	public static void setBoxLayout(int axis, JPanel... panels){
		
		for (JPanel panel : panels) panel.setLayout(new BoxLayout(panel, axis));
	}
	
	//iterates through the components and adds them to the panel with glue in between
	@SafeVarargs
	public static <T extends JComponent> void addGlue(JPanel panel, int axis, Boolean addInitialGlue, T... components){
		
		if (addInitialGlue) panel.add(axis == BoxLayout.PAGE_AXIS? Box.createVerticalGlue() : Box.createHorizontalGlue());
			
		for (T component : components){
			panel.add(component);
			panel.add(axis == BoxLayout.PAGE_AXIS? Box.createVerticalGlue() : Box.createHorizontalGlue());
		}
	}
	
	//sets foreground of supplied JComponents
	@SafeVarargs
	public static <T extends JComponent>void setForeground(Color foreGround, T ... components ){
		
		for (T component : components) component.setForeground(foreGround);
	}
	
	//sets font of supplied JComponents
	@SafeVarargs
	public static <T extends JComponent>void setFont(Font font, T ... components ){
		
		for (T component : components) component.setFont(font);
	}
	
	//centers the text for the passed in labels
	@SafeVarargs
	public static void setTextAlignment(JLabel ... labels ){
		
		for (JLabel label : labels) label.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public static void setCenterAlignment(JComponent ... labels ){
		
		for (JComponent label : labels) label.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	//sets components opaque
	@SafeVarargs
	public static <T extends JComponent>void setOpaque(T ... components ){
		
		for (T component : components) component.setOpaque(true);
	}
	
	//sets visibility of supplied components
	@SafeVarargs
	public static <T extends JComponent>void setVisible(Boolean visible, T ... components ){
		
		for (T component : components) component.setVisible(visible);
	}
	
	//sets size of components
	@SafeVarargs
	public static <T extends JComponent> void setSize(int x, int y, T ...components){
		
		Dimension dim = new Dimension(x, y);
		
		for (T component: components){
			component.setPreferredSize(dim);
		}
	}
	
	//sets properties of a JSlider
	public static void setSliders(int min, int max, int currentValue, int majorTickSpacing, Font font, JSlider slider){

		slider.setFont(font);
		slider.setValue(currentValue);
		slider.setMaximum(max);
		slider.setMinimum(min);
		slider.setMajorTickSpacing(majorTickSpacing);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}
	
	//sets the minor tick spacing on a JSlider
	public static void setMinTickSliders(int minorTickSpacing, JSlider setBetSlider){
	
		setBetSlider.setMinorTickSpacing(minorTickSpacing);
	}
	
	//method to remove the border on buttons
	public static void unSetBorderOnButtons(JButton ... buttons){
		
		for (JButton button: buttons) button.setBorderPainted(false);
	}
}
