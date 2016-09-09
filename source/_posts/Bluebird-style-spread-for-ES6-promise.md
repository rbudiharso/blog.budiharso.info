title: Bluebird style spread for ES6 promise
date: 2016-09-09 10:46:41
tags:
- NodeJS
- Javascript
- ES6
- Promise
- Bluebird
---

I like [Bluebird](http://bluebirdjs.com) as a Promise library because of its speed and features. One of the feature that use a lot is `spread` that make it easier when handling with array-returning function in a promise chain. But ES6 promise doesn't have `spread`, so what do you do when you need `spread` while using ES6 promise?
<!-- more -->

It turn out quite easy to have a pretty similar feature with `spread` in ES6, you just need to write a little helper function like this:

``` javascript
function spread (callback) {
  return array => {
    return callback.apply(null, array)
  }
}
```

And you use it like this:

``` javascript
new Promise((resolve, reject) => {
  resolve([1, 2, 3, 4])
})
.then(spread((one, two, three, four) => {
  console.log(one) // 1
  console.log(two) // 2
  console.log(three) // 3
  console.log(four) // 4
}))
```

What other Bluebird feature that you miss when using ES6 promise?
