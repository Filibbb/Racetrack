package ch.zhaw.pm2.racetrack.game.util;

/**
 * Holds a position (vector to x,y-position of the car on the track grid)
 * or a velocity vector (x,y-components of the velocity vector of a car).
 * <p>
 * Created by mach 21.01.2020
 */
public final class PositionVector {
    private int x; // horizontal component (position / velocity)
    private int y; // vertical component (position / velocity)

    /**
     * Enum representing a direction on the track grid.
     * Also representing the possible acceleration values.
     */
    public enum Direction {
        NONE(new PositionVector(0, 0)),
        UP_LEFT(new PositionVector(-1, -1)),
        UP(new PositionVector(0, -1)),
        UP_RIGHT(new PositionVector(1, -1)),
        LEFT(new PositionVector(-1, 0)),
        RIGHT(new PositionVector(1, 0)),
        DOWN_LEFT(new PositionVector(-1, 1)),
        DOWN(new PositionVector(0, 1)),
        DOWN_RIGHT(new PositionVector(1, 1));

        private final PositionVector vector;

        Direction(PositionVector vector) {
            this.vector = vector;
        }

        /**
         * Utility method to convert a int vector selection into a direction
         *
         * @param vectorSelection the selection choice made by the user
         * @return a direction object according to the user selection
         */
        public static Direction convertVectorSelectionToDirection(final int vectorSelection) {
            if (vectorSelection < values().length && vectorSelection >= 0) {
                return values()[vectorSelection];
            }
            return null;
        }

        public PositionVector getVector() {
            return vector;
        }

        /**
         * Translates a position into a direction or none
         *
         * @param position position object that should be converted into a direction
         * @return a direction object matching the given position based on their vector coordinates
         */
        public static Direction translateToDirection(PositionVector position) {
            for (Direction direction : values()) {
                if (position.equals(direction.getVector())) {
                    return direction;
                }
            }
            return NONE;
        }
    }

    /**
     * Adds two PositionVectors (e.g. car position and velocity vector or two velocity vectors).
     *
     * @param vectorA A position or velocity vector
     * @param vectorB A position or velocity vector
     * @return A new PositionVector holding the result of the addition. If both
     * arguments are positions (not velocity), the result is mathematically
     * correct but meaningless.
     */
    public static PositionVector add(final PositionVector vectorA, final PositionVector vectorB) {
        return new PositionVector(vectorA.getX() + vectorB.getX(), vectorA.getY() + vectorB.getY());
    }

    /**
     * Subtracts two PositionVectors (e.g. car position and velocity vector or two velocity vectors).
     *
     * @param vectorA A position or velocity vector
     * @param vectorB A position or velocity vector
     * @return A new PositionVector holding the result of the addition. If both
     * arguments are positions (not velocity), the result is mathematically
     * correct but meaningless.
     */
    public static PositionVector subtract(final PositionVector vectorA, final PositionVector vectorB) {
        return new PositionVector(vectorA.getX() - vectorB.getX(), vectorA.getY() - vectorB.getY());
    }

    /**
     * Calculates the scalar product (Skalarprodukt) of two 2D vectors. The scalar product
     * multiplies the lengths of the parallel components of the vectors.
     *
     * @param vectorA A position or velocity vector
     * @param vectorB A position or velocity vector
     * @return The scalar product (vectorA * vectorB). Since vectorA and
     * vectorB are PositionVectors, which hold only integer coordinates,
     * the resulting scalar product is an integer.
     */
    public static int scalarProduct(final PositionVector vectorA, final PositionVector vectorB) {
        return (vectorA.getY() * vectorB.getY()) + (vectorA.getX() * vectorB.getX());
    }

    public enum Axis {
        Y, X
    }

    public PositionVector(final int x, final int y) {
        this.y = y;
        this.x = x;
    }

    /**
     * Copy constructor
     *
     * @param other
     */
    public PositionVector(final PositionVector other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    public PositionVector() {
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValueOnAxis(Axis axis) {
        if (axis == Axis.Y) {
            return y;
        } else {
            return x;
        }
    }

    public void setValueOnAxis(Axis axis, int value) {
        if (axis == Axis.Y) {
            y = value;
        } else {
            x = value;
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof PositionVector)) throw new ClassCastException();
        final PositionVector otherPositionVector = (PositionVector) other;
        return this.y == otherPositionVector.getY() && this.x == otherPositionVector.getX();
    }

    @Override
    public int hashCode() {
        return this.x ^ this.y;
    }

    @Override
    public String toString() {
        return "(X:" + this.x + ", Y:" + this.y + ")";
    }
}
