package bfst20.logic.entities;

import java.io.Serializable;
import javafx.scene.paint.Color;

public class SerializableColor implements Serializable
{
    private double red;
    private double green;
    private double blue;
    private double opacity;

    public SerializableColor(Color color)
    {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.opacity = color.getOpacity();
    }

    public Color getFXColor()
    {
        return new Color(red, green, blue, opacity);
    }
}
