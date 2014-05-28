<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>
        Application
    </title>

    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=visualization"></script>
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet">
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
    <script src="../../resources/main.js" type="text/javascript"></script>

</head>
<body>
<div class="container col-md-9 col-md-offset-3"><h1>Google Maps API 3</h1></div>
<div id="1map-canvas" style="height: 450px; width: 100%;"></div>
<br/>



<div class="container col-md-9 col-md-offset-3">

    <div id="input">
        <table>
            <tr>
                <td>x</td>
                <td><input id="x" size=5></td>
                <td>y</td>
                <td><input id="y" size=5></td>
                <td>Потужність викиду</td>
                <td><input id="lm" size=5 value=22 placeholder="г/с"></td>
                <td>
                    Висота викиду
                </td>
                <td><input id="lh" size=5 value=200 placeholder="м"></td>
            </tr>

            <tr>
                <td>Площа, м:</td>
                <td><input id="square" size=5></td>
                <td>Концентрація, г/м<sup>3</sup>:</td>
                <td><input id="concentr" size=5></td>
            </tr>
        </table>
    </div>
    <div class="col-md-9">
        <button onclick="toggleHeatmap()">Вирахувати</button>
        <button onclick="drawMap()">Намалювати</button>
        <button onclick="changeRadius()">Змінити радіус</button>
        <button onclick="changeOpacity()">Прозорість</button>
    </div>
    <form class="form-group col-md-6">
        <table border="0" class="table">
            <tr>
                <td>
                    <label for="output">Output </label><input type="text" id="output" class="form-control" value="200">
                </td>
                <td>
                    <label for="atmosphere">Atmosphere </label>
                    <select id="atmosphere" class="form-control">
                        <option>A</option>
                        <option>B</option>
                        <option>C</option>
                        <option>D</option>
                        <option>E</option>
                        <option>F</option>
                        <option>G</option>
                    </select>
                </td>
                <td>
                    <label for="temperature">Temperature </label><input type="text" id="temperature"
                                                                        class="form-control" value="25">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="x0">X<sub>0</sub> </label><input type="text" id="x0" class="form-control" value="48.291174">
                </td>
                <td>
                    <label for="y0">Y<sub>0</sub> </label><input type="text" id="y0" class="form-control" value="25.923094">
                </td>
                <td>
                    <label for="height">Height </label><input type="text" id="height" class="form-control" value="300">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="sigX">&sigma;<sub>x</sub> </label><input type="text" id="sigX" class="form-control"
                                                                         value="9">
                </td>
                <td>
                    <label for="sigY">&sigma;<sub>y</sub> </label><input type="text" id="sigY" class="form-control"
                                                                         value="8">
                </td>
                <td>
                    <label for="sigZ">&sigma;<sub>z</sub> </label><input type="text" id="sigZ" class="form-control"
                                                                         value="7">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="timeBegin">Time begin </label><input type="text" id="timeBegin" class="form-control"
                                                                         value="0">
                </td>
                <td>
                    <label for="timeEnd">Time end </label><input type="text" id="timeEnd" class="form-control"
                                                                         value="360">
                </td>
                <td>
                    <label for="timeDelta">Time delta </label><input type="text" id="timeDelta" class="form-control"
                                                                         value="60">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="eps">Epsilon </label><input type="text" id="eps" class="form-control"
                                                                      value="0.01">
                </td>
                <td>
                    <label for="layerCount">Layer count </label><input type="text" id="layerCount" class="form-control"
                                                                         value="5">
                </td>
                <td>
                    <label for="layerHeight">Layer height </label><input type="text" id="layerHeight" class="form-control"
                                                                         value="200">
                </td>
            </tr>
            <tr>
                <td>
                    <a id="sendValues" class="btn btn-primary col-md-12">Send values</a>
                </td>
                <td>
                    <a id="getResult" class="btn btn-warning col-md-12">Get result</a>
                </td>
                <td>

                </td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>