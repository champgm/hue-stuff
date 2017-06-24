const Hs100Api = require('hs100-api');
const path = require('path');
const logger = require('bunyan')
  .createLogger({ name: `${path.basename(__filename)}` });


class TPLinkPlugUtil {

  constructor(broadcastAddress) {
    this.knownPlugIps = [];
    this.knownPlugs = {};
    this.client = new Hs100Api.Client({
      broadcast: broadcastAddress
    });

    const watchForNewPlugs = (plug) => {
      plug.getInfo().then((info) => {
        logger.info({ plugName: plug.name, host: plug.host }, 'New plug found.');
      });
      plug.setPowerState(true);
      this.knownPlugs[plug.host] = this.trimPlug(plug);
      if (!(plug.host in this.knownPlugs)) {
        plug.id = plug.host;
        this.knownPlugIps.push(plug.host);
      }
    };

    const watchForKnownPlugs = (plug) => {
      plug.getInfo().then((info) => {
        logger.info({ plugName: plug.name, host: plug.host }, 'Known plug found.');
        this.knownPlugs[plug.host] = this.trimPlug(plug);
        if (!(plug.host in this.knownPlugs)) {
          plug.id = plug.host;
          this.knownPlugIps.push(plug.host);
        }
      });
    };

    logger.info('Starting plug discovery.');
    this.client.startDiscovery()
      .on('plug-new', watchForNewPlugs)
      .on('plug-online', watchForKnownPlugs);
    this.client.sendDiscovery();
  }

  async getAll() {
    return this.knownPlugs;
  }

  async get(plugId) {
    const plug = await this.client.getPlug({ host: plugId });
    return plug;
  }

  async setState(plugId, onBoolean) {
    const plug = await this.get(plugId);
    const setStateResult = await plug.setPowerState(onBoolean);
    return setStateResult;
  }

  async select(plugId) {
    const plug = await this.get(plugId);
    const currentPowerState = await plug.getPowerState();
    return this.setState(plugId, !currentPowerState);
  }

  async turnOff(plugId) {
    return this.setState(plugId, false);
  }

  async turnOn(plugId) {
    return this.setState(plugId, true);
  }

  async update(itemId, json) {
    const response = await this.setState(itemId, json.on);
    return response;
  }

  trimPlug(originalPlug) {
    const trimmedPlug = {};
    trimmedPlug.deviceId = originalPlug.deviceId;
    trimmedPlug.host = originalPlug.host;
    trimmedPlug.port = originalPlug.port;
    trimmedPlug.name = originalPlug.name;
    trimmedPlug.deviceName = originalPlug.deviceName;
    trimmedPlug.model = originalPlug.model;
    trimmedPlug.softwareVersion = originalPlug.softwareVersion;
    trimmedPlug.hardwareVersion = originalPlug.hardwareVersion;
    trimmedPlug.mac = originalPlug.mac;
    trimmedPlug.latitude = originalPlug.latitude;
    trimmedPlug.longitude = originalPlug.longitude;
    trimmedPlug.status = originalPlug.status;
    trimmedPlug.sysInfo = originalPlug.sysInfo;
    trimmedPlug.cloudInfo = originalPlug.cloudInfo;
    trimmedPlug.consumption = originalPlug.consumption;
    return trimmedPlug;
  }
}

module.exports = TPLinkPlugUtil;
