const RequestOptionsUtil = require('./util/RequestOptionsUtil');
const TPLinkPlugUtil = require('./util/tplink/plug/TPLinkPlugUtil');
const AlexaApplication = require('./applications/AlexaApplication');
const WebApplication = require('./applications/WebApplication');
const LightUtil = require('./util/hue/light/LightUtil');
const SceneUtil = require('./util/hue/scene/SceneUtil');
const GroupUtil = require('./util/hue/group/GroupUtil');
const RuleUtil = require('./util/hue/rule/RuleUtil');
const bodyParser = require('body-parser');
const morgan = require('morgan');

class Controller {
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
      lightUtil: new LightUtil(this.bridgeUri),
      sceneUtil: new SceneUtil(this.bridgeUri),
      groupUtil: new GroupUtil(this.bridgeUri),
      ruleUtil: new RuleUtil(this.bridgeUri),
      plugUtil: new TPLinkPlugUtil(this.plugIps)
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

module.exports = Controller;
