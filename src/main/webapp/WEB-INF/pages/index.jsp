<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>
        Application
    </title>

    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=visualization"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=visualization"></script>
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet">
    <script src="http://code.jquery.com/jquery.min.js"></script>
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script>
    <script src="../../resources/main.js" type="text/javascript"></script>
</head>
<body>
<div class="container col-md-9 col-md-offset-3"><h1>Google Maps API 3</h1></div>
<div id="map-canvas" style="height: 450px; width: 100%;"></div>
<br/>


<div class="container col-md-9 col-md-offset-2">
    <div class="col-md-1">
        <div>
            <a id="sendProperties" class="btn btn-info btn-lg"
               data-toggle="modal" data-target="#properties-modal"><span class="glyphicon glyphicon-pencil"></span></a>
        </div>
    </div>
    <div class="col-md-2">
        <div>
            <a id="getResult" class="btn btn-warning btn-lg disabled">Get result</a>
        </div>
    </div>
</div>

<div class="modal fade" id="properties-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Send values</h4>
            </div>
            <div class="modal-body" style="min-height: 260px;">
                <div class="col-xs-12">
                    <div class="col-xs-3"><label for="output">Output </label><input type="text" id="output"
                                                                                    class="form-control"
                                                                                    value="200">
                    </div>
                    <div class="col-xs-3"><label for="atmosphere">Atmosphere </label>
                        <select id="atmosphere" class="form-control">
                            <option>A</option>
                            <option>B</option>
                            <option>C</option>
                            <option>D</option>
                            <option>E</option>
                            <option>F</option>
                            <option>G</option>
                        </select>
                    </div>
                    <div class="col-xs-3"><label for="x0">X<sub>0</sub> </label><input type="text" id="x0"
                                                                                       class="form-control"
                                                                                       value="48.291174">
                    </div>
                    <div class="col-xs-3"><label for="y0">Y<sub>0</sub> </label><input type="text" id="y0"
                                                                                       class="form-control"
                                                                                       value="25.923094">
                    </div>
                    <div class="col-xs-3"><label for="height">Height </label><input type="text" id="height"
                                                                                    class="form-control"
                                                                                    value="300">
                    </div>
                    <div class="col-xs-3"><label for="sigX">&sigma;<sub>x</sub> </label><input type="text" id="sigX"
                                                                                               class="form-control"
                                                                                               value="9">
                    </div>
                    <div class="col-xs-3"><label for="sigY">&sigma;<sub>y</sub> </label><input type="text" id="sigY"
                                                                                               class="form-control"
                                                                                               value="8">
                    </div>
                    <div class="col-xs-3"><label for="sigZ">&sigma;<sub>z</sub> </label><input type="text" id="sigZ"
                                                                                               class="form-control"
                                                                                               value="7">
                    </div>
                    <div class="col-xs-3"><label for="timeBegin">Time begin </label><input type="text" id="timeBegin"
                                                                                           class="form-control"
                                                                                           value="0">
                    </div>
                    <div class="col-xs-3"><label for="timeEnd">Time end </label><input type="text" id="timeEnd"
                                                                                       class="form-control"
                                                                                       value="360">
                    </div>
                    <div class="col-xs-3"><label for="timeDelta">Time delta </label><input type="text" id="timeDelta"
                                                                                           class="form-control"
                                                                                           value="60">
                    </div>
                    <div class="col-xs-3"><label for="eps">Epsilon </label><input type="text" id="eps"
                                                                                  class="form-control"
                                                                                  value="0.01">
                    </div>
                    <div class="col-xs-3"><label for="layerCount">Layer count </label><input type="text" id="layerCount"
                                                                                             class="form-control"
                                                                                             value="5">
                    </div>
                    <div class="col-xs-3"><label for="layerHeight">Layer height </label><input type="text"
                                                                                               id="layerHeight"
                                                                                               class="form-control"
                                                                                               value="200">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="sendValues" data-dismiss="modal" class="btn btn-primary">Send</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>