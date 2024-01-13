package v1;
import battlecode.common.*;
import static v1.Constants.*;

public class Pathfinding {
    private static MapLocation destination;

    // ------------------------------------------ GETTERS AND SETTERS  ----------------------------------------------
    public static void setDestination(MapLocation newDestination) {
        destination = newDestination;
    }
    public static void resetDestination() {
        destination = null;
    }
    public static MapLocation getDestination() {
        return destination;
    }


    // ------------------------------------------- STATIC FUNCTIONS -------------------------------------------------
    /*
        Currently using greedy pathfinding. Below are other options in order of efficiency:
        1. Precomputed global BFS
        2. Parallelized Bellman-Ford    (Resource: https://discordapp.com/channels/386965718572466197/401058232346345473/1070487254969098263)
        3. TODO: Local BFS
        4. TODO: BUG2 / BUG1            (Resource: https://www.cs.cmu.edu/~motionplanning/lecture/Chap2-Bug-Alg_howie.pdf)
    */
    /**
     * Continues to the path set in destination, optional pathfinding end callback
     * @param rc Robot Controller
     * @throws GameActionException Game error
     */
    public static void step(RobotController rc) throws GameActionException {
        if(!rc.isSpawned()) return;         // We do not exist
        if(!rc.isMovementReady()) return;   // Already moved

        // We have somewhere to go and we exist
        if(destination != null) {
            MapLocation currLoc = rc.getLocation();
            int currDist = currLoc.distanceSquaredTo(destination);

            Direction bestMove = null;
            for (Direction direction : DIRECTIONS) {
                MapLocation newLoc = currLoc.add(direction);
                int newDist = newLoc.distanceSquaredTo(destination);
                if ((newDist < currDist || bestMove == null) && rc.canMove(direction)) {
                    bestMove = direction;
                }
            }

            if(bestMove != null) {
                rc.move(bestMove);
            }
        }

    }

    /**
     * Move once in one direction or its adjacent directions
     * @param rc Robot Controller
     * @param dir The direction to travel in
     * @throws GameActionException Game error
     */
    public static void moveOnce(RobotController rc, Direction dir) throws GameActionException {
        if(rc.canMove(dir)) rc.move(dir);
        else if(rc.canMove(dir.rotateLeft())) rc.move(dir.rotateLeft());
        else if(rc.canMove(dir.rotateRight())) rc.move(dir.rotateRight());
    }
}
