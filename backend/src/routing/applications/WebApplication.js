const util = require('util');
const path = require('path');
const express = require('express');
const logger = require('../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });

const routePlugs = require('./routers/RoutePlugs');
const routeRules = require('./routers/RouteRules');
const routeLights = require('./routers/RouteLights');
const routeGroups = require('./routers/RouteGroups');
const routeScenes = require('./routers/RouteScenes');
const routeSensors = require('./routers/RouteSensors');
const routeSchedules = require('./routers/RouteSchedules');

class WebApplication {
  constructor(controllers, port) {
    this.application = express();
    this.controllers = controllers;
    this.port = port;
  }

  getApplication() {
    return this.application;
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

    this.application = routePlugs(this.controllers.plugController, this.application);
    this.application = routeRules(this.controllers.ruleController, this.application);
    this.application = routeLights(this.controllers.lightController, this.application);
    this.application = routeGroups(this.controllers.groupController, this.application);
    this.application = routeScenes(this.controllers.sceneController, this.application);
    this.application = routeSensors(this.controllers.sensorController, this.application);
    this.application = routeSchedules(this.controllers.scheduleController, this.application);

    // Stoart the local server
    logger.info('Starting Hue application...');
    try {
      this.application.listen(this.port, () => {
        logger.info({ port: this.port }, 'Hue Application listening.');
      });
    } catch (caughtError) {
      logger.error({ keys: Object.getOwnPropertyNames(caughtError) }, 'error keys');
      const error = {
        message: caughtError.message,
        type: caughtError.type,
        stack: caughtError.stack
      };
      logger.error({ error }, 'An unhandled error was caught.');
    }
  }

  routeStaticEndpoints() {
    // Add the webapp folder as static content. This contains the app UI.
    const webAppFolder = path.join(__dirname, '../../../../dist');
    logger.info({ webAppFolder }, 'Static content will be read.');
    this.application.use('/', express.static(webAppFolder));

    // Add the documentation folder as static content. This contains the doc UI.
    const docFolder = path.join(__dirname, '../../../documentation');
    logger.info({ docFolder }, 'Documenation content will be read.');
    this.application.use('/documentation', express.static(docFolder));

    // It needs to access the node_modules.
    // I really haven't found a better way to do this.
    const nodeModulesFolder = path.join(__dirname, '../../../../node_modules');
    logger.info({ nodeModulesFolder }, 'Node Modules content will be read.');
    this.application.use('/node_modules', express.static(nodeModulesFolder));
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
}

module.exports = WebApplication;
