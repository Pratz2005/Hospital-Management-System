package UserMenu;

import java.util.Scanner;

public abstract class AbstractMenu implements Menu {
    protected static final Scanner sc = new Scanner(System.in);

    // Common functionality for displaying the logout option
    protected void displayLogoutOption(int x) {
        System.out.println("("+x+") Logout");
    }

    // Abstract method for displaying specific menu content
    public abstract void displayMenu();
}
