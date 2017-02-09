function printLights(json) {
  $('#lights').empty().append('<strong>Lights</strong> </br>');

  let contentToInsert = '';
  $.each(json, (i, light) => {

    const state = light.state;
    const on = state.on;
    const id = light.id;
    const name = light.name;

    const button_on = `<button type="button" id="${id}"`
      + ` class="btn light btn-on btn-lg">${name} </button>`;

    const button_off = `<button type="button" id="${id}"`
      + ` class="btn light btn-off btn-lg">${name} </button>`;

    if (on) {
      contentToInsert += `<div class="col-sm-6 col-md-4">${button_on
         }</div>`;
    } else {
      contentToInsert += `<div class="col-sm-6 col-md-4">${button_off
         }</div>`;
    }

  });

  $('#lights').append('<div class="row">').append(contentToInsert).append(
    '</div>');
}

function printScenes(json) {
  $('#scenes').empty().append('<strong>Scenes</strong> </br>');

  let contentToInsert = '';
  $.each(json, (i, scene) => {
    const id = scene.id;
    const name = scene.name;

    const scene_btn = `<button type="button" id="${id}"`
      + ` class="btn scene btn-lg">${name} </button>`;

    contentToInsert += `<div class="col-sm-6 col-md-4">${scene_btn
       }</div>`;
  });

  $('#scenes').append('<div class="row">').append(contentToInsert).append(
    '</div>');
}

function printGroups(json) {
  $('#groups').empty().append('<strong>Groups</strong> </br>');

  let contentToInsert = '';
  $.each(json, (i, group) => {
    const id = group.id;
    const name = group.name;
    const action = group.action;
    const on = action.on;

    const button_on = `<button type="button" id="${id}"`
      + ` class="btn group btn-on btn-lg">${name} </button>`;

    const button_off = `<button type="button" id="${id}"`
      + ` class="btn group btn-off btn-lg">${name} </button>`;

    if (on) {
      contentToInsert += `<div class="col-sm-6 col-md-4">${button_on
         }</div>`;
    } else {
      contentToInsert += `<div class="col-sm-6 col-md-4">${button_off
         }</div>`;
    }

  });

  $('#groups').append('<div class="row">').append(contentToInsert).append(
    '</div>');
}

function printSchedules(json) {
  $('#schedules').empty().append('<strong>Schedules</strong> </br>');

  let contentToInsert = '';
  $.each(json, (i, schedule) => {
    const name = schedule.name;
    const id = schedule.id;
    const description = schedule.description;
    const autodelete = schedule.autodelete;
    const created = schedule.created;
    const starttime = schedule.starttime;
    const status = schedule.status;
    const time = schedule.time;
    const command = schedule.command;
    const address = command.address;
    const method = command.method;
    const body = command.body;

    const schedule_text = `Id: ${id}</br>`
      + `Description: ${description}</br>`
      + `Autodelete: ${autodelete}</br>`
      + `Created: ${created}</br>`
      + `Starttime: ${starttime}</br>`
      + `Time: ${time}</br>`
      + 'Command:</br>'
      + `      Address: ${address}</br>`
      + `      Method: ${method}</br>`
      + `      Body: ${body}</br></br>`;

    contentToInsert += `${'<a href="#" class="list-group-item">'
      + '<h4 class="list-group-item-heading">'}${name}</h4>`
      + `<p class="list-group-item-text"><pre>${schedule_text}</pre></p></a>`;
  });

  $('#schedules').append('<div class="list-group">').append(contentToInsert)
    .append('</div>');
}

function printSensors(json) {
  $('#sensors').empty().append('<strong>Sensors</strong> </br>');

  let contentToInsert = '';
  $.each(json, (i, sensor) => {
    const id = sensor.id;
    const state = sensor.state;
    const daylight = state.daylight;
    const lastupdated = state.lastupdated;
    const buttonevent = state.buttonevent;
    const status = state.status;
    const config = sensor.config;
    const on = config.on;
    const battery = config.battery;
    const reachable = config.reachable;
    const lat = config.lat;
    const long = config.long;
    const sunriseoffset = config.sunriseoffset;
    const sunsetoffset = config.sunsetoffset;
    const type = sensor.type;
    const modelid = sensor.modelid;
    const manufacturername = sensor.manufacturername;
    const uniqueid = sensor.uniqueid;
    const swversion = sensor.swversion;
    const name = sensor.name;

    const sensor_text = `ID: ${id}</br>`
      + 'State:</br>'
      + `      Daylight: ${daylight}</br>`
      + `      Lastupdated: ${lastupdated}</br>`
      + `      Buttonevent: ${buttonevent}</br>`
      + `      Status: ${status}</br>`
      + 'Config:</br>'
      + `      On: ${on}</br>`
      + `      Battery: ${battery}</br>`
      + `      Reachable: ${reachable}</br>`
      + `      Lat: ${lat}</br>`
      + `      Long: ${long}</br>`
      + `      Sunriseoffset: ${sunriseoffset}</br>`
      + `      Sunsetoffset: ${sunsetoffset}</br>`
      + `Type: ${type}</br>`
      + `Modelid: ${modelid}</br>`
      + `Manufacturername: ${manufacturername}</br>`
      + `Uniqueid: ${uniqueid}</br>`
      + `Swversion: ${swversion}</br></br>`;

    contentToInsert += `${'<a href="#" class="list-group-item">'
      + '<h4 class="list-group-item-heading">'}${name}</h4>`
      + `<p class="list-group-item-text"><pre>${sensor_text}</pre></p></a>`;
  });
  $('#sensors').append('<div class="list-group">').append(contentToInsert)
    .append('</div>');
}

function printPlugs(json) {
  $('#plugs').empty().append('<strong>Plugs</strong> </br>');

  let contentToInsert = '';
  $.each(json, (i, plug) => {

    const state = plug.state;
    const on = state.on;
    const id = plug.id;
    const name = plug.name;

    const button_on = `<button type="button" id="${id}"`
      + ` class="btn plug btn-on btn-lg">${name} </button>`;

    const button_off = `<button type="button" id="${id}"`
      + ` class="btn plug btn-off btn-lg">${name} </button>`;

    if (on) {
      contentToInsert += `<div class="col-sm-6 col-md-4">${button_on
         }</div>`;
    } else {
      contentToInsert += `<div class="col-sm-6 col-md-4">${button_off
         }</div>`;
    }

  });

  $('#plugs').append('<div class="row">').append(contentToInsert).append(
    '</div>');
}
