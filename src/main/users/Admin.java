package users;

public class Admin extends User{
    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void showInfo() {
        System.out.println("Admin Username: " + getName());
        System.out.println("Email: " + getEmail());
    }
}
