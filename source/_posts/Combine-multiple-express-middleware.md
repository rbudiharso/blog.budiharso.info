title: Combine multiple express middleware
date: 2015-07-28 11:26:45
tags:
- javascript
- expressjs
- middleware
---
I love [express](http://expressjs.com/)'s middleware concept, it makes splitting application functionality into small modules and composing them in the routes very easy. But when you want to combine those middlewares into one easy to use function, how do you do it?

<!-- more -->

I face this issue before, but with [connect](https://www.npmjs.com/package/connect) you can do just this.  Imagine you have this kind of middlewares in your express route:

```JS
var express = require('express');
var router = express.Router();

function middleware1(req, res, next) {
  console.log('=====================');
  console.log('midleware 1 executed');
  console.log('=====================');
  next();
}

function middleware2(req, res, next) {
  console.log('*********************');
  console.log('midleware 2 executed');
  console.log('*********************');
  next();
}

function handleRequest(req, res) {
  res.render('index', { title: 'Express' });
}

router.get(
  '/',
  middleware1,
  middleware2,
  handleRequest
);

module.exports = router;
```

when you access the `/` then on your console you will see something like
this:

{% asset_img screenshot1.png [console output] %}

you can combine those middlewares into this:

```JS
var express = require('express');
var router = express.Router();
var connect = require('connect'); // we require connect

function middleware1(req, res, next) {
  // snip...
}

function middleware2(req, res, next) {
  // snip...
}

function handleRequest(req, res) {
  // snip...
}

var combinedMiddleware = (function() {
  var chain = connect();
  [middleware1, middleware2].forEach(function(middleware) {
    chain.use(middleware);
  });
  return chain;
})();

router.get(
  '/',
  combinedMiddleware, // one easy to use middleware
  handleRequest
);

module.exports = router;
```

You can do this for any combination of middlewares, and reuse those combination on any route in your express app.
