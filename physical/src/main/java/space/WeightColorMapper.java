package space;

import java.awt.Color;

public class WeightColorMapper {


        public static Color weightToColor(double weight) {
            if (weight < 1e10) return Color.GREEN;
            if (weight < 1e12) return Color.CYAN;
            if (weight < 1e14) return Color.MAGENTA;
            if (weight < 1e16) return Color.BLUE;
            if (weight < 1e18) return Color.GRAY;
            if (weight < 1e20) return Color.RED;
            if (weight < 1e22) return Color.ORANGE;
            if (weight < 1e25) return Color.PINK;
            if (weight < 1e28) return Color.YELLOW;
            return Color.WHITE;
        }
}
