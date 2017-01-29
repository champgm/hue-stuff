
const makeRequest = require('request-promise');
const RequestOptionsUtil = require('../RequestOptionsUtil');

class LightUtil {
  constructor(bridgeUri) {
    this.bridgeUri = bridgeUri;
    this.requestOptionsUtil = new RequestOptionsUtil(bridgeUri);
  }

  async getAllLights() {
    const options = this.requestOptionsUtil.simpleGet('lights');
    console.log(`Will GET with options: ${JSON.stringify(options)}`);
    const lights = await makeRequest(options);

    // Now, we need to record each light's ID INSIDE of the light as well.
    // The web UI depends on that.
    for (const lightId in lights) {
      if (Object.prototype.hasOwnProperty.call(lights, lightId)) {
        const light = lights[lightId];
        light.id = lightId;
        lights[lightId] = light;
      }
    }
    return lights;
  }

  async getLight(lightId) {
    const getOptions = this.requestOptionsUtil.simpleGet(`lights/${lightId}`);
    console.log(`Will GET with options: ${JSON.stringify(getOptions)}`);
    const light = await makeRequest(getOptions);
    return light;
  }

  async setLightState(lightId, state) {
    const putOptions = this.requestOptionsUtil.putWithBody(`lights/${lightId}/state`, state);
    console.log(`Will PUT with options: ${JSON.stringify(putOptions)}`);
    await makeRequest(putOptions);
    return this.getLight(lightId);
  }

  async toggleLight(lightId) {
    const light = await this.getLight(lightId);
    const toggledState = { on: !(light.state.on) };
    return this.setLightState(lightId, toggledState);
  }

  async turnLightOff(lightId) {
    const offState = { on: false };
    return this.setLightState(lightId, offState);
  }

  async turnLightOn(lightId) {
    const onState = { on: true };
    return this.setLightState(lightId, onState);
  }
}

module.exports = LightUtil;
