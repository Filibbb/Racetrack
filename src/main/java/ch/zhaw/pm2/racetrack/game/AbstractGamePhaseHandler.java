package ch.zhaw.pm2.racetrack.game;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.game.strategy.*;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;

import java.util.List;

import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType.*;

/**
 * The game phase handler that provides the methods to set up and start the main game.
 * Class provides methods for the user interface specific implementations.
 *
 * @author abuechi
 * @version 1.0.0
 */
public abstract class AbstractGamePhaseHandler {
    private final Config config;
    private Game game;

    /**
     * Initializes the game phase handler with the required config object
     *
     * @param config the config object given by initializer class.
     */
    protected AbstractGamePhaseHandler(final Config config) {
        this.config = config;
    }

    /**
     * Displays a welcome introduction when starting a game
     */
    protected abstract void displayGameIntroduction();

    /**
     * Method that is called once a winner is decided.
     *
     * @param winnerId the winners symbol or name
     */
    protected abstract void handleGameCompletedWithWinner(String winnerId);

    /**
     * Shows the user that turns changed
     *
     * @param currentCarId the char representation of the user who is next to play
     */
    protected abstract void displayChangeOfTurns(char currentCarId);

    /**
     * Displays the current track state
     *
     * @param track the track instance
     */
    protected abstract void displayTrack(Track track);

    /**
     * Displays an error when everyone selected no-move strategy
     */
    protected abstract void displayGameWithOnlyNoMovementAsStrategy();

    /**
     * Informs the user that the follower strategy was already selected as only one can select it at a time
     */
    protected abstract void informUserAboutFollowerStrategyAlreadySelected();

    /**
     * Prompts the user to select a movement strategy for this game
     *
     * @param carId the users character representation
     * @return aa selection
     */
    protected abstract int promptMovementStrategy(char carId);

    /**
     * Asks the user for the next acceleration
     *
     * @return the selected ordinal of the acceleration
     */
    protected abstract int promptAcceleration();

    /**
     * Retrieves the track file selector for the current game instance
     *
     * @return the file selector instance required for selecting a track file
     */
    protected abstract AbstractFileSelector<Track> getTrackFileSelector();

    /**
     * Retrieves the follower file loader for the current game instance
     *
     * @return the follower file loader  instance required for selecting a follower file
     */
    protected abstract AbstractFileSelector<List<Direction>> getFollowerFileLoader();

    /**
     * Retrieves the move list loader for the current game instance
     *
     * @return the move list file loader  instance required for selecting a move list file
     */
    protected abstract AbstractFileSelector<List<Direction>> getMoveListFileLoader();

    /**
     * Starts the game instance by going through the setup phase and main phase afterwards
     */
    public void startGame() {
        startSetupPhase();
        if (hasNoMovementStrategyForAllPlayers()) {
            displayTrack(game.getTrack());
            displayGameWithOnlyNoMovementAsStrategy();
        } else {
            startMainPhase();
        }
    }

    private void startSetupPhase() {
        displayGameIntroduction();
        final AbstractFileSelector<Track> trackFileSelector = getTrackFileSelector();
        game = new Game(trackFileSelector.selectFile());
        for (Car car : game.getTrack().getCarsOnTrack()) {
            selectMovementStrategy(car);
        }

    }

    private void startMainPhase() {
        while (!game.hasWinner()) {
            displayChangeOfTurns(game.getCarId(game.getCurrentCarIndex()));
            displayTrack(game.getTrack());
            doPlayerTurn();
            if (game.hasWinner()) {
                handleGameCompletedWithWinner(String.valueOf(game.getCarId(game.getWinner())));
            }
            game.switchToNextActiveCar();
        }
    }

    private void doPlayerTurn() {
        final Car car = game.getTrack().getCar(game.getCurrentCarIndex());
        final Direction acceleration = getDirection(car.getMoveStrategy());
        game.doCarTurn(acceleration);
    }

    private Direction getDirection(MoveStrategy moveStrategy) {
        return switch (moveStrategy.getMovementStrategyType()) {
            case DO_NOT_MOVE, PATH_FOLLOWER, MOVE_LIST, PATH_FINDER -> moveStrategy.nextMove();
            case USER -> moveStrategy.nextMove(promptAcceleration());
        };
    }

    private void selectMovementStrategy(Car car) {
        final int selectedMovementStrategy = promptMovementStrategy(car.getId());
        final MoveStrategy moveStrategy = createMoveStrategyBySelection(selectedMovementStrategy, car);
        if (moveStrategy != null) {
            car.setMoveStrategy(moveStrategy);
        } else {
            selectMovementStrategy(car);
        }
    }

    private boolean hasNoMovementStrategyForAllPlayers() {
        for (Car car : game.getTrack().getCarsOnTrack()) {
            if (car.getMoveStrategy().getMovementStrategyType() != DO_NOT_MOVE) {
                return false;
            }
        }
        return true;
    }

    private boolean isFollowerStrategyAlreadySelected() {
        for (Car car : game.getTrack().getCarsOnTrack()) {
            if (car.getMoveStrategy() != null && car.getMoveStrategy().getMovementStrategyType() == PATH_FOLLOWER) {
                return true;
            }
        }
        return false;
    }

    /**
     * A utility method to create a move strategy object by a selection made by the user
     *
     * @param selection the selection made by the user
     * @return returns the related move strategy object used for the movement strategy in turns
     */
    private MoveStrategy createMoveStrategyBySelection(int selection, Car car) {
        if (selection < values().length && selection >= 0) {
            final StrategyType selectedMovementStrategy = values()[selection];
            return switch (selectedMovementStrategy) {
                case DO_NOT_MOVE -> new DoNotMoveStrategy();
                case USER -> new UserMoveStrategy();
                case MOVE_LIST -> new MoveListStrategy(getMoveListFileLoader().selectFile());
                case PATH_FINDER -> new PathFinderMoveStrategy(game);
                case PATH_FOLLOWER -> {
                    if (isFollowerStrategyAlreadySelected()) {
                        informUserAboutFollowerStrategyAlreadySelected();
                        selectMovementStrategy(car);
                    }
                    yield new PathFollowerMoveStrategy(getFollowerFileLoader().selectFile());
                }
            };
        }
        return null;
    }

    protected Config getConfig() {
        return config;
    }

    protected Game getGame() {
        return game;
    }
}
