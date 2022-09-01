package ch.zhaw.pm2.racetrack.game;

import ch.zhaw.pm2.racetrack.game.track.FinishLineChecker;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PathCalculator;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import ch.zhaw.pm2.racetrack.given.GameSpecification;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType.WALL;

/**
 * Game controller class, performing all actions to modify the game state.
 * It contains the logic to move the cars, detect if they are crashed
 * and if we have a winner.
 */
public class Game implements GameSpecification {
    private static final int FIRST = 0;
    private final Track track;
    private final PathCalculator pathCalculator;
    private final FinishLineChecker finishLineChecker;
    private int currentCarIndex;
    private Car winner = null;

    /**
     * Creates a game with a specific track
     *
     * @param track the given track previously loaded by user.
     */
    public Game(Track track) {
        this.track = track;
        pathCalculator = new PathCalculator();
        finishLineChecker = new FinishLineChecker(track);
    }

    /**
     * Return the index of the current active car.
     * Car indexes are zero-based, so the first car is 0, and the last car is getCarCount() - 1.
     *
     * @return The zero-based number of the current car
     */
    @Override
    public int getCurrentCarIndex() {
        return currentCarIndex;
    }

    /**
     * Get the id of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A char containing the id of the car
     */
    @Override
    public char getCarId(int carIndex) {
        return track.getCarsOnTrack().get(carIndex).getId();
    }

    /**
     * Get the position of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current position
     */
    @Override
    public PositionVector getCarPosition(int carIndex) {
        return track.getCarsOnTrack().get(carIndex).getPosition();
    }

    /**
     * Get the velocity of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     */
    @Override
    public PositionVector getCarVelocity(int carIndex) {
        return track.getCarsOnTrack().get(carIndex).getVelocity();
    }

    /**
     * Return the winner of the game. If the game is still in progress, returns NO_WINNER.
     *
     * @return The winning car's index (zero-based, see getCurrentCar()), or NO_WINNER if the game is still in progress
     */
    @Override
    public int getWinner() {
        return track.getCarsOnTrack().indexOf(winner);
    }

    /**
     * Execute the next turn for the current active car.
     * <p>This method changes the current car's velocity and checks on the path to the next position,
     * if it crashes (car state to crashed) or passes the finish line in the right direction (set winner state).</p>
     * <p>The steps are as follows</p>
     * <ol>
     *   <li>Accelerate the current car</li>
     *   <li>Calculate the path from current (start) to next (end) position
     *       (see {@link Game#calculatePath(PositionVector, PositionVector)})</li>
     *   <li>Verify for each step what space type it hits:
     *      <ul>
     *          <li>TRACK: check for collision with other car (crashed &amp; don't continue), otherwise do nothing</li>
     *          <li>WALL: car did collide with the wall - crashed &amp; don't continue</li>
     *          <li>FINISH_*: car hits the finish line - wins only if it crosses the line in the correct direction</li>
     *      </ul>
     *   </li>
     *   <li>If the car crashed or wins, set its position to the crash/win coordinates</li>
     *   <li>If the car crashed, also detect if there is only one car remaining, remaining car is the winner</li>
     *   <li>Otherwise move the car to the end position</li>
     * </ol>
     * <p>The calling method must check the winner state and decide how to go on.</p>
     *
     * @param acceleration A Direction containing the current car's acceleration vector (-1,0,1) in x and y direction
     *                     for this turn
     */
    @Override
    public void doCarTurn(Direction acceleration) {
        Car currentCar = track.getCarsOnTrack().get(currentCarIndex);
        PositionVector currentCarPosition = currentCar.getPosition();
        currentCar.accelerate(acceleration);
        final List<PositionVector> path = calculatePath(currentCarPosition, currentCar.nextPosition());
        for (PositionVector nextPosition : path) {
            if (willCarCrash(currentCarIndex, nextPosition)) {
                currentCar.crash();
                currentCar.setPosition(nextPosition);
                List<PositionVector> pathUntilCrash = calculatePath(currentCarPosition, nextPosition);
                finishLineChecker.checkFinishLineCrossings(currentCar, pathUntilCrash);
            }
        }
        winByLastAlive();
        if (!hasWinner() && !currentCar.isCrashed()) {
            finishLineChecker.checkFinishLineCrossings(currentCar, path);
            currentCar.move();
        }
        winByCrossingFinishLine();
    }

