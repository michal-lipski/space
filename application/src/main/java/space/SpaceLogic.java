package space;

import java.util.ArrayList;
import java.util.List;

public class SpaceLogic {


    static final double G = 6.67428e-11; // m3/kgs2
    static List<PhysicalObject> objects = new ArrayList<PhysicalObject>();

    static void logicStep() {
        if (!Space.IS_BOUNCING_BALLS) {
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
                aff.x = aff.x - ax * Math.pow(Space.seconds, 2) / 2 + aff.vx * Space.seconds;
                aff.y = aff.y - ay * Math.pow(Space.seconds, 2) / 2 + aff.vy * Space.seconds;
                aff.vx = aff.vx - ax * Space.seconds;
                aff.vy = aff.vy - ay * Space.seconds;
            }
        } else {
            for (PhysicalObject physicalObject : objects) {
                physicalObject.x = physicalObject.x + physicalObject.vx * Space.seconds;
                physicalObject.y = physicalObject.y + physicalObject.vy * Space.seconds;
            }

        }
    }
}
