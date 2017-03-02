package space;

import java.util.ArrayList;
import java.util.List;

public class SpaceLogic {


    static final double G = 6.67428e-11; // m3/kgs2
    static final double ASTRONOMICAL_UNIT = 149597870.7e3;
    static List<PhysicalObject> objects = new ArrayList<PhysicalObject>();

    static void logicStep() {
        if (!SpaceView.IS_BOUNCING_BALLS) {
            for (PhysicalObject aff : objects) {
                double fx = 0;
                double fy = 0;
                for (PhysicalObject oth : objects) {
                    if (aff == oth)
                        continue;
                    double[] d = new double[]{aff.x - oth.x, aff.y - oth.y};
                    double r2 = Math.pow(d[0], 2) + Math.pow(d[1], 2);
                    double f = G * aff.mass * oth.mass / r2;
                    double sqrtOfR2 = Math.sqrt(r2);
                    fx += f * d[0] / sqrtOfR2;
                    fy += f * d[1] / sqrtOfR2;
                }
                double ax = fx / aff.mass;
                double ay = fy / aff.mass;
                aff.x = aff.x - ax * Math.pow(SpaceView.seconds, 2) / 2 + aff.vx * SpaceView.seconds;
                aff.y = aff.y - ay * Math.pow(SpaceView.seconds, 2) / 2 + aff.vy * SpaceView.seconds;
                aff.vx = aff.vx - ax * SpaceView.seconds;
                aff.vy = aff.vy - ay * SpaceView.seconds;
            }
        } else {
            for (PhysicalObject physicalObject : objects) {
                physicalObject.x = physicalObject.x + physicalObject.vx * SpaceView.seconds;
                physicalObject.y = physicalObject.y + physicalObject.vy * SpaceView.seconds;
            }

        }
    }

    static void collide() {
        List<PhysicalObject> remove = new ArrayList<PhysicalObject>();
        for (PhysicalObject one : objects) {
            if (remove.contains(one))
                continue;
            for (PhysicalObject other : objects) {
                if (one == other || remove.contains(other))
                    continue;
                if (!SpaceView.IS_BOUNCING_BALLS) {
                    if (Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2)) < 5e9) {
                        one.absorb(other);
                        remove.add(other);
                    }
                } else {
                    double distance = Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2));
                    double collsionDistance = one.radius + other.radius;
                    if (distance < collsionDistance) {
                        one.hitBy(other, SpaceView.seconds);
                    }
                }
            }
            // Wall collision reverses speed in that direction
            if (SpaceView.IS_BOUNCING_BALLS) {
                if (one.x - one.radius < 0) {
                    one.vx = -one.vx;
                }
                if (one.x + one.radius > 800) {
                    one.vx = -one.vx;
                }
                if (one.y - one.radius < 0) {
                    one.vy = -one.vy;
                }
                if (one.y + one.radius > 800 && !SpaceView.IS_BREAKOUT) {
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

    public static void nonBouncingBallsMainLogic() {
        for (int i = 0; i < 50; i++) {
            // radius,weight in [1,20]
            double radiusAndWeight = 1 + 19 * Math.random();
            //x,y in [max radius, width or height - max radius]
            add(radiusAndWeight, 20 + 760 * Math.random(), 20 + 760 * Math.random(), 3 - 6 * Math.random(), 3 - 6 * Math.random(), radiusAndWeight);
        }
    }

    public static double bouncingBallsMainLogic(int nrOfObjects) {
        double outerLimit = ASTRONOMICAL_UNIT * 20;

        for (int i = 0; i < nrOfObjects; i++) {
            double angle = randSquare() * 2 * Math.PI;
            double radius = (0.1 + 0.9 * Math.sqrt(randSquare())) * outerLimit;
            double weightKilos = 1e3 * SpaceView.EARTH_WEIGHT * (Math.pow(0.00001 + 0.99999 * randSquare(), 12));
            double x = radius * Math.sin(angle);
            double y = radius * Math.cos(angle);
            double speedRandom = Math.sqrt(1 / radius) * 2978000*1500 * (0.4 + 0.6 * randSquare());

            double vx = speedRandom * Math.sin(angle - Math.PI / 2);
            double vy = speedRandom * Math.cos(angle - Math.PI / 2);
            add(weightKilos, x, y, vx, vy, 1);
        }
        return outerLimit;
    }

    public static PhysicalObject add(double weightKilos, double x, double y,
                                     double vx, double vy, double radius) {
        PhysicalObject physicalObject = new PhysicalObject(weightKilos, x, y,
                vx, vy, radius);
        objects.add(physicalObject);
        return physicalObject;
    }
}