    /**
     * Switches to the next car who is still in the game. Skips crashed cars.
     */
    @Override
    public void switchToNextActiveCar() {
        do {
            currentCarIndex = (currentCarIndex + 1) % track.getCarsOnTrack().size();
            winByLastAlive();
        } while (track.getCarsOnTrack().get(currentCarIndex).isCrashed() && !hasWinner());
    }

    /**
     * Returns all the grid positions in the path between two positions, for use in determining line of sight.
     * Determine the 'pixels/positions' on a raster/grid using Bresenham's line algorithm.
     * (https://de.wikipedia.org/wiki/Bresenham-Algorithmus)
     * Basic steps are
     * - Detect which axis of the distance vector is longer (faster movement)
     * - for each pixel on the 'faster' axis calculate the position on the 'slower' axis.
     * Direction of the movement has to correctly considered
     *
     * @param startPosition Starting position as a PositionVector
     * @param endPosition   Ending position as a PositionVector
     * @return Intervening grid positions as a List of PositionVector's, including the starting and ending positions.
     */
    @Override
    public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
        return pathCalculator.calculatePath(startPosition, endPosition);
    }

    /**
     * Does indicate if a car would have a crash with a WALL space or another car at the given position.
     *
     * @param carIndex    The zero-based carIndex number
     * @param endPosition A PositionVector of the possible crash position
     * @return A boolean indicator if the car would crash with a WALL or another car.
     */
    @Override
    public boolean willCarCrash(int carIndex, PositionVector endPosition) {
        return hitsWall(endPosition) || hitsDifferentCar(endPosition, track.getCar(carIndex), track.getCarsOnTrack());
    }

    /**
     * Checks whether the current game instance has an active winner set and game therefore is complete.
     *
     * @return boolean value defining if a winner has been decided.
     */
    public boolean hasWinner() {
        return winner != null;
    }

    /**
     * This Method checks whether the current player will win the Game by crossing the finish line.
     */
    private void winByCrossingFinishLine() {
        final Car currentPlayer = track.getCarsOnTrack().get(currentCarIndex);
        if (currentPlayer.getFinishLineCrossings() >= 1) {
            winner = currentPlayer;
            currentPlayer.setPosition(currentPlayer.getPosition());
        }
    }

    private boolean hitsDifferentCar(PositionVector currentPlayersPathPoint, Car currentPlayer, List<Car> allPlayers) {
        for (Car car : allPlayers) {
            if (car.getId() != currentPlayer.getId() && currentPlayersPathPoint.equals(car.getPosition())) {
                return true;
            }
        }
        return false;
    }

    private boolean hitsWall(PositionVector currentPlayersPath) {
        return track.getSpaceType(currentPlayersPath).equals(WALL);
    }

    /**
     * Checks if only one car is alive. If so, this car is set as winner.
     */
    private void winByLastAlive() {
        final List<Car> allNotCrashedCars = getAllNotCrashedCars();
        if (allNotCrashedCars.size() <= 1) {
            final Car calculatedWinnerCar = allNotCrashedCars.get(FIRST);
            this.winner = calculatedWinnerCar;
            calculatedWinnerCar.setPosition(calculatedWinnerCar.getPosition());
        }
    }

    private List<Car> getAllNotCrashedCars() {
        final List<Car> carsAlive = new ArrayList<>();
        for (Car car : track.getCarsOnTrack()) {
            if (!car.isCrashed()) {
                carsAlive.add(car);
            }
        }
        return carsAlive;
    }

    public Track getTrack() {
        return track;
    }
}
