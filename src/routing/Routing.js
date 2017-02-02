const KnownParameterNames = require('./util/KnownParameterNames');
const RequestOptionsUtil = require('./util/RequestOptionsUtil');
const TPLinkPlugUtil = require('./util/tplink/plug/TPLinkPlugUtil');
const LightUtil = require('./util/hue/light/LightUtil');
const SceneUtil = require('./util/hue/scene/SceneUtil');
const GroupUtil = require('./util/hue/group/GroupUtil');
const makeRequest = require('request-promise');
const express = require('express');
const path = require('path');
const util = require('util');

class Routing {
  constructor(expressPort, bridgeIp, bridgeToken, bridgePort) {
    this.expressPort = expressPort;
    this.bridgeIp = bridgeIp;
    this.bridgeToken = bridgeToken;
    this.bridgePort = bridgePort;
    this.bridgeUri = `http://${this.bridgeIp}:${this.bridgePort}/api/${this.bridgeToken}`;
  }

  async start() {
    const application = express();
    const requestOptionsUtil = new RequestOptionsUtil(this.bridgeUri);
    const lightUtil = new LightUtil(this.bridgeUri);
    const sceneUtil = new SceneUtil(this.bridgeUri);
    const groupUtil = new GroupUtil(this.bridgeUri);
    const plugUtil = new TPLinkPlugUtil();


    // Add the webapp folder as static content. This contains the UI.
    const webAppFolder = path.join(__dirname, '../../webapp');
    console.log(`Static content will be read from: ${webAppFolder}`);
    application.use(express.static(webAppFolder));

    /**
     * The requests that come in from express have cyclic references, so
     * JSON.stringify() freaks out. Use this for debugging instead.
     *
     * @param {any} request - the express request
     * @returns undefined
     */
    function printRequest(request) {
      console.log(`Request: ${JSON.stringify(util.inspect(request))}`);
    }

    application.get('/debug', async (request, response) => {
      console.log('debug called');
      const result = await lightUtil.getAllLights();
      response.send(result);
      console.log('Request handled.');
    });

    /**
     * Begin GET-related functionality
     */
    application.get('/getlights', async (request, response) => {
      console.log('getlights called');
      const lights = await lightUtil.getAllLights();
      response.send(lights);
      console.log('Request handled.');
    });

    application.get('/getscenes', async (request, response) => {
      console.log('getscenes called');
      const v2ScenesRequested =
        (KnownParameterNames.getV2() in request.query) &&
        (request.query[KnownParameterNames.getV2()] === 'true');

      const scenes = sceneUtil.getAllScenes(v2ScenesRequested);
      response.send(scenes);
      console.log('Request handled.');
    });

    application.get('/getschedules', async (request, response) => {
      console.log('getschedules called');
      const options = requestOptionsUtil.simpleGet('schedules');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
      console.log('Request handled.');
    });

    application.get('/getgroups', async (request, response) => {
      console.log('getgroups called');
      const groups = await groupUtil.getAllGroups();
      response.send(groups);
      console.log('Request handled.');
    });

    application.get('/getsensors', async (request, response) => {
      console.log('getsensors called');
      const options = requestOptionsUtil.simpleGet('sensors');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
      console.log('Request handled.');
    });

    application.get('/getrules', async (request, response) => {
      console.log('getrules called');
      const options = requestOptionsUtil.simpleGet('rules');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
      console.log('Request handled.');
    });

    application.get('/getplugs', async (request, response) => {
      console.log('getplugs called');

      const result = await plugUtil.getAllPlugs();
      response.send(result);
      console.log('Request handled.');
    });

    /**
     * Begin PUT-related functionality.
     */

    /**
     * Utility method to retrieve an item from the request query.
     *
     * @param {any} request - the express request
     * @param {any} response - the express response
     * @param {any} idKey - the key of the ID that should've been sent
     * @returns the ID, if an error isn't thrown
     */
    function getRequiredId(request, response, idKey) {
      let lightId;
      if (idKey in request.query) {
        lightId = request.query[idKey];
      } else {
        const error = new Error(`'${idKey}' not found in request: ${JSON.stringify(util.inspect(request))}`);
        response.send(error);
        throw error;
      }
      return lightId;
    }

    application.get('/togglelight', async (request, response) => {
      console.log('togglelight called');
      const lightId = getRequiredId(request, response, KnownParameterNames.getLightId());
      const toggledLight = await lightUtil.toggleLight(lightId);
      response.send(toggledLight);
      console.log('Request handled.');
    });

    application.get('/activatescene', async (request, response) => {
      console.log('activatescene called');
      const sceneId = getRequiredId(request, response, KnownParameterNames.getSceneId());
      console.log(`Scene ID: ${sceneId}`);

      sceneUtil.activateScene(sceneId);
      console.log('Request handled.');
    });

    application.get('/togglegroup', async (request, response) => {
      console.log('togglegroup called');
      const groupId = getRequiredId(request, response, KnownParameterNames.getGroupId());
      const toggledGroup = await groupUtil.toggleGroup(groupId);
      response.send(toggledGroup);
      console.log('Request handled.');
    });

    application.get('/toggleplug', async (request, response) => {
      console.log('toggleplug called');
      const plugId = getRequiredId(request, response, KnownParameterNames.getPlugId());
      const toggleResult = await plugUtil.togglePlug(plugId);
      response.send(toggleResult);
      console.log('Request handled.');
    });

    application.listen(this.expressPort, () => {
      console.log(`hue-stuff listening on port ${this.expressPort}!`);
    });

    return false;
  }
}

module.exports = Routing;
