package space;

public class Space {


    static int createSpace(int width, boolean isBouncingBalls) {
        int nrOfObjects = 75;
        if (!isBouncingBalls) {
            int stepSize = 3600 * 24 * 7;

            double outerLimit = SpaceConstant.ASTRONOMICAL_UNIT * 20;

            for (int i = 0; i <  nrOfObjects; i++) {
                double angle = RandomSquareUtil.randSquare() * 2 * Math.PI;
                double radius = (0.1 + 0.9 * Math.sqrt(RandomSquareUtil.randSquare())) * outerLimit;
                double weightKilos = 1e3 * SpaceConstant.EARTH_WEIGHT * (Math.pow(0.00001 + 0.99999 * RandomSquareUtil.randSquare(), 12));
                double x = radius * Math.sin(angle);
                double y = radius * Math.cos(angle);
                double speedRandom = Math.sqrt(1 / radius) * 2978000*1500 * (0.4 + 0.6 * RandomSquareUtil.randSquare());

                double vx = speedRandom * Math.sin(angle - Math.PI / 2);
                double vy = speedRandom * Math.cos(angle - Math.PI / 2);
                SpaceApp.add(weightKilos, x, y, vx, vy, 1);
            }

            SpaceApp.scale = outerLimit / width;

            SpaceApp.add(SpaceConstant.EARTH_WEIGHT * 20000, 0, 0, 0, 0, 1);
            return stepSize;
        } else {
            nrOfObjects = 50;
            int stepSize = 1;
            for (int i = 0; i < nrOfObjects; i++) {
                // radius,weight in [1,20]
                double radiusAndWeight = 1 + 19 * Math.random();
                //x,y in [max radius, width or height - max radius]
                SpaceApp.add(radiusAndWeight, 20 + 760 * Math.random(), 20 + 760 * Math.random(), 3 - 6 * Math.random(), 3 - 6 * Math.random(), radiusAndWeight);
            }
            SpaceApp.scale = 1;
            SpaceApp.centrex = 400;
            SpaceApp.centrey = 390; //Must compensate for title bar
            return stepSize;
        }
    }
}
