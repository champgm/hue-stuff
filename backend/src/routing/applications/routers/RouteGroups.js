const path = require('path');
const logger = require('../../../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });

const itemType = 'groups';

const route = (util, application) => {
  application.get(`/${itemType}`, async (request, response, next) => {
    logger.info(`get ${itemType} called`);
    const items = await util.getAllGroups();
    response.send(items);
    logger.info('Request handled.');
  });

  application.get(`/${itemType}/:itemId`, async (request, response, next) => {
    response.status(400);
    response.json({ error: 'This endpoint has not yet been implemented.' });
  });

  application.put(`/${itemType}/:itemId`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `update ${itemType} called`);
    const item = await util.update(itemId, request.body);
    response.json(item);
    logger.info('Request handled.');
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
    const itemId = request.itemId;
    logger.info({ itemId }, `select ${itemType} called`);
    const item = await util.toggleGroup(itemId);
    response.json(item);
    logger.info('Request handled.');
  });

  return application;
};

module.exports = route;
