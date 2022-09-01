package ch.zhaw.pm2.racetrack.game.strategy;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;

public interface MoveStrategy {
    /**
     * Provides a user specific movement strategy implementation used while playing the game
     *
     * @return the direction provided by the move strategy
     */
    Direction nextMove();

    /**
     * Provides a user specific movement strategy implementation used while playing the games with a given acceleration input.
     * Overloaded method of {@link MoveStrategy#nextMove()}
     *
     * @return the direction provided by the movestrategy
     */
    Direction nextMove(int acceleration);

    /**
     * Provides the movement strategy type (enum value)
     *
     * @return Movement strategy type
     */
    StrategyType getMovementStrategyType();
}
