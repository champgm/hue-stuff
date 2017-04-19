const path = require('path');
const logger = require('../../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });

const route = (itemType, controller, application) => {

  application.get(`/${itemType}`, async (request, response, next) => {
    try {
      logger.info(`get ${itemType} called`);
      const items = await controller.getAll();
      response.json(items);
      logger.info('Request handled.');
    } catch (caughtError) {
      logger.error({ keys: Object.getOwnPropertyNames(caughtError) }, 'error keys');
      const error = {
        message: caughtError.message,
        type: caughtError.type,
        stack: caughtError.stack
      };
      logger.error({ error }, 'An unhandled error was caught.');
    }
  });

  application.get(`/${itemType}/:itemId`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `get ${itemType} called`);
    const item = await controller.get(itemId);
    response.json(item);
    logger.info('Request handled.');
  });

  application.put(`/${itemType}/:itemId`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `put ${itemType} called`);
    const item = await controller.update(itemId, request.body);
    response.json(item);
    logger.info('Request handled.');
  });

  return application;
};

module.exports = route;
