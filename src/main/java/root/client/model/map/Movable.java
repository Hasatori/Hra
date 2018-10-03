package root.client.model.map;

public interface Movable {

    public boolean tryMoveLeft();

    public boolean tryMoveRight();

    public boolean tryMoveup();

    public boolean tryMoveDown();

    default void switchPositionsWith( MapPart part) {
        Position firstPartPosition = ((MapPart)this).getPosition();
        Position secondPartPosition = part.getPosition();
        part.setPosition(secondPartPosition);
        part.setPosition(firstPartPosition);
    }

}
