const path = require('path');
const logger = require('../../../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });
const KnownParameterNames = require('../../util/KnownParameterNames');

const itemType = 'scenes';

const route = (util, application) => {
  application.get(`/${itemType}`, async (request, response, next) => {
    logger.info(`get ${itemType} called`);
    const v2ScenesRequested =
      (KnownParameterNames.getV2() in request.query) &&
      (request.query[KnownParameterNames.getV2()] === 'true');

    const items = await util.getAllScenes(v2ScenesRequested);
    response.send(items);
    logger.info('Request handled.');
  });

  application.get(`/${itemType}/:itemId`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `get ${itemType} called`);
    const item = await util.getScene(itemId);
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

  application.get(`/${itemType}/:itemId/select`, async (request, response, next) => {
    const itemId = request.itemId;
    logger.info({ itemId }, `select ${itemType} called`);
    const result = await util.activateScene(itemId);
    response.json(result);
    logger.info('Request handled.');
  });

  return application;
};

module.exports = route;
