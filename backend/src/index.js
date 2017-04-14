const Controller = require('./routing/Controller');
const getSecrets = require('./configuration/GetSecrets');
const getPorts = require('./configuration/GetPorts');
const getBridgeDetails = require('./configuration/GetBridgeDetails');
const getPlugIps = require('./configuration/GetPlugIps');

const secretConfiguration = getSecrets();
const expressConfiguration = getPorts();
const bridgeDetails = getBridgeDetails();
const plugIps = getPlugIps();

const server = new Controller(
  expressConfiguration,
  bridgeDetails,
  plugIps,
  secretConfiguration
);

server.start();

