const RequestOptionsUtil = require("./util/RequestOptionsUtil");
const TPLinkPlugUtil = require("./util/tplink/plug/TPLinkPlugUtil");
const AlexaApplication = require("./applications/AlexaApplication");
const HueApplication = require("./applications/HueApplication");
const LightUtil = require("./util/hue/light/LightUtil");
const SceneUtil = require("./util/hue/scene/SceneUtil");
const GroupUtil = require("./util/hue/group/GroupUtil");
const RuleUtil = require("./util/hue/rule/RuleUtil");

class Routing {
  constructor(expressConfiguration, bridgeDetails, plugIps, secretConfiguration) {
    this.bridgeUri = `http://${bridgeDetails.bridgeIp}:${bridgeDetails.bridgePort}/` +
      `api/${bridgeDetails.bridgeToken}`;
    this.externalExpressPort = expressConfiguration.externalPort;
    this.internalExpressPort = expressConfiguration.internalPort;
    this.secretConfiguration = secretConfiguration;
    this.plugIps = plugIps;
  }

  async start() {
    const requestOptionsUtil = new RequestOptionsUtil(this.bridgeUri);
    const lightUtil = new LightUtil(this.bridgeUri);
    const sceneUtil = new SceneUtil(this.bridgeUri);
    const groupUtil = new GroupUtil(this.bridgeUri);
    const ruleUtil = new RuleUtil(this.bridgeUri);
    const plugUtil = new TPLinkPlugUtil(this.plugIps);

    const hueUtilities = {
      requestOptionsUtil,
      lightUtil,
      sceneUtil,
      groupUtil,
      ruleUtil,
      plugUtil
    };

    const alexaApplication = new AlexaApplication(
      hueUtilities,
      this.externalExpressPort,
      this.secretConfiguration);
    alexaApplication.start();

    const hueApplication = new HueApplication(hueUtilities, this.internalExpressPort);
    hueApplication.start();

    return true;
  }
}

module.exports = Routing;
