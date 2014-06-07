var map, heatmap, heatMapGreen, heatMax;
var count = 0;
x0 = 48.291174;
y0 = 25.923094;
var step;
step = 0;
var maxPoint = [];
var points = [];
var pointsGreen = [];

var cellCount = 20;
var cellWidth = 1000;

var output, x0, y0, height, sigX, sigY, sigZ, atmosphere, temperature;
var timeBegin, timeEnd, timeDelta, eps, layerCount, layerHeight;

var json = '{"data":{"current_condition":[{"temp_C": "13", "winddirDegree": "310","windspeedKmph":"15"}]}}';
var jsonUrl = "http://api.worldweatheronline.com/free/v1/weather.ashx?q="+x0+"%3B"+y0+"%3B&format=json&num_of_days=1&key=ks9t3qx33mvme6t6238vfu56";
var heightMap =[];

function getJSON() {
    $.ajax({
        async : false,
        url: jsonUrl,
        type:"GET",
        crossDomain: true,
        dataType: "jsonp",
        success: function(response){
            json = JSON.stringify(response.data);
        },
        error : function(response){
            console.error(response);
        },
        beforeSend:function(){
            headers:{'Access-Control-Allow-Origin : http://localhost:8080'}
        }
    });
}

function getHeightMap(){
    var dx = 1./cellWidth;
    for(i=-parseInt(cellCount/2); i<parseInt(cellCount/2); i++)
        for(j=-parseInt(cellCount/2); j<parseInt(cellCount/2); j++){
            x = parseFloat(x0)+i*dx;
            y = parseFloat(y0)+j*dx;
            getHeight(x,y);
        }
}


$(document)
    .ready(function () {
        output = $('#output').val();
        x0 = $('#x0').val();
        y0 = $('#y0').val();
        height = $('#height').val();
        sigX = $('#sigX').val();
        sigY = $('#sigY').val();
        sigZ = $('#sigZ').val();
        atmosphere = $('#atmosphere').val();

        timeBegin = $('#timeBegin').val();
        timeEnd = $('#timeEnd').val();
        timeDelta = $('#timeDelta').val();
        eps = $('#eps').val();
        layerCount = $('#layerCount').val();
        layerHeight = $('#layerHeight').val();

        //get json with weather
        getJSON();
        $('#sendValues').click(function () {
            $("#getResult").removeClass("disabled");
            getHeightMap();
            $.ajax({
                type: "post",
                url: "getValues",
                data: "output=" + output + "&x0=" + x0 + "&y0=" + y0 + "&height=" + height + "&sigX=" + sigX + "&sigY=" + sigY + "&sigZ=" +
                    sigZ + "&atmosphere=" + atmosphere,
                success: function (response) {
                    console.info("Send values: success");
                },
                error: function (e) {
                    console.error(e);
                }
            });
            $.ajax({
                type: "post",
                url: "getJSON",
                data: json,
                success: function (response) {
                    console.info("Send JSON: success");
                },
                error : function(e){
                    console.error(e);
                }
            });
        });
        $('#clear').click(function () {
            $("#getResult").addClass("disabled");
            points = [];
            pointsGreen = [];
            maxPoint = [];
            initialize();
        });

        $('#getResult').click(function () {
            points = [];
            pointsGreen = [];
            maxPoint = [];
            $.ajax({
                type: "post",
                url: "getHeightMap",
                data: JSON.stringify(heightMap),
                success: function (data) {
                    console.info("Get result: success");
                    var result = $.parseJSON(data);
                    q = findMax(result);
                    maxPoint.push(new google.maps.LatLng(result[q].x, result[q].y));
                    for(i=0; i<result.length; i++){
                        if(result[i].c > output * 0.01){
                            var xPoint = result[i].x;
                            var yPoint = result[i].y;
                            var point = new google.maps.LatLng(x,y);
                            points.push(new google.maps.LatLng(xPoint, yPoint));
                        }
                        else if(result[i].c > output * 0.001){
                            var xPoint = result[i].x;
                            var yPoint = result[i].y;
                            var point = new google.maps.LatLng(x,y);
                            pointsGreen.push(new google.maps.LatLng(xPoint, yPoint));
                        }
                    }
                    initialize();
                },
                error : function(e){
                    console.error(e);
                }
            });
        });
    });

