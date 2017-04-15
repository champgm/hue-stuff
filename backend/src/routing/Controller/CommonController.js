const makeRequest = require('request-promise');
const RequestOptionsUtil = require('./utilities/RequestOptionsUtil');
const path = require('path');

class CommonUtil {
  constructor(type, bridgeUri, logger) {
    this.type = type;
    this.logger = logger;
    this.requestOptionsUtil = new RequestOptionsUtil(bridgeUri);
  }

  async getAll() {
    const options = this.requestOptionsUtil.simpleGet(this.type);
    this.logger.info({ options }, `Will GET with options.`);
    const items = await makeRequest(options);

    for (const id in items) {
      if (Object.prototype.hasOwnProperty.call(items, id)) {
        const item = items[id];
        item.id = id;
        items[id] = item;
      }
    }

    return items;
  }

  async get(id) {
    const options = this.requestOptionsUtil.simpleGet(`${this.type}/${id}`);
    this.logger.info({ options }, `Will GET with options.`);
    const item = await makeRequest(options);
    return item;
  }


  async update(itemId, json) {
    const uri = `${this.type}/${itemId}`;
    const putOptions = this.requestOptionsUtil.putWithBody(uri, json);
    const response = await makeRequest(putOptions);
    return response;
  }

  async turnOff(itemId) {
    const offState = { on: false };
    return this.setState(itemId, offState);
  }

  async turnOn(itemId) {
    const onState = { on: true };
    return this.setState(itemId, onState);
  }

}

module.exports = CommonUtil;
