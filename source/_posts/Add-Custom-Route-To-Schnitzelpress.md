title: Add custom route to Schnitzelpress
date: 2012-05-20
tags:
- Ruby
- Programming
---
<!-- more -->
_This is a migrated post from my old blog that sadly I have to take down_

I like [Schnitzelpress](http://schnitzelpress.org/), but just like any other application I use, I'm not happy with just the defaults that come with it. I wan't to tweak it to my liking.
In case of Schnitzelpress, I wan't to be able to add custom routes without having to fork and modify Schnitzelpress itself, so I set up for a little monkey patching. What I thought as a simple hack turns out to be a bit tricky.

<!-- more -->

## Problem?
The problem is, Schnitzelpress has a catch all route defined at the very bottom of its routes stack, and because [sinatra](http://sinatrarb.com) (Schnitzelpress is based on sinatra) is matching its routes in order they are defined, when I add additional route either through module or extension, the added path will be added after the catch all route, thus whenever the custom routes is accessed through web browser, you'll get a 404.

{% asset_img schnitzelpress-404.png "oh no, 404" %}

So after a series of trial and error, I manage to modify the routes stack so that my custom routes gets added before the catch all route, and it turns out to be quite simple.

First you need to modify the `config.ru` file, mine goes like this:

``` ruby
# encoding: UTF-8
require 'rubygems'
require 'bundler'
Bundler.require
require File.expand_path('../custom/actions', __FILE__)
$stdout.sync = true

module MyExtension
  extend Sinatra::Extension

  registered do
    @routes['GET'].pop
    include Custom::Actions
    get '/*/?' do
      slug = params[:splat].first
      @post = ::Schnitzelpress::Post.where(:slugs => slug).first
      render_post
    end
  end
end

Schnitzelpress::App.register MyExtension
run Schnitzelpress.omnomnom!
```

and then I create a module in `custom/actions.rb`

``` ruby
# custom/actions.rb
require 'sinatra/extension'

module Custom
  module Actions
    extend ActiveSupport::Concern

    included do
      get '/about' do
        haml :about
      end
    end
  end
end
```

after that you just need to create the `views/about.haml` file

```ruby
%section.posts
  %article.article.post.published
    %header
      %h1
        %a.instapaper_title{ :href => '/about' } About
    .instapaper_body
      %p
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis pulvinar est non sapien condimentum vel ultrices turpis blandit.  Curabitur augue metus, gravida in pretium non, hendrerit vel lacus.  Phasellus et turpis enim, ac consequat turpis. Mauris rhoncus fermentum magna, sed vestibulum mi porta a. Nulla condimentum diam feugiat neque ornare tristique. Vivamus molestie urna sit amet orci mattis imperdiet rhoncus neque porttitor. Nulla volutpat tempor leo a dignissim. Vivamus scelerisque odio nec orci eleifend ullamcorper. Curabitur malesuada bibendum nisl.
```

and behold...

{% asset_img schnitzelpress-about.png "how about that?" %}

## Huh? how's that works?
Sinatra keeps all its compiled routes in an instance variable called `@routes` which is a hash with the HTTP verbs (GET, POST, etc) as keys and the corresponding routes as values, so whenever you define a route like this:

``` ruby
get '/someroute' do
  "Hi, I'm a route"
end
```

sinatra then compiles it and append the compiled result to `@routes`. And because Schnitzelpress already define a bunch of routes, and the very last route it defines is a catch-all route with a splat operator like this:

``` ruby
# lib/schnitzelpress/actions/blog.rb
get '/*/?' do
  slug = params[:splat].first
  @post = Post.where(:slugs => slug).first
  render_post
end
```

Now if at this point you define additional route, it will be added after the catch-all route, and as the result your new route will be catched by… the catch-all route because the catch-all route will catch… all, and when the catch-all route doesn't know how to handle a particular request, it will simply returns a 404.

Because the catch-all is a GET, and it is added as the last element in @routes, we simply remove it

``` ruby
@routes['GET'].pop
```

After the catch-all route is gone, we can happily add our custom routes

``` ruby
include Custom::Actions
```

and after that we need to put the catch-all route back into its proper place, the end of `@routes`. This is copied directly from Schnitzelpress source code, slightly modified

``` ruby
get '/*/?' do
  slug = params[:splat].first
  @post = ::Schnitzelpress::Post.where(:slugs => slug).first
  render_post
end
```

And with that code in place, our monkey patch is complete :D

Well, that's all for todays tips and tricks for Schnitzelpress, hope you enjoy it, and stay tune for more :D
