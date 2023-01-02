package cellular_automaton;
import javafx.scene.layout.GridPane;

public interface ICellularAutomaton {
  public void advance();
  public void display(GridPane grid);
}
