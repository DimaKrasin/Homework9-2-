package com.goit.homework;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UI extends Application{

    public static void getReady(){
        launch();
    }

    public void start(Stage stage){
        Pane root = new Pane();
        Scene scene = new Scene(root,600,600);

        TextField textField = new TextField();
        textField.setTranslateX(10);
        textField.setTranslateY(10);
        textField.setPrefWidth(580);
        textField.setPrefHeight(15);
        textField.setText("UCkyDCHkb3M");

        Button searchBut = new Button();
        searchBut.setText("search");
        searchBut.setTranslateX(10);
        searchBut.setTranslateY(50);
        searchBut.setPrefSize(80,10);

        searchBut.setOnAction((event) ->{
                Search search = new Search();
                search.getStart(textField.getText().toString(),root);
            });


        root.getChildren().addAll(textField,searchBut);

        stage.setScene(scene);
        stage.show();
    }
}
