package projectofmine;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        Scanner command = new Scanner(System.in);
        APIGateway api = new APIGateway();

        System.out.println("Search Movie: ");
        boolean running = true;

        while(running){

            String comm = command.nextLine();
            api.APIRequestMovie(comm);
        }
        command.close();
    }
}