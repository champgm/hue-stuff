const path = require('path');
const logger = require('../../logger/logger.js')
  .child({ fileName: `${path.basename(__filename)}` });

const errorHandler = (caughtError, request, response, next) => {
  const error = {};
  Object.keys(caughtError).forEach((key) => {
    error[key] = caughtError[key];
  });
  logger.info({ error }, 'An unhandled error was caught.');

  response.status(500).send('Something broke!');
};

module.exports = errorHandler;
