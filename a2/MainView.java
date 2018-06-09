
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class MainView extends JFrame implements Observer {

    private Model model;

    /**
     * Create a new View.
     */
    public MainView(Model model) {
        // Set up the window.
        this.setTitle("CS349 W18 A2");
        this.setResizable(true);
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600); // the default size
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);

        setVisible(true);
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {

    }
}
