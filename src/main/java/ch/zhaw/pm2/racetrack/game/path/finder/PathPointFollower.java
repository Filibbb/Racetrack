package ch.zhaw.pm2.racetrack.game.path.finder;

import ch.zhaw.pm2.racetrack.game.Game;
import ch.zhaw.pm2.racetrack.game.path.finder.AbstractPathPointComparator.DecelerateComparator;
import ch.zhaw.pm2.racetrack.game.path.finder.AbstractPathPointComparator.LongDistanceComparator;
import ch.zhaw.pm2.racetrack.game.path.finder.AbstractPathPointComparator.LowVelocityComparator;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.*;
import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction.NONE;
import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction.values;
import static java.lang.Integer.signum;
import static java.util.Arrays.asList;

/**
 * Class containing the logic for a path point follower
 *
 * @author fupat002, abuechi
 * @version 1.0.0
 */
public class PathPointFollower {
    private final PathFinder pathFinder;
    private final Track track;
    private final int currentCarIndex;
    private PositionVector nextDestination;
    private final Game game;

    /**
     * Creates a path point follower object
     *
     * @param pathFinder  - the pathfinder class that calculates the path to follow for this class
     * @param playerIndex - the current players index
     * @param game        - game instance required for utility method
     */
    public PathPointFollower(final PathFinder pathFinder, final int playerIndex, final Game game) {
        this.pathFinder = pathFinder;
        this.track = this.pathFinder.getTrack();
        this.currentCarIndex = playerIndex;
        this.game = game;
        updateNextDestination();
    }

    /**
     * Calculates the direction based on the next position to head to
     *
     * @return the calculated direction where to head next
     */
    public Direction calculateNextMove() {
        PositionVector currentPosition = track.getCarPos(currentCarIndex);
        PositionVector currentVelocity = track.getCarVelocity(currentCarIndex);

        if (currentPosition.equals(nextDestination)) {
            updateNextDestination();
        }

        final MovesMap movesMap = new MovesMap(currentPosition, nextDestination, currentVelocity);
        final Comparator<Direction> comparator = getBestComparatorForCurrentTurn(currentPosition, currentVelocity, movesMap);
        Queue<Direction> nextMoveCandidates = new PriorityQueue<>(values().length, comparator);
        nextMoveCandidates.addAll(asList(values()));

        do {
            Direction candidate = nextMoveCandidates.remove();
            if (!game.willCarCrash(currentCarIndex, movesMap.getPosition(candidate))) {
                return candidate;
            }
        } while (!nextMoveCandidates.isEmpty());
        return NONE;
    }

    private Comparator<Direction> getBestComparatorForCurrentTurn(PositionVector currentPosition, PositionVector currentVelocity, MovesMap potentialMoves) {
        if (isWrongDirection(currentPosition, currentVelocity, nextDestination) || isPastStoppingDistance(add(currentPosition, currentVelocity), currentVelocity, nextDestination)) {
            return new LongDistanceComparator(potentialMoves);
        } else if (currentVelocity.getY() > 1 || currentVelocity.getX() > 1) {
            return new DecelerateComparator(potentialMoves);
        } else {
            return new LowVelocityComparator(potentialMoves);
        }
    }

    private void updateNextDestination() {
        nextDestination = pathFinder.getNextPathFinderPoint();
    }

    private PositionVector getStoppingDistance(PositionVector startVelocity) {
        PositionVector result = new PositionVector();
        for (Axis axis : Axis.values()) {
            int speed = Math.abs(startVelocity.getValueOnAxis(axis));
            int distanceNeeded = speed * (speed + 1) / 2;
            result.setValueOnAxis(axis, distanceNeeded);
        }
        return result;
    }

    private boolean isWrongDirection(final PositionVector position, final PositionVector velocity, final PositionVector goal) {
        for (Axis axis : Axis.values()) {
            int distanceToGoal = goal.getValueOnAxis(axis) - position.getValueOnAxis(axis);
            int velocityComponent = velocity.getValueOnAxis(axis);
            if (distanceToGoal != 0 && velocityComponent != 0 && !samePrefix(distanceToGoal, velocityComponent)) {
                return true;
            }
        }
        return false;
    }

    private boolean samePrefix(int firstNumber, int secondNumber) {
        return signum(firstNumber) == signum(secondNumber);
    }

    private boolean isPastStoppingDistance(final PositionVector position, final PositionVector velocity, final PositionVector goal) {
        PositionVector stoppingVector = getStoppingDistance(velocity);
        for (Axis axis : Axis.values()) {
            int distanceToGoal = Math.abs(goal.getValueOnAxis(axis) - position.getValueOnAxis(axis));
            int stoppingDistance = stoppingVector.getValueOnAxis(axis);
            if (distanceToGoal > stoppingDistance) {
                return true;
            }
        }
        return false;
    }
}

