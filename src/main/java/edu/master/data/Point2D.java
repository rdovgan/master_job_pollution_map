package edu.master.data;

import static java.lang.Math.*;

/**
 * Created by rdovgan on 5/12/14.
 */
public class Point2D implements Comparable<Point2D>{

    private long x;
    private long y;


    public Point2D() {
        this.x = 0;
        this.y = 0;
    }

    public Point2D(double x, double y) {
        this.x = round(x);
        this.y = round(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = round(x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = round(y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point2D)) return false;

        Point2D point2D = (Point2D) o;

        if (x != point2D.x) return false;
        if (y != point2D.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (x ^ (x >>> 32));
        result = 31 * result + (int) (y ^ (y >>> 32));
        return result;
    }

    @Override
    public int compareTo(Point2D o) {
        double s1 = Math.sqrt(x*x+y*y);
        double s2 = Math.sqrt(o.getX()*o.getX()+o.getY()*o.getY());
        if(s1>s2)
            return 1;
        if(s2>s1)
            return -1;
        return 0;
    }
}
