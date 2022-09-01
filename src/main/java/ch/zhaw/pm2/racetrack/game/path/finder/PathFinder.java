package ch.zhaw.pm2.racetrack.game.path.finder;

import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.util.PathCalculator;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType;

import java.util.*;

import static ch.zhaw.pm2.racetrack.game.path.finder.PathPoint.POSITION_COMPARATOR;
import static ch.zhaw.pm2.racetrack.game.util.PositionVector.*;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType.*;
import static java.lang.Double.MAX_VALUE;
import static java.util.Comparator.comparing;

/**
 * Class containing the logic for the pathfinder strategy.
 *
 * @author fupat002, abuechi
 * @version 1.0.0
 */
public class PathFinder {
    private static final int MAX_SURROUNDING_POINTS = 8;
    private static final double COST_IMPASSABLE = MAX_VALUE / 1000000;
    private static final double COST_OPEN = 1.0;
    private static final double COST_NEAR_WALL = 2.0;
    private static final double COST_DIRECTION_CONSTANT = 0.001;
    private static final double NO_COST = 0.0;
    private static final int MAX_GRID_SIZE = 3;

    private final Deque<PositionVector> calculatedPath;
    private final Track track;
    private final PathCalculator pathCalculator;

    /**
     * Creates a Pathfinder object based on track and current car index
     *
     * @param track           the track to find the path on
     * @param currentCarIndex current car index
     */
    public PathFinder(final Track track, final int currentCarIndex) {
        calculatedPath = new LinkedList<>();
        this.track = track;
        this.pathCalculator = new PathCalculator();
        findPathToFinishLine(track.getCar(currentCarIndex).getPosition());
    }

    /**
     * Removes and returns the next point on the calculated path.
     *
     * @return The next point on the path or null if empty
     */
    public PositionVector getNextPathFinderPoint() {
        return calculatedPath.pollFirst();
    }

    private void findPathToFinishLine(PositionVector currentCarPosition) {
        final PathPoint calculatedPathEnd = calculatePath(currentCarPosition);
        if (calculatedPathEnd != null) {
            optimizeCalculatedPath(calculatedPathEnd);
            createPathStack(calculatedPathEnd);
        }
    }

    private void createPathStack(PathPoint calculatedPathEnd) {
        PathPoint currentPathPoint = calculatedPathEnd;
        while (currentPathPoint != null) {
            calculatedPath.addFirst(currentPathPoint.getPosition());
            currentPathPoint = currentPathPoint.getPrev();
        }
    }

    private void optimizeCalculatedPath(PathPoint pathEndPoint) {
        PathPoint previousPathEndPoint = pathEndPoint.getPrev();
        if (previousPathEndPoint != null && previousPathEndPoint.getPrev() != null) {
            boolean hasLineOfSightToNextGoal = true;
            final List<PositionVector> calculatedPathToPoint = pathCalculator.calculatePath(pathEndPoint.getPosition(), previousPathEndPoint.getPrev().getPosition());
            for (PositionVector point : calculatedPathToPoint) {
                if (track.getSpaceType(point).equals(WALL)) {
                    hasLineOfSightToNextGoal = false;
                    break;
                }
            }
            if (hasLineOfSightToNextGoal) {
                pathEndPoint.setPrev(previousPathEndPoint.getPrev());
                optimizeCalculatedPath(pathEndPoint);
            } else {
                optimizeCalculatedPath(pathEndPoint.getPrev());
            }
        }
    }

    private PathPoint calculatePath(PositionVector startingPoint) {
        PriorityQueue<PathPoint> borderPoints = new PriorityQueue<>(comparing(PathPoint::getTotalCost));
        borderPoints.add(new PathPoint(startingPoint, null, NO_COST));
        final Set<PathPoint> visitedPoints = new TreeSet<>(POSITION_COMPARATOR);
        while (!borderPoints.isEmpty()) {
            PathPoint currentFinderPoint = borderPoints.remove();
            if (currentFinderPoint.getTotalCost() < COST_IMPASSABLE) {
                if (!isSpaceTypePartOfFinishLine(track.getSpaceType(currentFinderPoint.getPosition()))) {
                    calculateNextPointToMoveTo(borderPoints, visitedPoints, currentFinderPoint);
                    visitedPoints.add(currentFinderPoint);
                } else {
                    return currentFinderPoint;
                }
            }
        }
        return null;
    }

