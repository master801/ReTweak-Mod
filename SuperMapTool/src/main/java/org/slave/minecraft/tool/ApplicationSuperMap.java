package org.slave.minecraft.tool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Created by Master on 9/9/2019 at 1:50 PM
 *
 * @author Master
 */
public final class ApplicationSuperMap extends Application {

    private static ResourceBundle resourceBundle;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        ApplicationSuperMap.resourceBundle = ResourceBundle.getBundle("SuperMap");

        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationSuperMap.class.getResource("/scenes/SuperMap.fxml"), ApplicationSuperMap.resourceBundle);
        Parent root = fxmlLoader.load();
        ControllerSuperMap controller = fxmlLoader.getController();

        Scene scene = new Scene(root);

        primaryStage.setTitle(ApplicationSuperMap.resourceBundle == null ? "Super Map Tool" : ApplicationSuperMap.resourceBundle.getString("key.title.label"));
        controller.init();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
