package client.model.map;

/**
 * Wrapper class for better indication of position of an object in a map.
 */
public class Position {
    private final int column;
    private final int row;

    /**
     * @param row row number
     * @param column column number
     */
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

    /**
     * Getter for column number
     * @return column number
     */
    public int getColumn(){
        return column;
    }

    /**
     * Getter for row number
     * @return row number
     */
    public int getRow(){
        return row;
    }
}
