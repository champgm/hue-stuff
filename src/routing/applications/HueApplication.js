const KnownParameterNames = require('../util/KnownParameterNames');
const makeRequest = require('request-promise');
const express = require('express');
const path = require('path');
const util = require('util');

class HueApplication {
  constructor(hueUtilities, port) {
    this.application = express();
    this.hueUtilities = hueUtilities;
    this.port = port;
  }

  start() {
    // Add the webapp folder as static content. This contains the UI.
    const webAppFolder = path.join(__dirname, '../../webapp');
    console.log(`Static content will be read from: ${webAppFolder}`);
    this.application.use(express.static(webAppFolder));

    // Route gets and puts for the internal application
    this.routeInternalGets(this.application, this.hueUtilities);
    this.routeInternalPuts(this.application, this.hueUtilities);

    // Stoart the local server
    console.log(`${this.constructor.name}: Starting Hue application...`);
    this.application.listen(this.port, () => {
      console.log(`Hue Application listening on port ${this.port}!`);
    });
  }

  routeInternalPuts(internalApplication, utils) {
    const lightUtil = utils.lightUtil;
    const sceneUtil = utils.sceneUtil;
    const groupUtil = utils.groupUtil;
    const plugUtil = utils.plugUtil;

    console.log('Routing togglelight');
    internalApplication.get('/togglelight', async (request, response) => {
      console.log('togglelight called');
      const lightId = HueApplication.getRequiredId(request, response, KnownParameterNames.getLightId());
      const toggledLight = await lightUtil.toggleLight(lightId);
      response.send(toggledLight);
      console.log('Request handled.');
    });

    console.log('Routing activatescene');
    internalApplication.get('/activatescene', async (request, response) => {
      console.log('activatescene called');
      const sceneId = HueApplication.getRequiredId(request, response, KnownParameterNames.getSceneId());
      console.log(`Scene ID: ${sceneId}`);

      const result = await sceneUtil.activateScene(sceneId);
      response.send(result);
      console.log('Request handled.');
    });

    console.log('Routing togglegroup');
    internalApplication.get('/togglegroup', async (request, response) => {
      console.log('togglegroup called');
      const groupId = HueApplication.getRequiredId(request, response, KnownParameterNames.getGroupId());
      const toggledGroup = await groupUtil.toggleGroup(groupId);
      response.send(toggledGroup);
      console.log('Request handled.');
    });

    console.log('Routing toggleplug');
    internalApplication.get('/toggleplug', async (request, response) => {
      console.log('toggleplug called');
      const plugId = HueApplication.getRequiredId(request, response, KnownParameterNames.getPlugId());
      console.log(`Plugid from request: ${plugId}`);

      const toggleResult = await plugUtil.togglePlug(plugId);
      response.send(toggleResult);
      console.log('Request handled.');
    });
  }

  routeInternalGets(internalApplication, utils) {
    // internalApplication.get('/debug', async (request, response) => {
    //   console.log('debug called');
    //   const result = await lightUtil.getAllLights();
    //   response.send(result);
    //   console.log('Request handled.');
    // });

    const requestOptionsUtil = utils.requestOptionsUtil;
    const lightUtil = utils.lightUtil;
    const sceneUtil = utils.sceneUtil;
    const groupUtil = utils.groupUtil;
    const plugUtil = utils.plugUtil;

    console.log('Routing getlights');
    internalApplication.get('/getlights', async (request, response) => {
      console.log('getlights called');
      const lights = await lightUtil.getAllLights();
      response.send(lights);
      console.log('Request handled.');
    });

    console.log('Routing getscenes');
    internalApplication.get('/getscenes', async (request, response) => {
      console.log('getscenes called');
      const v2ScenesRequested =
        (KnownParameterNames.getV2() in request.query) &&
        (request.query[KnownParameterNames.getV2()] === 'true');

      const scenes = await sceneUtil.getAllScenes(v2ScenesRequested);
      response.send(scenes);
      console.log('Request handled.');
    });

    console.log('Routing getschedules');
    internalApplication.get('/getschedules', async (request, response) => {
      console.log('getschedules called');
      const options = requestOptionsUtil.simpleGet('schedules');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
      console.log('Request handled.');
    });

    console.log('Routing getgroups');
    internalApplication.get('/getgroups', async (request, response) => {
      console.log('getgroups called');
      const groups = await groupUtil.getAllGroups();
      response.send(groups);
      console.log('Request handled.');
    });

    console.log('Routing getsensors');
    internalApplication.get('/getsensors', async (request, response) => {
      console.log('getsensors called');
      const options = requestOptionsUtil.simpleGet('sensors');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
      console.log('Request handled.');
    });

    console.log('Routing getrules');
    internalApplication.get('/getrules', async (request, response) => {
      console.log('getrules called');
      const options = requestOptionsUtil.simpleGet('rules');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
      console.log('Request handled.');
    });

    console.log('Routing getplugs');
    internalApplication.get('/getplugs', async (request, response) => {
      console.log('getplugs called');

      const result = await plugUtil.getAllPlugs();
      response.send(result);
      console.log('Request handled.');
    });
    return internalApplication;
  }

  /**
 * The requests that come in from express have cyclic references, so
 * JSON.stringify() freaks out. Use this for debugging instead.
 *
 * @param {any} request - the express request
 * @returns undefined
 */
  static printRequest(request) {
    console.log(`Request: ${JSON.stringify(util.inspect(request))}`);
  }

  /**
 * Utility method to retrieve an item from the request query.
 *
 * @param {any} request - the express request
 * @param {any} response - the express response
 * @param {any} idKey - the key of the ID that should've been sent
 * @returns the ID, if an error isn't thrown
 */
  static getRequiredId(request, response, idKey) {
    let id;
    if (idKey in request.query) {
      id = request.query[idKey];
    } else {
      const error = new Error(`'${idKey}' not found in request: ` +
        `${JSON.stringify(util.inspect(request))}`);
      response.send(error);
      throw error;
    }
    return id;
  }
}

module.exports = HueApplication;
