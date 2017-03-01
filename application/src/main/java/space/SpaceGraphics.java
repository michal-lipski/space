package space;


import java.awt.*;

public class SpaceGraphics {

    public void paintPhysicalObject(Graphics2D graphics, PhysicalObject po) {
        if (!SpaceApp.IS_BOUNCING_BALLS) {
            graphics.setColor(SpaceApp.weightToColor(po.mass));
            int diameter = po.mass >= SpaceConstant.EARTH_WEIGHT * 10000 ? 7 : 2;
            int xtmp = (int) ((po.x - SpaceApp.centrex) / SpaceApp.scale + SpaceApp.frame.getSize().width / 2);
            int ytmp = (int) ((po.y - SpaceApp.centrey) / SpaceApp.scale + SpaceApp.frame.getSize().height / 2);
            graphics.fillOval(
                    xtmp-diameter/2,
                    ytmp-diameter/2,
                    diameter,
                    diameter);
        } else { //BREAKOUT
            graphics.setColor(Color.WHITE);
            int xtmp = (int) ((po.x - SpaceApp.centrex)  + SpaceApp.frame.getSize().width / 2);
            int ytmp = (int) ((po.y - SpaceApp.centrey)  + SpaceApp.frame.getSize().height / 2);
            graphics.fillOval(
                    (int) (xtmp - po.radius ),
                    (int) (ytmp - po.radius ),
                    (int) (2 * po.radius),
                    (int) (2 * po.radius));
        }
    }
}
