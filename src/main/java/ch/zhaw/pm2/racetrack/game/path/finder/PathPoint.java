package ch.zhaw.pm2.racetrack.game.path.finder;

import ch.zhaw.pm2.racetrack.game.util.PositionVector;

import java.util.Comparator;

import static java.util.Comparator.comparingInt;

/**
 * Class containing the logic for path points
 *
 * @author fupat002
 * @version 1.0.0
 */
public class PathPoint {
    private final PositionVector position;
    private PathPoint preview;
    private Double totalCost;

    /**
     * Compares pathfinder nodes based on their location on the track.
     */
    public static final Comparator<PathPoint> POSITION_COMPARATOR = comparingInt((PathPoint firstPoint) -> firstPoint.getPosition().getY()).thenComparingInt(firstPoint -> firstPoint.getPosition().getX());

    /**
     * Creates a path point object.
     *
     * @param position  - current position
     * @param preview   - next position
     * @param totalCost - cost for moving there
     */
    public PathPoint(PositionVector position, PathPoint preview, double totalCost) {
        this.position = position;
        this.preview = preview;
        this.totalCost = totalCost;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(final double totalCost) {
        this.totalCost = totalCost;
    }

    public PositionVector getPosition() {
        return position;
    }

    public PathPoint getPrev() {
        return preview;
    }

    public void setPrev(PathPoint preview) {
        this.preview = preview;
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof PathPoint secondNode) {
            return POSITION_COMPARATOR.compare(this, secondNode) == 0;
        } else if (other instanceof PositionVector point) {
            return this.position.equals(point);
        } else {
            throw new ClassCastException();
        }
    }

    @Override
    public int hashCode() {
        return this.position.getX() * this.position.getY();
    }

    @Override
    public String toString() {
        return "Location: " + position.toString() + "; Cost: " + totalCost.toString();
    }
}
