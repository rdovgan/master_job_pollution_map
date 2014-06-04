var map, pointArray, heatmap;
var count = 0;
x0 = 48.291174;
y0 = 25.923094;
var step;
step = 0;
var points = [];

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
            getHeightMap();
            $.ajax({
                type: "post",
                url: "getValues",
                data: "output=" + output + "&x0=" + x0 + "&y0=" + y0 + "&height=" + height + "&sigX=" + sigX + "&sigY=" + sigY + "&sigZ=" +
                    sigZ + "&atmosphere=" + atmosphere,
                success: function (response) {
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
                },
                error : function(e){
                    console.error(e);
                }
            });
        });

        $('#getResult').click(function () {
            $.ajax({
                type: "post",
                url: "getHeightMap",
                data: JSON.stringify(heightMap),
                success: function (data) {
                    var result = $.parseJSON(data);
                    var redPoints = 0;
                    for(i=0; i<result.length; i++){
                        if(result[i].c > 1)
                            redPoints++;
                    }
                    for(i=0; i<result.length; i++){
                        if(result[i].c > output * 0.01){
                            var xPoint = result[i].x / cellWidth;
                            var yPoint = result[i].y / cellWidth;
                            var point = new google.maps.LatLng(x,y);
                            points.push(new google.maps.LatLng(xPoint, yPoint));
                        }
                    }
                    initialize();
                },
                error : function(e){
                    console.error(e);
                }
            });
        });
        $('#test').click(function () {
            points = [
                new google.maps.LatLng(48.291174, 25.929094),
                new google.maps.LatLng(48.293134, 25.926024),
                new google.maps.LatLng(48.297134, 25.963024),
                new google.maps.LatLng(48.293634, 25.913024),
                new google.maps.LatLng(48.293154, 25.927024),
                new google.maps.LatLng(48.243134, 25.922024),
                new google.maps.LatLng(48.253134, 25.925024),
                new google.maps.LatLng(48.263134, 25.923024),
                new google.maps.LatLng(48.223134, 25.923324),
                new google.maps.LatLng(48.271124, 25.913434)
            ];
            initialize();
        });
    });

function initialize() {
    x.value = x0;
    y.value = y0;
    var pos = new google.maps.LatLng(x0, y0);
    var mapOptions = {
        zoom: 13,
        center: pos,
        mapTypeId: google.maps.MapTypeId.SATELLITE
    };

    map = new google.maps.Map(document.getElementById('map-canvas'),
        mapOptions);

    var pointArray = new google.maps.MVCArray(points);
    alert(points.length);

    heatmap = new google.maps.visualization.HeatmapLayer({
        data: pointArray
    });

    marker = new google.maps.Marker({
        map: map,
        draggable: true,
        animation: google.maps.Animation.DROP,
        position: pos
    });

    google.maps.event.addListener(marker, 'dragend', function () {
        x.value = marker.position.lat();
        y.value = marker.position.lng();
        x0 = x.value;
        y0 = y.value;
    });

    heatmap.setMap(map);
}

function changeGradient() {
    var gradient = [
        'rgba(0, 255, 255, 0)',
        'rgba(0, 255, 255, 1)',
        'rgba(0, 191, 255, 1)',
        'rgba(0, 127, 255, 1)',
        'rgba(0, 63, 255, 1)',
        'rgba(0, 0, 255, 1)',
        'rgba(0, 0, 223, 1)',
        'rgba(0, 0, 191, 1)',
        'rgba(0, 0, 159, 1)',
        'rgba(0, 0, 127, 1)',
        'rgba(63, 0, 91, 1)',
        'rgba(127, 0, 63, 1)',
        'rgba(191, 0, 31, 1)',
        'rgba(255, 0, 0, 1)'
    ]
    heatmap.set('gradient', heatmap.get('gradient') ? null : gradient);
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