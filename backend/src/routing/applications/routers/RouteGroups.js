const path = require('path');
const logger = require('../../../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });

const itemType = 'groups';

const routeGroups = (util, application) => {
  application.get(`/${itemType}`, async (request, response, next) => {
    logger.info(`get ${itemType} called`);
    const items = await util.getAllGroups();
    response.send(items);
    logger.info('Request handled.');
  });

  application.get(`/${itemType}/:itemId`, async (request, response, next) => {

  });

  application.put(`/${itemType}/:itemId`, async (request, response, next) => {
    logger.info(`update ${itemType} called`);
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
    const item = await util.toggleGroup(itemId);
    response.json(item);
    logger.info('Request handled.');
  });

  return application;
};

module.exports = routeGroups;
