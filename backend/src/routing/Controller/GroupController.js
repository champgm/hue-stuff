const makeRequest = require('request-promise');
const CommonController = require('./CommonController');
const path = require('path');
const logger = require('../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });

const type = 'groups';

class GroupUtil extends CommonController {

  constructor(bridgeUri) {
    super(type, bridgeUri, logger);
  }

  async setState(itemId, state) {
    const options = this.requestOptionsUtil.putWithBody(`${this.type}/${itemId}/action`, state);
    console.log(`Will PUT with options: ${JSON.stringify(options)}`);
    await makeRequest(options);
    return this.getState(itemId);
  }

  async getState(itemId) {
    const options = this.requestOptionsUtil.simpleGet(`${this.type}/${itemId}/action`);
    this.logger.info({ options }, 'Will GET with options.');
    const item = await makeRequest(options);
    return item;
  }

  async select(groupId) {
    const group = await this.getGroup(groupId);
    const toggledAction = { on: !(group.action.on) };
    return this.setState(groupId, toggledAction);
  }

}

module.exports = GroupUtil;
