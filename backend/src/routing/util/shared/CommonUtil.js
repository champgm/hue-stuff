const makeRequest = require('request-promise');
const RequestOptionsUtil = require('./RequestOptionsUtil');
const path = require('path');
const logger = require('../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });

class CommonUtil {
  constructor(type, bridgeUri) {
    this.type = type;
    this.bridgeUri = bridgeUri;
    this.requestOptionsUtil = new RequestOptionsUtil(bridgeUri);
  }

  async getAll() {
    const options = this.requestOptionsUtil.simpleGet(this.type);
    console.log(`Will GET with options: ${JSON.stringify(options)}`);
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
    const getOptions = this.requestOptionsUtil.simpleGet(`${this.type}/${id}`);
    console.log(`Will GET with options: ${JSON.stringify(getOptions)}`);
    const item = await makeRequest(getOptions);
    return item;
  }

}

module.exports = CommonUtil;
