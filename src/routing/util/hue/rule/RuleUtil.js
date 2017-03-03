const makeRequest = require('request-promise');
const RequestOptionsUtil = require('../../RequestOptionsUtil');

class RuleUtil {
  constructor(bridgeUri) {
    this.bridgeUri = bridgeUri;
    this.requestOptionsUtil = new RequestOptionsUtil(bridgeUri);
  }

  async updateRule(ruleId, ruleJson) {
    const rulesUri = `rules/${ruleId}`;
    const putOptions = this.requestOptionsUtil.putWithBody(rulesUri, ruleJson);
    const response = await makeRequest(putOptions);
    return response;
  }

}

module.exports = RuleUtil;
