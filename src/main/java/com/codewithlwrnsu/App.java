package com.codewithlwrnsu;

import Database.DatabaseController;
import com.codewithlwrnsu.utilities.Functions;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App
{
    public static void main( String[] args ) throws SQLException {
        DatabaseController db = new DatabaseController();
        Functions func = new Functions();
        Scanner scan = new Scanner(System.in);

        db.connect();

        int serviceOption = 0;

        do{
            try {
                System.out.println("\nWelcome to Score Tracker - DEMO.\n");
                System.out.println("Select Services:");
                System.out.println("1. Add Score.");
                System.out.println("2. View Scores.");
                System.out.println("3. Calculate Grade per Subject.");
                System.out.println("4. Add Subject.");
                System.out.println("5. Drop Subject.");
                System.out.println("6. Exit.");
                System.out.print("\nEnter your choice: ");
                serviceOption = scan.nextInt();

                switch(serviceOption) {
                    case 1:
                        func.addScore();
                        break;
                    case 2:
                        func.viewScores();
                        break;
                    case 3:
                        System.out.println("3");
                        break;
                    case 4:
                        func.addSubjects();
                        break;
                    case 5:
                        func.dropSubject();
                        break;
                    case 6:
                        System.out.println("Thank you, Goodluck GCeans!");
                        break;
                    default:
                        System.out.println("Wrong Input! Please Try Again!");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong Input! Please Try Again!");
                scan.nextLine();
            }
        } while(serviceOption != 6);
    }
}
