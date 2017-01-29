const KnownParameterNames = require('./util/KnownParameterNames');
const RequestOptionsUtil = require('./util/RequestOptionsUtil');
const makeRequest = require('request-promise');
const express = require('express');
const path = require('path');
const util = require('util');

class HueServer {
  constructor(expressPort, bridgeIp, bridgeToken, bridgePort) {
    this.expressPort = expressPort;
    this.bridgeIp = bridgeIp;
    this.bridgeToken = bridgeToken;
    this.bridgePort = bridgePort;
    this.bridgeUrl = `http://${this.bridgeIp}:${this.bridgePort}/api/${this.bridgeToken}`;
  }

  async start() {
    const application = express();
    const requestOptionsUtil = new RequestOptionsUtil(this.bridgeUrl);

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

    /**
     * Begin GET-related functionality
     */
    application.get('/getlights', async (request, response) => {
      console.log('getlights called');
      const options = requestOptionsUtil.simpleGet('lights');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const lights = await makeRequest(options);

      // Now, we need to record each light's ID INSIDE of the light as well.
      // The web UI depends on that.
      for (const lightId in lights) {
        if (Object.prototype.hasOwnProperty.call(lights, lightId)) {
          const light = lights[lightId];
          light.id = lightId;
          lights[lightId] = light;
        }
      }

      response.send(lights);
    });

    application.get('/getscenes', async (request, response) => {
      console.log('getscenes called');
      const options = requestOptionsUtil.simpleGet('scenes');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const scenes = await makeRequest(options);

      // V2 scenes provide a 'desired' state object for each of their lights.
      // We need these in order to provide the scene toggling functionality
      // So, determine if V2 scenes were requested.
      const v2ScenesRequested =
        (KnownParameterNames.getV2() in request.query) &&
        (request.query[KnownParameterNames.getV2()] === 'true');
      if (v2ScenesRequested) {
        console.log('V2 Scenes requested, will return only V2 scenes.');
      } else {
        console.log('V2 Scenes not requested, will return all.');
      }

      // Now, we need to record each light's ID INSIDE of the light.
      // While we do this, we might as well do the V2 filtering as well.
      const resultScenes = {};
      // For each top-level attribute (which, if it's what we're looking for, will be a scene ID)
      for (const sceneId in scenes) {
        // Check to make sure it's a real attribute and not some weird superclass attribute
        if (Object.prototype.hasOwnProperty.call(scenes, sceneId)) {
          // Grab the scene and put its ID inside
          const scene = scenes[sceneId];
          scene.id = sceneId;
          scenes[sceneId] = scene;

          // Filter out non-V2 scenes, if they're not wanted.
          if (v2ScenesRequested) {
            if (scene.version === 2) {
              resultScenes[sceneId] = scenes[sceneId];
            }
          } else {
            resultScenes[sceneId] = scenes[sceneId];
          }
        }
      }

      response.send(resultScenes);
    });

    application.get('/getschedules', async (request, response) => {
      console.log('getschedules called');
      const options = requestOptionsUtil.simpleGet('schedules');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
    });

    application.get('/getgroups', async (request, response) => {
      console.log('getgroups called');
      const options = requestOptionsUtil.simpleGet('groups');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
    });

    application.get('/getsensors', async (request, response) => {
      console.log('getsensors called');
      const options = requestOptionsUtil.simpleGet('sensors');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
    });

    application.get('/getrules', async (request, response) => {
      console.log('getrules called');
      const options = requestOptionsUtil.simpleGet('rules');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
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

      const getOptions = requestOptionsUtil.simpleGet(`lights/${lightId}`);
      console.log(`Will GET with options: ${JSON.stringify(getOptions)}`);
      const light = await makeRequest(getOptions);

      const toggledState = { on: !(light.state.on) };
      const putOptions = requestOptionsUtil.putWithBody(`lights/${lightId}/state`, toggledState);
      console.log(`Will PUT with options: ${JSON.stringify(putOptions)}`);
      await makeRequest(putOptions);

      const currentLightState = await makeRequest(getOptions);
      response.send(currentLightState);
    });

    application.get('/togglegroup', async (request, response) => {
      console.log('togglegroup called');
      const groupId = getRequiredId(request, response, KnownParameterNames.getGroupId());

      const getOptions = requestOptionsUtil.simpleGet(`groups/${groupId}`);
      console.log(`Will GET with options: ${JSON.stringify(getOptions)}`);
      const group = await makeRequest(getOptions);

      const toggledState = { on: !(group.action.on) };
      const putOptions = requestOptionsUtil.putWithBody(`groups/${groupId}/action`, toggledState);
      console.log(`Will PUT with options: ${JSON.stringify(putOptions)}`);
      await makeRequest(putOptions);

      const currentGroupState = await makeRequest(getOptions);
      response.send(currentGroupState);
    });

    application.get('/activatescene', async (request, response) => {
      console.log('activatescene called');
      const sceneId = getRequiredId(request, response, KnownParameterNames.getSceneId());

      const getOptions = requestOptionsUtil.simpleGet(`scenes/${sceneId}`);
      console.log(`Will GET with options: ${JSON.stringify(getOptions)}`);
      const scene = await makeRequest(getOptions);

      for (const lightId in scene.lightstates) {
        if (Object.prototype.hasOwnProperty.call(scene.lightstates, lightId)) {
          const lightState = scene.lightstates[lightId];
          const putOptions = requestOptionsUtil.putWithBody(`lights/${lightId}/state`, lightState);
          console.log(`Putting light state: ${JSON.stringify(putOptions)}`);
          await makeRequest(putOptions);
        }
      }

      const currentSceneState = await makeRequest(getOptions);
      response.send(currentSceneState);
    });

    application.listen(this.expressPort, () => {
      console.log(`hue-stuff listening on port ${this.expressPort}!`);
    });

    return false;
  }
}

module.exports = HueServer;
