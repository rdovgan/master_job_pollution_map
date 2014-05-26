var map, pointarray, heatmap;
var arrTemp = [];
var data = [];
var arrH = [];
var radiusPoint = 20;
var radiusLarge = 80;
x0 = 48.291174;
y0 = 25.923094;
var n = 1
var koef = 0.00005;
var step;
step = 0;
var Cmm, Cmax, Xmm, Xmax;
var bool = 1;
var pointH = [];
var marker;

var output, x0, y0, height, sigX, sigY, sigZ, atmosphere, temperature;

var json = "empty";
var jsonUrl = "http://api.worldweatheronline.com/free/v1/weather.ashx?q="+x0+"%3B"+y0+"%3B&format=json&num_of_days=1&key=ks9t3qx33mvme6t6238vfu56";


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
        temperature = $('#temperature').val();

        //get json with weather
        getJSON();

        $('#sendValues').click(function () {
            $.ajax({
                type: "post",
                url: "getValues",
                data: "output=" + output + "&x0=" + x0 + "&y0=" + y0 + "&height=" + height + "&sigX=" + sigX + "&sigY=" + sigY + "&sigZ=" +
                    sigZ + "&atmosphere=" + atmosphere + "&temperature=" + temperature,
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
            alert('getResult');
        });
    });

function test() {
    alert(output + " " + x0 + " " + y0 + " " + height + " " + sigX + " " + sigY + " " + sigZ + " " + atmosphere + " " + temperature);
}

function initialize() {
    x.value = x0;
    y.value = y0;
    var pos = new google.maps.LatLng(x0, y0);
    var mapOptions = {
        zoom: 13,
        center: pos,
        mapTypeId: google.maps.MapTypeId.SATELLITE
    };

    map = new google.maps.Map(document.getElementById("map-canvas"),
        mapOptions);

    pointArray = new google.maps.MVCArray(data);

    heatmap = new google.maps.visualization.HeatmapLayer({
        data: pointArray
    });

    heatmap.setMap(null);
    heatmap.setOptions({radius: radiusPoint});

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
}

function changeH(temp, step) {
    var elevator = new google.maps.ElevationService();
    var locations = [];
    var clickedLocation = temp;
    locations.push(clickedLocation);
    var positionalRequest = {
        'locations': locations
    }
    var rez;
    elevator.getElevationForLocations(positionalRequest, function (results, status) {
        if (status == google.maps.ElevationStatus.OK) {
            if (results[0]) {
                rez = results[0].elevation;
                arrH[step] = rez;
            } else {
                alert('No results found');
            }
        } else {
            alert('Elevation service failed due to: ' + status);
        }
    });
}

function toggleHeatmap() {
    var vM = 22;
    var vH = 500;
    var vdT = 100;
    var vD = 2;
    var vw0 = 10;
    var vA = 140;
    var vnu = 1;
    var vF = 1;
    var vu1 = 2;
    var vu2 = 10;
    var f = 1000. * vw0 * vw0 * vD / (vH * vH * vdT);
    var m = 1. / (2 / 3. + 0.1 * Math.sqrt(f) + 1 / 3. * Math.pow(f, 1 / 3.));
    var V1 = Math.PI * vD * vD * vw0 / 4;
    var Vm = 2 / 3. * Math.pow(V1 * vdT / vH, 1 / 3.);
    var kn;
    if (Vm >= 2) kn = 1; else kn = 2;
    var M = vM * 1000 * 1000 / 3600.;
    var Cm = vA * M * vF * m * kn / (vH * vH * Math.pow(V1 * vdT * vnu, 1 / 3.));
    d = 7. * Math.sqrt(Vm) * (1 + 2 / 7. * Math.sqrt(f));
    var Xm = (5 - vF) / 4. * d * vH;
    var um = Vm * (1 + 0.12 * Math.sqrt(f));
    var kr, kp;
    if (vu1 / um < 1) {
        kr = 2 / 3. * (vu1 / um) + 5 / 3. * Math.pow((vu1 / um), 2) - 4 / 3. * Math.pow((vu1 / um), 3);
        kp = 8.43 * Math.pow((1 - vu1 / um), 5) + 1;
    }
    else {
        kr = 3 * (vu1 / um) / (2 * Math.pow((vu1 / um), 2) - (vu1 / um) + 2);
        kp = 1 / 3. * vu1 / um + 2 / 3.;
    }
    Cmm = Cm * kr;
    Xmm = Xm * kp;
    if (vu2 / um < 1) {
        kr = 2 / 3. * (vu2 / um) + 5 / 3. * Math.pow((vu2 / um), 2) - 4 / 3. * Math.pow((vu2 / um), 3);
        kp = 8.43 * Math.pow((1 - vu2 / um), 5) + 1;
    }
    else {
        kr = 3 * (vu2 / um) / (2 * Math.pow((vu2 / um), 2) - (vu2 / um) + 2);
        kp = 1 / 3. * vu2 / um + 2 / 3.;
    }
    Cmax = Cm * kr;
    Xmax = Xm * kp;
    square.value = Xmm;
    concentr.value = Cmm;
    var finish = Math.floor(Cmm) + 1;
    radiusPoint = Xmm * 40 / 1000;
    radiusLarge = Xmax * 40 / 1000.;
    heatmap.setOptions({radius: radiusPoint});
    step = 0;
    for (var i = 0; i < finish; i++) {
        var m;
        if (i > 0) m = i * 8;
        else m = 1;
        h = 360. / m;
        for (var j = 0; j < m; j++) {
            var r = i * radiusPoint * koef;
            var fi = j * h;
            var x, y;
            x = Number(x0 + r * Math.cos(fi * Math.PI / 180) * 1.0).toFixed(6);
            y = Number(y0 + r * Math.sin(fi * Math.PI / 180) * 1.0).toFixed(6);
            var temp = new google.maps.LatLng(x, y);
            pointH = changeH(temp, step);
            arrTemp[step] = temp;
            step++;
        }
    }
}

function drawMap() {
    var j = 0;
    var H = 200;
    for (var i = 0; i < step; i++) {
        if (arrH[i] < H) {
            data[j] = arrTemp[i];
            j++;
        }

    }
    heatmap.setMap(map);
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

function changeOpacity() {
    heatmap.setOptions({opacity: heatmap.get("opacity") ? null : 0.2});
}

google.maps.event.addDomListener(window, 'load', initialize);