const path = require('path');
const logger = require('../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });
const CommonController = require('./CommonController');

const type = 'rules';

class RuleUtil extends CommonController {

  constructor(bridgeUri) {
    super(type, bridgeUri, logger);
  }

}

module.exports = RuleUtil;
