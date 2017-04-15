const Hs100Api = require('hs100-api');

class TPLinkPlugUtil extends CommonUtil {

    constructor(bridgeUri) {
    super(type, bridgeUri, logger);
  }


  constructor(plugIps) {
    this.knownPlugs = [];
    plugIps.forEach(async (plugIp) => {
      this.knownPlugs.push(plugIp);
    });
  }


  async getAll() {
    const resultPlugs = {};
    for (const plugIp of this.knownPlugs) {
      const plug = await this.getPlug(plugIp);
      const info = await plug.getInfo();
      resultPlugs[plugIp] = info;
      resultPlugs[plugIp].id = plugIp;
    }
    return resultPlugs;
  }

  async get(plugId) {
    const client = new Hs100Api.Client();
    const plug = await client.getPlug({ host: plugId });
    return plug;
  }

  async setState(plugId, onBoolean) {
    const plug = await this.getPlug(plugId);
    const setStateResult = await plug.setPowerState(onBoolean);
    return setStateResult;
  }

  async select(plugId) {
    const plug = await this.getPlug(plugId);
    const currentPowerState = await plug.getPowerState();
    return this.setPlugState(plugId, !currentPowerState);
  }

  async turnOff(plugId) {
    return this.setPlugState(plugId, false);
  }

  async turnOn(plugId) {
    return this.setPlugState(plugId, true);
  }
}

module.exports = TPLinkPlugUtil;
