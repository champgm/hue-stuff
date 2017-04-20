const CommonController = require('./CommonController');
const makeRequest = require('request-promise');
const path = require('path');
const logger = require('../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });

const type = 'lights';

class LightUtil extends CommonController {

  constructor(bridgeUri) {
    super(type, bridgeUri, logger);
  }

  async setState(itemId, state) {
    const options = this.requestOptionsUtil.putWithBody(`${this.type}/${itemId}/state`, state);
    console.log(`Will PUT with options: ${JSON.stringify(options)}`);
    await makeRequest(options);
    return this.get(itemId);
  }

  async getState(itemId) {
    const options = this.requestOptionsUtil.simpleGet(`${this.type}/${itemId}/state`);
    this.logger.info({ options }, 'Will GET with options.');
    const item = await makeRequest(options);
    return item;
  }

  async select(itemId) {
    logger.info({ itemId }, `Select called for ${type}`);
    const item = await this.get(itemId);
    logger.info({ item }, 'Item retrieved.');
    const toggledState = { on: !(item.state.on) };
    return this.setState(itemId, toggledState);
  }

}

module.exports = LightUtil;
