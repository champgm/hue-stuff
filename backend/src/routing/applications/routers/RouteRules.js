const path = require('path');
const logger = require('../../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });


const itemType = 'sensors';

const route = (util, application) => {
  application.get(`/${itemType}`, async (request, response, next) => {
    logger.info(`get ${itemType} called`);
    const items = await util.getAll();
    response.json(items);
    logger.info('Request handled.');
  });

  application.get(`/${itemType}/:itemId`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `get ${itemType} called`);
    const item = await util.get(itemId);
    response.json(item);
    logger.info('Request handled.');
  });

  application.put(`/${itemType}/:itemId`, async (request, response, next) => {
    logger.info(`put ${itemType} called`);
    const itemId = request.itemId;
    const updateResponse = await util.update(itemId, request.body);
    response.send(updateResponse);
    logger.info('Request handled.');
  });

  return application;
};

module.exports = route;
