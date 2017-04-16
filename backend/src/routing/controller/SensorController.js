const path = require('path');
const logger = require('../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });
const CommonController = require('./CommonController');

const type = 'sensors';

class SensorController extends CommonController {

  constructor(bridgeUri) {
    super(type, bridgeUri, logger);
  }

}

module.exports = SensorController;
