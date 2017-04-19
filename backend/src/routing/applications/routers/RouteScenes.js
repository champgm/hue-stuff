const path = require('path');
const logger = require('../../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });
const routeCommon = require('./RouteCommon');
const routeSelect = require('./RouteSelect');
const KnownParameterNames = require('../../controllers/utilities/KnownParameterNames');

const itemType = 'scenes';

const route = (controller, application) => {

  application.get(`/${itemType}`, async (request, response, next) => {
    logger.info(`get ${itemType} called`);
    logger.info({ query: request.query, param: KnownParameterNames.getV2() }, 'request.query');
    const v2ScenesRequested =
      (KnownParameterNames.getV2() in request.query) &&
      (request.query[KnownParameterNames.getV2()] === 'true');

    const items = await controller.getAll(v2ScenesRequested);
    response.send(items);
    logger.info('Request handled.');
  });

  routeCommon(itemType, controller, application);
  routeSelect(itemType, controller, application);
  return application;
};

module.exports = route;
