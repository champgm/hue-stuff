const path = require('path');
const logger = require('../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });

const getSecrets = () => {
  // Just a logger to let you know if the secret endpoint is configured
  if (!process.env.SECRET_EXTERNAL ||
    !process.env.SECRET_WHITE_ID ||
    !process.env.SECRET_RED_ID) {
    console.error('All secret endpoints not configured, will not start external server.');
    logger.info({ secretExternalEndpoint: process.env.SECRET_EXTERNAL }, 'process.env.SECRET_EXTERNAL');
    logger.info({ secretWhiteId: process.env.SECRET_WHITE_ID }, 'process.env.SECRET_WHITE_ID');
    logger.info({ secretRedId: process.env.SECRET_RED_ID }, 'process.env.SECRET_RED_ID');
  } else {
    logger.info('Found all secret endpoints.');
  }

  // This is the full path to the SSL certificate
  let sslCertPath;
  if (process.env.SSL_CERT_PATH) {
    logger.info('SSL certificate path set.');
    sslCertPath = process.env.SSL_CERT_PATH;
  } else {
    logger.info('SSL certificate path not set.');
  }
  logger.info({ sslCertPath }, 'SSL certificate path configured.');

  // This is the full path to the SSL key
  let sslKeyPath;
  if (process.env.SSL_KEY_PATH) {
    logger.info('SSL key path set.');
    sslKeyPath = process.env.SSL_KEY_PATH;
  } else {
    logger.info('SSL key path not set.');
  }
  logger.info({ sslKeyPath }, 'SSL key path configured.');

  return {
    endpoint: process.env.SECRET_EXTERNAL,
    whiteSceneId: process.env.SECRET_WHITE_ID,
    redSceneId: process.env.SECRET_RED_ID,
    sslCertPath,
    sslKeyPath
  };
};

module.exports = getSecrets;
