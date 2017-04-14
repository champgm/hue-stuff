
const makeRequest = require('request-promise');
const RequestOptionsUtil = require('../RequestOptionsUtil');
const UtilityScenes = require('./UtilityScenes');
const LightUtil = require('./LightUtil');

class SceneUtil {
  constructor(bridgeUri) {
    this.bridgeUri = bridgeUri;
    this.requestOptionsUtil = new RequestOptionsUtil(bridgeUri);
    this.lightUtil = new LightUtil(bridgeUri);
  }

  async getAllScenes(v2ScenesRequested) {
    // First, get all of the bridge scenes
    const options = this.requestOptionsUtil.simpleGet('scenes');
    console.log(`Will GET with options: ${JSON.stringify(options)}`);
    const scenes = await makeRequest(options);

    if (v2ScenesRequested) {
      console.log('V2 Scenes requested, will return only V2 scenes.');
    } else {
      console.log('V2 Scenes not requested, will return all.');
    }

    // Start collecting all scenes.
    const resultScenes = {};

    // Add any Utility scenes first.
    const utilityScenes = UtilityScenes.getAllUtilityScenes();
    for (const sceneId in utilityScenes) {
      if (Object.prototype.hasOwnProperty.call(utilityScenes, sceneId)) {
        resultScenes[sceneId] = utilityScenes[sceneId];
      }
    }

    // Now, we need to record each light's ID INSIDE of the light.
    // While we do this, we might as well do the V2 filtering as well.
    // For each top-level attribute (which, if it's what we're looking for, will be a scene ID)
    for (const sceneId in scenes) {
      // Check to make sure it's a real attribute and not some weird superclass attribute
      if (Object.prototype.hasOwnProperty.call(scenes, sceneId)) {
        // Grab the scene and put its ID inside
        const scene = scenes[sceneId];
        scene.id = sceneId;
        scenes[sceneId] = scene;

        // Filter out non-V2 scenes, if they're not wanted.
        if (v2ScenesRequested) {
          if (scene.version === 2) {
            resultScenes[sceneId] = scenes[sceneId];
          }
        } else {
          resultScenes[sceneId] = scenes[sceneId];
        }
      }
    }
    return resultScenes;
  }

  async getScene(sceneId) {
    const getOptions = this.requestOptionsUtil.simpleGet(`scenes/${sceneId}`);
    console.log(`Will GET with options: ${JSON.stringify(getOptions)}`);
    const scene = await makeRequest(getOptions);
    return scene;
  }

  async activateScene(sceneId) {
    if (UtilityScenes.getAllUtilitySceneIds().includes(sceneId)) {
      switch (sceneId) {
        case UtilityScenes.getAllOffId(): {
          const lights = await this.lightUtil.getAllLights();
          for (const lightId in lights) {
            if (Object.prototype.hasOwnProperty.call(lights, lightId)) {
              await this.lightUtil.turnLightOff(lightId);
            }
          }
          return { sceneId: { sceneId } };
        }
        default: {
          const unsupportedMessage = `Unsupported utility scene ID: ${sceneId}`;
          console.log(unsupportedMessage);
          return new Error(unsupportedMessage);
        }
      }
    } else {
      const getOptions = this.requestOptionsUtil.simpleGet(`scenes/${sceneId}`);
      console.log(`Will GET with options: ${JSON.stringify(getOptions)}`);
      const scene = await makeRequest(getOptions);

      for (const lightId in scene.lightstates) {
        if (Object.prototype.hasOwnProperty.call(scene.lightstates, lightId)) {
          const lightState = scene.lightstates[lightId];
          await this.lightUtil.setLightState(lightId, lightState);
        }
      }
      const currentSceneState = await makeRequest(getOptions);
      return currentSceneState;
    }
  }

  async update(itemId, json) {
    const uri = `scenes/${itemId}`;
    const putOptions = this.requestOptionsUtil.putWithBody(uri, json);
    const response = await makeRequest(putOptions);
    return response;
  }

}

module.exports = SceneUtil;
