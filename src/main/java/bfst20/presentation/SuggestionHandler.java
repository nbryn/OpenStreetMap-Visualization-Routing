package bfst20.presentation;

import bfst20.data.AddressData;
import bfst20.logic.AppController;
import bfst20.logic.entities.Address;
import bfst20.logic.misc.Vehicle;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.Map;
import java.util.Queue;

public class SuggestionHandler {

    private VBox suggestionsList;
    private TextField textField;
    AppController appController;

    ContextMenu test;

    public SuggestionHandler(AppController appController, TextField textField){
        this.textField = textField;
        this.appController = appController;
    }

    public void show(String text){
        hide();
        AddressData addressData = AddressData.getInstance();

        Queue<Address> addresses = addressData.searchSuggestions(text);

        test = new ContextMenu();

        test.setPrefWidth(400);

        for(int i = 0; i < 10; i++){
            TextFlow entryFlow = new TextFlow(new Text(addresses.poll().toString()));

            CustomMenuItem t1 = new CustomMenuItem(entryFlow, true);

            entryFlow.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                String addressText = addresses.poll().toString();
                appController.setSearchString(addressText);
                textField.setText(addressText);

                test.hide();
            });


            test.getItems().add(t1);
        }


        test.show(textField, Side.BOTTOM, 0, 0);

    }

    public void hide(){
        if(test == null) return;
        test.getItems().clear();
        test.hide();

    }
}
