function printLights(json) {
  $("#lights").empty().append("<strong>Lights</strong> </br>");

  var contentToInsert = '';
  $.each(json, function (i, light) {

    var state = light.state;
    var on = state.on;
    var id = light.id;
    var name = light.name;

    var button_on = '<button type="button" id="' + id + '"'
      + ' class="btn light btn-on btn-lg">' + name + ' </button>';

    var button_off = '<button type="button" id="' + id + '"'
      + ' class="btn light btn-off btn-lg">' + name + ' </button>';

    if (on) {
      contentToInsert += '<div class="col-sm-6 col-md-4">' + button_on
        + '</div>';
    } else {
      contentToInsert += '<div class="col-sm-6 col-md-4">' + button_off
        + '</div>';
    }

  });

  $("#lights").append('<div class="row">').append(contentToInsert).append(
    '</div>');
}

function printScenes(json) {
  $("#scenes").empty().append("<strong>Scenes</strong> </br>");

  var contentToInsert = '';
  $.each(json, function (i, scene) {
    var id = scene.id;
    var name = scene.name;

    var scene_btn = '<button type="button" id="' + id + '"'
      + ' class="btn scene btn-lg">' + name + ' </button>';

    contentToInsert += '<div class="col-sm-6 col-md-4">' + scene_btn
      + '</div>';
  });

  $("#scenes").append('<div class="row">').append(contentToInsert).append(
    '</div>');
}

function printGroups(json) {
  $("#groups").empty().append("<strong>Groups</strong> </br>");

  var contentToInsert = '';
  $.each(json, function (i, group) {
    var id = group.id
    var name = group.name
    var action = group.action
    var on = action.on

    var button_on = '<button type="button" id="' + id + '"'
      + ' class="btn group btn-on btn-lg">' + name + ' </button>';

    var button_off = '<button type="button" id="' + id + '"'
      + ' class="btn group btn-off btn-lg">' + name + ' </button>';

    if (on) {
      contentToInsert += '<div class="col-sm-6 col-md-4">' + button_on
        + '</div>';
    } else {
      contentToInsert += '<div class="col-sm-6 col-md-4">' + button_off
        + '</div>';
    }

  });

  $("#groups").append('<div class="row">').append(contentToInsert).append(
    '</div>');
}

function printSchedules(json) {
  $("#schedules").empty().append("<strong>Schedules</strong> </br>");

  var contentToInsert = '';
  $.each(json, function (i, schedule) {
    var name = schedule.name;
    var id = schedule.id;
    var description = schedule.description
    var autodelete = schedule.autodelete
    var created = schedule.created
    var starttime = schedule.starttime
    var status = schedule.status
    var time = schedule.time
    var command = schedule.command
    var address = command.address
    var method = command.method
    var body = command.body

    var schedule_text = 'Id: ' + id + '</br>'
      + 'Description: ' + description + '</br>'
      + 'Autodelete: ' + autodelete + '</br>'
      + 'Created: ' + created + '</br>'
      + 'Starttime: ' + starttime + '</br>'
      + 'Time: ' + time + '</br>'
      + 'Command:</br>'
      + '      Address: ' + address + '</br>'
      + '      Method: ' + method + '</br>'
      + '      Body: ' + body + '</br></br>';

    contentToInsert += '<a href="#" class="list-group-item">'
      + '<h4 class="list-group-item-heading">' + name + '</h4>'
      + '<p class="list-group-item-text"><pre>' + schedule_text + '</pre></p></a>';
  });

  $("#schedules").append('<div class="list-group">').append(contentToInsert)
    .append('</div>');
}

function printSensors(json) {
  $("#sensors").empty().append("<strong>Sensors</strong> </br>");

  var contentToInsert = '';
  $.each(json, function (i, sensor) {
    var id = sensor.id;
    var state = sensor.state;
    var daylight = state.daylight;
    var lastupdated = state.lastupdated;
    var buttonevent = state.buttonevent;
    var status = state.status;
    var config = sensor.config;
    var on = config.on;
    var battery = config.battery
    var reachable = config.reachable;
    var lat = config.lat;
    var long = config.long;
    var sunriseoffset = config.sunriseoffset;
    var sunsetoffset = config.sunsetoffset;
    var type = sensor.type
    var modelid = sensor.modelid
    var manufacturername = sensor.manufacturername;
    var uniqueid = sensor.uniqueid;
    var swversion = sensor.swversion;
    var name = sensor.name;

    var sensor_text = 'ID: ' + id + '</br>'
      + 'State:</br>'
      + '      Daylight: ' + daylight + '</br>'
      + '      Lastupdated: ' + lastupdated + '</br>'
      + '      Buttonevent: ' + buttonevent + '</br>'
      + '      Status: ' + status + '</br>'
      + 'Config:</br>'
      + '      On: ' + on + '</br>'
      + '      Battery: ' + battery + '</br>'
      + '      Reachable: ' + reachable + '</br>'
      + '      Lat: ' + lat + '</br>'
      + '      Long: ' + long + '</br>'
      + '      Sunriseoffset: ' + sunriseoffset + '</br>'
      + '      Sunsetoffset: ' + sunsetoffset + '</br>'
      + 'Type: ' + type + '</br>'
      + 'Modelid: ' + modelid + '</br>'
      + 'Manufacturername: ' + manufacturername + '</br>'
      + 'Uniqueid: ' + uniqueid + '</br>'
      + 'Swversion: ' + swversion + '</br></br>';

    contentToInsert += '<a href="#" class="list-group-item">'
      + '<h4 class="list-group-item-heading">' + name + '</h4>'
      + '<p class="list-group-item-text"><pre>' + sensor_text + '</pre></p></a>';
  });
  $("#sensors").append('<div class="list-group">').append(contentToInsert)
    .append('</div>');
}

function printPlugs(json) {
  $("#plugs").empty().append("<strong>Plugs</strong> </br>");

  var contentToInsert = '';
  $.each(json, function (i, plug) {

    var state = plug.state;
    var on = state.on;
    var id = plug.id;
    var name = plug.name;

    var button_on = '<button type="button" id="' + id + '"'
      + ' class="btn plug btn-on btn-lg">' + name + ' </button>';

    var button_off = '<button type="button" id="' + id + '"'
      + ' class="btn plug btn-off btn-lg">' + name + ' </button>';

    if (on) {
      contentToInsert += '<div class="col-sm-6 col-md-4">' + button_on
        + '</div>';
    } else {
      contentToInsert += '<div class="col-sm-6 col-md-4">' + button_off
        + '</div>';
    }

  });

  $("#plugs").append('<div class="row">').append(contentToInsert).append(
    '</div>');
}
