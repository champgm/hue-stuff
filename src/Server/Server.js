const path = require('path');
const util = require('util');
const express = require('express');
const makeRequest = require('request-promise');
const KnownParameterNames = require('./util/KnownParameterNames');

class Server {
  constructor(hueWebPort, bridgeIp, bridgeToken, bridgePort = 80) {
    this.hueWebPort = hueWebPort;
    this.bridgeIp = bridgeIp;
    this.bridgeToken = bridgeToken;
    this.bridgePort = bridgePort;
    this.bridgeUrl = `http://${this.bridgeIp}:${this.bridgePort}/api/${this.bridgeToken}`;
  }

  async start() {
    const application = express();


    const webAppFolder = path.join(__dirname, '../../webapp');
    console.log(`Static content will be read from: ${webAppFolder}`);
    application.use(express.static(webAppFolder));

    // application.use('/', express.static('src/webapp'));
    // application.get('/', async (request, response) => {
    //   // response.send('GET request to the homepage');
    //   response.redirect('/webapp');
    // });

    /**
     * Begin get-related uris
     */
    application.get('/getlights', async (request, response) => {
      // console.log(`Request: ${JSON.stringify(util.inspect(request))}`);
      const options = {
        method: 'GET',
        uri: `${this.bridgeUrl}/lights`,
        json: true
      };
      console.log(`Will request with options: ${JSON.stringify(options)}`);
      const lights = await makeRequest(options);

      // Now, we need to record each light's ID INSIDE of the light as well.
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
      // console.log(`Request: ${JSON.stringify(util.inspect(request))}`);

      const v2ScenesRequested =
        (KnownParameterNames.getV2() in request.query) &&
        (request.query[KnownParameterNames.getV2()] === 'true');
      console.log(`V2 Scenes requested: ${v2ScenesRequested}`);


      const options = {
        method: 'GET',
        uri: `${this.bridgeUrl}/scenes`,
        json: true
      };

      console.log(`Will request with options: ${JSON.stringify(options)}`);
      const scenes = await makeRequest(options);

      // Now, we need to record each light's ID INSIDE of the light as well.
      // Also, just in case, if it's a V2 scene, store that in a special collection.
      const resultScenes = {};
      for (const sceneId in scenes) {
        if (Object.prototype.hasOwnProperty.call(scenes, sceneId)) {
          const scene = scenes[sceneId];
          // Add scene ID inside
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

    application.get('/schedules', async (request, response) => {
      // console.log(`Request: ${JSON.stringify(util.inspect(request))}`);
      const options = {
        method: 'GET',
        uri: `${this.bridgeUrl}/schedules`,
        json: true
      };
      console.log(`Will request with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
    });

    application.get('/getgroups', async (request, response) => {
      // console.log(`Request: ${JSON.stringify(util.inspect(request))}`);
      const options = {
        method: 'GET',
        uri: `${this.bridgeUrl}/groups`,
        json: true
      };
      console.log(`Will request with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
    });

    application.get('/getsensors', async (request, response) => {
      // console.log(`Request: ${JSON.stringify(util.inspect(request))}`);
      const options = {
        method: 'GET',
        uri: `${this.bridgeUrl}/sensors`,
        json: true
      };
      console.log(`Will request with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
    });

    application.get('/getrules', async (request, response) => {
      // console.log(`Request: ${JSON.stringify(util.inspect(request))}`);
      const options = {
        method: 'GET',
        uri: `${this.bridgeUrl}/rules`,
        json: true
      };
      console.log(`Will request with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
    });

    application.get('/togglelight', async (request, response) => {
      // console.log(`Request: ${JSON.stringify(util.inspect(request))}`);
      let lightId;
      if (KnownParameterNames.getLightId() in request.query) {
        lightId = request.query[KnownParameterNames.getLightId()];
      } else {
        response.send(`'${KnownParameterNames.getLightId()}' not found in request: ${JSON.stringify(util.inspect(request))}`);
        return;
      }

      const getOptions = {
        method: 'GET',
        uri: `${this.bridgeUrl}/lights/${lightId}`,
        json: true
      };

      console.log(`Will request with options: ${JSON.stringify(getOptions)}`);
      const light = await makeRequest(getOptions);
      const putOptions = {
        method: 'PUT',
        uri: `${this.bridgeUrl}/lights/${lightId}/state`,
        body: {
          on: !(light.state.on)
        },
        json: true
      };
      await makeRequest(putOptions);

      const currentLightState = await makeRequest(getOptions);
      response.send(currentLightState);
    });

    application.get('/togglegroup', async (request, response) => {
      let groupId;
      if (KnownParameterNames.getGroupId() in request.query) {
        groupId = request.query[KnownParameterNames.getGroupId()];
      } else {
        response.send(`'${KnownParameterNames.getGroupId()}' not found in request: ${JSON.stringify(util.inspect(request))}`);
        return;
      }

      const getOptions = {
        method: 'GET',
        uri: `${this.bridgeUrl}/groups/${groupId}`,
        json: true
      };

      console.log(`Will request with options: ${JSON.stringify(getOptions)}`);
      const group = await makeRequest(getOptions);
      const putOptions = {
        method: 'PUT',
        uri: `${this.bridgeUrl}/groups/${groupId}/action`,
        body: {
          on: !(group.action.on)
        },
        json: true
      };
      await makeRequest(putOptions);

      const currentGroupState = await makeRequest(getOptions);
      response.send(currentGroupState);
    });

    application.get('/setlightstate', async (request, response) => {
      response.send('This is not implemented yet');
    });

    application.get('/activatescene', async (request, response) => {
      let sceneId;
      if (KnownParameterNames.getSceneId() in request.query) {
        sceneId = request.query[KnownParameterNames.getSceneId()];
      } else {
        response.send(`'${KnownParameterNames.getSceneId()}' not found in request: ${JSON.stringify(util.inspect(request))}`);
        return;
      }

      const getOptions = {
        method: 'GET',
        uri: `${this.bridgeUrl}/scenes/${sceneId}`,
        json: true
      };

      console.log(`Will request with options: ${JSON.stringify(getOptions)}`);
      const scene = await makeRequest(getOptions);

      for (const lightId in scene.lightstates) {
        if (Object.prototype.hasOwnProperty.call(scene.lightstates, lightId)) {
          const lightState = scene.lightstates[lightId];
          const putOptions = {
            method: 'PUT',
            uri: `${this.bridgeUrl}/lights/${lightId}/state`,
            body: lightState,
            json: true
          };
          console.log(`Putting light state: ${JSON.stringify(putOptions)}`);
          await makeRequest(putOptions);
        }
      }

      const currentSceneState = await makeRequest(getOptions);
      response.send(currentSceneState);
    });

    application.listen(this.hueWebPort, () => {
      console.log(`hue-stuff listening on port ${this.hueWebPort}!`);
    });

    return false;
  }
}

module.exports = Server;
