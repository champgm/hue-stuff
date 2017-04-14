const AlexaApplication = require('./applications/AlexaApplication');
const WebApplication = require('./applications/WebApplication');
const RequestOptionsUtil = require('./controller/utilities/RequestOptionsUtil');
const plugController = require('./controller/PlugController');
const LightController = require('./controller/LightController');
const SceneController = require('./controller/SceneController');
const GroupController = require('./controller/GroupController');
const RuleController = require('./controller/RuleController');
const bodyParser = require('body-parser');
const morgan = require('morgan');

class ApplicationRouter {
  constructor(expressConfiguration, bridgeDetails, plugIps, secretConfiguration) {
    this.bridgeUri =
      `http://${bridgeDetails.bridgeIp}:` +
      `${bridgeDetails.bridgePort}/` +
      `api/${bridgeDetails.bridgeToken}`;
    this.externalExpressPort = expressConfiguration.externalPort;
    this.internalExpressPort = expressConfiguration.internalPort;
    this.secretConfiguration = secretConfiguration;
    this.plugIps = plugIps;
  }

  async start() {
    const hueUtilities = {
      requestOptionsUtil: new RequestOptionsUtil(this.bridgeUri),
      lightController: new LightController(this.bridgeUri),
      sceneController: new SceneController(this.bridgeUri),
      groupController: new GroupController(this.bridgeUri),
      ruleController: new RuleController(this.bridgeUri),
      plugUtil: new plugController(this.plugIps)
    };

    const alexaApplication = new AlexaApplication(
      hueUtilities,
      this.externalExpressPort,
      this.secretConfiguration);

    alexaApplication.use(morgan('common'));
    alexaApplication.use(bodyParser.json());
    alexaApplication.start();

    const webApplication = new WebApplication(
      hueUtilities,
      this.internalExpressPort);
    webApplication.use(morgan('common'));
    webApplication.use(bodyParser.json());
    webApplication.start();

    return true;
  }
}

module.exports = ApplicationRouter;
