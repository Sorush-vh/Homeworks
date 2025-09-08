import view.*;
import model.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        makeAdminsAccount(scanner);
        LoginMenu.run(scanner);
    }

    private static void makeAdminsAccount(Scanner scanner){
        String username=scanner.nextLine();
        String password=scanner.nextLine();
        Admin admin=new Admin(username, password);
    }
}
