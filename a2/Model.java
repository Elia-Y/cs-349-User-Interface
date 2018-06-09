import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.vecmath.*;
import javax.swing.border.BevelBorder;

import java.util.*;

public class Model {
    /** The observers that are watching this model for changes. */
    private ArrayList<Observer> observers;
    private ArrayList<Item> allItems;
    private Item selectItem;
    private ArrayList<Item> allSelected;

    private boolean drawMode;
    private String selectDrawingShape;
    private float selectThickness;  // ignore this first
    private Color selectColor;
    private Color fillColor;

    private double scalar;
    private double rotate;

    private boolean multiMode;

    /**
     * Create a new model.
     */
    public Model() {
        this.observers = new ArrayList<Observer>();
        this.allItems = new ArrayList<Item>();  // all drawn items
        this.allSelected = new ArrayList<Item>();

        this.drawMode = true;
        this.selectDrawingShape = new String("Freedom");
        this.selectThickness = 1;
        this.selectColor = Color.BLACK;
        this.fillColor = Color.WHITE;

        //this.scalar = 0.1
    }

    // This is used to clear the canvas, i.e. select new in edit option!
    public void clearCanvas() {
        this.allItems = new ArrayList<Item>();
    }

    public void setSelectItem(Item update) {
        if (this.multiMode == false) {
            if (this.selectItem != null) {
                this.selectItem.selected = false;
            }
        } else {
            this.allSelected.add(update);
        }
        update.selected = true;
        this.selectItem = update;

    }

    public void setUnselectItem(Item i) {
        i.selected = false;
        if (this.multiMode == false) {
            this.selectItem.selected = false;
            this.selectItem = null;
        } else {
            allSelected.remove(i);
        }

    }

    public Item getSelectItem() {
        return this.selectItem;
    }

    public Item getSelectItem(int i) {
        return this.allSelected.get(i);
    }

    public int getSelectSize() {
        return this.allSelected.size();
    }

    public void clearSelectItem() {
        this.selectItem = null;
    }

    public boolean checkMultiMode() {
        return this.multiMode;
    }

    public void setMultiMode(boolean update) {
        this.multiMode = update;
    }

    public boolean checkDrawMode() {
        if (drawMode == false || multiMode == true) {
            return false;
        }

        return true;
    }

    public void setDrawMode(boolean update) {
        this.drawMode = update;
        //this.multiMode = !update;
        if (this.multiMode == false) {
            if (this.selectItem != null) {
                this.selectItem.selected = false;
                clearSelectItem(); 
            }
        } else {
            for (int i = allSelected.size() - 1; i >= 0; i--) {
                allSelected.get(i).selected = false;
            }
            this.allSelected = new ArrayList<Item>();
            this.multiMode = false;
        }
    }

    public int getSize() {
        return this.allItems.size();
    }

    public Item getItem(int i) {
        return this.allItems.get(i);
    }

    public void addItem(Item i) {
        this.allItems.add(i);
    }

    public void removeAllSelected() {
        for (int i = allSelected.size() - 1; i >= 0; i--) {
                this.allItems.remove(allSelected.get(i));
        }
        this.allSelected = new ArrayList<Item>();
    }

    public void removeItem(Item i) {
        this.allItems.remove(i);
        selectItem = null;
    }

    public void removeItem(int i) {
        this.allItems.remove(i);
        selectItem = null;
    }

    // Get current drawing shape
    public String getDrawingShape() {
        return this.selectDrawingShape;
    }

    // Set drawing shape
    public void setDrawingShape(String update) {
        this.selectDrawingShape = update;
    }

    public Color getDrawingColor() {
        return this.selectColor;
    }

    public void setDrawingColor(Color update) {
        this.selectColor = update;
        if (multiMode == false) {
            if (drawMode == false && selectItem != null) {
                selectItem.strokeColor = update;
            }
        } else {
            for (int i = allSelected.size() - 1; i >= 0; i--) {
                allSelected.get(i).strokeColor = update;
            }
        }
    }

    public Color getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(Color update) {
        this.fillColor = update;
        if (multiMode == false) {
            if (drawMode == false && selectItem != null) {
                selectItem.fillColor = update;
            }
        } else {
            for (int i = allSelected.size() - 1; i >= 0; i--) {
                allSelected.get(i).fillColor = update;
            }
        }

    }

    // Get current drawing thickness
    public float getThickness() {
        return this.selectThickness;

    }

    // Set drawing thickness
    public void setThickness(float update) {
        this.selectThickness = update;

        if (multiMode == false) {
            if (drawMode == false && selectItem != null) {
                this.selectItem.strokeThickness = update; 
            }
        } else {
            for (int i = allSelected.size() - 1; i >= 0; i--) {
                allSelected.get(i).strokeThickness = update;
            }
        }
    }

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }
}
