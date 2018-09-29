package root.client.model.map;

public class Position {
    public final int column;
    public final int row;

    public Position(int row, int column) {
        this.column = column;
        this.row = row;
    }

    @Override
    public String toString() {
        return "\n" + this.row + "," + this.column + "\n";
    }
}