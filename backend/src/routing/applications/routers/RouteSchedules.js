const path = require('path');
const logger = require('../../../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });
const makeRequest = require('request-promise');

const itemType = 'schedules';

const routeGroups = (util, application) => {
  application.get(`/${itemType}`, async (request, response, next) => {
    logger.info('getschedules called');
    const options = util.simpleGet('schedules');

    logger.info({ options }, 'Will GET with options.');
    const result = await makeRequest(options);
    response.send(result);
    logger.info('Request handled.');
  });

  application.get(`/${itemType}/:itemId`, async (request, response, next) => {

  });

  application.put(`/${itemType}/:itemId`, async (request, response, next) => {
    response.status(400);
    response.json({ error: "This endpoint has not yet been implemented." });
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
