const express = require('express');
const request = require('request-promise');


class Server {
  constructor(bridgeIp, bridgeToken, bridgePort = 80) {
    this.bridgeIp = bridgeIp;
    this.bridgeToken = bridgeToken;
    this.bridgePort = bridgePort;
    this.bridgeUrl = `http://${this.bridgeIp}:${this.bridgePort}/api/${this.bridgeToken}/`;
  }

  async start() {
    const application = express();
    // Lights
    application.get('/', (req, res) => {
      res.send('GET request to the homepage');
    });

    application.get('/getlights', async (req, res) => {
      const options = {
        method: 'GET',
        uri: `${this.bridgeUrl}/lights`,
        json: true
      };
      const result = await request(options);
      res.send(result);
    });

    application.get('/getscenes', (req, res) => {
      res.send('GET request to getscenes');
    });

    application.get('/getschedules', (req, res) => {
      res.send('GET request to getschedules');
    });

    application.get('/getgroups', (req, res) => {
      res.send('GET request to getgroups');
    });

    application.get('/getsensors', (req, res) => {
      res.send('GET request to getsensors');
    });

    application.get('/getrules', (req, res) => {
      res.send('GET request to getrules');
    });

    application.get('/togglelight', (req, res) => {
      res.send('GET request to togglelight');
    });

    application.get('/togglegroup', (req, res) => {
      res.send('GET request to togglegroup');
    });

    application.get('/setlightstate', (req, res) => {
      res.send('GET request to setlightstate');
    });

    application.get('/activatescene', (req, res) => {
      res.send('GET request to activatescene');
    });


    return false;
  }
}

module.exports = Server;
