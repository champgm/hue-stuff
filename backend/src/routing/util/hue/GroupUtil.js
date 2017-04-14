
const makeRequest = require('request-promise');
const RequestOptionsUtil = require('../RequestOptionsUtil');

class GroupUtil {
  constructor(bridgeUri) {
    this.bridgeUri = bridgeUri;
    this.requestOptionsUtil = new RequestOptionsUtil(bridgeUri);
  }

  async getAllGroups() {
    const options = this.requestOptionsUtil.simpleGet('groups');
    console.log(`Will GET with options: ${JSON.stringify(options)}`);
    const groups = await makeRequest(options);

    for (const groupId in groups) {
      if (Object.prototype.hasOwnProperty.call(groups, groupId)) {
        const group = groups[groupId];
        group.id = groupId;
        groups[groupId] = group;
      }
    }

    return groups;
  }

  async getGroup(groupId) {
    const getOptions = this.requestOptionsUtil.simpleGet(`groups/${groupId}`);
    console.log(`Will GET with options: ${JSON.stringify(getOptions)}`);
    const group = await makeRequest(getOptions);
    return group;
  }

  async setGroupAction(groupId, action) {
    const putOptions = this.requestOptionsUtil.putWithBody(`groups/${groupId}/action`, action);
    console.log(`Will PUT with options: ${JSON.stringify(putOptions)}`);
    await makeRequest(putOptions);
    return this.getGroup(groupId);
  }

  async toggleGroup(groupId) {
    const group = await this.getGroup(groupId);
    const toggledAction = { on: !(group.action.on) };
    return this.setGroupAction(groupId, toggledAction);
  }

  async turnGroupOff(groupId) {
    const offAction = { on: false };
    return this.setGroupAction(groupId, offAction);
  }

  async turnGroupOn(groupId) {
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
