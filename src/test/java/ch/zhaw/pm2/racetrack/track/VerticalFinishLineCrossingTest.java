package ch.zhaw.pm2.racetrack.track;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.track.FinishLineChecker;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PathCalculator;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static ch.zhaw.pm2.racetrack.helpers.TestTrackCreationUtil.createChallengeTrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerticalFinishLineCrossingTest {

    private FinishLineChecker finishLineChecker;
    private PathCalculator pathCalculator;
    private Car playerOne;

    /**
     * Sets uo the path checker class.
     */
    @BeforeEach
    public void setUp() {
        Track trackWithHorizontalFinishLine = new Track(createChallengeTrack());
        finishLineChecker = new FinishLineChecker(trackWithHorizontalFinishLine);
        pathCalculator = new PathCalculator();
        playerOne = trackWithHorizontalFinishLine.getCarsOnTrack().get(0);
    }

    /**
     * Tests whether the player who crosses the finish line backwards gets a minus point.
     */
    @Test
    public void verticalFinishLineCrossingDrivingBackwardsTest() {
        finishLineChecker.checkFinishLineCrossings(playerOne, pathCalculator.calculatePath(playerOne.getPosition(), new PositionVector(18, 22)));
        assertEquals(-1, playerOne.getFinishLineCrossings());
    }

    /**
     * Tests whether the player who crosses the finish line backwards and then forwards again does not get a plus point.
     */
    @Test
    public void verticalFinishLineCrossingDrivingBackwardsThenForwardTest() {
        finishLineChecker.checkFinishLineCrossings(playerOne, pathCalculator.calculatePath(playerOne.getPosition(), new PositionVector(18,23)));
        playerOne.setPosition(new PositionVector(18, 23));
        finishLineChecker.checkFinishLineCrossings(playerOne, pathCalculator.calculatePath(playerOne.getPosition(), new PositionVector(33,23)));
        assertEquals(0, playerOne.getFinishLineCrossings());
    }

    /**
     * Tests whether the player who crosses the finish line forwards gets a plus point.
     */
    @Test
    public void verticalFinishLineCrossingDrivingForwardsTest() {
        Car newPlayer = new Car('T', new PositionVector(18, 23));
        finishLineChecker.checkFinishLineCrossings(newPlayer, pathCalculator.calculatePath(newPlayer.getPosition(), new PositionVector(33,23)));
        assertEquals(1, newPlayer.getFinishLineCrossings());
    }
}
