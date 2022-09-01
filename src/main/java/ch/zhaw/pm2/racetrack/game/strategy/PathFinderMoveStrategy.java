package ch.zhaw.pm2.racetrack.game.strategy;

import ch.zhaw.pm2.racetrack.game.Game;
import ch.zhaw.pm2.racetrack.game.path.finder.PathFinder;
import ch.zhaw.pm2.racetrack.game.path.finder.PathPointFollower;
import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType;

import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType.PATH_FINDER;

/**
 * Contains the logic for the pathfinder strategy.
 *
 * @author abuechi
 * @version 1.0.0
 */
public class PathFinderMoveStrategy implements MoveStrategy {
    private final PathPointFollower pathPointFollower;

    /**
     * Creates a pathfinder move strategy object
     *
     * @param gameInstance the current game instance
     */
    public PathFinderMoveStrategy(Game gameInstance) {
        final PathFinder pathFinder = new PathFinder(gameInstance.getTrack(), gameInstance.getCurrentCarIndex());
        pathPointFollower = new PathPointFollower(pathFinder, gameInstance.getCurrentCarIndex(), gameInstance);
    }

    @Override
    public Direction nextMove() {
        return pathPointFollower.calculateNextMove();
    }

    @Override
    public Direction nextMove(int acceleration) {
        //NOP
        throw new UnsupportedOperationException("CALLED NOP IMPLEMENTATION");
    }

    @Override
    public StrategyType getMovementStrategyType() {
        return PATH_FINDER;
    }
}
