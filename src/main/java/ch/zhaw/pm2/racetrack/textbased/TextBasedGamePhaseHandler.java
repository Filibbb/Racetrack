package ch.zhaw.pm2.racetrack.textbased;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.game.AbstractFileSelector;
import ch.zhaw.pm2.racetrack.game.AbstractGamePhaseHandler;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType;
import ch.zhaw.pm2.racetrack.textbased.file.selector.TerminalBasedFollowerFileSelector;
import ch.zhaw.pm2.racetrack.textbased.file.selector.TerminalBasedMoveListFileSelector;
import ch.zhaw.pm2.racetrack.textbased.file.selector.TerminalBasedTrackFileSelector;

import java.util.List;

import static ch.zhaw.pm2.racetrack.textbased.io.TextIOHelper.println;
import static ch.zhaw.pm2.racetrack.textbased.io.TextIOHelper.readIntFromTerminal;

/**
 * This class loads the config and sets up a text based game instance
 *
 * @author abuechi
 * @version 1.0.0
 */
public class TextBasedGamePhaseHandler extends AbstractGamePhaseHandler {
    private static final int FIRST_OPTION = 0;

    /**
     * Initializes the text based game phase handler with the related methods for text based game instance.
     *
     * @param config the config object used for track directories etc.
     */
    public TextBasedGamePhaseHandler(Config config) {
        super(config);
    }

    /**
     * The text based specific implementation of printing the game introduction
     */
    @Override
    public void displayGameIntroduction() {
        println("Welcome to Racetrack");
    }

    /**
     * The text based specific implementation when the game is completed and has a winner.
     */
    @Override
    public void handleGameCompletedWithWinner(String winner) {
        displayTrack(getGame().getTrack());
        println(winner + " has won. GG. \n. Press CTRL + C to exit.");
    }

    /**
     * The text based specific implementation of displaying turn changing information.
     */
    @Override
    protected void displayChangeOfTurns(char currentCarId) {
        println("Player " + currentCarId + " turn.");
    }

    @Override
    protected void displayTrack(Track track) {
        println(track.toString());
    }

    @Override
    protected void displayGameWithOnlyNoMovementAsStrategy() {
        println("All players selected as strategy to not move. No point in playing therefore. Press CTRL + C to exit.");
    }

    @Override
    protected void informUserAboutFollowerStrategyAlreadySelected() {
        println("Only 1 user is allowed to select the path follower strategy. Please select another strategy.");
    }

    /**
     * The text based specific implementation of retrieving the movement strategy used for the current playthrough
     */
    @Override
    protected int promptMovementStrategy(char carId) {
        return readIntFromTerminal(FIRST_OPTION, StrategyType.values().length - 1, "Please select strategy for car " + carId + "\n" + "Available options are:\n" + getAvailableMovementStrategiesRepresentation());
    }

    private String getAvailableMovementStrategiesRepresentation() {
        StringBuilder stringBuilder = new StringBuilder();
        for (StrategyType movementStrategy : StrategyType.values()) {
            stringBuilder.append(movementStrategy.ordinal());
            stringBuilder.append(" ");
            stringBuilder.append(movementStrategy.name());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * The text based specific implementation of prompting the acceleration input
     *
     * @return the by the user inputted acceleration.
     */
    @Override
    protected int promptAcceleration() {
        final int lastIndex = Direction.values().length - 1;
        println("Please enter a number between " + Direction.values()[FIRST_OPTION].ordinal() + " and " + Direction.values()[lastIndex].ordinal() + " to determine your acceleration Vector\n" + getAccelerationGridRepresentation());
        return readIntFromTerminal(FIRST_OPTION, lastIndex, "Selection");
    }

    @Override
    protected AbstractFileSelector<Track> getTrackFileSelector() {
        return new TerminalBasedTrackFileSelector(getConfig());
    }

    @Override
    protected AbstractFileSelector<List<Direction>> getFollowerFileLoader() {
        return new TerminalBasedFollowerFileSelector(getConfig(), getGame().getTrack().getCar(getGame().getCurrentCarIndex()));
    }

    @Override
    protected AbstractFileSelector<List<Direction>> getMoveListFileLoader() {
        return new TerminalBasedMoveListFileSelector(getConfig());
    }

    private String getAccelerationGridRepresentation() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Direction.UP_LEFT.ordinal());
        stringBuilder.append(" ");
        stringBuilder.append(Direction.UP.ordinal());
        stringBuilder.append(" ");
        stringBuilder.append(Direction.UP_RIGHT.ordinal());
        stringBuilder.append("\n");
        stringBuilder.append(Direction.LEFT.ordinal());
        stringBuilder.append(" ");
        stringBuilder.append(Direction.NONE.ordinal());
        stringBuilder.append(" ");
        stringBuilder.append(Direction.RIGHT.ordinal());
        stringBuilder.append("\n");
        stringBuilder.append(Direction.DOWN_LEFT.ordinal());
        stringBuilder.append(" ");
        stringBuilder.append(Direction.DOWN.ordinal());
        stringBuilder.append(" ");
        stringBuilder.append(Direction.DOWN_RIGHT.ordinal());
        stringBuilder.append(" ");
        return stringBuilder.toString();
    }
}
