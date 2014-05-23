package edu.master.main;

import edu.master.equation.Distribution;

/**
 * Created by Roma on 09.05.14.
 */
public class Main {

    public void main(String args[]) {
        Distribution distribution = new Distribution();
        for (int t = 1; t < 11; t += 5)
            for (int x = 0; x < 100; x += 10)
                for (int y = 0; y < 100; y += 10)
                    System.out.println("x=" + x + "\t y=" + y + "\t t=" + t + "\t\t c=" + r(distribution.getDistribution(x, y, 10, t)));


    }

    public double r(double value) {
        double newVal = value * 1000;
        newVal = Math.round(newVal);
        return newVal / 1000.;
    }

}
