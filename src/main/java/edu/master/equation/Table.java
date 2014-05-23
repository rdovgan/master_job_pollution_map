package edu.master.equation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roma on 15.05.14.
 */
public final class Table {

    private static Map<Character, Double> m = new HashMap<Character, Double>();
    private static Map<Character, TwoDoubles> j = new HashMap<Character, TwoDoubles>();
    private static Map<Character, Double> sigma = new HashMap<Character, Double>();
    private static Map<Character, Integer> hTop = new HashMap<Character, Integer>();
    private static Map<Character, Integer> hLower = new HashMap<Character, Integer>();
    private static Map<Character, TwoDoubles> windRange = new HashMap<Character, TwoDoubles>();
    private static Map<Character, Double> k = new HashMap<Character, Double>();
    private static Map<Character, Double> r = new HashMap<Character, Double>();


    public class TwoDoubles{
        public double d1;
        public double d2;

        public TwoDoubles(double d1, double d2){
            this.d1 = d1;
            this.d2 = d2;
        }
    }

    public Table(){
        m.put('A',0.08);
        m.put('B',0.165);
        m.put('C',0.215);
        m.put('D',0.31);
        m.put('E',0.405);
        m.put('F',0.43);
        m.put('G',0.44);

        j.put('A',new TwoDoubles(-0.19, -0.019));
        j.put('B',new TwoDoubles(-0.019, -0.017));
        j.put('C',new TwoDoubles(-0.017, -0.015));
        j.put('D',new TwoDoubles(-0.015, -0.005));
        j.put('E',new TwoDoubles(-0.005, 0.015));
        j.put('F',new TwoDoubles(0.015, 0.04));
        j.put('G',new TwoDoubles(0.04, 0.4));

        sigma.put('A', 25.);
        sigma.put('B', 20.);
        sigma.put('C', 15.);
        sigma.put('D', 10.);
        sigma.put('E', 5.);
        sigma.put('F', 2.5);
        sigma.put('G', 1.7);

        hTop.put('A', 2000);
        hTop.put('B', 1500);
        hTop.put('C', 1000);
        hTop.put('D', 750);
        hTop.put('E', 300);
        hTop.put('F', 250);
        hTop.put('G', 250);

        hLower.put('A', 250);
        hLower.put('B', 250);
        hLower.put('C', 150);
        hLower.put('D', 150);
        hLower.put('E', 150);
        hLower.put('F', 100);
        hLower.put('G', 100);

        windRange.put('A', new TwoDoubles(0.5, 2));
        windRange.put('B', new TwoDoubles(0.5, 2));
        windRange.put('C', new TwoDoubles(2, 10));
        windRange.put('D', new TwoDoubles(3, 10));
        windRange.put('E', new TwoDoubles(2, 5));
        windRange.put('F', new TwoDoubles(0.5, 3));
        windRange.put('G', new TwoDoubles(0.5, 1));

        k.put('A', 160.);
        k.put('B', 100.);
        k.put('C', 70.);
        k.put('D', 15.);
        k.put('E', 5.);
        k.put('F', 1.5);
        k.put('G', .13);

        r.put('A', 1.);
        r.put('B', 0.87);
        r.put('C', 0.7);
        r.put('D', 0.58);
        r.put('E', 0.51);
        r.put('F', 0.43);
        r.put('G', 0.3);

    }

    public static Map<Character, Double> getM() {
        return m;
    }

    public static Map<Character, TwoDoubles> getJ() {
        return j;
    }

    public static Map<Character, Double> getSigma() {
        return sigma;
    }

    public static Map<Character, Integer> getHTop() {
        return hTop;
    }

    public static Map<Character, Integer> getHLower() {
        return hLower;
    }

    public static Map<Character, TwoDoubles> getWindRange() {
        return windRange;
    }

    public static Map<Character, Double> getK() {
        return k;
    }

    public static Map<Character, Double> getR() {
        return r;
    }

    public static double getM(char c){
        return m.get(c);
    }

    public static double getJBegin(char c){
        return j.get(c).d1;
    }

    public double getJEnd(char c){
        return j.get(c).d2;
    }

    public static double getSigma(char c){
        return sigma.get(c);
    }

    public static int getHTop(char c){
        return hTop.get(c);
    }

    public static int getHLower(char c){
        return hLower.get(c);
    }

    public static double getWindRangeBegin(char c){
        return windRange.get(c).d1;
    }

    public static double getWindRangeEnd(char c){
        return windRange.get(c).d2;
    }

    public static double getK(char c){
        return k.get(c);
    }

    public static double getR(char c){
        return r.get(c);
    }
}
