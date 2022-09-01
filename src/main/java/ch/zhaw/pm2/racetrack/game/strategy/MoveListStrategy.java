package ch.zhaw.pm2.racetrack.game.strategy;

import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType;

import java.util.List;

import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType.MOVE_LIST;

/**
 * The Move List Strategy class determines the next move based on a file containing directions.
 *
 * @author weberph5
 * @version 1.0.0
 */

public class MoveListStrategy implements MoveStrategy {
    private final List<Direction> directionsList;
    private static final int FIRST_ROW_INDEX = 0;

    /**
     * Creates a move list strategy object.
     *
     * @param directionsList a list of directions which was previously loaded
     */

    public MoveListStrategy(List<Direction> directionsList) {
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
        return MOVE_LIST;
    }
}
