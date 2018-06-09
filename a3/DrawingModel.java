import java.util.*;
import java.util.List;
import javax.swing.undo.*;

public class DrawingModel extends Observable {

    private List<ShapeModel> shapes = new ArrayList<>();

    ShapeModel.ShapeType shapeType = ShapeModel.ShapeType.Rectangle;
    ShapeModel selectedShape;
    boolean selected = false;  // if selected is true, then selectedShape cannot be null!

    UndoManager undoManager = new UndoManager();

    public ShapeModel.ShapeType getShape() {
        return shapeType;
    }

    public void setShape(ShapeModel.ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public DrawingModel() { }

    public List<ShapeModel> getShapes() {
        return Collections.unmodifiableList(shapes);
    }

    public void addShape(ShapeModel shape) {
        this.shapes.add(shape);
        this.setChanged();
        this.notifyObservers();
    }

    public void removeShape(ShapeModel shape) {
        this.shapes.remove(shape);
        this.setChanged();
        this.notifyObservers();
    }

    public void updateViews() {
        setChanged();
        notifyObservers();
    }

    // undo and redo methods
    // - - - - - - - - - - - - - -

    public void undo() {
        if (canUndo()) 
            undoManager.undo();
    }
    public void redo() {
        if (canRedo())
            undoManager.redo();
    }
    public boolean canUndo() {
        return undoManager.canUndo();
    }
    public boolean canRedo() {
        return undoManager.canRedo();
    }
}
