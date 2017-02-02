const Hs100Api = require('hs100-api');

class TPLinkPlugUtil {
  constructor() {
    this.client = new Hs100Api.Client();
    this.knownPlugs = {};
    const discovery = this.client.startDiscovery();
    discovery.on('plug-new', (plug) => {
      plug.getInfo().then(console.log);
      plug.setPowerState(true);
      this.knownPlugs[plug.deviceId] = plug;
    });
    discovery.on('plug-online', (plug) => {
      plug.getInfo().then(console.log);
      this.knownPlugs[plug.deviceId] = JSON.parse(JSON.stringify(plug));
      this.knownPlugs[plug.deviceId].id = plug.deviceId;
    });
  }


  async getAllPlugs() {
    return this.knownPlugs;
  }

  async getPlug(plugId) {
    return this.knownPlugs[plugId];
  }

  async setPlugState(plugId, onBoolean) {
    const setStateResult = await this.getPlug(plugId).setPowerState(onBoolean);
    return setStateResult;
  }

  async togglePlug(plugId) {
    const currentPowerState = await this.getPlug(plugId).getPowerState();
    return this.setPlugState(!currentPowerState);
  }

  async turnPlugOff(plugId) {
    return this.setPlugState(plugId, false);
  }

  async turnPlugOn(plugId) {
    return this.setPlugState(plugId, true);
  }
}

module.exports = TPLinkPlugUtil;
