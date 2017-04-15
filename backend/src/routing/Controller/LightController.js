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

  async select(itemId) {
    const item = await this.get(itemId);
    const toggledState = { on: !(item.state.on) };
    return this.setState(itemId, toggledState);
  }

}

module.exports = LightUtil;
