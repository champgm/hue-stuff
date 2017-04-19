const path = require('path');
const logger = require('../../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });
const routeCommon = require('./RouteCommon');

const itemType = 'rules';

const route = (controller, application) => {
  routeCommon(itemType, controller, application);

  return application;
};

module.exports = route;
