import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import javax.swing.undo.*;

public class ToolbarView extends JToolBar implements Observer {
    private JButton undo = new JButton("Undo");
    private JButton redo = new JButton("Redo");
    private JButton duplicate = new JButton("Duplicate");

    private DrawingModel model;

    ToolbarView(DrawingModel model) {
        super();
        this.model = model;
        model.addObserver(this);

        setFloatable(false);
        add(undo);
        add(redo);
        add(duplicate);

        ActionListener drawingActionListener = e -> model.setShape(ShapeModel.ShapeType.valueOf(((JButton) e.getSource()).getText()));

        for(ShapeModel.ShapeType mode : ShapeModel.ShapeType.values()) {
            JButton button = new JButton(mode.toString());
            button.addActionListener(drawingActionListener);
            add(button);
        }

        duplicate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                duplicateSelected();

            }
        });

        // controllers for undo menu item
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.undo();
            }
        });
        // controller for redo menu item
        redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.redo();
            }
        });

        this.update(null, null);
    }

    private void duplicateSelected() {
        if (model.selected == false) {
            return;
        }

        ShapeModel lastSelect = model.selectedShape;
        ShapeModel newSelect = new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) model.selectedShape.a, (Point) model.selectedShape.b);
        
        UndoableEdit undoableEdit = new AbstractUndoableEdit() {
            public void redo() throws CannotRedoException {
                super.redo();
                model.addShape(newSelect);
                model.selectedShape = newSelect;
                model.notifyObservers();
            }

            public void undo() throws CannotUndoException {
                super.undo();
                model.removeShape(model.selectedShape);
                model.selectedShape = lastSelect;
                model.notifyObservers();
            }
        };

        model.undoManager.addEdit(undoableEdit);
        
        newSelect.rotate = model.selectedShape.rotate;
        newSelect.scaleX = model.selectedShape.scaleX;
        newSelect.scaleY = model.selectedShape.scaleY;
        newSelect.translateX = model.selectedShape.translateX + 10;
        newSelect.translateY = model.selectedShape.translateY + 10;

        model.addShape(newSelect);
        model.selectedShape = newSelect;
        model.updateViews();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (model.canUndo()) {
            undo.setEnabled(true);
        } else {
            undo.setEnabled(false);
        }

        if (model.canRedo()) {
            redo.setEnabled(true);
        } else {
            redo.setEnabled(false);
        }

        if (model.selected) {
            duplicate.setEnabled(true);
        } else {
            duplicate.setEnabled(false);
        }


    }
}
