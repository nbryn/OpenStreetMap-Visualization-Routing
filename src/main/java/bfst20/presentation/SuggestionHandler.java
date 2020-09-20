package bfst20.presentation;

import bfst20.logic.controllers.interfaces.AddressAPI;
import bfst20.logic.entities.Address;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


import java.util.Queue;

public class SuggestionHandler {

    public enum SuggestionEvent {
        SEARCH,
        ADDRESS,
        DESTINATION
    }

    private SuggestionEvent suggestionEvent;
    private View view;
    private AddressAPI addressAPI;
    private TextField textField;
    private ContextMenu cm;
    private boolean space;

    public SuggestionHandler(AddressAPI addressAPI, View view, TextField textField, SuggestionEvent suggestionEvent) {
        this.textField = textField;
        this.addressAPI = addressAPI;
        this.view = view;
        this.suggestionEvent = suggestionEvent;
        setupEvents();
    }

    public void show(String text) {
        hide();

        Queue<Address> addresses = addressAPI.fetchSearchSuggestions(text);
        cm = new ContextMenu();

        cm.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {
                    space = true;
                }
            }
        });

        cm.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {
                    space = false;
                }
            }
        });

        cm.setPrefWidth(400);
        if (addresses.size() <= 0) return;

        for (int i = 0; i < 10; i++) {
            Address address = addresses.poll();
            if (address == null) continue;

            MenuItem t1 = new MenuItem(address.toString());

            t1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (space) return;

                    String addressText = address.toString();
                    if (suggestionEvent == SuggestionEvent.SEARCH) {
                        Address address = addressAPI.findAddress(addressText);

                        if (address != null) {
                            view.setSearchAddress(address);
                        } else {
                            AlertHandler.alertOK(Alert.AlertType.INFORMATION, "Typed address not found!", true);
                        }
                    }
                    textField.setText(addressText);

                    cm.hide();
                }
            });

            cm.getItems().add(t1);
        }

        cm.show(textField, Side.BOTTOM, 0, 0);
    }

    public void hide() {
        if (cm == null) return;
        cm.getItems().clear();
        cm.hide();
        cm = null;
    }

    public void setupEvents() {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.equals("")) {
                    hide();
                    return;
                }

                show(newValue);
            }
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    hide();
                }
            }
        });
    }
}
