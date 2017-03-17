const UtilityScenes = require("../util/hue/scene/UtilityScenes");
const alexaVerifier = require("alexa-verifier-middleware");
const bodyParser = require("body-parser");
const express = require("express");
const https = require("https");
const fs = require("fs");

class AlexaApplication {
  constructor(hueUtilities, port, secretConfiguration) {
    this.application = express();
    this.hueUtilities = hueUtilities;
    this.secretConfiguration = secretConfiguration;
    this.port = port;
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
        "Will not add bindings to external server!");
      return false;
    }

    // Route gets and puts for the external application
    this.routeExternalPuts();

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
  }

  routeExternalPuts() {
    // Super secret stuff
    const whiteSceneId = this.secretConfiguration.whiteSceneId;
    const redSceneId = this.secretConfiguration.redSceneId;
    const secretEndpoint = this.secretConfiguration.endpoint;
    const sceneUtil = this.hueUtilities.sceneUtil;

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
          case "red":
            await sceneUtil.activateScene(redSceneId);
            break;
          case "white":
            await sceneUtil.activateScene(whiteSceneId);
            break;
          case "off":
            await sceneUtil.activateScene(UtilityScenes.getAllOffId());
            break;
          case "on":
            await sceneUtil.activateScene(whiteSceneId);
            break;
          default:
            await sceneUtil.activateScene(whiteSceneId);
            break;
        }
      } else {
        await sceneUtil.activateScene(whiteSceneId);
        chosenScene = "on";
      }

      // Craft a nice response telling echo what to say
      const alexaResponse = {
        version: "1.0",
        response: {
          outputSpeech: this.getOutputSpeech(chosenScene),
          reprompt: {
            outputSpeech: {
              type: "PlainText",
              text: null
            }
          },
          shouldEndSession: true
        }
      };
      response.send(alexaResponse);
      console.log("Request handled.");
    };

    // Start this app on the secret endpoint
    this.application.post(`/${secretEndpoint}`, processPost);
  }

  getOutputSpeech(chosenScene) {
    return {
      type: "PlainText",
      text: `Done turning lights ${chosenScene}.`
    };
  }
}

module.exports = AlexaApplication;
