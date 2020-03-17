package org.slave.minecraft.tool;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by Master on 3/15/2020 at 11:58 PM
 *
 * @author Master
 */
public final class ControllerSuperMap {

    private static final FileChooser.ExtensionFilter EXTENSION_FILTER_ZIP = new FileChooser.ExtensionFilter("Zip/Jar", "zip", "jar");
    private static final FileChooser.ExtensionFilter EXTENSION_FILTER_SUPER = new FileChooser.ExtensionFilter("Super", "super");

    //Input
    @FXML
    private TextField textFieldInput;

    //Output
    @FXML
    private TextField textFieldOutput;

    private SuperMap superMap = null;

    private FileChooser fileChooserZip = null, fileChooserSuper = null;

    void init() {
        superMap = new SuperMap();
        superMap.init();

        fileChooserZip = new FileChooser();
        fileChooserZip.initialDirectoryProperty().set(new File("."));
        fileChooserZip.selectedExtensionFilterProperty().setValue(EXTENSION_FILTER_ZIP);

        fileChooserSuper = new FileChooser();
        fileChooserSuper.initialDirectoryProperty().set(new File("."));
        fileChooserSuper.selectedExtensionFilterProperty().setValue(EXTENSION_FILTER_SUPER);
    }

    @FXML
    private void onButtonInput() {
        File chosen = fileChooserZip.showOpenDialog(null);
        if (chosen != null) {
            textFieldInput.textProperty().setValue(chosen.getAbsolutePath());
            superMap.setFileInput(chosen);
        }
    }

    @FXML
    private void onButtonProcess() {
        superMap.process();
    }

    @FXML
    private void onButtonOutput() {
        File chosen = fileChooserSuper.showSaveDialog(null);
        if (chosen != null) {
            textFieldOutput.textProperty().setValue(chosen.getAbsolutePath());
            superMap.setFileOutput(chosen);
        }
    }

}
