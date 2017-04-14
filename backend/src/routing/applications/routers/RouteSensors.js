const path = require('path');
const logger = require('../../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });
const routeCommon = require('./RouteCommon');
const makeRequest = require('request-promise');

const itemType = 'sensors';

const route = (util, application) => {
  application.get(`/${itemType}`, async (request, response, next) => {
    logger.info(`get ${itemType} called`);
    const options = util.simpleGet('sensors');

    logger.info({ options }, 'Will GET with options.');
    const items = await makeRequest(options);
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

  return application;
};

module.exports = route;
