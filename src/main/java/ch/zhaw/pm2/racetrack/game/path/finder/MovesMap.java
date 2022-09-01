package ch.zhaw.pm2.racetrack.game.path.finder;

import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;

import java.util.HashMap;
import java.util.Map;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction.values;
import static ch.zhaw.pm2.racetrack.game.util.PositionVector.add;

/**
 * Utility class for storing move data
 *
 * @author fupat002
 * @version 1.0.0
 */
public class MovesMap {
    private static final int VELOCITY_INDEX = 0;
    private static final int POSITION_INDEX = 1;

    private final PositionVector startPosition;
    private final PositionVector positionGoal;
    private final Map<Direction, PositionVector[]> moves = new HashMap<>();

    /**
     * Utility class holding all possible moves as well as current velocity and goal
     */
    public MovesMap(final PositionVector start, final PositionVector goal, final PositionVector baseVelocity) {
        startPosition = start;
        positionGoal = goal;
        for (Direction direction : values()) {
            PositionVector[] newEntry = new PositionVector[2];
            newEntry[VELOCITY_INDEX] = add(baseVelocity, direction.getVector());
            newEntry[POSITION_INDEX] = add(startPosition, newEntry[VELOCITY_INDEX]);
            moves.put(direction, newEntry);
        }
    }

    public PositionVector getStartPoint() {
        return startPosition;
    }

    public PositionVector getNextDestination() {
        return positionGoal;
    }

    public PositionVector getPosition(final Direction direction) {
        PositionVector[] entry = moves.get(direction);
        return (entry == null) ? null : entry[POSITION_INDEX];
    }

    public PositionVector getVelocity(final Direction direction) {
        PositionVector[] entry = moves.get(direction);
        return (entry == null) ? null : entry[VELOCITY_INDEX];
    }
}
