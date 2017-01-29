const Routing = require('./routing/Routing');

// This is the port on which express will start
let expressPort;
if (process.env.HUE_WEB_PORT) {
  console.log('Express port set.');
  expressPort = process.env.HUE_WEB_PORT;
} else {
  console.log('Express port not set.');
  expressPort = 8888;
}
console.log(`Express port: ${expressPort}`);

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

const server = new Routing(
  expressPort,
  process.env.HUE_BRIDGE_IP,
  process.env.HUE_BRIDGE_TOKEN,
  bridgePort
);

server.start();

