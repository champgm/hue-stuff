const path = require('path');
const logger = require('../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });

const getBridgeDetails = () => {
  // This is the IP on which the bridge resides
  if (!process.env.HUE_BRIDGE_IP) {
    throw new Error('Bridge IP not set.');
  } else {
    logger.info('Bridge IP set.');
  }
  logger.info({ bridgeIp: process.env.HUE_BRIDGE_IP }, 'Bridge IP configured.');

  // This is the API key that will authenticate you to the bridge.
  // More info here: https://developers.meethue.com/documentation/getting-started
  // ctrl+f for "please create a new resource"
  if (!process.env.HUE_BRIDGE_TOKEN) {
    throw new Error('Bridge token not set.');
  } else {
    logger.info('Bridge token configured.');
  }

  // This is the port on which the bridge listens
  let bridgePort;
  if (process.env.HUE_BRIDGE_PORT) {
    logger.info('Bridge port set.');
    bridgePort = process.env.HUE_BRIDGE_PORT;
  } else {
    logger.info('Bridge port not set.');
    bridgePort = 80;
  }
  logger.info({ bridgePort }, 'Bridge port configured.');

  return {
    bridgeIp: process.env.HUE_BRIDGE_IP,
    bridgeToken: process.env.HUE_BRIDGE_TOKEN,
    bridgePort
  };
};

module.exports = getBridgeDetails;
