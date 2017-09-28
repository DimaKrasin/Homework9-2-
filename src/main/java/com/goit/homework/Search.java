package com.goit.homework;

import com.alibaba.fastjson.JSON;
import com.goit.homework.entities.Items;
import com.goit.homework.entities.YouTubeObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.*;

public class Search {
    private static String URL = "http://www.youtube.com/embed/";
    private static String AUTO_PLAY = "?autoplay=1";

    public void getStart(String videoId,Pane root) {

        Player(root,videoId);

        Future<HttpResponse<JsonNode>> future = Unirest.get("https://www.googleapis.com/youtube/v3/videos")
                .queryString("part", "snippet")
                .queryString("id", videoId)
                .queryString("type", "video")
                .queryString("key", "AIzaSyD1hlEQi4zd0kZmxYraq8a945sKEf8J7yk")
                .asJsonAsync();

        try {
            String jsonString = future.get().getBody().toString();
            YouTubeObject youTubeObject = JSON.parseObject(jsonString, YouTubeObject.class);

            YouTubeObject YoutubeObject = JSON.parseObject(jsonString, YouTubeObject.class);
            Items[] items = YoutubeObject.getItems();

            Callable<ImageView> callable =  new Callable<ImageView>() {
                @Override
                public ImageView call() throws Exception {
                    try {
                        URL iconUrl = new URL(items[0].getSnippet().getThumbnails().getHigh().getUrl().toString());
                        Image image = new Image(iconUrl.openStream());
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);
                        imageView.setTranslateX(100);
                        imageView.setTranslateY(50);
                        return imageView;
                    } catch (IOException io) {
                        System.out.println("Проблемы с иконкой видео");
                    }
                    return null;
                }
            };

            FutureTask<ImageView> futurePict = new FutureTask<ImageView>(callable);

            Thread thread = new Thread(futurePict);
            thread.start();
            ImageView result = futurePict.get();

            Label VideoTitle = new Label();
            VideoTitle.setText(items[0].getSnippet().getTitle());
            VideoTitle.setTranslateX(220);
            VideoTitle.setTranslateY(50);

            Label ChannelTitle = new Label();
            ChannelTitle.setText(items[0].getSnippet().getChannelTitle());
            ChannelTitle.setTranslateX(220);
            ChannelTitle.setTranslateY(65);

            Label PublishedAt = new Label();
            PublishedAt.setText(items[0].getSnippet().getPublishedAt().getTime().toString());
            PublishedAt.setTranslateX(220);
            PublishedAt.setTranslateY(80);

            root.getChildren().addAll(VideoTitle,ChannelTitle,PublishedAt,result);
        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }

    public void Player(Pane root,String videoId){
        Button viewBut = new Button();
        viewBut.setText("View");
        viewBut.setTranslateX(10);
        viewBut.setTranslateY(80);
        viewBut.setPrefSize(80,10);

        root.getChildren().addAll(viewBut);

        viewBut.setOnAction((event) ->{
            WebView webView = new WebView();
            root.getChildren().addAll(webView);
            webView.getEngine().load(URL+videoId+AUTO_PLAY);
            webView.setPrefSize(580, 390);
            webView.setTranslateX(10);
            webView.setTranslateY(190);
        });
    }
}
