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
<div class="col-md-8 col-md-offset-2"><h1>Моделювання розповсюдження забруднень в повітрі </h1></div>
<div style="float: right; margin-top:20px; margin-right:20px;">
    <a id="info" class="btn btn-info btn-lg"
       data-toggle="modal" data-target="#info-modal"><span class="glyphicon glyphicon-info-sign"></span></a>
</div>
<div id="map-canvas" style="height: 75%; width: 100%;"></div>
<br/>


<div class="form-inline col-md-offset-2">
        <a id="sendProperties" class="btn btn-info btn-lg"
           data-toggle="modal" data-target="#properties-modal"><span class="glyphicon glyphicon-pencil"></span></a>
        <a id="getResult" class="btn btn-warning btn-lg disabled">Результат</a>
        <a id="clear" class="btn btn-danger btn-lg">Очистити</a>

</div>

<div class="modal fade" id="properties-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Задайте параметри</h4>
            </div>
            <div class="modal-body" style="min-height: 280px;">
                <div class="col-xs-12">
                    <div class="col-xs-4"><label for="output">Маса речовини (г) </label><input type="text" id="output"
                                                                                    class="form-control"
                                                                                    value="200">
                    </div>
                    <div class="col-xs-4"><label for="x0">X<sub>0</sub> </label><input type="text" id="x0"
                                                                                       class="form-control"
                                                                                       value="48.291174">
                    </div>
                    <div class="col-xs-4"><label for="y0">Y<sub>0</sub> </label><input type="text" id="y0"
                                                                                       class="form-control"
                                                                                       value="25.923094">
                    </div>

                    <div class="col-xs-4"><label for="sigX">&sigma;<sub>x</sub> </label><input type="text" id="sigX"
                                                                                               class="form-control"
                                                                                               value="9">
                    </div>
                    <div class="col-xs-4"><label for="sigY">&sigma;<sub>y</sub> </label><input type="text" id="sigY"
                                                                                               class="form-control"
                                                                                               value="8">
                    </div>
                    <div class="col-xs-4"><label for="sigZ">&sigma;<sub>z</sub> </label><input type="text" id="sigZ"
                                                                                               class="form-control"
                                                                                               value="7">
                    </div>
                    <div class="col-xs-4"><label for="atmosphere">Тип атмосфери </label>
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
                    <div class="col-xs-4"><label for="timeEnd">Тривалість (с)</label><input type="text" id="timeEnd"
                                                                                       class="form-control"
                                                                                       value="360">
                    </div>
                    <div class="col-xs-4"><label for="timeDelta">Проміжок (с) </label><input type="text" id="timeDelta"
                                                                                           class="form-control"
                                                                                           value="60">
                    </div>

                    <div class="col-xs-4"><label for="height">Висота викиду джерела(м)</label><input type="text" id="height"
                                                                                              class="form-control"
                                                                                              value="300">
                    </div>
                    <div class="col-xs-4"><label for="layerCount">Кількість атмосферних шарів </label><input type="text" id="layerCount"
                                                                                             class="form-control"
                                                                                             value="5">
                    </div>
                    <div class="col-xs-4"><label for="layerHeight">Товщина атмосферних шарів </label><input type="text"
                                                                                               id="layerHeight"
                                                                                               class="form-control"
                                                                                               value="200">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Відміна</button>
                <button type="button" id="sendValues" data-dismiss="modal" class="btn btn-primary">Відправити</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="info-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Класи атмосферної стабільності</h4>
            </div>
            <div class="modal-body" style="min-height: 450px;">
                <div class="col-xs-12">
                    <table class="table table-striped">
                        <tr>
                            <th width="20%">Клас</th>
                            <th>Ступінь стійкості атмосфери</th>
                            <th>Погода</th>
                        </tr>
                        <tr>
                            <td>A</td>
                            <td>дуже нестійка</td>
                            <td>дуже сонячна літня погода</td>
                        </tr>
                        <tr>
                            <td>B</td>
                            <td>середньо нестійка</td>
                            <td>сонячно і тепло</td>
                        </tr>
                        <tr>
                            <td>C</td>
                            <td>слабо нестійка</td>
                            <td>мінлива хмарність</td>
                        </tr>
                        <tr>
                            <td>D</td>
                            <td>нейтральна</td>
                            <td>хмарний день або ніч</td>
                        </tr>
                        <tr>
                            <td>E</td>
                            <td>слабо стійка</td>
                            <td>мінлива хмарність вночі</td>
                        </tr>
                        <tr>
                            <td>F</td>
                            <td>стійка</td>
                            <td>ясна ніч</td>
                        </tr>
                        <tr>
                            <td>G</td>
                            <td>сильно стійка</td>
                            <td>ясна холодна ніч, слабкий вітер</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>