package projectofmine;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws SQLException, TimeoutException {

        Scanner command = new Scanner(System.in);
        APIGateway api = new APIGateway();
        boolean chaos = false;

        System.out.println("Search Movie: ");
        boolean running = true;

        while(running){

            String comm = command.nextLine();
            if(comm.equals("quit")){
                running = false;
            }else if(comm.equals("chaos")){
                chaos = !chaos;
                System.out.println("Chaos mode: " + chaos);
            }else{
                api.APIRequestMovie(comm, chaos);
            }


        }
        command.close();
    }
}