title: Hash to deep object with ruby
date: 2012-05-22
tags:
- Ruby
- Programming
---
_This is a migration from my old blog post which sadly I have to take
down._
<!-- more -->
I find that I need this quite often, say if I have a hash like this:

``` ruby
hash = {
  top: 'top level',
  level1: {
    level2: 'level 2'
  }
}
```
<!-- more -->
and I want to access the hash like this:

``` ruby
obj.level1.level2 # => 'level 2'
```

so I use this class (taken form [here](http://pullmonkey.com/2008/01/06/convert-a-ruby-hash-into-a-class-object) to get what I want.

``` ruby
require 'ostruct'

class DeepObject < OpenStruct

  def initialize(hash)
    @original = hash
    hash.each do |k,v|
      v = DeepObject.new(v) if v.is_a?(::Hash)
      hash[k] = v
    end
    super
  end

  def to_hash
    @original
  end
end
```

and I use it like this:

``` ruby
deepobject = DeepObject.new(hash)
deepobject.top # => 'top level'
deepobject.level1.level2 # => 'level 2'
deepobject.level1.to_hash # => { :level2 => 'level 2' } }
```
