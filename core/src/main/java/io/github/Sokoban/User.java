package io.github.Sokoban;

public class User {
    String username;
    String email;
    String password;//to be hashed

    public User(){}
    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString(){
        String str="";
        str += "username: "+username;
        str += "\nemail: "+email;
        str += "\npassword hash: "+password+"\n";
        return str;
    }
}
