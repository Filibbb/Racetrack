package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.Game;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction.*;
import static ch.zhaw.pm2.racetrack.helpers.TestTrackCreationUtil.createChallengeTrack;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests game class
 *
 * @author wartmnic
 */
public class GameTest {

    private Track track;
    private Game game;


    /**
     * Sets up a Track object and a game Object.
     * These objects are stored in the Fields of this Class and used for testing.
     */
    @BeforeEach
    public void setUp() {
        track = new Track(createChallengeTrack());
        game = new Game(track);
    }

    /**
     * At the start of a Game the current Car should always have the Index 0
     * This Test checks, if the expected value is returned by game.getCurrentCarIndex.
     */
    @Test
    public void getCurrentCarIndexTest() {
        assertEquals(0, getCurrentCarIndex());
    }

    /**
     * With the chosen Setup the starting car is supposed to have the ID 'a'.
     * This Test checks, if the expected value is returned by game.getCarId.
     */
    @Test
    public void getCarIdTest() {
        assertEquals('a', game.getCarId(0));
    }

    /**
     * At the start of this Game Setup, we expect the current Car to have the Position X:23 Y:22.
     * An according Position Vector is created and then compared to the return Value of getCarPosition,
     * executed with the currentCarIndex as a Parameter.
     */
    @Test
    public void getCarPositionTest() {
        PositionVector expectedPosition = new PositionVector(24, 22);
        PositionVector returnedPosition = game.getCarPosition(getCurrentCarIndex());
        assertEquals(expectedPosition, returnedPosition);
    }

    /**
     * This Test checks, if hasWinner returns true, after we set a Winner by crossing the finish Line.
     */
    @Test
    public void hasWinnerByFinishLineTest() {
        setUpWinByFinishLine();
        game.doCarTurn(NONE);
        assertTrue(game.hasWinner());
    }

    /**
     * This Test checks, if hasWinner returns true, after letting one of the two cars (in this Setup) crash.
     */
    @Test
    public void hasWinnerByLastAliveTest() {
        setUpWinByLastAlive();
        game.doCarTurn(NONE);
        assertTrue(game.hasWinner());
    }

    /**
     * This test checks, if the returned Winner is the expected player.
     */
    @Test
    public void getWinnerTest() {
        setUpWinByFinishLine();
        game.doCarTurn(NONE);
        assertEquals(0, game.getWinner());
    }

    private void setUpWinByFinishLine() {
        Car currentCar = getCurrentCar();
        currentCar.setFinishLineCrossings(1);
    }

    private void setUpWinByLastAlive() {
        getCurrentCar().crash();
    }

    /**
     * the expected CarVelocity at the start of each game is X:0 Y:0.
     * An according PositionVector is created and compared to the return Value of game.getCarVelocity.
     */
    @Test
    public void getCarVelocityTest() {
        PositionVector expectedVelocity = new PositionVector(0, 0);
        PositionVector returnedVelocity = game.getCarVelocity(getCurrentCarIndex());
        assertEquals(expectedVelocity, returnedVelocity);
    }

    /**
     * To test the game.doCarTurn method, the initial Position and Velocity Values of the current Car are stored.
     * After executing the game.doCarTurn method, the new Position and Velocity Values are compared to those
     * stored previously.
     */
    @Test
    public void doCarTurnTest() {
        PositionVector initialPosition = game.getCarPosition(getCurrentCarIndex());
        PositionVector initialVelocity = game.getCarVelocity(getCurrentCarIndex());
        game.doCarTurn(RIGHT);
        PositionVector afterMovePosition = game.getCarPosition(getCurrentCarIndex());
        PositionVector afterMoveVelocity = game.getCarVelocity(getCurrentCarIndex());
        assertNotEquals(initialPosition, afterMovePosition);
        assertNotEquals(initialVelocity, afterMoveVelocity);
    }

    /**
     * Lets the current Car drive into the wall, and checks if the Car has crashed
     */
    @Test
    public void crashTest() {
        game.doCarTurn(UP);
        assertTrue(getCurrentCar().isCrashed());
    }

    /**
     * Tests whether there is only one car thta is not crashed if win is calculated
     */
    @Test
    public void winByLastAlive() {
        crashCars(1);
        assertTrue(game.hasWinner());
    }

    /**
     * makes given amount of cars Crash by driving into Wall
     *
     * @param carsToCrash amount of cars to crash
     */
    private void crashCars(int carsToCrash) {
        while (carsToCrash > 0) {
            getCurrentCar().setPosition(new PositionVector(1, 1));
            game.doCarTurn(DOWN_LEFT);
            carsToCrash--;
        }
    }

    /**
     * The current Car Index from before and after executing game.switchToNextActiveCar are compared.
     * Because there are more than one active Cars in this Setup, we expect the CarIndex to change.
     */
    @Test
    public void switchToNextActiveCarTest() {
        int initialCarIndex = getCurrentCarIndex();
        game.switchToNextActiveCar();
        assertNotEquals(initialCarIndex, getCurrentCarIndex());
    }

    /**
     * The second Car (Index 1) is crashed.
     * Because there are only 2 Cars on this Track, after game.switchToNextActiveCar() is invoked
     * the current car is expected to be the same as the game started with.
     */

    @Test
    public void crashAndSwitchTest() {
        int initialCarIndex = getCurrentCarIndex();
        game.getTrack().getCar(1).crash();
        game.doCarTurn(NONE);
        assertEquals(initialCarIndex, getCurrentCarIndex());
    }

    /**
     * All cars are crashed before game.switchToNextActiveCar() is invoked.
     * This test is to check, if the game.switchToNextActiveCar() does not get stuck in an endless Loop when all cars
     * are crashed - ergo no car is active.
     */
    @Test
    public void crashAllCarsAndSwitchTest() {
        crashCars(2);
        game.switchToNextActiveCar();
        assertTrue(true);
    }

    /**
     * compares, if the Track returned by the game.getTrack method is equal to the Track we used as a Parameter to
     * initialize the Game.
     */
    @Test
    public void getTrackTest() {
        assertEquals(this.track, game.getTrack());
    }

    /**
     * returns the current Car according to the Game
     *
     * @return current Car
     */
    private Car getCurrentCar() {
        return game.getTrack().getCar(getCurrentCarIndex());
    }

    private int getCurrentCarIndex() {
        return game.getCurrentCarIndex();
    }
}
