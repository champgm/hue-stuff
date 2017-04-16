const Hs100Api = require('hs100-api');
const path = require('path');
const logger = require('bunyan')
  .createLogger({ name: `${path.basename(__filename)}` });

const client = new Hs100Api.Client();

class TPLinkPlugUtil {

  constructor(plugIps) {
    this.knownPlugs = [];
    plugIps.forEach(async (plugIp) => {
      this.knownPlugs.push(plugIp);
    });

    const watchForNewPlugs = (plug) => {
      plug.getInfo().then((info) => {
        logger.info({ plug, info }, 'New plug found.');
      });
      plug.setPowerState(true);
    };

    const watchForKnownPlugs = (plug) => {
      plug.getInfo().then((info) => {
        logger.info({ plug, info }, 'Known plug found.');
      });
    };

    client.startDiscovery()
      .on('plug-new', watchForNewPlugs)
      .on('plug-online', watchForKnownPlugs);
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
