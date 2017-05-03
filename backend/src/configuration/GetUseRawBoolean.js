const path = require('path');
const logger = require('../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });

const getUseRawBoolean = () => {
  if (!process.env.HUE_WEB_USE_RAW) {
    logger.error({ useRaw: process.env.HUE_WEB_USE_RAW }, 'No use raw boolean configured or it is false.');
  } else {
    logger.info({ useRaw: process.env.HUE_WEB_USE_RAW }, 'Found use raw boolean.');
  }

  return process.env.HUE_WEB_USE_RAW;
};

module.exports = getUseRawBoolean;
