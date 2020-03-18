package bfst20.logic.drawables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.logic.interfaces.Drawable;
import bfst20.presentation.Parser;

public class DrawableFactory {
    public static Map<Type, List<Drawable>> createDrawables(){
        Map<Type, List<Drawable>> drawables = new HashMap<>();

        Parser parser = Parser.getInstance();
        Map<Long, Way> OSMWays = parser.getOSMWays();
        Map<Long, Node> OSMNodes = parser.getOSMNodes();
        List<Relation> OSMRelations = parser.getOSMRelations();

        for(Map.Entry<Long, Way> entry : OSMWays.entrySet()){

            if(drawables.get(Type.HIGHWAY) == null){
                drawables.put(Type.HIGHWAY, new ArrayList<Drawable>());
            }

            drawables.get(Type.HIGHWAY).add(new LinePath(entry.getValue(),Type.HIGHWAY, OSMNodes));

        }

        return drawables;
    }

}