import java.util.ArrayList            ;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

// Class to represent a single Quiz Question
class Question {
    private String questionText;
    private List<String> options;
    private int correctAnswerIndex; // 1-based index (1, 2, 3, 4)

    public Question(String questionText, List<String> options, int correctAnswerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}

public class QuizApplication {
    private List<Question> questions;
    private int score;
    private int totalQuestions;
    private List<String> userAnswersSummary;
    private boolean answered;
    private int selectedAnswer;

    public QuizApplication() {
        questions = new ArrayList<>();
        score = 0;
        userAnswersSummary = new ArrayList<>();
        loadQuestions();
        totalQuestions = questions.size();
    }

    // Requirement 1: Store quiz questions along with multiple-choice options and correct answers
    private void loadQuestions() {
        questions.add(new Question(
            "What is the extension of compiled Java files?",
            List.of(".txt", ".js", ".class", ".java"),
            3
        ));
        questions.add(new Question(
            "Which of the following is not an OOPs concept in Java?",
            List.of("Polymorphism", "Compilation", "Inheritance", "Encapsulation"),
            2
        ));
        questions.add(new Question(
            "Which component is used to compile, debug and execute Java programs?",
            List.of("JDK", "JRE", "JVM", "JIT"),
            1
        ));
    }

    public void startQuiz() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("========================================");
        System.out.println("       WELCOME TO THE JAVA QUIZ!        ");
        System.out.println("========================================");
        System.out.println("You have 10 seconds to answer each question.\n");

        // Loop through each question
        for (int i = 0; i < totalQuestions; i++) {
            Question q = questions.get(i);
            
            // Requirement 3: Present one question at a time with multiple-choice options
            System.out.println("\nQuestion " + (i + 1) + ": " + q.getQuestionText());
            List<String> options = q.getOptions();
            for (int j = 0; j < options.size(); j++) {
                System.out.println((j + 1) + ". " + options.get(j));
            }

            answered = false;
            selectedAnswer = -1;

            // Requirement 2: Implement a timer for each question (10 seconds)
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (!answered) {
                        System.out.println("\n[Time's up! Press Enter to move to the next question]");
                        answered = true;
                    }
                }
            };
            timer.schedule(task, 10000); // 10 seconds limit

            // Requirement 4: Allow users to select an option and submit their answer within time
            System.out.print("Your answer (1-4): ");
            
            // Thread-safe reading / checking mechanism for console input
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < 10000) {
                if (scanner.hasNextInt()) {
                    selectedAnswer = scanner.nextInt();
                    if (selectedAnswer >= 1 && selectedAnswer <= 4) {
                        answered = true;
                        break;
                    } else {
                        System.out.println("Invalid choice range. Enter a number between 1 and 4: ");
                    }
                }
            }

            timer.cancel(); // Cancel timer once answered or timed out

            // Evaluate answer and record summary
            String summaryResult = "";
            if (!answered || selectedAnswer == -1) {
                summaryResult = "Question " + (i + 1) + ": Time Out! (Correct Answer was: " + q.getCorrectAnswerIndex() + ")";
            } else if (selectedAnswer == q.getCorrectAnswerIndex()) {
                // Requirement 5: Score Calculation based on correct answers
                score++;
                summaryResult = "Question " + (i + 1) + ": Correct!";
            } else {
                summaryResult = "Question " + (i + 1) + ": Incorrect. (Your choice: " + selectedAnswer + ", Correct: " + q.getCorrectAnswerIndex() + ")";
            }
            userAnswersSummary.add(summaryResult);
        }

        // Requirement 6 Screen: Display the final score and summary
        displayResultScreen();
        scanner.close();
    }

    // Requirement 6: Result Screen
    private void displayResultScreen() {
        System.out.println("\n========================================");
        System.out.println("             QUIZ RESULTS               ");
        System.out.println("========================================");
        System.out.println("Final Score: " + score + " / " + totalQuestions);
        System.out.println("\nDetailed Summary:");
        for (String result : userAnswersSummary) {
            System.out.println("- " + result);
        }
        System.out.println("========================================");
        System.out.println("Thank you for participating!");
    }

    public static void main(String[] args) {
        QuizApplication quiz = new QuizApplication();
        quiz.startQuiz();
    }
}
