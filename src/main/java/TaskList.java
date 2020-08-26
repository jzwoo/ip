import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the list of saved tasks.
 */
public class TaskList {

    private static List<Task> taskList = new ArrayList<>();

    /**
     * Generates an Task list based on the retrieved file of the user's saved tasks.
     * @param file
     * @throws FileNotFoundException If the file does not exist.
     */
    public static void generateList(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            Task task = convertToTask(nextLine);
            taskList.add(task);
        }
    }

    /**
     * Generates a list of tasks.
     * @return A list of the tasks.
     */
    public static List<Task> getList() {
        return taskList;
    }

    /**
     * Adds new tasks to the list of tasks.
     * @param task
     */
    public static void add(Task task) {
        taskList.add(task);
    }

    /**
     * Returns the number of tasks in the list.
     * @return Size of the task list.
     */
    public static int size() {
        return taskList.size();
    }

    /**
     * Returns the task at the specified index.
     * @param index
     * @return Task at the specified index of the TaskList.
     */
    public static Task get(int index) {
        return taskList.get(index);
    }

    public static void delete(int index) {
        taskList.remove(index);
    }

    public static void markDone(int index) {
        taskList.set(index, taskList.get(index).markDone());
    }

    public static void flush() {
        taskList.clear();
    }

    private static Task convertToTask(String line) {
        if (line.startsWith("[T]", 2)) {
            //Is a todo task
            String[] parts = line.split(" ", 2);
            if (line.contains("[✘]")) {
                return new Task(parts[1]);
            } else {
                return new Task(parts[1]).markDone();
            }
        } else if (line.startsWith("[E]", 2)) {
            DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("d MMM yyyy");
            DateTimeFormatter myTimeFormat = DateTimeFormatter.ofPattern("h:mm a");
            //Is a event task
            String[] parts = line.split(" ", 2);
            String[] split = parts[1].split("\\(at:");
            String desc = split[0];
            String timeInfo = split[1].split("\\)")[0];
            if (line.contains("[✘]")) {
                String[] dateTime = timeInfo.trim().split(", ");
                String date = dateTime[1];
                String time = dateTime[2];
                return new Events(desc, LocalDate.parse(date, myDateFormat), LocalTime.parse(time, myTimeFormat));
            } else {
                String[] dateTime = timeInfo.trim().split(", ");
                String date = dateTime[1];
                String time = dateTime[2];
                return new Events(desc, LocalDate.parse(date, myDateFormat), LocalTime.parse(time, myTimeFormat));
            }
        } else {
            DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("d MMM yyyy");
            DateTimeFormatter myTimeFormat = DateTimeFormatter.ofPattern("h:mm a");
            //Is a deadline task
            String[] parts = line.split(" ", 2);
            String[] split = parts[1].split("\\(by:");
            String desc = split[0];
            String timeInfo = split[1].split("\\)")[0];
            if (line.contains("[✘]")) {
                String[] dateTime = timeInfo.trim().split(", ");
                String date = dateTime[1];
                String time = dateTime[2];
                return new Deadlines(desc, LocalDate.parse(date, myDateFormat), LocalTime.parse(time, myTimeFormat));
            } else {
                String[] dateTime = timeInfo.trim().split(", ");
                String date = dateTime[1];
                String time = dateTime[2];
                return new Deadlines(desc, LocalDate.parse(date, myDateFormat), LocalTime.parse(time, myTimeFormat)).markDone();
            }
        }
    }
}
