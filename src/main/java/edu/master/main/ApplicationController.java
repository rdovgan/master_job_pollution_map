package edu.master.main;

import edu.master.data.Point2D;
import edu.master.equation.Distribution;
import edu.master.equation.Model;
import edu.master.equation.Variables;
import edu.master.equation.Wind;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Math.*;
import static edu.master.equation.Table.*;


@Controller
@RequestMapping("/")
public class ApplicationController {


    private Model model = new Model();
    private Distribution distribution = new Distribution();
    private Variables variables = new Variables();

    private Map<Point2D, Integer> heightMap = null;
    private TreeMap<Point2D, Wind> windMap = null;


    public Wind getWindFromPolar(double degree, double speed) {
        if (speed <= 0) return new Wind(0, 0);
        double u = speed * cos(toRadians(degree));
        double v = speed * sin(toRadians(degree));
        return new Wind(u, v);
    }

    public void setRandomWindMap(double x, double y, double degree, double speed) {
        windMap = new TreeMap<Point2D, Wind>();
        x = round(x * cellWidth) / cellWidth;
        y = round(y * cellWidth) / cellWidth;
        double dx = 1. / cellWidth;
        for (int i = -cellCount / 2; i < cellCount / 2; i++)
            for (int j = -cellCount / 2; j < cellCount / 2; j++) {
                windMap.put(new Point2D(x + i * dx, y + i * dx), getWindFromPolar(degree + random() * degreeChange, speed + random() * speedChange));
            }
    }

    public void setHeightMap() {
        heightMap = new TreeMap<Point2D, Integer>();
        //TODO:set map with data
    }

    public void calculate() {
        //TODO:set variables
        setHeightMap();
        model = new Model(variables, distribution);
        //TODO:get arr with results
    }

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "index";
    }

    @RequestMapping(value = "/getValues", method = RequestMethod.POST)
    public
    @ResponseBody
    String getValues(@RequestParam(value = "output", required = true) String output,
                     @RequestParam(value = "x0", required = true) String x0,
                     @RequestParam(value = "y0", required = true) String y0,
                     @RequestParam(value = "height", required = true) String height,
                     @RequestParam(value = "sigX", required = true) String sigX,
                     @RequestParam(value = "sigY", required = true) String sigY,
                     @RequestParam(value = "sigZ", required = true) String sigZ,
                     @RequestParam(value = "atmosphere", required = true) String atmosphere,
                     @RequestParam(value = "temperature", required = true) String temperature) {
        distribution.setOutput(Double.parseDouble(output));
        distribution.setX0(Double.parseDouble(x0));
        distribution.setY0(Double.parseDouble(y0));
        distribution.setH(round(Double.parseDouble(height)));
        distribution.setSigX(Double.parseDouble(sigX));
        distribution.setSigY(Double.parseDouble(sigY));
        distribution.setSigZ(Double.parseDouble(sigZ));
        variables.setAtmosphere(atmosphere.charAt(0));
        variables.setT(Integer.parseInt(temperature) + 273);//TODO:remove, it takes from json
        return "success";
    }

    @RequestMapping(value = "/getJSON", method = RequestMethod.POST)
    public
    @ResponseBody
    String getJSON(@RequestBody String data) throws UnsupportedEncodingException {
        double tempC = variables.getT();
        double windDegree = 50; //from North-East
        double windSpeed = 2;
        data = URLDecoder.decode(data, "UTF-8");
        try {
            JSONObject json = new JSONObject(data);
            JSONArray jsonArr = json.getJSONArray("current_condition");
            tempC = Double.parseDouble(jsonArr.getJSONObject(0).getString("temp_C")) + 273;
            windSpeed = Double.parseDouble(jsonArr.getJSONObject(0).getString("windspeedKmph")) / 3.6;
            windDegree = Double.parseDouble(jsonArr.getJSONObject(0).getString("winddirDegree"));
        } catch (JSONException e) {
            System.out.println("error");
        }
        setRandomWindMap(distribution.getX0(),distribution.getY0(), windDegree, windSpeed);
        variables.setT((int)round(tempC));
        return "success";
    }
}