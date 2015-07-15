title: "Rails, Paperclip, and Fakes3"
date: 2014-10-29 22:19:08
tags:
- Ruby on Rails
- Programming
---
<!-- more -->
When developing a [rails](http://rubyonrails.org) application using [paperclip](https://github.com/thoughtbot/paperclip) with [S3](http://aws.amazon.com/s3/) for storage, you don't want to _literally_ uploading files to S3. Your app development will be slow and you have to be online to do it, that's cumbersome. This is where [fakes3](https://github.com/jubos/fake-s3) gem can help, and here's how you set it up.
<!-- more -->
Basically fakes3 create a local server to act as if it's S3 server, and you set your rails app (or paperclip to be exact) to use it in development environment. That way all your uploading will be faster and you don't have to be online to do it.

To set up fakes3 and paperclip I use these steps:

Add an entry in your hosts file, in linux and Mac OS X this is `/etc/hosts`. You may have to use sudo to edit that file. Add this line below in that file, subtitute `bucket` with your S3 bucket name:
``` shell /etc/hosts
127.0.0.1 bucket-name.localhost
```

After that you need to install fakes3 gem, but because paperclip use aws-sdk gem to deal with S3, and aws-sdk will use https to connect to S3, you need to use my fork of fakes3 to make it work, because the fakes3 gem from rubygems doesn't support https.
``` shell
git clone https://github.com/rbudiharso/fake-s3.git
cd fakes3
gem build fakes3.gemspec
gem install -l fakes3-0.1.5.3.gem
```

Then after that to start the fakes3 server, run the following command
``` shell
cd path/to/rails/app
fakes3 server -h localhost -r public/system -p 10001
```

And then you need to setup paperclip to use the fakes3 server when your app is running in development environment.
``` ruby models/something_with_paperclip.rb
has_attached_file :image, {
  styles: {
    large: '1024>',
    thumb: '128x128'
  }
}.merge(PAPERCLIP_STORAGE)
```

``` ruby config/environments/development.rb
PAPERCLIP_STORAGE = {
  storage: :s3,
  s3_credentials: "#{Rails.root}/config/aws.yml",
  url: ":s3_domain_url",
  path: ":class/:attachment/:id_partition/:style/:filename.:extension",
  s3_protocol: 'https',
  s3_hostname: 'localhost:10001',
  s3_options: { server: 'localhost', port: 10001 }
}
```

``` yaml config/aws.yml
development:
  bucket: 'bucket-name'
  access_key_id: YOUR_AWS_ACCESS_KEY_ID
  secret_access_key: YOUR_AWS_SECRET_KEY
staging:
  # snip
production:
  # snip
```

Lastly, start your rails server and you should be able to upload and retrieve file from fakes3 as if you uploading to AWS S3.
