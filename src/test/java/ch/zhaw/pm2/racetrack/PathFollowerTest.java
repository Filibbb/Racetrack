package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.Game;
import ch.zhaw.pm2.racetrack.game.path.follower.FollowerLoader;
import ch.zhaw.pm2.racetrack.game.strategy.PathFollowerMoveStrategy;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;
import static ch.zhaw.pm2.racetrack.helpers.TestTrackCreationUtil.createHorizontalTrack;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the pathfollower strategy.
 *
 * @author weberph5
 */

public class PathFollowerTest {
    private Track track;
    private Game game;
    private Car playerOne;

    @BeforeEach
    public void setUp() {
        track = new Track(createHorizontalTrack());
        game = new Game(track);
        playerOne = track.getCar(0);
    }

    @Test
    @DisplayName("Checks for the correct position after 3 valid path points")
    public void checkPositionAfterThreeValidPoints() throws IOException {
        File selectedFile = new File(".\\src\\test\\java\\ch\\zhaw\\pm2\\racetrack\\helpers\\ThreeValidPathPoints.txt");
        List<Direction> directionList = new FollowerLoader().loadFollowerFile(selectedFile, playerOne);
        playerOne.setMoveStrategy(new PathFollowerMoveStrategy(directionList));
        while (!directionList.isEmpty()) {
            final Car car = game.getTrack().getCar(0);
            final Direction acceleration = car.getMoveStrategy().nextMove();
            game.doCarTurn(acceleration);
        }
        assertEquals(new PositionVector(10, 1), playerOne.getPosition());
    }

    @Test
    @DisplayName("Checks the position after 3 invalid path points")
    public void checkPositionAfterThreeInvalidPoints() throws IOException {
        File selectedFile = new File(".\\src\\test\\java\\ch\\zhaw\\pm2\\racetrack\\helpers\\ThreeInvalidPathPoints.txt");
        List<Direction> directionList = new FollowerLoader().loadFollowerFile(selectedFile, playerOne);
        playerOne.setMoveStrategy(new PathFollowerMoveStrategy(directionList));
        while (!directionList.isEmpty()) {
            final Car car = game.getTrack().getCar(0);
            final Direction acceleration = car.getMoveStrategy().nextMove();
            game.doCarTurn(acceleration);
            System.out.println(track.toString());
        }
        assertEquals(new PositionVector(1, 1), playerOne.getPosition());
    }
}
