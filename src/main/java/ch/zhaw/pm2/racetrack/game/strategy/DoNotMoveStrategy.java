package ch.zhaw.pm2.racetrack.game.strategy;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;
import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction.NONE;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType.DO_NOT_MOVE;

/**
 * Class containing the logic for the do not move strategy
 *
 * @author wartmnic
 * @version 1.0.0
 */
public class DoNotMoveStrategy implements MoveStrategy {

    /**
     * Always returns the direction NONE so the Car with this Strategy assigned never moves.
     *
     * @return always returns NONE
     */
    @Override
    public Direction nextMove() {
        return NONE;
    }

    @Override
    public Direction nextMove(int acceleration) {
        //NOP
        throw new UnsupportedOperationException("CALLED NOP IMPLEMENTATION");
    }

    @Override
    public StrategyType getMovementStrategyType() {
        return DO_NOT_MOVE;
    }
}
