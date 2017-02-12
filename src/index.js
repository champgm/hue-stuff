const Routing = require('./routing/Routing');

// This is the port on which the normal hue-stuff will start
let internalPort;
if (process.env.HUE_WEB_PORT) {
  console.log('Internal Express port set.');
  internalPort = process.env.HUE_WEB_PORT;
} else {
  console.log('Internal Express port not set.');
  internalPort = 8888;
}
console.log(`Internal Express port: ${internalPort}`);

// This is the port on which the echo endpoints will start
let externalPort;
if (process.env.EXTERNAL_WEB_PORT) {
  console.log('External Express port set.');
  externalPort = process.env.EXTERNAL_WEB_PORT;
} else {
  console.log('External Express port not set.');
  externalPort = 8889;
}
console.log(`External Express port: ${externalPort}`);

// This is the full path to the SSL certificate
let sslCertPath;
if (process.env.SSL_CERT_PATH) {
  console.log('SSL certificate path set.');
  sslCertPath = process.env.SSL_CERT_PATH;
} else {
  console.log('SSL certificate path not set.');
}
console.log(`SSL certificate path: ${sslCertPath}`);

// This is the full path to the SSL key
let sslKeyPath;
if (process.env.SSL_KEY_PATH) {
  console.log('SSL key path set.');
  sslKeyPath = process.env.SSL_KEY_PATH;
} else {
  console.log('SSL key path not set.');
}
console.log(`SSL key path: ${sslKeyPath}`);

// This is the port on which the bridge listens
let bridgePort;
if (process.env.HUE_BRIDGE_PORT) {
  console.log('Bridge port set.');
  bridgePort = process.env.HUE_BRIDGE_PORT;
} else {
  console.log('Bridge port not set.');
  bridgePort = 80;
}
console.log(`Bridge port: ${bridgePort}`);

// This is the IP on which the bridge resides
if (!process.env.HUE_BRIDGE_IP) {
  throw new Error('Bridge IP not set.');
} else {
  console.log('Bridge IP set.');
}

// This is the API key that will authenticate you to the bridge.
// More info here: https://developers.meethue.com/documentation/getting-started
// ctrl+f for "please create a new resource"
if (!process.env.HUE_BRIDGE_TOKEN) {
  throw new Error('Bridge token not set.');
} else {
  console.log('Bridge token set.');
}

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

// Just a logger to let you know if the secret endpoint is configured
if (!process.env.SECRET_EXTERNAL || !process.env.SECRET_WHITE_ID || !process.env.SECRET_RED_ID) {
  console.error('All secret endpoints not configured, will not start external server.');
  console.log(`process.env.SECRET_EXTERNAL: ${process.env.SECRET_EXTERNAL}`);
  console.log(`process.env.SECRET_WHITE_ID: ${process.env.SECRET_WHITE_ID}`);
  console.log(`process.env.SECRET_RED_ID: ${process.env.SECRET_RED_ID}`);
} else {
  console.log('Found secret endpoints.');
}
const secretEndpoints = {
  endpoint: process.env.SECRET_EXTERNAL,
  whiteSceneId: process.env.SECRET_WHITE_ID,
  redSceneId: process.env.SECRET_RED_ID
};

const expressConfiguration = {
  externalPort,
  internalPort,
  sslCertPath,
  sslKeyPath
};

const bridgeDetails = {
  bridgeIp: process.env.HUE_BRIDGE_IP,
  bridgeToken: process.env.HUE_BRIDGE_TOKEN,
  bridgePort
};


const server = new Routing(
  expressConfiguration,
  bridgeDetails,
  plugIps,
  secretEndpoints
);

server.start();

