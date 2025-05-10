import java.util.Scanner;

class Course {
    private String code;
    private String name;
    private int[] teacherScores = new int[100];
    private int[] contentsScores = new int[100];
    private int[] examScores = new int[100];
    private int evaluationCount = 0;

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Adds evaluation scores for the course
     * @param teacher Teacher score (1-5)
     * @param contents Contents score (1-5)
     * @param exam Examination score (1-5)
     */
    public void addEvaluation(int teacher, int contents, int exam) {
        teacherScores[evaluationCount] = teacher;
        contentsScores[evaluationCount] = contents;
        examScores[evaluationCount] = exam;
        evaluationCount++;
    }

    /**
     * Calculates average scores for all categories and total average
     * @return Array of doubles: [teacherAvg, contentsAvg, examAvg, totalAvg]
     */
    public double[] calculateAverages() {
        double teacherSum = 0, contentsSum = 0, examSum = 0;
        for (int i = 0; i < evaluationCount; i++) {
            teacherSum += teacherScores[i];
            contentsSum += contentsScores[i];
            examSum += examScores[i];
        }
        
        double teacherAvg = evaluationCount > 0 ? teacherSum / evaluationCount : 0;
        double contentsAvg = evaluationCount > 0 ? contentsSum / evaluationCount : 0;
        double examAvg = evaluationCount > 0 ? examSum / evaluationCount : 0;
        double totalAvg = (teacherAvg + contentsAvg + examAvg) / 3;
        
        return new double[] {
            Math.round(teacherAvg * 100) / 100.0,
            Math.round(contentsAvg * 100) / 100.0,
            Math.round(examAvg * 100) / 100.0,
            Math.round(totalAvg * 100) / 100.0
        };
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getEvaluationCount() { return evaluationCount; }
    public int[] getTeacherScores() { return teacherScores; }
    public int[] getContentsScores() { return contentsScores; }
    public int[] getExamScores() { return examScores; }
}

class CourseManager {
    private Course[] courses = new Course[10];
    private int courseCount = 0;

    /**
     * Validates course code format (A0000A)
     * @param code Course code to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidCourseCode(String code) {
        return code.matches("[A-Z]\\d{4}[A-Z]");
    }

    /**
     * Adds a new course if code is valid and not duplicate
     * @param code Course code
     * @param name Course name
     * @return true if added successfully, false otherwise
     */
    public boolean addCourse(String code, String name) {
        if (!isValidCourseCode(code) || findCourse(code) != null) {
            return false;
        }
        
        courses[courseCount++] = new Course(code, name);
        return true;
    }

    /**
     * Finds course by code
     * @param code Course code to search
     * @return Course object or null if not found
     */
    public Course findCourse(String code) {
        for (int i = 0; i < courseCount; i++) {
            if (courses[i].getCode().equalsIgnoreCase(code)) {
                return courses[i];
            }
        }
        return null;
    }

    public Course[] getAllCourses() { return courses; }
    public int getCourseCount() { return courseCount; }
}

public class CourseEvaluator {
    private static Scanner scanner = new Scanner(System.in);
    private static CourseManager courseManager = new CourseManager();

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            
            if (choice.equalsIgnoreCase("q")) {
                System.out.println("Exiting program...");
                break;
            }
            
            switch (choice) {
                case "1": registerCourse(); break;
                case "2": registerEvaluation(); break;
                case "3": printEvaluationSummary(); break;
                case "4": printCourseList(false); break;
                case "5": printCourseList(true); break;
                default: System.out.println("Invalid option!");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n----------------------------------");
        System.out.println("# Course Evaluator");
        System.out.println("----------------------------------");
        System.out.println("1. Register new course");
        System.out.println("2. Register evaluation score");
        System.out.println("3. Print evaluation summary for a course");
        System.out.println("4. Print course list (sorted by course code)");
        System.out.println("5. Print course list (sorted by evaluation score)");
        System.out.println("q. End program");
        System.out.print("> Enter your option: ");
    }

    private static void registerCourse() {
        System.out.print("> Enter course code: ");
        String code = scanner.nextLine().trim().toUpperCase();
        System.out.print("> Enter course name: ");
        String name = scanner.nextLine().trim();
        
        if (courseManager.addCourse(code, name)) {
            System.out.printf("Course %s: %s was added!%n", code, name);
        } else {
            System.out.println("Invalid course code or duplicate entry!");
        }
    }

    private static void registerEvaluation() {
        System.out.print("> Enter course code: ");
        Course course = courseManager.findCourse(scanner.nextLine().trim());
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }
        
        System.out.printf("%nEnter your evaluation score for course %s.%n", course.getName());
        int teacher = getValidScore("Teacher");
        int contents = getValidScore("Course contents");
        int exam = getValidScore("Examination");
        
        course.addEvaluation(teacher, contents, exam);
        System.out.println("Your evaluation was registered, thank you!");
    }

    private static int getValidScore(String category) {
        while (true) {
            System.out.printf("> %s: ", category);
            try {
                int score = Integer.parseInt(scanner.nextLine());
                if (score >= 1 && score <= 5) return score;
                System.out.println("Score must be between 1-5!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format!");
            }
        }
    }

    private static void printEvaluationSummary() {
        System.out.print("> Enter course code: ");
        Course course = courseManager.findCourse(scanner.nextLine().trim());
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }
        
        System.out.printf("%nEvaluation history:%n");
        System.out.println("Teacher   Contents   Examination");
        for (int i = 0; i < course.getEvaluationCount(); i++) {
            System.out.printf("%-8d  %-8d  %-8d%n", 
                course.getTeacherScores()[i],
                course.getContentsScores()[i],
                course.getExamScores()[i]
            );
        }
        
        double[] averages = course.calculateAverages();
        System.out.printf("%nNumber of evaluations: %d%n", course.getEvaluationCount());
        System.out.println("\nAverage score");
        System.out.printf("Teacher: %.2f%n", averages[0]);
        System.out.printf("Contents: %.2f%n", averages[1]);
        System.out.printf("Examination: %.2f%n", averages[2]);
        System.out.printf("Total average: %.2f%n", averages[3]);
    }

    private static void printCourseList(boolean sortByScore) {
        Course[] courses = courseManager.getAllCourses();
        int count = courseManager.getCourseCount();
        
        if (sortByScore) {
            for (int i = 0; i < count-1; i++) {
                for (int j = 0; j < count-i-1; j++) {
                    double avg1 = courses[j].calculateAverages()[3];
                    double avg2 = courses[j+1].calculateAverages()[3];
                    if (avg1 < avg2) {
                        Course temp = courses[j];
                        courses[j] = courses[j+1];
                        courses[j+1] = temp;
                    }
                }
            }
        } else {
            for (int i = 0; i < count-1; i++) {
                for (int j = 0; j < count-i-1; j++) {
                    if (courses[j].getCode().compareTo(courses[j+1].getCode()) > 0) {
                        Course temp = courses[j];
                        courses[j] = courses[j+1];
                        courses[j+1] = temp;
                    }
                }
            }
        }
        
        System.out.println("\nCode     Name                           Teacher    Contents   Exam     Average");
        for (int i = 0; i < count; i++) {
            double[] avgs = courses[i].calculateAverages();
            System.out.printf("%-8s %-30s %-9.2f %-9.2f %-8.2f %.2f%n",
                courses[i].getCode(),
                courses[i].getName(),
                avgs[0],
                avgs[1],
                avgs[2],
                avgs[3]
            );
        }
    }
}

