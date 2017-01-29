const Server = require('./server/Server');

const server = new Server(
  process.env.HUE_WEB_PORT,
  process.env.BRIDGE_IP,
  process.env.BRIDGE_TOKEN,
  process.env.BRIDGE_PORT
);

server.start();

