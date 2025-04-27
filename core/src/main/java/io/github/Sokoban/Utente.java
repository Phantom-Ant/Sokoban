package io.github.Sokoban;

public class Utente{
    public int id;
    public String nome;
    public String cognome;

    public Utente(){}
    public Utente(int id, String nome, String cognome){
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
    }
    public Utente(String nome, String cognome){
        this.nome = nome;
        this.cognome = cognome;
    }

    @Override
    public String toString(){
        String str="";
        str += "Il mio id: "+id;
        str += "\nIl mio nome: "+nome;
        str += "\nIl mio cognome: "+cognome+"\n";
        return str;
    }

}
