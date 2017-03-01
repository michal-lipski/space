package space;

class PaintingDimensions {
    public final double centrex;
    public final double centrey;
    public final double scale;
    public final int frameWidth;
    public final int frameHeight;

    public PaintingDimensions(double centrex, double centrey, double scale, int width, int height) {

        this.centrex = centrex;
        this.centrey = centrey;
        this.scale = scale;
        this.frameWidth = width;
        this.frameHeight = height;
    }
}
