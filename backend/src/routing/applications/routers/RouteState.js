const path = require('path');
const logger = require('../../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });

const route = (itemType, controller, application) => {

  application.get(`/${itemType}/:itemId/state`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `get ${itemType} state called`);
    const state = await controller.getState(itemId);
    response.json(state);
    logger.info('Request handled.');
  });

  application.put(`/${itemType}/:itemId/state`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `put ${itemType} called`);
    const state = await controller.setState(itemId, request.body);
    response.json(state);
    logger.info('Request handled.');
  });

  return application;
};

module.exports = route;
