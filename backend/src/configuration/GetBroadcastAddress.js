const path = require('path');
const logger = require('../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });

const getBroadcastAddress = () => {
  // Just a logger to let you know if TP Link plugs are configured
  if (!process.env.BROADCAST_ADDRESS) {
    logger.error('No broadcast address configured.');
  } else {
    logger.info({ address: process.env.BROADCAST_ADDRESS }, 'Found broadcast address.');
  }

  return process.env.BROADCAST_ADDRESS;
};

module.exports = getBroadcastAddress;
