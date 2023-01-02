package cellular_automaton;

import cellular_automaton.CellularAutomaton1D;
import cellular_automaton.GameOfLife;
import cellular_automaton.ICellularAutomaton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class Selector {
  private static ICellularAutomaton _1dSelectFile() throws IOException {
    Scanner stdinScanner = new Scanner(System.in);
    int choice;
    do {
      System.out.println("Enter the file to select: ");
      System.out.println("1) rule126");
      System.out.println("2) rule30");
      choice = stdinScanner.nextInt();
    } while (choice != 1 && choice != 2);

    String filename = "src/main/resources/";
    if (choice == 1) {
      filename += "rule126.txt";
    } else {
      filename += "rule30.txt";
    }

    File file = new File(filename);
    Scanner scanner = new Scanner(file);
    String rule = scanner.nextLine();
    String initialState = scanner.nextLine();
    scanner.close();
    return new CellularAutomaton1D(rule, initialState);
  }

  private static ICellularAutomaton _1dSelectStdin() {
    Scanner scanner = new Scanner(System.in);
    int rule;
    do {
      System.out.println("Enter the rule integer: ");
      rule = scanner.nextInt();
    } while (rule > 255 || rule < 0);

    String initialState;
    boolean containsValidChars;
    scanner.nextLine(); // clears new line from stdin buffer
    do {
      System.out.println("Enter the initial state: ");
      initialState = scanner.nextLine();
    } while (!containsZerosAndOnes(initialState));
    String ruleStr =
        String.format("%8s", Integer.toBinaryString(rule)).replace(' ', '0');
    return new CellularAutomaton1D(ruleStr, initialState);
  }

  private static boolean containsZerosAndOnes(String initialState) {
    boolean containsValidChars = true;
    for (int i = 0; i < initialState.length(); i++) {
      char c = initialState.charAt(i);
      if (c != '0' && c != '1') {
        containsValidChars = false;
      }
    }
    return containsValidChars;
  }

  private static ICellularAutomaton _1dSelect() throws IOException {
    Scanner scanner = new Scanner(System.in);
    int choice;
    do {
      System.out.println("How do you want to input your automaton: ");
      System.out.println("1) From file");
      System.out.println("2) From stdin");
      choice = scanner.nextInt();
    } while (choice != 1 && choice != 2);
    switch (choice) {
    case 1:
      return _1dSelectFile();
    default:
      return _1dSelectStdin();
    }
  }

  private static ICellularAutomaton gameOfLifeFile() throws IOException {
    Scanner stdinScanner = new Scanner(System.in);
    int choice;
    do {
      System.out.println("Enter the file to select: ");
      System.out.println("1) blinker");
      System.out.println("2) dies1");
      System.out.println("3) dies2");
      System.out.println("4) glider1");
      System.out.println("5) repeats1");
      System.out.println("6) stable1");
      System.out.println("7) stable2");
      choice = stdinScanner.nextInt();
    } while (choice < 1 && choice > 8);

    String filename = "src/main/resources/";
    if (choice == 1) {
      filename += "blinker.txt";
    } else if (choice == 2) {
      filename += "dies1.txt";
    } else if (choice == 3) {
      filename += "dies2.txt";
    } else if (choice == 4) {
      filename += "glider1.txt";
    } else if (choice == 5) {
      filename += "repeats1.txt";
    } else if (choice == 6) {
      filename += "stable1.txt";
    } else {
      filename += "stable2.txt";
    }

    File file = new File(filename);
    Scanner scanner = new Scanner(file);
    int width = scanner.nextInt();
    int height = scanner.nextInt();
    scanner.nextLine(); // flush stdin buffer
    String initialState = "";
    while (scanner.hasNextLine()) {
      initialState += scanner.nextLine();
    }
    scanner.close();
    return new GameOfLife(width, height, initialState);
  }

  private static ICellularAutomaton gameOfLifeStdin() throws IOException {
    Scanner scanner = new Scanner(System.in);
    int width;
    int height;
    boolean isValid;
    ArrayList<String> list;

    do {
      System.out.println("Enter the width: ");
      width = scanner.nextInt();

      System.out.println("Enter the height: ");
      height = scanner.nextInt();
      scanner.nextLine(); // flush stdin

      list = new ArrayList<String>();
      isValid = true;

      for (int i = 0; i < height; i++) {
        String s = scanner.nextLine();
        if (s.length() != width || !containsZerosAndOnes(s)) {
          isValid = false;
        }
        list.add(s);
      }
    } while (!isValid);

    String initialState = "";
    for (String s : list) {
      initialState += s;
    }
    return new GameOfLife(width, height, initialState);
  }

  private static ICellularAutomaton gameOfLifeSelect() throws IOException {
    Scanner scanner = new Scanner(System.in);
    int choice;
    do {
      System.out.println("How do you want to input your automaton: ");
      System.out.println("1) From file");
      System.out.println("2) From stdin");
      choice = scanner.nextInt();
    } while (choice != 1 && choice != 2);
    switch (choice) {
    case 1:
      return gameOfLifeFile();
    default:
      return gameOfLifeStdin();
    }
  }

  public static ICellularAutomaton selectCa() throws IOException {
    Scanner scanner = new Scanner(System.in);
    int choice;
    do {
      System.out.println("Which cellular automaton do you want to view:");
      System.out.println("1) 1D Cellular Automaton");
      System.out.println("2) Game of Life");
      System.out.println("3) Langton's loop");
      choice = scanner.nextInt();
    } while (choice != 1 && choice != 2 && choice != 3);
    switch (choice) {
    case 1:
      return _1dSelect();
    case 2:
      return gameOfLifeSelect();
    default:
      return LangtonAutomaton.fromFile("src/main/resources/rule_table.txt",
                                       "src/main/resources/init_config.txt");
    }
  }
}

public class App extends Application {
  @Override
  public void start(Stage stage) {
    ICellularAutomaton c = null;
    try {
      c = Selector.selectCa();
    } catch (IOException e) {
    }
    final ICellularAutomaton ca = c;

    VBox vbox = new VBox();
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    Button button = new Button("Advance Generation");
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
      public void handle(ActionEvent e) {
        ca.advance();
        ca.display(grid);
      }
    };
    button.setOnAction(event);

    vbox.getChildren().add(grid);
    vbox.setVgrow(grid, Priority.ALWAYS);
    vbox.getChildren().add(button);
    vbox.setAlignment(Pos.CENTER);
    vbox.setMargin(button, new Insets(10));
    ca.display(grid);

    Scene scene = new Scene(vbox, 1200, 960);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String args[]) { launch(args); };
}
