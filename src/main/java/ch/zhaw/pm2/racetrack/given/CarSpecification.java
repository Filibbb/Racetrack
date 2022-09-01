package ch.zhaw.pm2.racetrack.given;

import ch.zhaw.pm2.racetrack.game.strategy.MoveStrategy;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;

/**
 * This interface specifies stuff we use to test Racetrack for grading. It shall not be altered!
 */
public interface CarSpecification {
    void setPosition(PositionVector position);

    PositionVector nextPosition();

    void accelerate(PositionVector.Direction acceleration);

    void move();

    void crash();

    boolean isCrashed();

    void setMoveStrategy(MoveStrategy moveStrategy);

    MoveStrategy getMoveStrategy();
}
