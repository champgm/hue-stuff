const path = require('path');
const logger = require('../../../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });

const itemType = 'lights';

const routeLights = (util, application) => {
  application.get(`/${itemType}`, async (request, response, next) => {
    logger.info(`get ${itemType} called`);
    const items = await util.getAllLights();
    response.json(items);
    logger.info('Request handled.');
  });

  application.get(`/${itemType}/:itemId`, async (request, response, next) => {

  });

  application.put(`/${itemType}/:itemId`, async (request, response, next) => {
    logger.info(`put ${itemType} called`);
    const itemId = request.itemId;
    const item = await util.update(itemId, request.body);
    response.json(item);
    logger.info('Request handled.');
  });

  application.get(`/${itemType}/:itemId/state`, async (request, response, next) => {

  });

  application.put(`/${itemType}/:itemId/state`, async (request, response, next) => {

  });

  application.get(`/${itemType}/:itemId/toggle`, async (request, response, next) => {
    logger.info(`toggle ${itemType} called`);
    const itemId = request.itemId;
    const item = await util.toggleLight(itemId);
    response.json(item);
    logger.info('Request handled.');
  });

  return application;
};

module.exports = routeLights;
