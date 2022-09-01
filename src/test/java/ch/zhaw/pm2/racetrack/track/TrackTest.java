package ch.zhaw.pm2.racetrack.track;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.zhaw.pm2.racetrack.helpers.TestTrackCreationUtil.createChallengeTrack;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the Track class.
 */
public class TrackTest {

    private Track track;

    @BeforeEach
    public void setUp() {
        track = new Track(createChallengeTrack());
    }

    /**
     * Tests whether the car that is already in the file is read in at the correct position.
     */
    @Test
    public void addCarsOnTrackWithFileReaderTest() {
        Car playerOne = track.getCarsOnTrack().get(0);
        PositionVector expectedEndPoint = new PositionVector(24, 22);
        assertEquals(expectedEndPoint, playerOne.getPosition());
    }
}
