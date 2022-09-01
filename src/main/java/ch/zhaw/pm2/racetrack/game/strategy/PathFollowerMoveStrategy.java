package ch.zhaw.pm2.racetrack.game.strategy;

import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;

import java.util.List;

import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType.PATH_FOLLOWER;

/**
 * The Path follower move strategy class determines the next move based on a file containing points on a path.
 *
 * @author weberph5
 * @version 1.0.0
 */
public class PathFollowerMoveStrategy implements MoveStrategy {

    private static final int FIRST_ROW_INDEX = 0;
    private final List<Direction> directionsList;

    /**
     * This class contains the logic for the Path Follower Strategy
     *
     * @param directionsList a list of directions to follow move by move.
     * @author weberph5
     */
    public PathFollowerMoveStrategy(List<Direction> directionsList) {
        this.directionsList = directionsList;
    }

    @Override
    public Direction nextMove() {
        if (directionsList.isEmpty()) {
            return Direction.NONE;
        }
        Direction direction = directionsList.get(FIRST_ROW_INDEX);
        directionsList.remove(FIRST_ROW_INDEX);
        return direction;
    }

    @Override
    public Direction nextMove(int acceleration) {
        //NOP
        throw new UnsupportedOperationException("CALLED NOP IMPLEMENTATION");
    }

    @Override
    public StrategyType getMovementStrategyType() {
        return PATH_FOLLOWER;
    }
}
