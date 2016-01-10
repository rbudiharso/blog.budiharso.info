title: Backbone.Model tidak memicu event 'change'
date: 2014-05-02
description: Backbone.Model tidak memicu event 'change' ketika nilai yang di set adalah Array dan cara mengatasinya
tags:
- Javascript
- Backbone.js
- Programming
---
Saya mendapati masalah ini ketika saya bermaksud mengganti nilai attribute dari sebuah backbone model tapi event `change` tidak terpicu.  Setelah mencari - cari ternyata solusinya sangat sederhana.
<!-- more -->
## Event 'change'
Dokumentasi Backbone.Model menyebutkan bahwa jika kita mengubah nilai sebuah attribut model dengan `Model#set` maka model tersebut akan memicu event `change`. Bagian lain dari aplikasi atau bahkan model itu sendiri bisa mendengarkan event `change` tersebut dan memanggil `callback` yang sudah di tentukan.

Tapi event `change` itu tidak akan terpicu jika nilai attribut yang kita ganti tersebut berupa `Array` dan kita menggunakan `Array#push`. Sebagai contoh kode dibawah ini tidak akan menjalankan fungsi `alert` yang sudah ditentukan.

``` javascript
var Modelku = Backbone.Model.extend({
  defaults: {
    todos: []
  },
  initialize: function () {
    this.listenTo(this.model, 'change', this.alert);
  },
  alert: function () {
    window.alert('Model change');
  }
});

var modelku = new Modelku();
var arr = modelku.get('todos');
arr.push('ngeblog');             // Array#push
modelku.set('todos', arr);       // tidak memicu event 'change'
```

atau bisa dicoba [disini](http://jsbin.com/loyuj/1/)

## Atasi dengan _.clone
Setelah membolak - balik halaman google, [artikel ini](http://stackoverflow.com/questions/9909799/backbone-js-change-not-firing-on-model-change) menerangkan pada dasarnya salah satu cara Backbone.Model mendeteksi perubahan adalah dengan melihat perubahan reference terhadap attributnya.

Jadi ketika kita menggunakan `Array#push` reference terhadap attribute itu tidak berubah walaupun nilainya berubah. Karena referencenya tidak berubah Backbone.Model tidak melihat perubahan itu sehingga event `change` tidak dipicu.

Salah satu cara untuk mengatasinya adalah dengan mengganti referencenya secara langsung atau menggunakan fungsi yang akan menghasilkan reference baru. Kita bisa menggunakan fungsi `Array#slice` atau `_.clone`. Contoh dibawah ini akan memicu event `change`.

``` javascript
var Modelku = Backbone.Model.extend({
  defaults: {
    todos: []
  },
  initialize: function () {
    this.listenTo(this.model, 'change', this.alert);
  },
  alert: function () {
    window.alert('Model change');
  }
});

var modelku = new Modelku();
var arr = _.clone(modelku.get('todos')); // kita menggunakan _.clone
arr.push('ngeblog');                     // Array#push
modelku.set('todos', arr);               // memicu event 'change'
```

Semoga artikel ini bisa membantu :)

