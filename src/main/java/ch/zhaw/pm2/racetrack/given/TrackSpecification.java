package ch.zhaw.pm2.racetrack.given;

import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType;

/**
 * This interface specifies stuff we use to test Racetrack for grading. It shall not be altered!
 */
public interface TrackSpecification {
    SpaceType getSpaceType(PositionVector position);

    int getCarCount();

    CarSpecification getCar(int carIndex);

    char getCarId(int carIndex);

    PositionVector getCarPos(int carIndex);

    PositionVector getCarVelocity(int carIndex);

    char getCharAtPosition(int y, int x, SpaceType currentSpace);

    String toString();
}
