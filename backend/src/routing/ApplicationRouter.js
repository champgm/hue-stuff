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

class ApplicationRouter {
  constructor(expressConfiguration, bridgeDetails, broadcastAddress, secretConfiguration) {
    this.bridgeUri =
      `http://${bridgeDetails.bridgeIp}:` +
      `${bridgeDetails.bridgePort}/` +
      `api/${bridgeDetails.bridgeToken}`;
    this.externalExpressPort = expressConfiguration.externalPort;
    this.internalExpressPort = expressConfiguration.internalPort;
    this.secretConfiguration = secretConfiguration;
    this.broadcastAddress = broadcastAddress;
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
      this.secretConfiguration);

    alexaApplication.use(morgan('common'));
    alexaApplication.use(bodyParser.json());
    alexaApplication.use(errorHandler);
    alexaApplication.start();

    const webApplication = new WebApplication(
      controllers,
      this.internalExpressPort);
    webApplication.use(morgan('common'));
    webApplication.use(bodyParser.json());
    alexaApplication.use(errorHandler);
    webApplication.start();

    return true;
  }
}

module.exports = ApplicationRouter;
