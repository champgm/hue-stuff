const ApplicationRouter = require('./routing/ApplicationRouter');
const getSecrets = require('./configuration/GetSecrets');
const getPorts = require('./configuration/GetPorts');
const getBridgeDetails = require('./configuration/GetBridgeDetails');
const getBroadcastAddress = require('./configuration/GetBroadcastAddress');

const secretConfiguration = getSecrets();
const expressConfiguration = getPorts();
const bridgeDetails = getBridgeDetails();
const plugIps = getBroadcastAddress();

const server = new ApplicationRouter(
  expressConfiguration,
  bridgeDetails,
  plugIps,
  secretConfiguration
);

server.start();

