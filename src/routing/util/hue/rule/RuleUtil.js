const makeRequest = require("request-promise");
const RequestOptionsUtil = require("../../RequestOptionsUtil");

class RuleUtil {
  constructor(bridgeUri) {
    this.bridgeUri = bridgeUri;
    this.requestOptionsUtil = new RequestOptionsUtil(bridgeUri);
  }

  async update(itemId, json) {
    const uri = `rules/${itemId}`;
    const putOptions = this.requestOptionsUtil.putWithBody(uri, json);
    const response = await makeRequest(putOptions);
    return response;
  }

}

module.exports = RuleUtil;
