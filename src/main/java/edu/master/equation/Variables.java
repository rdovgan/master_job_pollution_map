package edu.master.equation;

import edu.master.data.Point2D;

import static java.lang.Math.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by rdovgan on 5/12/14.
 */
public class Variables {

    private char atmosphere = 'A';

    private final double gamaA = 0.0098;
    private final double koefCp = 1.3;
    private final double t0 = 273;

    private TreeMap<Point2D, Wind> windMap = null;
    private Map<Point2D, Integer> heightMap = null;

    private int t = 293; //20*C

    public Variables() {
        windMap = new TreeMap<Point2D, Wind>();
        heightMap = new TreeMap<Point2D, Integer>();
    }

    public class Wind{
        public double u;
        public double v;

        public Wind(double u, double v){
            this.u = u;
            this.v = v;
        }
    }

    public double getQe(double x, double y, double z){
        return getWind(x,y,z)*koefCp*(t0 / t)*(t-t0);
    }

    public int getH(double x, double y){
        return (int)random()*100;//TODO: get value from heightMap
    }

    public Wind getWind(double x, double y){
        if(windMap==null)   return new Wind(1,1);
        Point2D point = new Point2D(x,y);
        Wind wind = windMap.get(point);
        if(wind == null){
            Map.Entry<Point2D, Wind> high = windMap.ceilingEntry(point);
            if(high==null){
                Map.Entry<Point2D, Wind> low = windMap.floorEntry(point);
                if(low==null)
                    return new Wind(1,1);
                return low.getValue();
            }
            return high.getValue();
        }
        return wind;
    }

    public Wind getWindFromJSON(double degree, double speed){
        if(speed<=0)return new Wind(0,0);
        double u = speed * cos(toRadians(degree));
        double v = speed * sin(toRadians(degree));
        return new Wind(u,v);
    }

    public double getU2D(double x, double y) {
        return getWind(x,y).u;
    }

    public double getV2D(double x, double y) {
        return getWind(x,y).v;
    }

    public double getU3D(double x, double y, double z) {
        return getU2D(x, y)*pow(z/getH(x,y), Table.getM(atmosphere));
    }

    public double getV3D(double x, double y, double z) {
        return getV2D(x, y)*pow(z/getH(x,y), Table.getM(atmosphere));
    }

    public double getWind(double x, double y, double z) {
        return sqrt(pow(getU3D(x, y, z), 2) + pow(getV3D(x, y, z), 2));
    }

    public double getRichardsonNumber(double x, double y, double z) {
        return 0;//TODO: get Richardson number
    }

    public double getK1() {
        return 0.1;
    }

    public double getZ1() {
        return 1;
    }

    public double getKg(double x, double y, double z) {
        return getK1() * (z / getZ1()) * pow(1 - getRichardsonNumber(x, y, z), 1. / 2);
    }

    public double getKz(double x, double y, double z) {
        int h = getH(x,y);
        if (z <= h) {
            return getK1() * pow(z / getZ1(), 1 - E);
        } else {
            return getK1() * pow(h / getZ1(), 1 - E);
        }
    }

    public double getDKz(double x, double y, double z){
        int h = getH(x,y);
        if(z <= h){
            return -(E-1.)*getK1()*pow(z/getZ1(), -E)/getZ1();
        }else{
            return 0;
        }
    }

    public char getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(char atmosphere) {
        if((atmosphere<'A')||(atmosphere>'G'))
            this.atmosphere='A';
        else
            this.atmosphere = atmosphere;
    }

    public double getGamaA() {
        return gamaA;
    }

    public double getKoefCp() {
        return koefCp;
    }

    public double getT0() {
        return t0;
    }

    public TreeMap<Point2D, Wind> getWindMap() {
        return windMap;
    }

    public void setWindMap(TreeMap<Point2D, Wind> windMap) {
        this.windMap = windMap;
    }

    public Map<Point2D, Integer> getHeightMap() {
        return heightMap;
    }

    public void setHeightMap(Map<Point2D, Integer> heightMap) {
        this.heightMap = heightMap;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }
}
