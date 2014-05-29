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

import static edu.master.equation.Table.degreeChange;
import static edu.master.equation.Table.speedChange;
import static java.lang.Math.*;


@Controller
@RequestMapping("/")
public class ApplicationController {


    private Model model = new Model();
    private Distribution distribution = new Distribution();
    private Variables variables = new Variables();

    private Map<Point2D, Integer> heightMap = null;
    private TreeMap<Point2D, Wind> windMap = null;

    private int cellWidth = 1000;
    private int cellCount = 20;


    public Wind getWindFromPolar(double degree, double speed) {
        if (speed <= 0) return new Wind(0, 0);
        double u = speed * cos(toRadians(degree));
        double v = speed * sin(toRadians(degree));
        return new Wind(u, v);
    }

    public void setRandomWindMap(double x, double y, double degree, double speed) {
        windMap = new TreeMap<Point2D, Wind>();
        x = round(x * cellWidth) * (1./cellWidth);
        y = round(y * cellWidth) * (1./ cellWidth);
        double dx = 1. / cellWidth;
        for (int i = -cellCount / 2; i < cellCount / 2; i++)
            for (int j = -cellCount / 2; j < cellCount / 2; j++) {
                windMap.put(new Point2D(x + i * dx, y + j * dx), getWindFromPolar(degree + random() * degreeChange, speed + random() * speedChange));
            }
    }

    public JSONObject calculate() {
        model = new Model(variables, distribution);
        double x0 = round(distribution.getX0()*cellWidth)*(1./cellWidth);
        double y0 = round(distribution.getY0()*cellWidth)*(1./cellWidth);
        JSONArray jsonArray = new JSONArray();
        for(int i=-cellCount/2; i<cellCount/2; i++){
            for(int j=-cellCount/2; j<cellCount/2; j++){
                double x = x0+i*(1./cellWidth);
                double y = y0+j*(1./cellWidth);
                double c = model.physicoChemical(x,y,heightMap.get(new Point2D(x,y)));
                JSONObject object = new JSONObject();
                try {
                    object.put("x",x);
                    object.put("y",y);
                    object.put("c",c);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(object);
            }
        }
        JSONObject result = new JSONObject(jsonArray);
        return result;
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
                     @RequestParam(value = "atmosphere", required = true) String atmosphere) {
        distribution.setOutput(Double.parseDouble(output));
        distribution.setX0(Double.parseDouble(x0));
        distribution.setY0(Double.parseDouble(y0));
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
            System.out.println("error");
        }
        setRandomWindMap(distribution.getX0(), distribution.getY0(), windDegree, windSpeed);
        variables.setT((int) round(tempC));
        variables.setCellWidth(cellWidth);
        return "success";
    }

    @RequestMapping(value = "/getConf", method = RequestMethod.POST)
    public @ResponseBody String getConf(@RequestParam(value = "timeBegin", required = true) String timeBegin,
                                        @RequestParam(value = "timeEnd", required = true) String timeEnd,
                                        @RequestParam(value = "timeDelta", required = true) String timeDelta,
                                        @RequestParam(value = "eps", required = true) String eps,
                                        @RequestParam(value = "layerCount", required = true) String layerCount,
                                        @RequestParam(value = "layerHeight", required = true) String layerHeight,
                                        @RequestParam("cellCount")String cellCountStr,
                                        @RequestParam("cellWidth")String cellWidthStr){
        cellWidth = Integer.parseInt(cellWidthStr);
        cellCount = Integer.parseInt(cellCountStr);
        variables.setCellWidth(cellWidth);
        model.setT1(Double.parseDouble(timeBegin));
        model.setT2(Double.parseDouble(timeEnd));
        model.setDelta(Double.parseDouble(timeDelta));
        model.setEps(Double.parseDouble(eps));
        model.setLayerCount(Integer.parseInt(layerCount));
        model.setLayerHeight(Integer.parseInt(layerHeight));
        return "success";
    }

    @RequestMapping(value = "/getHeightMap", method = RequestMethod.POST)
    public @ResponseBody String getHeightMap(@RequestBody String data)throws UnsupportedEncodingException{
        data = URLDecoder.decode(data, "UTF-8");
        try {
            JSONArray jsonArr = new JSONArray(data);
            for(int i=0; i<jsonArr.length(); i++){
                //TODO:check why do one time only
                JSONObject object = jsonArr.getJSONObject(i);
                double x = round(Double.parseDouble(object.get("x").toString())*cellWidth)*(1./cellWidth);
                double y = round(Double.parseDouble(object.get("y").toString())*cellWidth)*(1./cellWidth);
                int h = (int)round(Double.parseDouble(object.get("h").toString()));
                System.out.println(x+"\t"+y+"\t"+h);
                Point2D point = new Point2D(x,y);
                heightMap.put(point, h);
            }
        } catch (JSONException e) {
            System.out.println("error");
        }
        return "success";
    }

    @RequestMapping(value = "/getResult", method = RequestMethod.POST)
    public @ResponseBody String getResult(){
        return calculate().toString();
    }
}