    private void calculateNextPointToMoveTo(final PriorityQueue<PathPoint> borderPoints, final Set<PathPoint> visitedPoints, final PathPoint currentFinderPoint) {
        for (PathPoint neighbor : getNeighbouringPoints(currentFinderPoint)) {
            if (!visitedPoints.contains(neighbor)) {
                PathPoint neighbourOnBorder = getNeighbourOnBorder(borderPoints, neighbor);
                if (neighbourOnBorder == null) {
                    neighbourOnBorder = neighbor;
                }
                double totalCostToNeighbor = getMoveCost(currentFinderPoint, neighbourOnBorder) + currentFinderPoint.getTotalCost();
                if (totalCostToNeighbor < neighbourOnBorder.getTotalCost()) {
                    if (neighbourOnBorder.equals(neighbor)) {
                        borderPoints.remove(neighbourOnBorder);
                    }
                    neighbourOnBorder.setPrev(currentFinderPoint);
                    neighbourOnBorder.setTotalCost(totalCostToNeighbor);
                    borderPoints.add(neighbourOnBorder);
                }
            }
        }
    }

    private boolean isSpaceTypePartOfFinishLine(SpaceType spaceType) {
        return spaceType.equals(FINISH_UP) || spaceType.equals(FINISH_DOWN) || spaceType.equals(FINISH_LEFT) || spaceType.equals(FINISH_RIGHT);
    }

    private boolean isNearWall(PathPoint point) {
        for (PathPoint pathPoint : getNeighbouringPoints(point)) {
            if (track.getSpaceType(pathPoint.getPosition()).equals(WALL)) {
                return true;
            }
        }
        return false;
    }

    private double getMoveCost(PathPoint startPoint, PathPoint endPoint) {
        return switch (track.getSpaceType(endPoint.getPosition())) {
            case TRACK -> calculateSpaceCost(startPoint, endPoint);
            case FINISH_UP -> getCostForHorizontalFinishPosition(startPoint, endPoint, -1);
            case FINISH_DOWN -> getCostForHorizontalFinishPosition(startPoint, endPoint, 1);
            case FINISH_LEFT -> getCostForVerticalFinishPosition(startPoint, endPoint, -1);
            case FINISH_RIGHT -> getCostForVerticalFinishPosition(startPoint, endPoint, 1);
            case WALL -> COST_IMPASSABLE;
        };
    }

    private double getCostForHorizontalFinishPosition(PathPoint startPoint, PathPoint endPoint, int coordinate) {
        return subtract(endPoint.getPosition(), startPoint.getPosition()).getY() == coordinate ?
            calculateSpaceCost(startPoint, endPoint) : COST_IMPASSABLE;
    }

    private double getCostForVerticalFinishPosition(PathPoint startPoint, PathPoint endPoint, int coordinate) {
        return subtract(endPoint.getPosition(), startPoint.getPosition()).getX() == coordinate ?
            calculateSpaceCost(startPoint, endPoint) : COST_IMPASSABLE;
    }

    private double calculateSpaceCost(PathPoint startPoint, PathPoint endPoint) {
        double baseCost = calculateBaseCost(endPoint);
        double wrongDirectionPenalty = NO_COST;
        if (startPoint.getPrev() != null) {
            PositionVector previousDirection = subtract(startPoint.getPosition(), startPoint.getPrev().getPosition());
            PositionVector nextDirection = subtract(endPoint.getPosition(), startPoint.getPosition());
            wrongDirectionPenalty = calculateDirectionPenalty(previousDirection, nextDirection);
        }
        return baseCost + wrongDirectionPenalty;
    }

    private double calculateDirectionPenalty(PositionVector previousDirection, PositionVector nextDirection) {
        return COST_DIRECTION_CONSTANT * (1 - scalarProduct(previousDirection, nextDirection));
    }

    private double calculateBaseCost(PathPoint endPoint) {
        return isNearWall(endPoint) ? COST_NEAR_WALL : COST_OPEN;
    }

    private PathPoint getNeighbourOnBorder(PriorityQueue<PathPoint> border, PathPoint neighbor) {
        for (PathPoint point : border) {
            if (point.equals(neighbor)) {
                return point;
            }
        }
        return null;
    }

    private PathPoint[] getNeighbouringPoints(final PathPoint centerPoint) {
        final PathPoint[] neighbouringPoints = new PathPoint[MAX_SURROUNDING_POINTS];
        int neighbouringPointsIndex = 0;
        PositionVector topLeftPointDisplacement = new PositionVector(-1, -1);
        final PositionVector topLeft = add(centerPoint.getPosition(), topLeftPointDisplacement);
        for (int y = 0; y < MAX_GRID_SIZE; y++) {
            for (int x = 0; x < MAX_GRID_SIZE; x++) {
                if (x != 1 || y != 1) {
                    neighbouringPoints[neighbouringPointsIndex++] = new PathPoint(add(topLeft, new PositionVector(x, y)), centerPoint, MAX_VALUE);
                }
            }
        }

        return neighbouringPoints;
    }

    public Track getTrack() {
        return track;
    }
}
