const path = require('path');
const logger = require('../../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });

const route = (itemType, controller, application) => {

  application.get(`/${itemType}/:itemId/select`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `select ${itemType} called`);
    const item = await controller.select(itemId);
    response.json(item);
    logger.info('Request handled.');
  });

  return application;
};

module.exports = route;
