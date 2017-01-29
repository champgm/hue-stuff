const KnownParameterNames = require('./util/KnownParameterNames');
const RequestOptionsUtil = require('./util/RequestOptionsUtil');
const UtilityScenes = require('./util/scene/UtilityScenes');
const LightUtil = require('./util/light/LightUtil');
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
      // const options = requestOptionsUtil.simpleGet('scenes');
      // console.log(`Will GET with options: ${JSON.stringify(options)}`);
      // const result = await makeRequest(options);

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

      // Start collecting all scenes.
      const resultScenes = {};

      // Add any Utility scenes first.
      const utilityScenes = UtilityScenes.getAllUtilityScenes();
      for (const sceneId in utilityScenes) {
        if (Object.prototype.hasOwnProperty.call(utilityScenes, sceneId)) {
          resultScenes[sceneId] = utilityScenes[sceneId];
        }
      }

      // Now, we need to record each light's ID INSIDE of the light.
      // While we do this, we might as well do the V2 filtering as well.
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
      const options = requestOptionsUtil.simpleGet('groups');

      console.log(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
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
      console.log('Request handled.');
    });

    application.get('/activatescene', async (request, response) => {
      console.log('activatescene called');
      const sceneId = getRequiredId(request, response, KnownParameterNames.getSceneId());
      console.log(`Scene ID: ${sceneId}`);

      if (UtilityScenes.getAllUtilitySceneIds().includes(sceneId)) {
        switch (sceneId) {
          case UtilityScenes.getAllOffId(): {
            const lights = await lightUtil.getAllLights();
            for (const lightId in lights) {
              if (Object.prototype.hasOwnProperty.call(lights, lightId)) {
                await lightUtil.turnLightOff(lightId);
              }
            }
            response.send({ sceneId });
            break;
          }
          default: {
            const unsupportedMessage = `Unsupported utility scene ID: ${sceneId}`;
            console.log(unsupportedMessage);
            response.send(unsupportedMessage);
          }
        }
      } else {
        const getOptions = requestOptionsUtil.simpleGet(`scenes/${sceneId}`);
        console.log(`Will GET with options: ${JSON.stringify(getOptions)}`);
        const scene = await makeRequest(getOptions);

        for (const lightId in scene.lightstates) {
          if (Object.prototype.hasOwnProperty.call(scene.lightstates, lightId)) {
            const lightState = scene.lightstates[lightId];
            await lightUtil.setLightState(lightId, lightState);
          }
        }
        const currentSceneState = await makeRequest(getOptions);
        response.send(currentSceneState);
      }
      console.log('Request handled.');
    });

    application.listen(this.expressPort, () => {
      console.log(`hue-stuff listening on port ${this.expressPort}!`);
    });

    return false;
  }
}

module.exports = Routing;
