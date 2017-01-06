package space;


import java.awt.*;

public class SpaceGraphics {

    public void paintPhysicalObject(Graphics2D graphics, PhysicalObject po) {
        if (!Space.IS_BOUNCING_BALLS) {
            graphics.setColor(Space.weightToColor(po.mass));
            int diameter = po.mass >= Space.EARTH_WEIGHT * 10000 ? 7 : 2;
            int xtmp = (int) ((po.x - Space.centrex) / Space.scale + Space.frame.getSize().width / 2);
            int ytmp = (int) ((po.y - Space.centrey) / Space.scale + Space.frame.getSize().height / 2);
            graphics.fillOval(
                    xtmp-diameter/2,
                    ytmp-diameter/2,
                    diameter,
                    diameter);
        } else { //BREAKOUT
            graphics.setColor(Color.WHITE);
            int xtmp = (int) ((po.x - Space.centrex)  + Space.frame.getSize().width / 2);
            int ytmp = (int) ((po.y - Space.centrey)  + Space.frame.getSize().height / 2);
            graphics.fillOval(
                    (int) (xtmp - po.radius ),
                    (int) (ytmp - po.radius ),
                    (int) (2 * po.radius),
                    (int) (2 * po.radius));
        }
    }
}
