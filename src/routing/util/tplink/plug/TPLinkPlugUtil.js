const Hs100Api = require('hs100-api');

class TPLinkPlugUtil {
  constructor(plugIps) {
    this.knownPlugs = [];
    plugIps.forEach(async (plugIp) => {
      this.knownPlugs.push(plugIp);
    });
  }


  async getAllPlugs() {
    const resultPlugs = {};
    for (const plugIp of this.knownPlugs) {
      const plug = await this.getPlug(plugIp);
      const info = await plug.getInfo();
      resultPlugs[plugIp] = info;
      resultPlugs[plugIp].id = plugIp;
    }
    return resultPlugs;
  }

  async getPlug(plugId) {
    const client = new Hs100Api.Client();
    const plug = await client.getPlug({ host: plugId });
    return plug;
  }

  async setPlugState(plugId, onBoolean) {
    const plug = await this.getPlug(plugId);
    const setStateResult = await plug.setPowerState(onBoolean);
    return setStateResult;
  }

  async togglePlug(plugId) {
    const plug = await this.getPlug(plugId);
    const currentPowerState = await plug.getPowerState();
    return this.setPlugState(plugId, !currentPowerState);
  }

  async turnPlugOff(plugId) {
    return this.setPlugState(plugId, false);
  }

  async turnPlugOn(plugId) {
    return this.setPlugState(plugId, true);
  }
}

module.exports = TPLinkPlugUtil;
