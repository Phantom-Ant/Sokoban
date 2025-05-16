package io.github.Sokoban;

public class Score {
    String user;
    int level; //db id
    int time_spent;
    int moves;
    int pushes;

    public Score(){}
    public Score(String user, int time_spent, int moves, int pushes){
        this.user = user;
        this.time_spent = time_spent;
        this.moves = moves;
        this.pushes = pushes;
    }
    public Score(String user, int level, int time_spent, int moves, int pushes){
        this.user = user;
        this.level = level;
        this.time_spent = time_spent;
        this.moves = moves;
        this.pushes = pushes;
    }
}
