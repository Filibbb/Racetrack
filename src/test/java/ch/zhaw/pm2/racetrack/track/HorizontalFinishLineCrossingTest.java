package ch.zhaw.pm2.racetrack.track;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.track.FinishLineChecker;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PathCalculator;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static ch.zhaw.pm2.racetrack.helpers.TestTrackCreationUtil.createOvalClockTrack;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the finish line crossing with a vertical finish line.
 */
public class HorizontalFinishLineCrossingTest {

    private FinishLineChecker finishLineChecker;
    private PathCalculator pathCalculator;
    private Car playerOne;

    /**
     * Sets uo the path checker class.
     */
    @BeforeEach
    public void setUp() {
        Track trackWithHorizontalFinishLine = new Track(createOvalClockTrack());
        finishLineChecker = new FinishLineChecker(trackWithHorizontalFinishLine);
        playerOne = trackWithHorizontalFinishLine.getCarsOnTrack().get(0);
        pathCalculator = new PathCalculator();
    }

    /**
     * Tests whether the player who crosses the finish line backwards gets a minus point.
     */
    @Test
    public void horizontalFinishLineCrossingDrivingBackwardsTest() {
        finishLineChecker.checkFinishLineCrossings(playerOne, pathCalculator.calculatePath(playerOne.getPosition(), new PositionVector(8, 8)));
        assertEquals(-1, playerOne.getFinishLineCrossings());
    }

    /**
     * Tests whether the player who crosses the finish line backwards and then forwards again does not get a plus point.
     */
    @Test
    public void horizontalFinishLineCrossingDrivingBackwardsThenForwardTest() {
        finishLineChecker.checkFinishLineCrossings(playerOne, pathCalculator.calculatePath(playerOne.getPosition(), new PositionVector(9, 8)));
        playerOne.setPosition(new PositionVector(9, 8));
        finishLineChecker.checkFinishLineCrossings(playerOne, pathCalculator.calculatePath(playerOne.getPosition(), new PositionVector(9, 5)));
        assertEquals(0, playerOne.getFinishLineCrossings());
    }

    /**
     * Tests whether the player who crosses the finish line forwards gets a plus point.
     */
    @Test
    public void horizontalFinishLineCrossingDrivingForwardsTest() {
        Car newPlayer = new Car('T', new PositionVector(9, 8));
        finishLineChecker.checkFinishLineCrossings(newPlayer, pathCalculator.calculatePath(newPlayer.getPosition(), new PositionVector(9, 5)));
        assertEquals(1, newPlayer.getFinishLineCrossings());
    }
}
