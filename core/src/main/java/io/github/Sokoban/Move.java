package io.github.Sokoban;

public class Move {
    int x;
    int y;
    Box box;//TODO consider changing this idk

    public Move(){
    }
    public Move(int x,int y, Box box){
        this.x = x;
        this.y = y;
        this.box = box;
    }

}
