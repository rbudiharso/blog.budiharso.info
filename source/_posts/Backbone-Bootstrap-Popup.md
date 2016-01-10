title: Backbone Bootstrap Popup
date: 2014-01-18
tags:
- Javascript
- Backbone.js
- Programming
---
_Lihat demonya [disini](http://demo.budiharso.info/backbone-bootstrap-popup/) atau download kode sumbernya di [Github](https://github.com/rbudiharso/backbone-bootstrap-popup)_

Disuatu proyek yang saya kerjakan, kami menggunakan Bootstrap untuk CSS framework dan Backbone. Dan ada kebutuhan untuk membuat popup window untuk form atau sekedar dialog biasa.
<!-- more -->
Karena kami sudah menggunakan Backbone untuk proyek itu maka saya pikir sekalian popup ini pun akan di handle oleh Backbone, tapi saya mau popup ini bisa digunakan untuk segala hal yang membutuhkan popup, jadi tidak terbatas hanya untuk beberapa hal spesifik.

## Popup sebagai kontainer
Saya ingin popup ini hanya berfungsi sebagai kontainer dan hanya mengurusi hal yang berhubungan dengan popup saja seperti untuk menampilkan dan menyembunyikan jendela popup, sedangkan isi dari popup itu sendiri merupakan Backbone view lain lagi. Dengan begitu popup ini bisa digunakan oleh Backbone view yang lain yang ingin ditampilkan secara popup.

Untuk templating kita akan menggunakan [EJS](http://embeddedjs.com/)

``` javascript main.js
window.App = App = {
  Views = {};
}
```

``` javascript PopupContainerView.js
(function () {
    'use strict';

    App.Views.PopupContainerView = Backbone.View.extend({
        template: JST['app/scripts/templates/PopupContainerView.ejs'],
        className: 'modal fade',
        defaults: {
            title: 'Popup'
        },
        render: function() {
            this.$el.html(this.template({popup: this.defaults}));
            this.$el.modal({show: false});
            $('body').append(this.el);
            this.appendMainView()
            this.$el.modal('show');
            this.on('hidden.bs.modal', function() {
                self.remove();
            });
            return this;
        },
        renderMainView: function() {
            return {el: 'Override this to render main view'};
        },
        appendMainView: function() {
            this.$('#main-view').html(this.renderMainView().el);
        }
    });

})();
```



``` html PopupContainerView.ejs
<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button class="close" type="button" data-dismiss="modal">&times;</button>
            <h4 class="modal-title"><%= popup.title %></h4>
        </div>
        <div class="modal-body">
            <div id="main-view"></div>
        </div>
    </div>
</div>
```

Disini saya menggunakan [Yeoman](http://yeoman.io/) untuk memulai, dan file `index.html` yang dihasilkan adalah seperti ini:

``` html index.html
<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>blog</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">
        <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
        <!-- build:css(.tmp) styles/main.css -->
        <link rel="stylesheet" href="styles/main.css">
        <!-- endbuild -->
        <!-- build:js scripts/vendor/modernizr.js -->
        <script src="bower_components/modernizr/modernizr.js"></script>
        <!-- endbuild -->
    </head>
    <body>
        <!--[if lt IE 10]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->


        <div class="container">
            <h1>Backbone Bootstrap Popup</h1>
            <hr>

            <div id="target">
            </div>

        </div>


        <!-- build:js scripts/vendor.js -->
        <script src="bower_components/jquery/jquery.js"></script>
        <script src="bower_components/underscore/underscore.js"></script>
        <script src="bower_components/backbone/backbone.js"></script>
        <!-- endbuild -->

        <!-- build:js scripts/plugins.js -->
        <script src="bower_components/sass-bootstrap/js/affix.js"></script>
        <script src="bower_components/sass-bootstrap/js/alert.js"></script>
        <script src="bower_components/sass-bootstrap/js/dropdown.js"></script>
        <script src="bower_components/sass-bootstrap/js/tooltip.js"></script>
        <script src="bower_components/sass-bootstrap/js/modal.js"></script>
        <script src="bower_components/sass-bootstrap/js/transition.js"></script>
        <script src="bower_components/sass-bootstrap/js/button.js"></script>
        <script src="bower_components/sass-bootstrap/js/popover.js"></script>
        <script src="bower_components/sass-bootstrap/js/carousel.js"></script>
        <script src="bower_components/sass-bootstrap/js/scrollspy.js"></script>
        <script src="bower_components/sass-bootstrap/js/collapse.js"></script>
        <script src="bower_components/sass-bootstrap/js/tab.js"></script>
        <!-- endbuild -->

        <!-- build:js({.tmp,app}) scripts/main.js -->
        <script src="scripts/main.js"></script>
        <script src="scripts/templates.js"></script>
        <script src="scripts/views/PopupContainerView.js"></script>
        <script src="scripts/views/ButtonView.js"></script>
        <script src="scripts/views/SimplePopupView.js"></script>
        <script src="scripts/views/simpleView.js"></script>
        <script src="scripts/app.js"></script>
        <!-- endbuild -->
    </body>
</html>
```

Sampai disini `PopupContainer` kita sudah siap dan bisa digunakan sebagai kelas dasar untuk Backbone `view` yang lain.

## Backbone view sederhana
Sebagai contoh kita akan buat satu Backbone `view` sederhana, hanya berupa text, yang nantinya kita akan ubah dan tampilkan sebagai sebuah popup.

``` javascript simple-view
(function () {
    'use strict';

    App.Views.SimpleView = Backbone.View.extend({

        template: JST['app/scripts/templates/simpleView.ejs'],
        render: function() {
            this.$el.html(this.template());
            return this;
        }

    });

})();
```

dan _template_ file untuk Backbone view diatas seperti ini:

``` html simple-template
<h1>Simple View</h1>
<p>
    Kita bisa memakai Backbone view lain disini
</p>
```

ok, sederhana kan? dan kalau digunakan sebagai Backbone view biasa akan tampil seperti ini:

{% asset_img simple-view.png %}

## Backbone view dalam bentuk popup
Sekarang kita akan mengubah Backbone view sederhana itu kedalam bentuk popup.  Yang kita perlu lakukan hanya membuat sebuah Backbone view baru yang mengambil kelas dasar `PopupContainerView` yang sudah kita buat dan meng-override beberapa fungsinya.

``` javascript SimplePopupView.js
(function () {
    'use strict';

    Blog.Views.SimplePopupView = Blog.Views.PopupContainerView.extend({
        defaults: {
            title: 'Simple Popup'
        },
        renderMainView: function() {
            var view = new Blog.Views.SimpleView();
            return view.render();
        }
    });

})();
```

Kita cuma perlu meng-override fungsi `renderMainView` yang akan menentukan apa yang akan ditampilkan dalam popup tersebut.

## Popup beraksi
Layaknya sebuah popup, kita tidak akan melihat popup itu sampai kita melakukan sesuatu yang akan menjalankan popup tersebut. Untuk itu kita akan buat Backbone view lainnya yang akan menjadi pemicu untuk menampilkan popup tersebut



``` javascript ButtonView.js
(function () {
    'use strict';

    Blog.Views.ButtonView = Backbone.View.extend({

        template: JST['app/scripts/templates/ButtonView.ejs'],
        events: {
            'click button': 'showPopup'
        },
        render: function() {
            this.$el.html(this.template());
            return this;
        },
        showPopup: function() {
            var view = new Blog.Views.SimplePopupView({
                message: 'Simple Popup'
            });
            view.render();
        }

    });

})();
```

dan template untuk view tersebut adalah seperti ini:

``` html
<button>Click Me</button>
```

super sederhana kan? tombol itu yang nantinya akan menampilkan popup ketika di kilik. Sekarang bagaimana menggunakan Backbone view ini?

``` javascript app.js
$(document).ready(function () {
    'use strict';

    var view = new Blog.Views.ButtonView({
        el: '#target'
    });
    view.render();
});
```

dan jika kita lihat di browser maka akan terlihat seperti ini

{% asset_img button-view.png %}

dan jika kita klik tombol itu

{% asset_img popup-action.gif %}
