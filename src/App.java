import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.parser.*;

public class App {
    // Initiate variables
    public static Map<String, String> list = new HashMap<String, String>();

    public static void main(String[] args) {
        boolean usingList = true;
        // Usage loop
        do {
            // Get input from user
            displayOptions();
            Scanner entry = new Scanner(System.in);
            String task = entry.nextLine();
            if (task.equals("7")) {
                System.out.println("Goodbye!");
                usingList = false;
            } else if (task.equals("1")) {
                openList();
                displayList();
            } else if (task.equals("2")) {
                addTask();
                displayList();
            } else if (task.equals("3")) {
                completeTask();
            } else if (task.equals("4")) {
                deleteTask();
            } else if (task.equals("5")) {
                displayList();
            } else if (task.equals("6")) {
                saveTasks();
            } else {
                System.out.println("Invalid entry!");
            }
        } while (usingList);
    }

    public static void openList() {
        JSONParser parser = new JSONParser();
        try {
            // Get input from user
            Scanner entry = new Scanner(System.in);
            System.out.println("Where is the list: ");
            String path = entry.nextLine();
            path += ".json";
            // Reading file and adding to list
            Object listObj = parser.parse(new FileReader(path));
            String listString = listObj.toString();
            // Removing extra items from string
            listString = listString.replace(" ", "");
            listString = listString.replace("{", "");
            listString = listString.replace("\"", "");
            listString = listString.replace(":", " ");
            listString = listString.replace(",", " ");
            listString = listString.replace("}", "");
            String[] listArray = listString.split(" ");
            for (int i = 0; i < listArray.length; i += 2) {
                list.put(listArray[i], listArray[i + 1]);
            }
        } catch (ParseException | IOException e) {
            System.out.println("Cannot add empty items.");
        }
    }

    public static void addTask() {
        boolean adding = true;
        // Needed string variable to compare input from user
        String q = "Q";
        do {
            // Get input from user
            Scanner entry = new Scanner(System.in);
            System.out.println("Enter a task to add or Q to finish: ");
            String task = entry.nextLine();
            if (task.length() > 0) {
                if (task.length() == 1) {
                    task = task.toUpperCase();
                }
                if (!(task.equals(q))) {
                    // Add task to list
                    list.put(task, "incomplete");
                } else {
                    adding = false;
                }
            } else {
                System.out.println("Cannot add empty items.");
            }
        } while (adding);
    }

    public static void completeTask() {
        Scanner entry = new Scanner(System.in);
        System.out.println("Completed task: ");
        String task = entry.nextLine();
        if (list.get(task) != null) {
            // Add complete to list task item
            list.put(task, "complete");
        } else {
            System.out.println("Task was not found");
        }
    }

    public static void deleteTask() {
        Scanner entry = new Scanner(System.in);
        System.out.println("Task to delete: ");
        String task = entry.nextLine();
        if (list.get(task) != null) {
            // Delete task from list
            list.remove(task);
        } else {
            System.out.println("Task was not found");
        }
    }

    public static void saveTasks() {
        Scanner entry = new Scanner(System.in);
        System.out.println("Save as: ");
        String name = entry.nextLine();
        // Variable to store the string to save to file
        String saveTasks = "{";
        FileWriter fileWrite;
        try {
            fileWrite = new FileWriter("src/" + name + ".json");
            Set<String> keys = list.keySet();
            // Iterating through key/value pairs
            for (String key : keys) {
                String keyValue = list.get(key).toString();
                keyValue = keyValue.replace("[", "");
                keyValue = keyValue.replace("]", "");
                saveTasks += "\n\"" + key + "\" : \"" + keyValue + "\",";
            }
            saveTasks = saveTasks.substring(0, saveTasks.length() - 1);
            saveTasks += "\n}";
            fileWrite.write(saveTasks);
            fileWrite.close();
        } catch (IOException e1) {
            System.out.println("Something went wrong.");
        }
    }

    public static void displayList() {
        Set<String> keys = list.keySet();
        for (String key : keys) {
            System.out.println(key + " | " + list.get(key));
        }
    }

    public static void displayOptions() {
        System.out.println("\n1. Open task list.");
        System.out.println("2. Add tasks.");
        System.out.println("3. Complete tasks.");
        System.out.println("4. Delete tasks.");
        System.out.println("5. Display.");
        System.out.println("6. Save tasks.");
        System.out.println("7. Exit.");
    }
}
