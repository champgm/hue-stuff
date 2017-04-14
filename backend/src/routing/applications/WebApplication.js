const path = require('path');
const logger = require('../../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });
const makeRequest = require('request-promise');
const express = require('express');
const util = require('util');

const KnownParameterNames = require('../util/KnownParameterNames');
const routeLights = require('./routers/RouteLights');


class WebApplication {
  constructor(hueUtilities, port) {
    this.application = express();
    this.hueUtilities = hueUtilities;
    this.port = port;
  }

  use(middleware) {
    this.application.use(middleware);
  }

  start() {
    this.routeStaticEndpoints();

    // Set possible URL parameters
    this.application.param('itemId', (request, response, next, itemId) => {
      request.itemId = itemId;
      next();
    });

    // Route gets and puts for the internal application
    // this.routeInternalGets(this.application, this.hueUtilities);
    // this.routeInternalPuts(this.application, this.hueUtilities);
    this.application = routeLights(this.hueUtilities.lightUtil, this.application);

    // Stoart the local server
    logger.info('Starting Hue application...');
    this.application.listen(this.port, () => {
      logger.info({ port: this.port }, 'Hue Application listening.');
    });
  }

  routeStaticEndpoints() {
    // Add the webapp folder as static content. This contains the app UI.
    const webAppFolder = path.join(__dirname, '../../../dist');
    logger.info({ webAppFolder }, 'Static content will be read.');
    this.application.use('/', express.static(webAppFolder));

    // Add the documentation folder as static content. This contains the doc UI.
    const docFolder = path.join(__dirname, '../../documentation');
    logger.info({ docFolder }, 'Documenation content will be read.');
    this.application.use('/documentation', express.static(docFolder));

    // It needs to access the node_modules.
    // I really haven't found a better way to do this.
    const nodeModulesFolder = path.join(__dirname, '../../../node_modules');
    logger.info({ nodeModulesFolder }, 'Node Modules content will be read.');
    this.application.use('/node_modules', express.static(nodeModulesFolder));
  }


  routeInternalPuts(internalApplication, utils) {
    const sceneUtil = utils.sceneUtil;
    const ruleUtil = utils.ruleUtil;
    const plugUtil = utils.plugUtil;


    logger.info('Routing activatescene');
    internalApplication.get('/activatescene', async (request, response) => {
      logger.info('activatescene called');
      const sceneId = WebApplication.getRequiredId(request, response, KnownParameterNames.getSceneId());
      logger.info(`Scene ID: ${sceneId}`);

      const result = await sceneUtil.activateScene(sceneId);
      response.send(result);
      logger.info('Request handled.');
    });

    logger.info('Routing updatescene');
    internalApplication.put('/updatescene', async (request, response) => {
      logger.info('updatescene called');
      const itemId = WebApplication.getRequiredId(request, response, KnownParameterNames.getSceneId());
      const updateResponse = await sceneUtil.update(itemId, request.body);
      response.send(updateResponse);
      logger.info('Request handled.');
    });


    logger.info('Routing toggleplug');
    internalApplication.get('/toggleplug', async (request, response) => {
      logger.info('toggleplug called');
      const plugId = WebApplication.getRequiredId(request, response, KnownParameterNames.getPlugId());
      logger.info(`Plugid from request: ${plugId}`);

      const toggleResult = await plugUtil.togglePlug(plugId);
      response.send(toggleResult);
      logger.info('Request handled.');
    });

    logger.info('Routing updaterule');
    internalApplication.put('/updaterule', async (request, response) => {
      logger.info('updaterule called');
      const itemId = WebApplication.getRequiredId(request, response, KnownParameterNames.getRuleId());
      const updateResponse = await ruleUtil.update(itemId, request.body);
      response.send(updateResponse);
      logger.info('Request handled.');
    });
  }

  routeInternalGets(internalApplication, utils) {
    // internalApplication.get('/debug', async (request, response) => {
    //   logger.info('debug called');
    //   const result = await lightUtil.getAllLights();
    //   response.send(result);
    //   logger.info('Request handled.');
    // });

    const requestOptionsUtil = utils.requestOptionsUtil;
    const sceneUtil = utils.sceneUtil;
    const plugUtil = utils.plugUtil;


    logger.info('Routing getscenes');
    internalApplication.get('/getscenes', async (request, response) => {
      logger.info('getscenes called');
      const v2ScenesRequested =
        (KnownParameterNames.getV2() in request.query) &&
        (request.query[KnownParameterNames.getV2()] === 'true');

      const scenes = await sceneUtil.getAllScenes(v2ScenesRequested);
      response.send(scenes);
      logger.info('Request handled.');
    });

    logger.info('Routing getschedules');
    internalApplication.get('/getschedules', async (request, response) => {
      logger.info('getschedules called');
      const options = requestOptionsUtil.simpleGet('schedules');

      logger.info(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
      logger.info('Request handled.');
    });


    logger.info('Routing getsensors');
    internalApplication.get('/getsensors', async (request, response) => {
      logger.info('getsensors called');
      const options = requestOptionsUtil.simpleGet('sensors');

      logger.info(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
      logger.info('Request handled.');
    });

    logger.info('Routing getrules');
    internalApplication.get('/getrules', async (request, response) => {
      logger.info('getrules called');
      const options = requestOptionsUtil.simpleGet('rules');

      logger.info(`Will GET with options: ${JSON.stringify(options)}`);
      const result = await makeRequest(options);
      response.send(result);
      logger.info('Request handled.');
    });

    logger.info('Routing getplugs');
    internalApplication.get('/getplugs', async (request, response) => {
      logger.info('getplugs called');

      const result = await plugUtil.getAllPlugs();
      response.send(result);
      logger.info('Request handled.');
    });
    return internalApplication;
  }

  /**
 * The requests that come in from express have cyclic references, so
 * JSON.stringify() freaks out. Use this for debugging instead.
 *
 * @param {any} request - the express request
 * @returns undefined
 */
  static printRequest(request) {
    logger.info(`Request: ${JSON.stringify(util.inspect(request))}`);
  }

  /**
 * Utility method to retrieve an item from the request query.
 *
 * @param {any} request - the express request
 * @param {any} response - the express response
 * @param {any} idKey - the key of the ID that should've been sent
 * @returns the ID, if an error isn't thrown
 */
  static getRequiredId(request, response, idKey) {
    let id;
    if (idKey in request.query) {
      id = request.query[idKey];
    } else {
      const error = new Error(`'${idKey}' not found in request: ` +
        `${JSON.stringify(util.inspect(request))}`);
      response.send(error);
      throw error;
    }
    return id;
  }
}

module.exports = WebApplication;
