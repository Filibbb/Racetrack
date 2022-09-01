package ch.zhaw.pm2.racetrack.game.strategy;

import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction.convertVectorSelectionToDirection;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType.USER;

/**
 * Class containing the logic for the user move strategy.
 *
 * @author wartmnic
 * @version 1.0.0
 */
public class UserMoveStrategy implements MoveStrategy {

    @Override
    public Direction nextMove() {
        //NOP
        throw new UnsupportedOperationException("CALLED NOP IMPLEMENTATION");
    }

    @Override
    public Direction nextMove(int acceleration) {
        return convertVectorSelectionToDirection(acceleration);
    }

    @Override
    public ConfigSpecification.StrategyType getMovementStrategyType() {
        return USER;
    }
}
