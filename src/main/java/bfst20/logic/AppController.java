package bfst20.logic;

import java.util.List;
import java.util.Map;

import bfst20.data.Model;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.presentation.Parser;

public class AppController{

    Model model;
    Parser parser;

    public AppController(){
        model = Model.getInstance();
        parser = Parser.getInstance();
    }

    public void addRelationToModel(Relation relation){
        model.addRelation(relation);
    }

    public void setBoundsOnModel(float minlat, float maxlon, float maxlat, float minlon){
        model.setBounds(minlat, maxlon, maxlat, minlon);
    }

    public void addNodeToModel(long id, Node node){
        model.addToNodeMap(id, node);
    }

    public void addWayToModel(Way way){
        model.addWay(way);
    }

    public List<Way> getOSMWaysFromModel(){
        return model.getOSMWays();
    }

    public Map<Long, Node> getOSMNodesFromModel(){
        return model.getOSMNodes();
    }

    public List<Relation> getOSMRelationsFromModel() {
        return model.getOSMRelations();
    }
}