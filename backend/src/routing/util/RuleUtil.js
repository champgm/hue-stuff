const makeRequest = require('request-promise');
const RequestOptionsUtil = require('./RequestOptionsUtil');

class RuleUtil {
  constructor(bridgeUri) {
    this.bridgeUri = bridgeUri;
    this.requestOptionsUtil = new RequestOptionsUtil(bridgeUri);
  }

  async getAll() {
    const options = this.requestOptionsUtil.simpleGet('rules');
    console.log(`Will GET with options: ${JSON.stringify(options)}`);
    const items = await makeRequest(options);

    for (const itemId in items) {
      if (Object.prototype.hasOwnProperty.call(items, itemId)) {
        const light = items[itemId];
        light.id = itemId;
        items[itemId] = light;
      }
    }
    return items;
  }

  async get(itemId) {
    const getOptions = this.requestOptionsUtil.simpleGet(`rules/${itemId}`);
    console.log(`Will GET with options: ${JSON.stringify(getOptions)}`);
    const item = await makeRequest(getOptions);
    return item;
  }

  async update(itemId, json) {
    const uri = `rules/${itemId}`;
    const putOptions = this.requestOptionsUtil.putWithBody(uri, json);
    const response = await makeRequest(putOptions);
    return response;
  }

}

module.exports = RuleUtil;
