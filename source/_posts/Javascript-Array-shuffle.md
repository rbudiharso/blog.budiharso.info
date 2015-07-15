title: Javascript Array shuffle
date: 2014-10-08 16:31:23
tags:
- Javascript
- Programming
---
<!-- more -->
Javascript Array has loads of useful function, but still there are some function that I wish were there. One of those function is shuffling an array, rearrange it's components order in random sequence.
<!-- more -->
[Ruby](http://www.ruby-lang.org) have its [Array#shuffle](http://www.ruby-doc.org/core-2.1.3/Array.html#method-i-shuffle) and [Array#shuffle!](http://www.ruby-doc.org/core-2.1.3/Array.html#method-i-shuffle-21) to shuffle an array. Altough Javascript doesn't have a native method to do the same, we can easily create our own array shuffling method, courtesy of [CSS-Tricks](http://css-tricks.com/snippets/javascript/shuffle-array/).

{% code lang:javascript %}
[1, 2, 3, 4, 5].sort(function() { return 0.5 - Math.random() });

// or to extend its prototype
Array.prototype.shuffle = function() {
  return this.sort(function() { return 0.5 - Math.random() });
}

[1, 2, 3, 4, 5].shuffle(); // => [3, 1, 4, 2, 5]
{% endcode %}

So, there you have it, a function to shuffle an array in javascript. I find this function quite useful for one of my pet project, I hope this is usefull for you too.
