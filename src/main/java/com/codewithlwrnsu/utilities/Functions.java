package com.codewithlwrnsu.utilities;

import Database.DatabaseController;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static Database.DatabaseController.tables;
import static Database.DatabaseController.activities;
import java.time.LocalDate;

public class Functions {

    DatabaseController data = new DatabaseController();
    Scanner scan = new Scanner(System.in);


    public void addActivities() {
        viewSubjects();
        if(tables.isEmpty()) {
            System.out.println("Empty Subjects!");
        } else {
            System.out.print("Enter Subject to add Activities (0 to exit.): ");
            int num = scan.nextInt();
            if (num > 0 && num <= tables.size()) {
                String name = tables.get(num - 1);
                scan.nextLine();
                System.out.print("Activity Name: ");
                String activityName = scan.nextLine();
                System.out.print("Score: ");
                int activityScore = scan.nextInt();
                LocalDate now = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String today = now.format(formatter);
                data.addActivities(name, activityName, activityScore, today);
            } else if (num  == 0) {
                System.out.println("Exit.");
            } else {
                System.out.println("Invalid Input!");
            }

        }
    }

    public void addSubjects() {
        scan.nextLine();
        System.out.print("Enter name: ");
        String tableName = scan.nextLine();
        data.addTable(tableName);
    }

    public void viewSubjects() {
        for (int i = 0; i < tables.size(); i++) {
            System.out.println((i + 1) + ". " + tables.get(i));
        }
    }

    public void viewActivity() {
            viewSubjects();
            if(tables.isEmpty()) {
                System.out.println("Empty Subjects!");
            } else {
                System.out.print("\nEnter subject to view activites: ");
                int num = scan.nextInt();
                String name = tables.get(num - 1);
                if (activities.get(name).isEmpty()) {
                    System.out.println("No Activities Found!");
                } else {
                    data.viewActivities(name);
                    System.out.println("\nServices: ");
                    System.out.println("\n1. Update Details.");
                    System.out.println("2. Delete Activity.");
                    System.out.println("3. Exit.");
                    System.out.print("\nEnter Choice: ");
                    int status = scan.nextInt();
                    if (status == 1) {

                        System.out.print("What to update: ");
                        int updateStatus = scan.nextInt();
                        scan.nextLine();
                        String nameValue = activities.get(name).get(updateStatus - 1);
                        System.out.print("New Activty Name: ");
                        String updatedName = scan.nextLine();
                        System.out.print("New Score: ");
                        int updatedScore = scan.nextInt();
                        data.updateActivity(name, updatedName, nameValue, updatedScore);

                    } else if (status == 2) {
                        System.out.print("What to delete: ");
                        int deleteStatus = scan.nextInt();
                        String nameValue = activities.get(name).get(deleteStatus - 1);
                        data.deleteActivity(name, nameValue);
                    } else if (status == 3) {
                        System.out.println("Exit.");
                    }else {
                        System.out.println("Wrong Input! Please Try Again!");
                    }
                }
            }
        }
    public void dropSubject() {
        viewSubjects();
        if (tables.isEmpty()) {
            System.out.println("Empty Subjects!");
        } else {
            System.out.print("\nEnter to delete: ");
            int deleteStatus = scan.nextInt();
            String subject = tables.get(deleteStatus - 1);
            data.dropSubject(subject);
        }
    }
}
