package model;

public class User {
    private int id;
    private String name;
    private String surname;
    private String nick;
    private String password;

    public User(int id, String name, String surname, String nick, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.nick = nick;
        this.password = password;
    }

    // Gettery - niezbędne dla kontrolerów Login i UserPanel
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getNick() { return nick; }
    public String getPassword() { return password; }

    // Settery - przydatne przy edycji profilu
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setNick(String nick) { this.nick = nick; }
    public void setPassword(String password) { this.password = password; }
}