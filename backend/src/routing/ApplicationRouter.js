const AlexaApplication = require('./applications/AlexaApplication');
const WebApplication = require('./applications/WebApplication');
const RequestOptionsUtil = require('./controllers/utilities/RequestOptionsUtil');
const ScheduleController = require('./controllers/ScheduleController');
const SensorController = require('./controllers/SensorController');
const LightController = require('./controllers/LightController');
const SceneController = require('./controllers/SceneController');
const GroupController = require('./controllers/GroupController');
const PlugController = require('./controllers/PlugController');
const RuleController = require('./controllers/RuleController');
const errorHandler = require('./middleware/HandleErrors');
const bodyParser = require('body-parser');
const morgan = require('morgan');
const listEndpoints = require('express-list-endpoints');

const path = require('path');
const logger = require('../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });

class ApplicationRouter {
  constructor(expressConfiguration, bridgeDetails, broadcastAddress, secretConfiguration, useRawWebApp) {
    this.bridgeUri =
      `http://${bridgeDetails.bridgeIp}:` +
      `${bridgeDetails.bridgePort}/` +
      `api/${bridgeDetails.bridgeToken}`;
    this.externalExpressPort = expressConfiguration.externalPort;
    this.internalExpressPort = expressConfiguration.internalPort;
    this.secretConfiguration = secretConfiguration;
    this.broadcastAddress = broadcastAddress;
    this.useRawWebApp = useRawWebApp;
    logger.info({ useRawWebApp }, 'useRawWebApp');
    logger.info({ useRawWebApp: this.useRawWebApp }, 'this.useRawWebApp');
  }

  async start() {
    const controllers = {
      requestOptionsUtil: new RequestOptionsUtil(this.bridgeUri),
      scheduleController: new ScheduleController(this.bridgeUri),
      sensorController: new SensorController(this.bridgeUri),
      lightController: new LightController(this.bridgeUri),
      sceneController: new SceneController(this.bridgeUri),
      groupController: new GroupController(this.bridgeUri),
      ruleController: new RuleController(this.bridgeUri),
      plugController: new PlugController(this.broadcastAddress)
    };

    const alexaApplication = new AlexaApplication(
      controllers,
      this.externalExpressPort,
      this.secretConfiguration,
      this.useRawWebApp);

    // alexaApplication.use(morgan('common'));
    alexaApplication.use(bodyParser.json());
    alexaApplication.use(bodyParser.urlencoded({ extended: true }));
    // alexaApplication.use(errorHandler);
    alexaApplication.start();

    // const alexaEndpoints = listEndpoints(alexaApplication.getApplication());
    // logger.info({ alexaEndpoints }, 'Alexa Endpoints');

    const webApplication = new WebApplication(
      controllers,
      this.internalExpressPort);
    webApplication.use(morgan('common'));
    webApplication.use(bodyParser.json());
    webApplication.use(errorHandler);
    webApplication.start();

    // const webEndpoints = listEndpoints(webApplication.getApplication());
    // logger.info({ webEndpoints }, 'Web Endpoints');

    return true;
  }
}

module.exports = ApplicationRouter;
