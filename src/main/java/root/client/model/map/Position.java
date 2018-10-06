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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position) {
            Position position = (Position) o;
            if (this.column == position.column && this.row == position.row) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("Object must be of type Position");
    }
}