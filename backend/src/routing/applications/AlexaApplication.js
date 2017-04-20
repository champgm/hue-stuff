const UtilityScenes = require('../controllers/utilities/UtilityScenes');
const alexaVerifier = require('alexa-verifier-middleware');
const bodyParser = require('body-parser');
const express = require('express');
const https = require('https');
const fs = require('fs');

class AlexaApplication {
  constructor(controllers, port, secretConfiguration) {
    this.application = express();
    this.controllers = controllers;
    this.secretConfiguration = secretConfiguration;
    this.port = port;
  }

  use(middleware) {
    this.application.use(middleware);
  }

  start() {
    // Check to make sure everything needed for the external server is available
    if (!(this.secretConfiguration &&
      this.secretConfiguration.endpoint &&
      this.secretConfiguration.redSceneId &&
      this.secretConfiguration.whiteSceneId &&
      this.secretConfiguration.sslCertPath &&
      this.secretConfiguration.sslKeyPath)) {
      console.log(`${this.constructor.name}: Secret endpoints not configured! ` +
        'Will not add bindings to external server!');
      return false;
    }

    // Route gets and puts for the external application
    this.routeExternalPuts();


    try {
      // Read the SSL stuff
      const certificate = fs.readFileSync(this.secretConfiguration.sslCertPath);
      const privateKey = fs.readFileSync(this.secretConfiguration.sslKeyPath);

      // Start the external server
      console.log(`${this.constructor.name}: Starting Alexa Application...`);
      https.createServer({ key: privateKey, cert: certificate }, this.application)
        .listen(this.port, () => {
          console.log(`Alexa Application listening on port ${this.port}!`);
        });
      return true;
    } catch (error) {
      console.log('Error starting Alexa endpoint.');
      console.log(`${JSON.stringify(error)}`);
      return false;
    }
  }

  routeExternalPuts() {
    // Super secret stuff
    const whiteSceneId = this.secretConfiguration.whiteSceneId;
    const redSceneId = this.secretConfiguration.redSceneId;
    const secretEndpoint = this.secretConfiguration.endpoint;
    const sceneController = this.controllers.sceneController;

    // For security, there's a thing which will verify that this request comes from amazon
    // and isn't an attack or something
    const alexaRouter = express.Router(); // Instantiate an express "router"
    alexaRouter.use(alexaVerifier); // Make the router use the Alexa call verifier
    this.application.use(alexaRouter); // Use it

    // This will help us parse POSTs
    this.application.use(bodyParser.json()); // support json encoded bodies
    this.application.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

    // Right now, these are the only accepted responses.
    const processPost = async (request, response) => {
      let chosenScene;
      if (request.body.request.intent) {
        const scene = request.body.request.intent.slots.Scene.value;
        chosenScene = scene;
        switch (scene) {
          case 'red':
            await sceneController.select(redSceneId);
            break;
          case 'white':
            await sceneController.select(whiteSceneId);
            break;
          case 'off':
            await sceneController.select(UtilityScenes.getAllOffId());
            break;
          case 'on':
            await sceneController.select(whiteSceneId);
            break;
          default:
            await sceneController.select(whiteSceneId);
            break;
        }
      } else {
        await sceneController.select(whiteSceneId);
        chosenScene = 'on';
      }

      // Craft a nice response telling echo what to say
      const alexaResponse = {
        version: '1.0',
        response: {
          outputSpeech: this.getOutputSpeech(chosenScene),
          reprompt: {
            outputSpeech: {
              type: 'PlainText',
              text: null
            }
          },
          shouldEndSession: true
        }
      };
      response.send(alexaResponse);
      console.log('Request handled.');
    };

    // Start this app on the secret endpoint
    this.application.post(`/${secretEndpoint}`, processPost);
  }

  getOutputSpeech(chosenScene) {
    let text;
    switch (chosenScene) {
      case 'red':
        text = 'Is it almost time for bed?';
        break;
      case 'white':
        text = 'Are you going to study?';
        break;
      case 'on':
        text = this.getOnText();
        break;
      case 'off':
        text = this.getOffText();
        break;
      default:
        text = `Done turning lights ${chosenScene}.`;
    }
    return {
      type: 'PlainText',
      text
    };
  }

  getOnText() {
    const date = new Date();
    const currentHour = date.getHours();
    if (currentHour < 12) {
      return 'Good morning!';
    }
    if ((currentHour >= 12) && (currentHour <= 4)) {
      return 'Good afternoon! Welcome home!';
    }
    if ((currentHour > 5) && currentHour >= 12) {
      return 'Good evening! Welcome home!';
    }
    return 'Your lights should be on now.';
  }

  getOffText() {
    const date = new Date();
    const currentHour = date.getHours();
    if (currentHour < 12) {
      return 'Goodbye, have a nice day!';
    }
    if ((currentHour >= 12) && (currentHour <= 16)) {
      return 'Is it time for a nap?';
    }
    if ((currentHour > 16) && currentHour >= 21) {
      return 'Going out on the town? Have fun!';
    }
    if (currentHour > 21) {
      return 'Where are you going at this time of night?';
    }
    return 'Your lights should be off now.';
  }
}

module.exports = AlexaApplication;