function initialize() {
    var pos = new google.maps.LatLng(x0, y0);
    var mapOptions = {
        zoom: 13,
        center: pos,
        mapTypeId: google.maps.MapTypeId.SATELLITE
    };

    map = new google.maps.Map(document.getElementById('map-canvas'),
        mapOptions);

    var pointArray = new google.maps.MVCArray(points);
    var pointArrayGreen = new google.maps.MVCArray(pointsGreen);
    var pointOne = new google.maps.MVCArray(maxPoint);

    heatmap = new google.maps.visualization.HeatmapLayer({
        data: pointArray
    });
    heatmap.setOptions({radius: 40});

    heatMapGreen = new google.maps.visualization.HeatmapLayer({
        data: pointArrayGreen
    });
    heatMapGreen.setOptions({radius: 20});

    heatMax = new google.maps.visualization.HeatmapLayer({
        data: pointOne
    });
    heatMax.setOptions({radius: 60});

    marker = new google.maps.Marker({
        map: map,
        draggable: true,
        animation: google.maps.Animation.DROP,
        position: pos
    });

    google.maps.event.addListener(marker, 'dragend', function () {
        var x,y;
        x = marker.position.lat();
        y = marker.position.lng();
        x0 = x;
        y0 = y;
        $('#x0').val(x);
        $('#y0').val(y);
    });
    changeGradient();
    changeGradientRed();
    heatMapGreen.setMap(map);
    heatmap.setMap(map);
//    heatMax.setMap(map);
    console.warn("Draw map");
}

function findMax(data){
    max = data[0].c;
    maxi = 0;
    for(i=0; i< data.length; i++){
        if(data[i].c > max){
            max = data[i].c;
            maxi = i;
        }
    }
    return maxi;
}

function changeGradient() {
    var gradient = [
        'rgba(200, 200, 200, 0)',
        'rgba(180, 200, 180, 1)',
        'rgba(160, 200, 160, 1)',
        'rgba(140, 200, 140, 1)',
        'rgba(120, 190, 120, 1)',
        'rgba(100, 190, 100, 1)',
        'rgba(70, 180, 70, 1)',
        'rgba(40, 180, 40, 1)',
        'rgba(10, 170, 10, 1)',
        'rgba(0, 170, 0, 1)',
        'rgba(0, 160, 0, 1)',
        'rgba(0, 160, 0, 1)',
        'rgba(0, 150, 0, 1)',
        'rgba(0, 150, 0, 1)'
    ]
    heatMapGreen.set('gradient', heatMapGreen.get('gradient') ? null : gradient);
}


function changeGradientRed() {
    var gradient = [
        'rgba(200, 60, 40, 0)',
        'rgba(180, 50, 30, 1)',
        'rgba(175, 30, 20, 1)',
        'rgba(170, 20, 15, 1)',
        'rgba(165, 14, 10, 1)',
        'rgba(160, 9, 5, 1)',
        'rgba(155, 5, 0, 1)',
        'rgba(150, 3, 0, 1)',
        'rgba(145, 1, 0, 1)',
        'rgba(140, 0, 0, 1)',
        'rgba(135, 0, 0, 1)',
        'rgba(130, 0, 0, 1)',
        'rgba(125, 0, 0, 1)',
        'rgba(120, 0, 0, 1)'
    ]
    heatMax.set('gradient', heatMax.get('gradient') ? null : gradient);
}

function changeRadius() {
    heatmap.setOptions({radius: heatmap.get("radius") == radiusLarge ? radiusPoint : radiusLarge});
    if (bool == 0) {
        heatmap.setOptions({radius: radiusPoint});
        square.value = Xmm;
        bool = 1;
    }
    else {
        heatmap.setOptions({radius: radiusLarge});
        square.value = Xmax;
        bool = 0;
    }
}

function getHeight(x,y) {
    var point = new google.maps.LatLng(x, y);
    var elevator = new google.maps.ElevationService();
    var locations = [];
    var clickedLocation = point;
    locations.push(clickedLocation);
    var positionalRequest = {
        'locations': locations
    }
    elevator.getElevationForLocations(positionalRequest, function (results, status) {
        if (status == google.maps.ElevationStatus.OK) {
            if (results[0]) {
                var result = (results[0]);
                heightMap.push({
                    x : x ,
                    y : y,
                    h : result.elevation

                });
            } else {
                alert('No results found');
            }
        } else {
            alert('Elevation service failed due to: ' + status);
        }
    });
}

function changeOpacity() {
    heatmap.setOptions({opacity: heatmap.get("opacity") ? null : 0.2});
}

google.maps.event.addDomListener(window, 'load', initialize);