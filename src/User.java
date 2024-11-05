public class User{
    private String id;
    private String password;
    private String role;
    private String name;

    public User(){
        password = "password";
    }

    public User(String id, String password, String role, String name){
        this.id = id;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public User (String id, String name){
        this.id = id;
        this.name = name;
    }

    public void setPassword(String pass){
        password = pass;
    }

    public String getId() {
        return id;
    }

    public String getRole(){
        return role;
    }

    public boolean checkPassword(String pass) {
        return this.password.equals(pass);
    }

    public String getName() {
        return name;
    }

}

