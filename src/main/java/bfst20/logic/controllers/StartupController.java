package bfst20.logic.controllers;

import bfst20.data.*;
import bfst20.logic.filehandling.FileHandler;
import bfst20.logic.filehandling.Parser;
import bfst20.logic.controllers.interfaces.AddressAPI;
import bfst20.logic.controllers.interfaces.KDTreeAPI;
import bfst20.logic.controllers.interfaces.LinePathAPI;
import bfst20.logic.controllers.interfaces.OSMElementAPI;
import bfst20.logic.services.AddressService;
import bfst20.logic.services.LinePathService;
import bfst20.logic.services.RoutingService;
import bfst20.presentation.AlertHandler;
import bfst20.presentation.View;
import javafx.scene.control.Alert;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

public class StartupController {
    private RoutingController routingController;
    private OSMElementAPI osmElementController;
    private LinePathService linePathService;
    private LinePathAPI linePathController;
    private AddressService addressService;
    private OSMElementData osmElementData;
    private RoutingService routingService;
    private AddressAPI addressController;
    private KDTreeAPI kdTreeController;

    private LinePathData linePathData;
    private FileHandler fileHandler;
    private RoutingData routingData;
    private AddressData addressData;
    private KDTreeData kdTreeData;

    private boolean isBinary = false;

    private Parser parser;

    public StartupController() {
        osmElementData = OSMElementData.getInstance();
        linePathData = LinePathData.getInstance();
        routingData = RoutingData.getInstance();
        addressData = AddressData.getInstance();
        kdTreeData = KDTreeData.getInstance();

        linePathService = LinePathService.getInstance(linePathData);
        addressService = new AddressService(addressData);

        osmElementController = new OSMElementController();
        addressController = new AddressController(addressService);
        kdTreeController = new KDTreeController();

        routingService = new RoutingService(routingData);
        routingController = new RoutingController(routingService, addressService);
        linePathController = new LinePathController(linePathService);

        parser = new Parser(osmElementController, addressController);

        fileHandler = new FileHandler.Builder()
                .withParser(parser)
                .withKDTreeAPI(kdTreeController)
                .withRoutingAPI(routingController)
                .withAddressAPI(addressController)
                .withLinePathAPI(linePathController)
                .withOSMElementAPI(osmElementController)
                .build();

    }

    public void initialize(View view, File file) {
        loadFile(file);
        if (!isBinary) {
            linePathController.init(osmElementController.fetchAllWays(), osmElementController.fetchAllNodes(),
                    osmElementController.fetchAllRelations());

            osmElementController.clearNodeData();
            routingController.buildRoutingGraph(linePathController.fetchHighways());
            kdTreeController.constructKDTrees(linePathController.fetchLinePathData(), osmElementController.fetchBoundsData());
        }

        view.initialize(isBinary);
        System.gc();
    }

    private void loadFile(File file) {
        clearExistingData();
        try {
            isBinary = file.getName().endsWith(".bin") ? true : false;

            fileHandler.load(file);
        } catch (IOException ioException) {
            AlertHandler.alertOK(Alert.AlertType.ERROR, "Invalid xml data, exiting.", true);
            System.exit(1);
        } catch (XMLStreamException xmlStreamException) {
            AlertHandler.alertOK(Alert.AlertType.ERROR, "Invalid xml data, exiting.", true);
            System.exit(1);
        } catch (NullPointerException exception) {
            AlertHandler.alertOK(Alert.AlertType.ERROR, "Error finding file, exiting.", true);
            System.exit(1);
        }
    }

    public void generateBinary() {
        clearAllNonBinData();
        try {
            fileHandler.generateBinary();
        } catch (Exception e) {
            AlertHandler.alertOK(Alert.AlertType.ERROR, "Error generating binary, please retry.", false);
        }
    }

    private void clearExistingData() {
        linePathData.clearMotorways();
        linePathData.clearCoastlines();
        kdTreeData.clearData();

        linePathController.clearLinePathData();
        addressData.clearData();

        InterestPointData interestPointData = InterestPointData.getInstance();
        interestPointData.clearData();

        osmElementController.clearNodeData();
    }

    private void clearAllNonBinData() {
        osmElementController.clearNodeData();
        linePathController.clearLinePathData();
        routingData.clearData();
    }
}