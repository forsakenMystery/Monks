/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id3;

import Fetch.Fetch;
import Fetch.Tree;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Hamed Khashehchi
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField coef;
    @FXML
    private Button train;
    @FXML
    private Button test;
    @FXML
    private Button training;
    @FXML
    private Button draw;

    private String trainer;
    private String tester;
    private Tree tree;
    @FXML
    private Button exit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void trainClicked(MouseEvent event) {
        File file = fileChooser("train");
        if (file != null) {
            test.setDisable(false);
            coef.setDisable(false);
            trainer = file.getAbsolutePath();
            System.out.println("trainer = " + trainer);
        }
    }

    private File fileChooser(String s) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Forsaken files (*." + s + ")", "*." + s);
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(ID3.stage);
    }

    @FXML
    private void testClicked(MouseEvent event) {
        File file = fileChooser("test");
        if (file != null) {
            tester = file.getAbsolutePath();
            System.out.println("tester = " + tester);
            training.setDisable(false);
        }
    }

    @FXML
    private void trainingClicked(MouseEvent event) {
        Fetch f = new Fetch(trainer, tester);
        tree = null;
        try {
            tree = new Tree(f, Integer.parseInt(coef.getText()));
        } catch (Exception e) {
            tree = new Tree(f, 0.1);
        } finally {
            tree.traverse(Tree.getRoot());
            draw.setDisable(false);
        }

    }

    @FXML
    private void drawClicked(MouseEvent event) {
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        Draw draw = new Draw(tree);
                        draw.start(new Stage());
                        ID3.stage.close();
                    }
                });
                return null;
            }

        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @FXML
    private void exitClickes(MouseEvent event) {
        System.exit(0);
    }

}
