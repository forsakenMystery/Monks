/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id3;

import Fetch.Fetch;
import Fetch.Node;
import Fetch.Tree;
import javafx.stage.Stage;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Draw extends Application {

    Tree tree;

    public Draw() {

    }

    public Draw(Tree tree) {
        this.tree = tree;
    }

    @Override
    public void start(Stage stage) {
        Group g = new Group();
        final Scene scene = new Scene(g, 800, 600);
        System.out.println("scene.getWidth() = " + scene.getWidth());
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                Platform.runLater(() -> {
                    double changes = (scene.getHeight() / (tree.getTreeHeight(Tree.getRoot())));
                    DrawTree(g, scene.getWidth() / 2, changes / 2, changes, changes, Tree.getRoot());
                });
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
//        DrawTree(g, 0, scene.getWidth(), 0, (scene.getHeight() / (tree.getheight(BinaryTree.getRoot()))), BinaryTree.getRoot());

        stage.setScene(scene);
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                Task task = new Task<Void>() {
                    @Override
                    public Void call() throws Exception {
                        Platform.runLater(() -> {
                            g.getChildren().remove(0, g.getChildren().size() - 1);
                            double changes = (scene.getHeight() / (tree.getTreeHeight(Tree.getRoot())));
                            DrawTree(g, scene.getWidth() / 2, changes / 2, changes, changes, Tree.getRoot());
                        });
                        return null;
                    }
                };
                Thread th = new Thread(task);
                th.setDaemon(true);
                th.start();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                Task task = new Task<Void>() {
                    @Override
                    public Void call() throws Exception {
                        Platform.runLater(() -> {
                            g.getChildren().remove(0, g.getChildren().size() - 1);
                            double changes = (scene.getHeight() / (tree.getTreeHeight(Tree.getRoot())));
                            DrawTree(g, scene.getWidth() / 2, changes / 2, changes, changes, Tree.getRoot());
                        });
                        return null;
                    }
                };
                Thread th = new Thread(task);
                th.setDaemon(true);
                th.start();
            }
        });
        stage.setTitle("Decision Tree");
        stage.setOnCloseRequest((WindowEvent we) -> {
            Platform.runLater(() -> {
                ID3 dt = new ID3();
                try {
                    dt.start(new Stage());
                } catch (Exception ex) {
                    Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
        stage.show();
    }

    public void DrawTree(Group g, double x1, double y1, double y2, double change, Node node) {
        System.out.println("node = " + node);
        System.out.println("x1 = " + x1);
        System.out.println("y1 = " + y1);
        System.out.println("y2 = " + y2);

        String data = node.getFeature() + "";
        if (data.equals("-1")) {
            data = "FOUND";
        }
        if (!node.isLeaf()) {
            int i = 0;
            for (Node n : node.getChildren()) {
                Text tt = new Text(n.getFeature() + "");
                tt.setFont(new javafx.scene.text.Font("Tahoma", 10));
                double x2 = 0;
                if (i < node.getChildren().size() / 2) {
                    x2 = (i++ - 2) * 150 + x1;
                } else {
                    x2 = i++ * 150 + x1;
                }
                System.out.println("x2 = " + x2);
                Line l = new Line(x1, y1, x2, y2);
                l.setFill(Color.BLUE);
                g.getChildren().add(l);
                Fetch ff=new Fetch();
                Text text = new Text((x1 + x2) / 2 + 10, (y1 + y2) / 2, ff.getFeatures()[node.getFeature()][i-1]+"");
                text.setRotate(Math.atan((y2 - y1) / (x2 - x1)));
                text.setFont(new javafx.scene.text.Font("Tahoma", 10));
                g.getChildren().add(text);
                DrawTree(g, x2, y2, y2 + change, change, n);
            }
        }
        Ellipse el = new Ellipse(0, 0, 20, 20);
        el.setFill(Color.RED);

        Button b = new Button(data);
        b.setShape(el);
        b.setLayoutX(x1 - 20);
        b.setLayoutY(y1 - 20);
        if (node.isLeaf()) {
            b.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            b.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        b.setOnMouseEntered((MouseEvent event) -> {
            b.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        });
        b.setOnMouseExited((MouseEvent event) -> {
            if (node.isLeaf()) {
                b.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                b.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });
        b.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("node.getCanHave() = " + node.getClasses());
            Alert a = new Alert(Alert.AlertType.INFORMATION, "You can be these monks :\n" + node.getClasses(), ButtonType.OK);
            a.setTitle("Your monk");
            a.showAndWait();
        });
        g.getChildren().add(b);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
