package bfst20.logic.interfaces;

import java.util.List;

import bfst20.logic.Type;
import bfst20.logic.entities.Way;
import javafx.scene.canvas.GraphicsContext;

public interface Drawable{
    public void draw(GraphicsContext gc);
    public Type getType();

}