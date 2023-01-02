package cellular_automaton;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellularAutomaton1D implements ICellularAutomaton {
  private String rule;
  private String state;
  private int height;
  private int width;

  public CellularAutomaton1D(String rule, String initialState) {
    this.rule = rule;
    this.width = initialState.length();
    this.height = 1;
    state = initialState;
  }

  public void advance() {
    String nextState = "";
    for (int i = 0; i < state.length(); i++) {
      int mid = state.charAt(i) - '0';
      int left = state.charAt((i - 1 + state.length()) % state.length()) - '0';
      int right = state.charAt((i + 1 + state.length()) % state.length()) - '0';
      int out = left * 4 + mid * 2 + right;
      nextState += rule.charAt(rule.length() - 1 - out);
    }
    state = nextState;
  }

  public void display(GridPane grid) {
    grid.getChildren().clear();
    int gapSize = 5;
    grid.setHgap(gapSize);
    grid.setVgap(gapSize);
    NumberBinding rectsAreaSize =
        Bindings.min(grid.heightProperty(), grid.widthProperty()).subtract(80);

    for (int i = 0; i < state.length(); i++) {
      int cell = state.charAt(i) - '0';
      Color color = cell == 0 ? Color.RED : Color.BLUE;
      Rectangle rect = new Rectangle();
      rect.setFill(color);

      rect.heightProperty().bind(
          rectsAreaSize.divide(state.length()).subtract(gapSize));
      rect.widthProperty().bind(rect.heightProperty());
      grid.add(rect, i, 0);
    }
  }
}
