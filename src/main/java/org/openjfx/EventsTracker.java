package org.openjfx;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EventsTracker extends Application {
    private static Calendar calendar;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        calendar = new Calendar(new SQLiteStorage());
    }

//    public static void main(String[] args) {
//        calendar = new Calendar(new SQLiteStorage());
//        calendar.start();
//        menu();
//    }

//    public static void menu() {
//        System.out.println("Menu");
//        System.out.println("0. Quit");
//        System.out.println("1. Get nearest events");
//        System.out.println("2. Get all events");
//        System.out.println("3. Write new event");
//        System.out.println("4. Update event");
//        System.out.println("5. Delete specific event");
//        System.out.println("6. Delete all events");
//        System.out.print("Your choice: ");
//
//        Scanner scanner = new Scanner(System.in);
//        scanner.useDelimiter("\n");
//        String date = null, time = null, description = null;
//        Integer notifyBefore = null;
//        int id;
//        try {
//            int choice = scanner.nextInt();
//            switch (choice) {
//                case 0:
//                    calendar.stopThread();
//                    calendar.interrupt();
//                    break;
//
//                case 1, 2:
//                    Event[] events;
//                    if (choice == 1) {
//                        events = calendar.getNearestEvents();
//                    } else {
//                        events = calendar.getEvents();
//                    }
//
//                    if (events.length == 0) {
//                        System.out.println("No events");
//                    }
//                    else {
//                        for (Event event: events) {
//                            System.out.println(event.toString());
//                        }
//                    }
//                    menu();
//                    break;
//
//                case 3:
//                    System.out.print("Enter date (as yyyy-mm-dd): ");
//                    date = scanner.next(Pattern.compile("\\d{4}-\\d{2}-\\d{2}"));
//                    System.out.print("Enter time (as hh:mm): ");
//                    time = scanner.next(Pattern.compile("\\d{2}:\\d{2}"));
//                    System.out.print("Enter description: ");
//                    description = scanner.next();
//                    System.out.print("Enter notify_before: ");
//                    notifyBefore = scanner.nextInt();
//                    calendar.writeEvent(date + " " + time, description, notifyBefore);
//
//                    menu();
//                    break;
//
//                case 4:
//                    System.out.print("Enter ID: ");
//                    id = scanner.nextInt();
//                    System.out.print("Do you want to update description? [y/N]: ");
//                    if (scanner.next().equals("y")){
//                        System.out.print("Enter new description: ");
//                        description = scanner.next();
//                    }
//                    System.out.print("Do you want to update datetime? [y/N]: ");
//                    if (scanner.next().equals("y")){
//                        System.out.print("Enter new date (as yyyy-mm-dd): ");
//                        date = scanner.next(Pattern.compile("\\d{4}-\\d{2}-\\d{2}"));
//                        System.out.print("Enter new time (as hh:mm): ");
//                        time = scanner.next(Pattern.compile("\\d{2}:\\d{2}"));
//                    }
//                    System.out.print("Do you want to update notify_before? [y/N]: ");
//                    if (scanner.next().equals("y")){
//                        System.out.print("Enter new notify_before: ");
//                        notifyBefore = scanner.nextInt();
//                    }
//                    if (description != null || date != null || notifyBefore != null) {
//                        calendar.updateEvent(id, date != null ? date + " " + time : null, description, notifyBefore);
//                    }
//                    menu();
//                    break;
//
//                case 5:
//                    System.out.print("Enter ID of the event: ");
//                    id = scanner.nextInt();
//                    if (calendar.deleteEvent(id) == 1){
//                        System.out.println("Event was deleted");
//                    } else {
//                        System.out.println("No such an event");
//                    }
//
//                    menu();
//                    break;
//
//                case 6:
//                    System.out.print("Are you sure you want to delete all events? [y/N]: ");
//                    if (scanner.next().equals("y")){
//                        calendar.deleteEvents();
//                        System.out.println("Events were deleted");
//                    } else {
//                        System.out.println("Okay, I won't delete anything");
//                    }
//
//                    menu();
//                    break;
//
//                default:
//                    menu();
//            }
//        } catch (InputMismatchException e) {
//            System.out.println("Wrong choice");
//            menu();
//        }
//    }
}
