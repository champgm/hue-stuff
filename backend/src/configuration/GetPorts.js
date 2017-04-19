const path = require('path');
const logger = require('../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });

const getPorts = () => {
  // This is the port on which the normal hue-stuff will start
  let internalPort;
  if (process.env.HUE_WEB_PORT) {
    logger.info('Internal Express port set.');
    internalPort = process.env.HUE_WEB_PORT;
  } else {
    logger.info('Internal Express port not set.');
    internalPort = 8888;
  }
  logger.info({ internalPort }, 'Internal Express port configured.');

  // This is the port on which the echo endpoints will start
  let externalPort;
  if (process.env.EXTERNAL_WEB_PORT) {
    logger.info('External Express port set.');
    externalPort = process.env.EXTERNAL_WEB_PORT;
  } else {
    logger.info('External Express port not set.');
    externalPort = 8889;
  }
  logger.info({ externalPort }, 'External Express port configured.');

  return {
    externalPort,
    internalPort,
  };
};

module.exports = getPorts;
