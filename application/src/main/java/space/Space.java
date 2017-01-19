package space;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Space implements Serializable {
    static double seconds = 1;
    private static int nrOfObjects = 75;
    int step = 0;
    List<PhysicalObject> objects = new ArrayList<PhysicalObject>();
    static boolean IS_BOUNCING_BALLS = false;
    static boolean IS_BREAKOUT = false; // Opens bottom, only active if IS_BOUNCING_BALLS is true

    public Space() {
    }

    SpaceConfig create(int width) {
        if (!IS_BOUNCING_BALLS) {
            setStepSize(3600 * 24 * 7);

            double outerLimit = SpaceConstants.ASTRONOMICAL_UNIT * 20;

            for (int i = 0; i < nrOfObjects; i++) {
                double angle = randSquare() * 2 * Math.PI;
                double radius = (0.1 + 0.9 * Math.sqrt(randSquare())) * outerLimit;
                double weightKilos = 1e3 * SpaceConstants.EARTH_WEIGHT * (Math.pow(0.00001 + 0.99999 * randSquare(), 12));
                double x = radius * Math.sin(angle);
                double y = radius * Math.cos(angle);
                double speedRandom = Math.sqrt(1 / radius) * 2978000 * 1500 * (0.4 + 0.6 * randSquare());

                double vx = speedRandom * Math.sin(angle - Math.PI / 2);
                double vy = speedRandom * Math.cos(angle - Math.PI / 2);
                add(weightKilos, x, y, vx, vy, 1);
            }

            SpaceApp.scale = outerLimit / width;

            add(SpaceConstants.EARTH_WEIGHT * 20000, 0, 0, 0, 0, 1);
            return new SpaceConfig(outerLimit / width, 400, 390);
        } else {
            nrOfObjects = 50;
            setStepSize(1); // One second per iteration
            for (int i = 0; i < nrOfObjects; i++) {
                // radius,weight in [1,20]
                double radiusAndWeight = 1 + 19 * Math.random();
                //x,y in [max radius, width or height - max radius]
                add(radiusAndWeight, 20 + 760 * Math.random(), 20 + 760 * Math.random(), 3 - 6 * Math.random(), 3 - 6 * Math.random(), radiusAndWeight);
            }
            SpaceApp.scale = 1;
            SpaceApp.centrex = 400;
            SpaceApp.centrey = 390; //Must compensate for title bar
            return new SpaceConfig(1,400,390);
        }
    }

    public void step() {
        if (!IS_BOUNCING_BALLS) {
            for (PhysicalObject aff : objects) {
                double fx = 0;
                double fy = 0;
                for (PhysicalObject oth : objects) {
                    if (aff == oth)
                        continue;
                    double[] d = new double[]{aff.x - oth.x, aff.y - oth.y};
                    double r2 = Math.pow(d[0], 2) + Math.pow(d[1], 2);
                    double f = SpaceConstants.G * aff.mass * oth.mass / r2;
                    double sqrtOfR2 = Math.sqrt(r2);
                    fx += f * d[0] / sqrtOfR2;
                    fy += f * d[1] / sqrtOfR2;
                }
                double ax = fx / aff.mass;
                double ay = fy / aff.mass;
                aff.x = aff.x - ax * Math.pow(seconds, 2) / 2 + aff.vx * seconds;
                aff.y = aff.y - ay * Math.pow(seconds, 2) / 2 + aff.vy * seconds;
                aff.vx = aff.vx - ax * seconds;
                aff.vy = aff.vy - ay * seconds;
            }
        } else {
            for (PhysicalObject physicalObject : objects) {
                physicalObject.x = physicalObject.x + physicalObject.vx * seconds;
                physicalObject.y = physicalObject.y + physicalObject.vy * seconds;
            }

        }
        step++;

    }

    public PhysicalObject add(double weightKilos, double x, double y,
                              double vx, double vy, double radius) {
        PhysicalObject physicalObject = new PhysicalObject(weightKilos, x, y,
                vx, vy, radius);
        objects.add(physicalObject);
        return physicalObject;
    }

    void collide() {
        List<PhysicalObject> remove = new ArrayList<PhysicalObject>();
        for (PhysicalObject one : objects) {
            if (remove.contains(one))
                continue;
            for (PhysicalObject other : objects) {
                if (one == other || remove.contains(other))
                    continue;
                if (!IS_BOUNCING_BALLS) {
                    if (Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2)) < 5e9) {
                        one.absorb(other);
                        remove.add(other);
                    }
                } else {
                    double distance = Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2));
                    double collsionDistance = one.radius + other.radius;
                    if (distance < collsionDistance) {
                        one.hitBy(other, seconds);
                    }
                }
            }
            // Wall collision reverses speed in that direction
            if (IS_BOUNCING_BALLS) {
                if (one.x - one.radius < 0) {
                    one.vx = -one.vx;
                }
                if (one.x + one.radius > 800) {
                    one.vx = -one.vx;
                }
                if (one.y - one.radius < 0) {
                    one.vy = -one.vy;
                }
                if (one.y + one.radius > 800 && !IS_BREAKOUT) {
                    one.vy = -one.vy;
                } else if (one.y - one.radius > 800) {
                    remove.add(one);
                }
            }
        }
        objects.removeAll(remove);
    }

    static double randSquare() {
        double random = Math.random();
        return random * random;
    }

    public void setStepSize(double seconds) {
        Space.seconds = seconds;
    }

    String spaceInfo() {
        return "Objects:" + objects.size() + " scale:" + SpaceApp.scale + " steps:" + step;
    }

    boolean isNotBalls() {
        return !IS_BOUNCING_BALLS;
    }
}