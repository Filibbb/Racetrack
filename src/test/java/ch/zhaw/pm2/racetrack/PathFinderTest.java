package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.Game;
import ch.zhaw.pm2.racetrack.game.strategy.DoNotMoveStrategy;
import ch.zhaw.pm2.racetrack.game.strategy.PathFinderMoveStrategy;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static ch.zhaw.pm2.racetrack.helpers.TestTrackCreationUtil.createChallengeTrack;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests whether the pathfinder works or not
 *
 * @author fupat002, abuechi
 */
public class PathFinderTest {

    private static final int CAR_INDEX = 1;
    private static final int FIRST_CAR_INDEX = 0;

    private Game game;

    /**
     * Sets up the path checker class.
     */
    @BeforeEach
    public void setUp() {
        Track track = new Track(createChallengeTrack());
        game = new Game(track);
        game.getTrack().getCar(FIRST_CAR_INDEX).setMoveStrategy(new PathFinderMoveStrategy(game));
        game.getTrack().getCar(CAR_INDEX).setMoveStrategy(new DoNotMoveStrategy());
    }

    /**
     * Tests whether the car can successfully maneuver through the track
     */
    @Disabled("Test is malfunctioning")
    @Test
    public void pathFinderTest() {
        while (!game.hasWinner()) {
            System.out.println(game.getTrack().toString());
            final Car car = game.getTrack().getCar(game.getCurrentCarIndex());
            final Direction acceleration = car.getMoveStrategy().nextMove();
            game.doCarTurn(acceleration);

            game.switchToNextActiveCar();
        }
        assertEquals("a", String.valueOf(game.getCarId(game.getWinner())));

    }
}
