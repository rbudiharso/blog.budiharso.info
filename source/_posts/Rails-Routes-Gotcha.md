title: Rails Routes Gotcha
date: 2014-01-28
tags:
- Ruby on Rails
- Programming
---
Sekian lama bekerja dengan [Rails](http://rubyonrails.org) saya masih menemukan beberapa hal yang cukup mengejutkan.

## Rails routes

Di file `config/routes.rb` bukan hal yang aneh kalo kita membuat route tambahan selain RESTFul routes yang di hasilkan oleh generator rails.

``` ruby
Rails::Application.routes.draw do
  resources :post do
    post :share
  end
end
```
<!-- more -->

Hasil dari routes diatas selain 7 rute REST standar rails juga akan menghasilkan satu rute tambahan dengan path seperti ini:

``` bash
post_share POST   /posts/:post_id/share(.:format)     posts#share
```

Perhatikan bahwa di rute itu menggunakan params `post_id` untuk pengenal post mana yang akan di cari dan helper yang kita dapat adalah `post_share_path`, sedangkan jika kita membuat route kita seperti dibawah ini:


``` ruby
Rails::Application.routes.draw do
  resources :post do
    member do
      post :share
    end
  end
end
```

File routes diatas akan menghasilkan rute tambahan dengan path seperti ini:

``` bash
share_post POST   /posts/:id/share(.:format)     posts#share
```

Perhatikan bahwa sekarang rute ini akan menggunakan params `id` sebagai pengenal post mana yang akan di cari di controller, dan helper yang kita dapat adalah `share_post_path`

