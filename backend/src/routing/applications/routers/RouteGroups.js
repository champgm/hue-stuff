const path = require('path');
const logger = require('../../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });
const routeCommon = require('./RouteCommon');
const routeSelect = require('./RouteSelect');
const routeState = require('./RouteState');

const itemType = 'groups';

const route = (controller, application) => {
  routeCommon(itemType, controller, application);
  routeSelect(itemType, controller, application);
  routeState(itemType, controller, application);

  return application;
};

module.exports = route;
