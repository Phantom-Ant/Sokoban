package io.github.Sokoban;

public class Level {
    int id;
    String name;
    String data;

    String publish_date;
    String publisher;
    Level(){}
    Level(int id, String name, String data, String publish_date,String publisher){
        this.id=id;
        this.name = name;
        this.data = data;
        this.publish_date = publish_date;
        this.publisher = publisher;
    }
    Level(String name, String data){
        this.name = name;
        this.data = data;
    }
}
