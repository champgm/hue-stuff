const path = require('path');
const logger = require('../../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });
const routeCommon = require('./RouteCommon');
const routeSelect = require('./RouteSelect');

const itemType = 'plugs';

const route = (controller, application) => {
  routeCommon(itemType, controller, application);
  routeSelect(itemType, controller, application);

  application.get(`/${itemType}`, async (request, response, next) => {
    logger.info(`get ${itemType} called`);
    const items = await controller.getAll();
    response.send(items);
    logger.info('Request handled.');
  });

  application.get(`/${itemType}/:itemId`, async (request, response, next) => {
    response.status(400);
    response.json({ error: 'This endpoint has not yet been implemented.' });
  });

  application.put(`/${itemType}/:itemId`, async (request, response, next) => {
    response.status(400);
    response.json({ error: 'This endpoint has not yet been implemented.' });
  });

  application.get(`/${itemType}/:itemId/state`, async (request, response, next) => {
    response.status(400);
    response.json({ error: 'This endpoint has not yet been implemented.' });
  });

  application.put(`/${itemType}/:itemId/state`, async (request, response, next) => {
    response.status(400);
    response.json({ error: 'This endpoint has not yet been implemented.' });
  });

  application.get(`/${itemType}/:itemId/select`, async (request, response, next) => {
    logger.info(`select ${itemType} called`);
    const itemId = request.itemId;
    logger.info(`Plugid from request: ${itemId}`);
    const item = await controller.select(itemId);
    response.send(item);
    logger.info('Request handled.');
  });

  return application;
};

module.exports = route;
