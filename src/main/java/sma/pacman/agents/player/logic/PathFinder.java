package sma.pacman.agents.player.logic;

import sma.pacman.game.Direction;
import sma.pacman.util.PointUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PathFinder {

    private Integer[][] map;
    Integer mapWidth;
    Integer mapHeight;

    /** The set of nodes that have been searched through */
//    private ArrayList closed = new ArrayList();
    /** The set of nodes that we do not yet consider fully searched */
    private List<Node> open = new ArrayList<Node>();
    private Set<Node> closed = new HashSet<Node>();

    private Node[][] nodes;

    public PathFinder(Integer[][] map) {
        this.map = map;
        this.mapWidth = map.length;
        this.mapHeight = map[0].length;

        nodes = new Node[mapWidth][mapHeight];
        Point p = new Point();
        for (p.x = 0; p.x <  mapWidth; ++p.x) {
            for (p.y = 0; p.y < mapHeight; ++p.y) {
                nodes[p.x][p.y] = new Node(new Point(p));
            }
        }
    }

    public Queue<Direction> findPath(Point source, Point target) {
        if (isBlocked(target)) {
            return null;
        }

        Node sourceNode = nodes[source.x][source.y];
        Node targetNode = nodes[target.x][target.y];

        clearAll();

        sourceNode.cost = 0;
        open.add(sourceNode);

        while (!open.isEmpty()) {
            Node currentNode = open.get(0);

            if (currentNode == targetNode) {
                break;
            }

            open.remove(0);
            closed.add(currentNode);

            for(Direction direction: Direction.values()) {
                Point neighbour = new Point(currentNode.position);
                PointUtils.translateTo(neighbour, direction);

                if(!isValid(neighbour)) {
                    continue;
                }

                Node neighbourNode = nodes[neighbour.x][neighbour.y];

                Float nextStepCost = currentNode.cost + getMovementCost(neighbour);

                if (nextStepCost < neighbourNode.cost) {
                    open.remove(neighbourNode);
                    closed.remove(neighbourNode);
                }

                if (!open.contains(neighbourNode) && !closed.contains(neighbourNode)) {
                    neighbourNode.cost = nextStepCost;
                    neighbourNode.heuristic = getHeuristicCost(neighbour, target);
                    neighbourNode.parent = currentNode;
                    neighbourNode.move = direction;

                    open.add(neighbourNode);

                    Collections.sort(open);
                }
            }
        }

        if (targetNode.move == null) {
            return null;
        }

        Deque<Direction> path = new ArrayDeque<Direction>();

        Node currentNode = targetNode;
        while(currentNode != sourceNode) {
            path.addFirst(currentNode.move);
            currentNode = currentNode.parent;
        }

        return path;
    }

    protected void clearAll() {
        closed.clear();
        open.clear();

        Point p = new Point();
        for (p.x = 0; p.x <  mapWidth; ++p.x) {
            for (p.y = 0; p.y < mapHeight; ++p.y) {
                Node node = nodes[p.x][p.y];
                node.cost = Float.POSITIVE_INFINITY;
                node.heuristic = Float.POSITIVE_INFINITY;
                node.move = null;
                node.parent = null;
            }
        }
    }

    protected Boolean isValid(Point p) {
        if(p.x < 0 || p.y < 0) {
            return false;
        }

        if(p.x >= mapWidth || p.y >= mapHeight) {
            return false;
        }

        return true;
    }

    protected Boolean isBlocked(Point p) {
        if(!isValid(p)) {
            return true;
        }

        return (map[p.x][p.y] == 1);
    }

    protected Float getMovementCost(Point p) {
        if(isBlocked(p)) {
            return Float.POSITIVE_INFINITY;
        }

        return 1f;
    }

    protected Float getHeuristicCost(Point p, Point target) {
        return (float) p.distance(target);
    }

    private class Node implements Comparable {
        private Point position;
        private float cost;
        private Direction move;
        private float heuristic;
        private Node parent;

        public Node(Point position) {
            this.position = position;
        }

        public int compareTo(Object other) {
            Node o = (Node) other;

            float f = heuristic + cost;
            float of = o.heuristic + o.cost;

            if (f < of) {
                return -1;
            } else if (f > of) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
