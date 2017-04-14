const path = require('path');
const logger = require('../../../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });

const itemType = 'lights';

const route = (util, application) => {
  application.get(`/${itemType}`, async (request, response, next) => {
    logger.info(`get ${itemType} called`);
    const items = await util.getAllLights();
    response.json(items);
    logger.info('Request handled.');
  });

  application.get(`/${itemType}/:itemId`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `get ${itemType} called`);
    const item = await util.getLight(itemId);
    response.json(item);
    logger.info('Request handled.');
  });

  application.put(`/${itemType}/:itemId`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `put ${itemType} called`);
    const item = await util.update(itemId, request.body);
    response.json(item);
    logger.info('Request handled.');
  });

  application.get(`/${itemType}/:itemId/state`, async (request, response, next) => {

  });

  application.put(`/${itemType}/:itemId/state`, async (request, response, next) => {

  });

  application.get(`/${itemType}/:itemId/select`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `select ${itemType} called`);
    const item = await util.toggleLight(itemId);
    response.json(item);
    logger.info('Request handled.');
  });

  return application;
};

module.exports = route;
