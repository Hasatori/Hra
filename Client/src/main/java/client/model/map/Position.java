package client.model.map;

public class Position {
    private final int column;
    private final int row;

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
            return column == position.getColumn() && row == position.getRow();
        }
        return false;
    }

    public int getColumn(){
        return column;
    }

    public int getRow(){
        return row;
    }
}
