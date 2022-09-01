package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.Game;
import ch.zhaw.pm2.racetrack.game.strategy.MoveListStrategy;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;
import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction.DOWN;
import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction.RIGHT;
import static ch.zhaw.pm2.racetrack.helpers.TestTrackCreationUtil.createHorizontalTrack;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the Move List Strategy.
 *
 * @author weberph5
 */
public class MoveListTest {
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
    @DisplayName("This test tests 3 movements to the right on a horizontal track")
    public void checkMoveThreeToRight() {
        List<Direction> directionList = new ArrayList<>(List.of(RIGHT, RIGHT, RIGHT));
        playerOne.setMoveStrategy(new MoveListStrategy(directionList));
        while (!directionList.isEmpty()) {
            final Car car = game.getTrack().getCar(0);
            final Direction acceleration = car.getMoveStrategy().nextMove();
            game.doCarTurn(acceleration);
        }
        assertEquals(new PositionVector(7, 1), playerOne.getPosition());
    }

    @Test
    @DisplayName("This test tests 1 movement to the right and then 1 down on a horizontal track")
    public void checkMoveOneRightOneDown() {
        List<Direction> directionList = new ArrayList<>(List.of(RIGHT, DOWN));
        playerOne.setMoveStrategy(new MoveListStrategy(directionList));
        while (!directionList.isEmpty()) {
            final Car car = game.getTrack().getCar(0);
            final Direction acceleration = car.getMoveStrategy().nextMove();
            game.doCarTurn(acceleration);
        }
        assertEquals(new PositionVector(3, 2), playerOne.getPosition());
    }

}
