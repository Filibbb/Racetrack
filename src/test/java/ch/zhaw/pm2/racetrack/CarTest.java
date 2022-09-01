package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.strategy.DoNotMoveStrategy;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction.DOWN_RIGHT;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType.DO_NOT_MOVE;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests the car class
 *
 * @author wartmnic
 */
public class CarTest {

    private Car car;
    private final PositionVector initialCarPosition = new PositionVector(22, 22);
    private final PositionVector alternativePosition = new PositionVector(24, 24);
    private final PositionVector initialCarVelocity = new PositionVector(0, 0);
    private final PositionVector alternativeVelocity = new PositionVector(1, 1);

    /**
     * Sets up a Car Object with id: 'a' and Position =  initialCarPosition;
     */
    @BeforeEach
    public void setUp() {
        car = new Car('a', initialCarPosition);
    }

    /**
     * Tests both car.getPosition and car.setPosition
     */
    @Test
    public void positionAccessorsTest() {
        assertEquals(car.getPosition(), initialCarPosition);
        car.setPosition(alternativePosition);
        assertEquals(car.getPosition(), alternativePosition);
    }

    /**
     * Tests, if car.getPosition() and car.getVelocity() return the expected Values
     * accelerates the Car
     * Verifies the expected value of Velocity
     * compares expected Value of the car after a move (car.nextPosition) to the actual Position of the Car after a
     * move (car.getPosition)
     */
    @Test
    public void accelerationAndMovementTest() {
        assertEquals(car.getPosition(), initialCarPosition);
        assertEquals(car.getVelocity(), initialCarVelocity);
        car.accelerate(DOWN_RIGHT);
        assertEquals(car.getVelocity(), alternativeVelocity);
        PositionVector nextPosition = car.nextPosition();
        car.move();
        assertEquals(nextPosition, car.getPosition());
    }

    /**
     * Checks bot car.getMoveStrategy and car.setMoveStrategy
     */
    @Test
    public void movementStrategyAccessorsTest() {
        car.setMoveStrategy(new DoNotMoveStrategy());
        assertTrue(car.getMoveStrategy() instanceof DoNotMoveStrategy);
        assertEquals(DO_NOT_MOVE, car.getMoveStrategy().getMovementStrategyType());
    }

    /**
     * Checks bot car.getFinishLineCrossings and car.setFinishLineCrossings
     */
    @Test
    public void finishLineCrossingTest() {
        assertEquals(0, car.getFinishLineCrossings());
        car.setFinishLineCrossings(1);
        assertEquals(1, car.getFinishLineCrossings());
    }

    /**
     * Verifies expeced car.isCrashed return values before and after crashing the car, using car.crash
     */
    @Test
    public void crashTest() {
        assertFalse(car.isCrashed());
        car.crash();
        assertTrue(car.isCrashed());
    }
}
