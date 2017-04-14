const path = require('path');
const logger = require('../logger/logger.js').child({ fileName: `${path.basename(__filename)}` });

const getPlugIps = () => {
  // Just a logger to let you know if TP Link plugs are configured
  if (!process.env.TP_LINK_PLUGS) {
    console.error('No TP Link plugs configured, will not display them.');
  } else {
    console.log('Found TP Link plugs.');
  }

  // Now, actually parse the plug IPs out
  let plugIps = [];
  try {
    plugIps = JSON.parse(process.env.TP_LINK_PLUGS).plugIps;
  } catch (error) {
    console.error(`Unparseable Plug IPs: ${process.env.TP_LINK_PLUGS}`);
  }

  return plugIps;
};

module.exports = getPlugIps;
