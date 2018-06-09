import javax.swing.*;
import java.awt.*;

public class A3Basic extends JPanel {

    A3Basic() {
        DrawingModel model = new DrawingModel();

        setLayout(new BorderLayout());
        add(new ToolbarView(model), BorderLayout.PAGE_START);
        add(new CanvasView(model), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CS349 W18 A3");
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new A3Basic());
        frame.setVisible(true);
    }
}
