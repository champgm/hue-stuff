
/**
 * A utility class to help create options for HTTP requests
 *
 * @class RequestOptionsUtil
 */
class RequestOptionsUtil {

  /**
   * Creates an instance of RequestOptionsUtil.
   *
   * @param {any} bridgeUri - The URI prefix for all bridge calls.
   *                          It should look something like this:
   *                          http://${bridgeIp}:${bridgePort}/api/${bridgeToken}
   *
   * @memberOf RequestOptionsUtil
   */
  constructor(bridgeUri) {
    this.bridgeUri = bridgeUri;
  }

  /**
   * Builds a GET with given URI
   *
   * @param {any} uri - the URI to append to the bridge URI
   * @returns the options object
   *
   * @memberOf RequestOptionsUtil
   */
  simpleGet(uri) {
    const getOptions = {
      method: 'GET',
      uri: `${this.bridgeUri}/${uri}`,
      json: true
    };
    return getOptions;
  }

  /**
   * Builds a PUT with the given URI and includes the given body
   *
   * @param {any} uri - the URI to append to the bridge URI
   * @param {any} body - the body to send
   * @returns the options object
   *
   * @memberOf RequestOptionsUtil
   */
  putWithBody(uri, body) {
    const putOptions = {
      method: 'PUT',
      uri: `${this.bridgeUri}/${uri}`,
      json: true,
      body
    };
    return putOptions;
  }

}

module.exports = RequestOptionsUtil;
