package ch.zhaw.pm2.racetrack.game.path.finder;

import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;

import java.util.Comparator;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.scalarProduct;
import static ch.zhaw.pm2.racetrack.game.util.PositionVector.subtract;

/**
 * Abstract base for comparators to compare path points depending on use case
 *
 * @author abuechi, fupat002
 * @version 1.0.0
 */
public abstract class AbstractPathPointComparator implements Comparator<Direction> {
    private final MovesMap potentialMoves;
    private final PositionVector distanceToNextDestination;

    /**
     * Creates an abstract path point comparator
     *
     * @param movesMap the possible moves to compare with for the best option
     */
    protected AbstractPathPointComparator(MovesMap movesMap) {
        this.potentialMoves = movesMap;
        distanceToNextDestination = subtract(this.potentialMoves.getNextDestination(), this.potentialMoves.getStartPoint());
    }

    /**
     * Retrieves the MovesMap with potential moves
     *
     * @return potential moves
     */
    protected MovesMap getPotentialMoves() {
        return potentialMoves;
    }

    /**
     * Retrieves the distance to the next destination
     *
     * @return the distance between starting point and next destination
     */
    protected PositionVector getDistanceToNextDestination() {
        return distanceToNextDestination;
    }

    /**
     * Comparator to use if distance is far or car has to decelerate fastly
     *
     * @author fupat002, abuechi
     */
    public static class LongDistanceComparator extends AbstractPathPointComparator {

        /**
         * Creates a path point comparator for longer distances
         *
         * @param movesMap the possible moves to compare with for the best option
         */
        public LongDistanceComparator(MovesMap movesMap) {
            super(movesMap);
        }

        @Override
        public int compare(Direction o1, Direction o2) {
            PositionVector velocity1 = getPotentialMoves().getVelocity(o1);
            PositionVector velocity2 = getPotentialMoves().getVelocity(o2);

            // (0, 0) has the lowest priority therefore prioritize others.
            if (velocity1.getY() == 0 && velocity1.getX() == 0) {
                return velocity2.getY() == 0 && velocity2.getX() == 0 ? 0 : 1;
            } else if (velocity2.getY() == 0 && velocity2.getX() == 0) {
                return -1;
            } else {
                int dotProduct = scalarProduct(getDistanceToNextDestination(), velocity2) - scalarProduct(getDistanceToNextDestination(), velocity1);

                if (dotProduct == 0) {
                    return (velocity2.getY() * velocity2.getY() + velocity2.getX() * velocity2.getX()) - (velocity1.getY() * velocity1.getY() + velocity1.getX() * velocity1.getX());
                } else if (dotProduct < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

    /**
     * Comparator to use if car has to decelerate fastly if it would crash otherwise
     *
     * @author fupat002, abuechi
     */
    public static class DecelerateComparator extends AbstractPathPointComparator {

        /**
         * Creates a path point comparator for deceleration distances
         *
         * @param movesMap the possible moves to compare with for the best option
         */
        public DecelerateComparator(MovesMap movesMap) {
            super(movesMap);
        }

        @Override
        public int compare(Direction o1, Direction o2) {
            PositionVector velocity1 = getPotentialMoves().getVelocity(o1);
            PositionVector velocity2 = getPotentialMoves().getVelocity(o2);
            int comparison = (Math.abs(velocity1.getY()) + Math.abs(velocity1.getX())) - (Math.abs(velocity2.getY()) + Math.abs(velocity2.getX()));
            if (comparison != 0) {
                return comparison;
            } else {
                int secondComparison = PositionVector.scalarProduct(getDistanceToNextDestination(), velocity2) - PositionVector.scalarProduct(getDistanceToNextDestination(), velocity1);
                if (secondComparison < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

    /**
     * Comparator to use if car needs to drive with the lowest possible velocity
     *
     * @author fupat002, abuechi
     */
    public static class LowVelocityComparator extends AbstractPathPointComparator {

        /**
         * Creates a path point comparator for moving minimal velocity
         *
         * @param movesMap the possible moves to compare with for the best option
         */
        public LowVelocityComparator(MovesMap movesMap) {
            super(movesMap);
        }

        @Override
        public int compare(Direction o1, Direction o2) {
            PositionVector velocity1 = getPotentialMoves().getVelocity(o1);
            PositionVector velocity2 = getPotentialMoves().getVelocity(o2);

            boolean isSlow1 = velocity1.getY() <= 1 && velocity1.getX() <= 1;
            boolean isSlow2 = velocity2.getY() <= 1 && velocity2.getX() <= 1;

            if (isSlow1 != isSlow2) {
                return isSlow1 ? -1 : 1;
            }

            int comparison = scalarProduct(getDistanceToNextDestination(), velocity2) - scalarProduct(getDistanceToNextDestination(), velocity1);
            if (comparison < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
