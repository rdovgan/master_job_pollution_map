package edu.master.equation;

/**
 * Created by rdovgan on 5/12/14.
 */
public class Model {

    private double eps = 0.01;

    private double t1 = 0;
    private double t2 = 10;
    private double delta = 1;

    private int layerCount = 5;
    private int layerHeight = 200;

    private Variables vars = new Variables();
    private Distribution distr = new Distribution();
    private Table table = new Table();

    public Model() {
    }

    public Model(Variables vars, Distribution distr) {
        this.vars = vars;
        this.distr = distr;
    }

    public double eps(double value) {
        if (value < eps) return 0;
        return value;
    }

    public int getHeightLayer(int layerNumber, int height) {
        int layersHeight = (layerNumber + 1) * layerHeight;
        int result = layersHeight - height;
        if (result < 0) return 0;
        if (result > layerHeight) return layersHeight;
        return result;
    }

    public double integralAdvection(double x, double y, double z, double t, int layer) {
        int h = getHeightLayer(layer, vars.getH(x, y));
        double dx = distr.getDx(x, y, z, t);
        double dy = distr.getDy(x, y, z, t);
        double u = vars.getU3D(x, y, z);
        double v = vars.getV3D(x, y, z);
        double result = h * u * dx + h * v * dy;
//        System.out.println("dx = "+ eps(distr.getDx(x, y, z, t)));
//        System.out.println("dy = "+ eps(distr.getDy(x, y, z, t))+"\n");
        return result;//TODO:check advection; add eps
    }

    public double layerAdvection(double x, double y, double z, int layer) {
        double result = 0;
        double prevResult = -1;
        double tLeft, tRight, t;
        tLeft = t1;
        int n = (int) ((t2 - t1) / delta);
//        while (abs(prevResult - result) > eps) {
        prevResult = result;
        result = 0;
        for (int i = 0; i < n; i++) {
            tRight = tLeft + delta;
            t = (tRight + tLeft) / 2;
            result += delta * integralAdvection(x, y, z, t, layer);
            tLeft = tRight;
        }
//        }
        double heightLayer = getHeightLayer(layer, vars.getH(x, y));
        double distribution = distr.getDistribution(x, y, z, t1);
        result = distribution - result / heightLayer;
        return result;//add eps
    }

    //return concentration of point for all layers
    public double advection(double x, double y, double z) {
        double totalResult = 0;
        for (int l = 0; l < layerCount; l++) {
            totalResult += layerAdvection(x, y, z, l);
        }
        return totalResult;//add eps
    }

    public double integralHorizontalTurbulence(double x, double y, double z, double t, int layer) {
        int h = getHeightLayer(layer, vars.getH(x, y));
        //TODO:large result
        double k = vars.getKg(x, y, z);
        double d2x = distr.getD2x(x, y, z, t);
        double d2y = distr.getD2y(x, y, z, t);
        double qe = vars.getQe(x, y, h);
        double result = (k * (d2x + d2y) + qe) * table.h(h);
        return result;
    }

    public double layerHorizontalTurbulence(double x, double y, double z, int layer) {
        double result = 0;
        double prevResult = -1;
        double tLeft, tRight, t;
        tLeft = t1;
        int n = (int) ((t2 - t1) / delta);
//        while (abs(prevResult - result) > eps) {
        prevResult = result;
        result = 0;
        for (int i = 0; i < n; i++) {
            tRight = tLeft + delta;
            t = (tRight + tLeft) / 2;
            result += delta * integralHorizontalTurbulence(x, y, z, t, layer);
            tLeft = tRight;
        }
//        }
        int heightLayer = getHeightLayer(layer, vars.getH(x, y));
        double advection = layerAdvection(x, y, z, layer);
        result = advection + (result) / heightLayer;
        return result;
    }

    public double horizontalTurbulence(double x, double y, double z) {
        double totalResult = 0;
        for (int l = 0; l < layerCount; l++) {
            totalResult += layerHorizontalTurbulence(x, y, z, l);
        }
        return totalResult;
    }

    public double integralVerticalTurbulence(double x, double y, double z, double t, int layer) {
        int h = getHeightLayer(layer, vars.getH(x, y));
        double result = vars.getDKz(x, y, z) * distr.getDz(x, y, z, t);
        return eps(result);
    }

    public double layerVerticalTurbulence(double x, double y, double z, int layer) {
        double result = 0;
        double prevResult = -1;
        double tLeft, tRight, t;
        tLeft = t1;
        int n = (int) ((t2 - t1) / delta);
//        while (abs(prevResult - result) > eps) {
        prevResult = result;
        result = 0;
        for (int i = 0; i < n; i++) {
            tRight = tLeft + delta;
            t = (tRight + tLeft) / 2;
            result += delta * integralVerticalTurbulence(x, y, z, t, layer);
            tLeft = tRight;
        }
//        }
        double horizontalTurbulence = layerHorizontalTurbulence(x, y, z, layer);
        result = horizontalTurbulence + result;
        return result;
    }

    public double verticalTurbulence(double x, double y, double z) {
        double totalResult = 0;
        for (int l = 0; l < layerCount; l++) {
            totalResult += layerVerticalTurbulence(x, y, z, l);
        }
        return totalResult;
    }

    public double integralPhysicoChemical(double x, double y, double z, double t, int layer) {
        double r = table.getR(vars.getAtmosphere());
        double h = getHeightLayer(layer, vars.getH(x, y));
        double c = distr.getDistribution(x, y, z, t);
        double result = r * h * c;
        return eps(result);
    }

    public double layerPhysicoChemical(double x, double y, double z, int layer) {
        double result = 0;
        double prevResult = -1;
        double tLeft, tRight, t;
        tLeft = t1;
        int n = (int) ((t2 - t1) / delta);
//        while (abs(prevResult - result) > eps) {TODO:remove all 'while' or tLeft = t1;
        prevResult = result;
        result = 0;
        for (int i = 0; i < n; i++) {
            tRight = tLeft + delta;
            t = (tRight + tLeft) / 2;
            result += delta * integralPhysicoChemical(x, y, z, t, layer);
            tLeft = tRight;
        }
//        }
        double heightLayer = getHeightLayer(layer, vars.getH(x, y));
        double verticalTurbulence = layerVerticalTurbulence(x, y, z, layer);
        result = verticalTurbulence + result / heightLayer;
        return result;
    }

    public double physicoChemical(double x, double y, double z) {
        double totalResult = 0;
        for (int l = 0; l < layerCount; l++) {
            totalResult += layerPhysicoChemical(x, y, z, l);
        }
        return totalResult;
    }

    public double getT1() {
        return t1;
    }

    public void setT1(double t1) {
        this.t1 = t1;
    }

    public double getT2() {
        return t2;
    }

    public void setT2(double t2) {
        this.t2 = t2;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public Variables getVars() {
        return vars;
    }

    public void setVars(Variables vars) {
        this.vars = vars;
    }

    public Distribution getDistr() {
        return distr;
    }

    public void setDistr(Distribution distr) {
        this.distr = distr;
    }

    public int getLayerHeight() {
        return layerHeight;
    }

    public void setLayerHeight(int layerHeight) {
        this.layerHeight = layerHeight;
    }

    public int getLayerCount() {
        return layerCount;
    }

    public void setLayerCount(int layerCount) {
        this.layerCount = layerCount;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }
}
