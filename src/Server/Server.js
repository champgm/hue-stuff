const express = require('express');
const makeRequest = require('request-promise');
const util = require('util');
const KnownParameterNames = require('./util/KnownParameterNames');

class Server {
  constructor(bridgeIp, bridgeToken, bridgePort = 80) {
    this.bridgeIp = bridgeIp;
    this.bridgeToken = bridgeToken;
    this.bridgePort = bridgePort;
    this.bridgeUrl = `http://${this.bridgeIp}:${this.bridgePort}/api/${this.bridgeToken}`;
  }

  async start() {
    const application = express();
    // Lights
    application.get('/', async (request, response) => {
      response.send('GET request to the homepage');
    });

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
      const result = await makeRequest(options);
      response.send(result);
    });

    application.get('/getscenes', async (request, response) => {
      // console.log(`Request: ${JSON.stringify(util.inspect(request))}`);
      const options = {
        method: 'GET',
        uri: `${this.bridgeUrl}/scenes`,
        json: true
      };

      console.log(`Will request with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);

      // V2 scenes support extra features, sometimes people only want to see those.
      if (KnownParameterNames.getV2() in request.query) {
        // Check each available scene to see if it's V2
        const v2Results = {};
        for (const sceneId in result) {
          if (Object.prototype.hasOwnProperty.call(result, sceneId)) {
            const scene = result[sceneId];
            if (scene.version === 2) {
              // Record if V2
              v2Results[sceneId] = result[sceneId];
            }
          }
        }
        // Return only V2
        response.send(v2Results);
      } else {
        // Just return all scenes
        response.send(result);
      }
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
      let lightId;
      if (KnownParameterNames.getLightId() in request.query) {
        lightId = request.query[KnownParameterNames.getLightId()];
      } else {
        response.send(`'lightid' not found in request: ${JSON.stringify(util.inspect(request))}`);
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
        response.send(`'groupid' not found in request: ${JSON.stringify(util.inspect(request))}`);
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
      response.send('GET request to setlightstate');
    });

    application.get('/activatescene', async (request, response) => {
      response.send('GET request to activatescene');
    });

    application.listen(3000, () => {
      console.log('Example app listening on port 3000!');
    });

    return false;
  }
}

module.exports = Server;
