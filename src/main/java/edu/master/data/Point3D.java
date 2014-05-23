package edu.master.data;

/**
 * Created by Roma on 05.05.14.
 */
public class Point3D extends Point2D{

    private long z;

    public Point3D() {
        super();
        this.z = 0;
    }

    public Point3D(double x, double y) {
        super(x,y);
        this.z = 0;
    }

    public Point3D(double x, double y, long z) {
        this(x,y);
        this.z = z;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }
}
