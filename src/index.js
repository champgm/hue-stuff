const Server = require('./server/Server');

let hueWebPort;
if (process.env.HUE_WEB_PORT) {
  console.log('hue-web port set.');
  hueWebPort = process.env.HUE_WEB_PORT;
} else {
  console.log('hue-web port not set.');
  hueWebPort = 8888;
}
console.log(`hue-web port: ${hueWebPort}`);

let bridgePort;
if (process.env.HUE_BRIDGE_PORT) {
  console.log('Bridge port set.');
  bridgePort = process.env.HUE_BRIDGE_PORT;
} else {
  console.log('Bridge port not set.');
  bridgePort = 80;
}
console.log(`Bridge port: ${bridgePort}`);

if (!process.env.HUE_BRIDGE_IP) {
  throw new Error('Bridge IP not set.');
} else {
  console.log('Bridge IP set.');
}

if (!process.env.HUE_BRIDGE_TOKEN) {
  throw new Error('Bridge token not set.');
} else {
  console.log('Bridge token set.');
}

const server = new Server(
  hueWebPort,
  process.env.HUE_BRIDGE_IP,
  process.env.HUE_BRIDGE_TOKEN,
  bridgePort
);

server.start();

