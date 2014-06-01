package edu.master.equation;

import static java.lang.Math.*;

/**
 * Created by Roma on 09.05.14.
 */
public class Distribution {

    private double output;

    private double sigX; // м
    private double sigY; // м
    private double sigZ; // м

    private double x0;
    private double y0;
    private long h;

    private double u; // м/с

    public Distribution(){
        sigX = 9;
        sigY = 8;
        sigZ = 7;
        output = 10000; //TODO: get
        x0 = 50; //TODO: get
        y0 = 50; //TODO: get
        h = 10; //TODO: get
        u = 5; //TODO: get value from Variables.getWind() in every function
    }

    public double getDistribution(double x, double y, double z, double t){
        return output/((pow(2*PI, 3./2)*sigX*sigY*sigZ)) * exp(-pow((x-x0)-u*t, 2)/(2*pow(sigX,2))) *
                exp(-(pow(y-y0,2))/(2*pow(sigY,2))) * ( exp(-(pow(z-h,2))/(2*pow(sigZ,2))) +
                exp(-pow(z+h,2)/(2*pow(sigZ,2))));
    }//checked

    public double getDx(double x, double y, double z, double t){
        return -(output*(exp(-pow(h-z,2)/(2*pow(sigZ,2))) +exp(-pow(h+z,2)/(2*pow(sigZ,2))))*(-t*u+x-x0)*exp(-pow(t*u-x+x0,2)/(2*pow(sigX,2))-pow(y-y0,2)/(2*pow(sigY,2))))/
                (2*sqrt(2)*pow(PI,3./2)*pow(sigX,3)*sigY*sigZ);
    }//checked

    public double getDy(double x, double y, double z, double t){
            return -(output*(y-y0)*(exp(-pow(z-h,2)/(2*pow(sigZ,2)))+exp(-pow(h+z,2)/(2*pow(sigZ,2))))*exp(-pow(-t*u+x-x0,2)/(2*pow(sigX,2))-pow(y-y0,2)/(2*pow(sigY,2))))/
                    (2*sqrt(2)*pow(PI,3./2)*sigX*pow(sigY,3)*sigZ);
    }//return negative

    public double getDz(double x, double y, double z, double t){
        return (output*(-(z-h)*exp(-pow(z-h,2)/(2*pow(sigZ,2)))/pow(sigZ,2)-(h+z)*exp(-pow(h+z,2)/(2*pow(sigZ,2)))/pow(sigZ,2))*
                exp(-pow(-t*u+x-x0,2)/(2*pow(sigX,2))-pow(y-y0,2)/(2*pow(sigY,2))))/
                (2*sqrt(2)*pow(PI,3./2)*sigX*sigY*sigZ);
    }

    public double getD2x(double x, double y, double z, double t){
        return (output*(exp(-pow(z-h,2)/(2*pow(sigZ,2)))+exp(-pow(h+z,2)/(2*pow(sigZ,2))))*
                (pow(-t*u+x-x0,2)*exp(-pow(-t*u+x-x0,2)/(2*pow(sigX,2))-pow(y-y0,2)/(2*pow(sigY,2)))/pow(sigX,4)-
                exp(-pow(-t*u+x-x0,2)/(2*pow(sigX,2))-pow(y-y0,2)/(2*pow(sigY,2)))/pow(sigX,2)))/
                (2*sqrt(2)*pow(PI,3./2)*sigX*sigY*sigZ);
    }//wrong

    public double getD2y(double x, double y, double z, double t){
        return (output*(exp(-pow(z-h,2)/(2*pow(sigZ,2)))+exp(-pow(h+z,2)/(2*pow(sigZ,2))))*
                (pow(y-y0,2)*exp(-pow(-t*u+x-x0,2)/(2*pow(sigX,2))-pow(y-y0,2)/(2*pow(sigY,2)))/pow(sigY,4)-
                    exp(-pow(-t*u+x-x0,2)/(2*pow(sigX,2))-pow(y-y0,2)/(2*pow(sigY,2)))/pow(sigY,2)))/
                (2*sqrt(2)*pow(PI,3./2)*sigX*sigY*sigZ);
    }//wrong

    public double getD2z(double x, double y, double z, double t){
        return (output*(pow(h,2)*(exp((2*h*z)/pow(sigZ,2))+1)-(pow(sigZ,2)-pow(z,2))*(exp((2*h*z)/(pow(sigZ,2)))+1)-2*h*z*(exp((2*h*z)/(pow(sigZ,2)))-1)))*
                exp(1./2*(-pow(h+z,2)/pow(sigZ,2)-pow(t*u-x+x0,2)/pow(sigX,2)-(pow(y-y0,2)/pow(sigY,2))))/
                (2*sqrt(2)*pow(PI,3./2)*sigX*sigY*pow(sigZ,5));
    }//negative values

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double getSigX() {
        return sigX;
    }

    public void setSigX(double sigX) {
        this.sigX = sigX;
    }

    public double getSigY() {
        return sigY;
    }

    public void setSigY(double sigY) {
        this.sigY = sigY;
    }

    public double getSigZ() {
        return sigZ;
    }

    public void setSigZ(double sigZ) {
        this.sigZ = sigZ;
    }

    public double getX0() {
        return x0;
    }

    public void setX0(double x0) {
        this.x0 = x0;
    }

    public double getY0() {
        return y0;
    }

    public void setY0(double y0) {
        this.y0 = y0;
    }

    public long getH() {
        return h;
    }

    public void setH(long h) {
        this.h = h;
    }

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }


}
