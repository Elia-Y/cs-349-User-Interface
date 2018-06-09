import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();

        MainView mainView = new MainView(model);
        
        Canvas canvas = new Canvas(model);
        MenuView menuView = new MenuView(model);
        ToolView toolView = new ToolView(model);

        double r_w = mainView.getHeight()*0.05;
		menuView.setPreferredSize(new Dimension(mainView.getWidth(), (int)r_w));
		toolView.setPreferredSize(new Dimension(mainView.getWidth(), (int)r_w+5));
		canvas.setPreferredSize(new Dimension(mainView.getWidth(), mainView.getHeight() - 100));

		// TODO: the interface is not desirable so far, 
		//	need to improve after realizing their functionalities first

		mainView.add(menuView);
		mainView.add(toolView);
		menuView.setToolView(toolView);
		mainView.add(canvas);

		// Add the view to a layout 
		mainView.getContentPane().add(menuView, BorderLayout.NORTH);
		mainView.getContentPane().add(toolView, BorderLayout.CENTER);
		//mainView.getContentPane().add(canvasPanel, BorderLayout.SOUTH);
		mainView.getContentPane().add(canvas, BorderLayout.SOUTH);


		//System.out.println("Interface done!");
		mainView.pack();
    }
}
