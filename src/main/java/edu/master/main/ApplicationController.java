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
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static edu.master.equation.Table.degreeChange;
import static edu.master.equation.Table.speedChange;
import static java.lang.Math.*;


@Controller
@RequestMapping("/")
public class ApplicationController {

    private Model model = new Model();
    private Distribution distribution = new Distribution();
    private Variables variables = new Variables();

    private TreeMap<Point2D, Integer> heightMap = null;
    private TreeMap<Point2D, Wind> windMap = null;
    private List<Point2D> points = null;

    private double cellWidth = 1000;
    private int cellCount = 20;


    public Wind getWindFromPolar(double degree, double speed) {
        if (speed <= 0) return new Wind(0, 0);
        double u = speed * cos(toRadians(degree));
        double v = speed * sin(toRadians(degree));
        return new Wind(u, v);
    }

    public void setRandomWindMap(double x, double y, double degree, double speed) {
        windMap = new TreeMap<Point2D, Wind>();
        x = round(x * cellWidth);
        y = round(y * cellWidth);
        double dx = cellWidth/1000.;
        for (int i = -cellCount / 2; i < cellCount / 2; i++)
            for (int j = -cellCount / 2; j < cellCount / 2; j++) {
                windMap.put(new Point2D(x + i * dx, y + j * dx), getWindFromPolar(degree + random() * degreeChange, speed + random() * speedChange));
            }
        variables.setWindMap(windMap);
    }

    public JSONArray calculateLayer(int layer){
        model = new Model(variables, distribution);
        JSONArray jsonArray = new JSONArray();
        int n = points.size();
        JSONObject object = new JSONObject();
        for(int i=0; i<n; i++){
            Point2D point = points.get(i);
            long x = point.getX();
            long y = point.getY();
            int h = model.getVars().getHeight(x,y);

            Double c = model.layerPhysicoChemical(x,y,h,layer);
            if(c.isInfinite()||c.isNaN()) c = .0;
            object = new JSONObject();
            try {
                object.put("x",x/cellWidth);
                object.put("y",y/cellWidth);
                object.put("c", c);
            } catch (JSONException e) {
                System.out.println("Error while create JSON with result");
                e.printStackTrace();
            }
            jsonArray.put(object);
        }
        return jsonArray;
    }

    public JSONArray calculate() {
        model = new Model(variables, distribution);
        JSONArray jsonArray = new JSONArray();
        int n = points.size();
        JSONObject object = new JSONObject();
        for(int i=0; i<n; i++){
            Point2D point = points.get(i);
            long x = point.getX();
            long y = point.getY();
            int h = model.getVars().getHeight(x,y);

            Double c = model.physicoChemical(x,y,h);
            if(c.isInfinite()||c.isNaN()) c = .0;
            object = new JSONObject();
            try {
                object.put("x",x/cellWidth);
                object.put("y",y/cellWidth);
                object.put("c", c);
            } catch (JSONException e) {
                System.out.println("Error while create JSON with result");
                e.printStackTrace();
            }
            jsonArray.put(object);
        }
        return jsonArray;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Main page");
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
                     @RequestParam(value = "atmosphere", required = true) String atmosphere) {
        distribution.setOutput(Double.parseDouble(output));
        distribution.setX0(Double.parseDouble(x0)*cellWidth);
        distribution.setY0(Double.parseDouble(y0)*cellWidth);
        distribution.setH(round(Double.parseDouble(height)));
        distribution.setSigX(Double.parseDouble(sigX));
        distribution.setSigY(Double.parseDouble(sigY));
        distribution.setSigZ(Double.parseDouble(sigZ));
        variables.setAtmosphere(atmosphere.charAt(0));
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
            System.out.println("Error while convert JSON to weather points");
        }
        setRandomWindMap(distribution.getX0(), distribution.getY0(), windDegree, windSpeed);
        variables.setT((int) round(tempC));
        variables.setCellWidth((int) cellWidth);
        return "success";
    }

    @RequestMapping(value = "/getHeightMap", method = RequestMethod.POST)
    public @ResponseBody String getHeightMap(@RequestBody String data)throws UnsupportedEncodingException{
        data = URLDecoder.decode(data, "UTF-8");
        heightMap = new TreeMap<Point2D, Integer>();
        points = new ArrayList<Point2D>();
        try {
            JSONArray jsonArr = new JSONArray(data);
            for(int i=0; i<jsonArr.length(); i++){
                JSONObject object = jsonArr.getJSONObject(i);
                double x = round(Double.parseDouble(object.get("x").toString())*cellWidth);
                double y = round(Double.parseDouble(object.get("y").toString())*cellWidth);
                Integer h = (int)round(Double.parseDouble(object.get("h").toString()));
                Point2D point = new Point2D(x,y);
                points.add(new Point2D(x,y));
                heightMap.put(point, h);
            }
        } catch (JSONException e) {
            System.out.println("Error while convert JSON to heightMap");
        }
        variables.setHeightMap(heightMap);
        JSONArray result = calculate();
        System.out.println(result.toString());
        return result.toString();
    }

    @RequestMapping(value="/test", method = RequestMethod.POST)
    public @ResponseBody String test(){
        System.out.println();
        model.physicoChemical(10,10,20);
        return "success";
    }
}