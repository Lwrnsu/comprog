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


    public void addScore() {
        viewSubjects();
        if(tables.isEmpty()) {
            System.out.println("Empty Subjects!");
        } else {
            try {
                System.out.print("Enter Subject to add Score (0 to exit.): ");
                int num = scan.nextInt();
                if (num > 0 && num <= tables.size()) {
                    String name = tables.get(num - 1);
                    if (activities.get(name).isEmpty()) {
                        System.out.print("Exam: ");
                        int examScore = scan.nextInt();
                        System.out.print("Total Exam: ");
                        int totalExamScore = scan.nextInt();
                        System.out.print("Activity: ");
                        int activityScore = scan.nextInt();
                        System.out.print("Total Activity: ");
                        int totalActivityScore = scan.nextInt();
                        System.out.print("Performance Task: ");
                        int pTaskScore = scan.nextInt();
                        System.out.print("Total Performance Task: ");
                        int totalPTaskScore = scan.nextInt();
                        LocalDate now = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String today = now.format(formatter);
                        data.addActivities(name, examScore, totalExamScore, activityScore, totalActivityScore, pTaskScore, totalPTaskScore,today);
                    } else {
                        System.out.println("Already has an score, please update to \"View Scores\"");
                    }
                } else if (num  == 0) {
                    System.out.println("Exit.");
                }  else {
                    System.out.println("Invalid Input!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong Input! Please Try Again!");
                scan.nextLine();
            }
        }
    }

    public void addSubjects() {
        try {
            scan.nextLine();
            System.out.print("Enter name: ");
            String tableName = scan.nextLine();
            data.addTable(tableName);
            System.out.print("Enter Exam Rubrics (No %): ");
            int rExam = scan.nextInt();
            System.out.print("Enter Activity Rubrics (No %): ");
            int rAct = scan.nextInt();
            System.out.print("Enter Performance Task Rubrics (No %): ");
            int rPTask = scan.nextInt();
            System.out.println("Subject Added: " + tableName);
            data.addSubject(tableName, rExam, rAct, rPTask);
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewSubjects() {
        for (int i = 0; i < tables.size(); i++) {
            System.out.println((i + 1) + ". " + tables.get(i));
        }
    }

    public void viewScores() {
            viewSubjects();
            if(tables.isEmpty()) {
                System.out.println("Empty Subjects!");
            } else {
                try {
                    System.out.print("\nEnter subject to view Scores: ");
                    int num = scan.nextInt();
                    String name = tables.get(num - 1);
                    if (activities.get(name).isEmpty()) {
                        System.out.println("No Scores Found!");
                    } else {
                        data.viewNames(name);
                        System.out.println("\nServices: ");
                        System.out.println("\n1. Update Details.");
                        System.out.println("2. Exit.");
                        System.out.print("\nEnter Choice: ");
                        int status = scan.nextInt();
                        if (status == 1) {
                            System.out.print("New Exam Score: ");
                            int updatedExamScore = scan.nextInt();
                            System.out.print("New Activity Score: ");
                            int updatedActivityScore = scan.nextInt();
                            System.out.print("New Performance Task Score: ");
                            int updatedPTaskScore = scan.nextInt();
                            data.updateActivity(name, updatedExamScore, updatedActivityScore, updatedPTaskScore);

                        } else if (status == 2) {
                            System.out.println("Exit.");
                        }else {
                            System.out.println("Wrong Input! Please Try Again!");
                        }
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Wrong Input! Please Try Again!");
                    scan.nextLine();
                }
            }
        }

    public void dropSubject() {
        viewSubjects();
        if (tables.isEmpty()) {
            System.out.println("Empty Subjects!");
        } else {
            try {
                System.out.print("\nEnter to delete (0 - Exit.): ");
                int deleteStatus = scan.nextInt();
                if (deleteStatus == 0) {
                    System.out.println("Exit.");
                } else if (deleteStatus > tables.size()){
                    System.out.println("No such subject!");
                } else {
                    String subject = tables.get(deleteStatus - 1);
                    data.dropSubject(subject);
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong Input! Please Try Again!");
                scan.nextLine();
            }
        }
    }

    public void calcGrades() {
        viewSubjects();
        if (tables.isEmpty()) {
            System.out.println("Empty Subjects!");
        } else {
            try {
                System.out.print("Enter subject: ");
                int num = scan.nextInt();
                String tableName = tables.get(num - 1);
                data.calculate(tableName);
            } catch (InputMismatchException e) {
                System.out.println("Wrong Input! Please Try Again!");
                scan.nextLine();
            }
        }
    }
}
