package edu.master.main;

import edu.master.equation.Distribution;
import edu.master.equation.Model;
import edu.master.equation.Variables;

/**
 * Created by Roma on 09.05.14.
 */
public class Main {

    public static void main(String args[]) {
        Distribution distribution = new Distribution();
        Variables variables = new Variables();
        Model model = new Model(variables, distribution);
        for(int i=0; i<100; i+=4)
            System.out.println(i+" "+
        distribution.getDistribution(i,i,20,1));
    }

}
