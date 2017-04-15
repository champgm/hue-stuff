
const makeRequest = require('request-promise');
const UtilityScenes = require('./utilities/UtilityScenes');
const LightController = require('./LightController');
const CommonController = require('./CommonController');
const path = require('path');
const logger = require('../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });

const type = 'scenes';

class SceneUtil extends CommonController {

  constructor(bridgeUri) {
    super(type, bridgeUri, logger);
    this.lightController = new LightController(bridgeUri);
  }

  async getAll(v2ScenesRequested) {
    if (v2ScenesRequested) {
      logger.info('V2 Scenes requested, will return only V2 scenes.');
    } else {
      logger.info('V2 Scenes not requested, will return all.');
    }

    // First, get all of the bridge scenes
    const scenes = super.getAll();

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
        const scene = scenes[sceneId];
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

  async select(sceneId) {
    if (UtilityScenes.getAllUtilitySceneIds().includes(sceneId)) {
      switch (sceneId) {
        case UtilityScenes.getAllOffId(): {
          const lights = await this.lightController.getAll();
          for (const lightId in lights) {
            if (Object.prototype.hasOwnProperty.call(lights, lightId)) {
              await this.lightController.turnOff(lightId);
            }
          }
          return { sceneId: { sceneId } };
        }
        default: {
          const unsupportedMessage = `Unsupported utility scene ID: ${sceneId}`;
          logger.info(unsupportedMessage);
          return new Error(unsupportedMessage);
        }
      }
    } else {
      const scene = this.get(sceneId);
      for (const lightId in scene.lightstates) {
        if (Object.prototype.hasOwnProperty.call(scene.lightstates, lightId)) {
          const lightState = scene.lightstates[lightId];
          await this.lightController.setState(lightId, lightState);
        }
      }

      // Return the current state of the scene
      return this.get(sceneId);
    }
  }

}

module.exports = SceneUtil;
