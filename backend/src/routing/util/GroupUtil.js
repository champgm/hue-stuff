
const makeRequest = require('request-promise');
const RequestOptionsUtil = require('./RequestOptionsUtil');
const CommonUtil = require('./shared/CommonUtil');

class GroupUtil extends CommonUtil {
  constructor(bridgeUri) {
    super('groups', bridgeUri);
  }

  async setState(groupId, action) {
    const putOptions = this.requestOptionsUtil.putWithBody(`groups/${groupId}/action`, action);
    console.log(`Will PUT with options: ${JSON.stringify(putOptions)}`);
    await makeRequest(putOptions);
    return this.getGroup(groupId);
  }

  async select(groupId) {
    const group = await this.getGroup(groupId);
    const toggledAction = { on: !(group.action.on) };
    return this.setGroupAction(groupId, toggledAction);
  }

  async turnOff(groupId) {
    const offAction = { on: false };
    return this.setGroupAction(groupId, offAction);
  }

  async turnOn(groupId) {
    const onAction = { on: true };
    return this.setGroupAction(groupId, onAction);
  }

  async update(itemId, json) {
    const uri = `groups/${itemId}`;
    const putOptions = this.requestOptionsUtil.putWithBody(uri, json);
    const response = await makeRequest(putOptions);
    return response;
  }
}

module.exports = GroupUtil;
