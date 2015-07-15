title: Keyword 'new' di JavaScript
date: 2014-05-03
tags:
- Javascript
- Programming
---
<!-- more -->
Jika kita membuat suatu `function` di JavaScript, kita bisa memanggil `function` tersebut dengan atau tanpa keyword `new`, apa bedanya?

<!-- more -->

## Keyword `new`

Berdasarkan [dokumentasi dari MDN](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/new), jika kita menggunakan `new` pada saat kita memanggil suatu `function`, maka kita akan mendapatkan sebuah turunan suatu object.

``` javascript
var Foo = function () {};
var fooNew = new Foo();
var fooNoNew = Foo();
console.log(fooNew instanceof Foo);   // true
console.log(fooNew);                  //=> {}
console.log(fooNoNew instanceof Foo); // false
console.log(fooNoNew);                //=> undefined
```

Dari contoh diatas kita lihat bahwa kita akan mendapatkan suatu object turunan dari Foo jika kita menggunakan `new` walaupun `function` yang kita bikin tidak mengembalikan apa - apa (tidak menggunakan keyword `return`). Bagaimana kalau `function` itu kita buat agar mengembalikan sesuatu?

``` javascript
var Foo = function () {
  var obj = {};
  obj.bar = function () {
    return "bar didalam Foo";
  };
  return obj;
};
var fooNew = new Foo();
var fooNoNew = Foo();
console.log(fooNew instanceof Foo);    // false
console.log(fooNew);                   //=> {}
console.log(fooNew.bar());             // "bar didalam Foo"
console.log(fooNoNew instanceof Foo);  // false
console.log(fooNoNew);                 //=> {}
console.log(fooNoNew.bar());           // "bar didalam Foo"
```

Disini kita lihat perbedaan dengan fungsi sebelumnya, jika kita dengan sengaja mengembalikan suatu object, maka walaupun kita menggunakan `new` object hasil dari `function` tersebut bukan turunan dari Foo.

## Dengan atau tanpa `new`

Berdasarkan contoh diatas apakah kita bisa membuat suatu `function` yang akan mengembalikan suatu object yang mirip ketika `function` itu dipanggil dengan atau tanpa keyword `new`? kita coba saja.

``` javascript
var Foo = function () {
  var obj = {};
  obj.bar = function () {
    return "bar didalam Foo";
  };
  obj.__proto__ = Object.create(Foo.prototype);
  return obj;
};

var fooNew = new Foo();
var fooNoNew = Foo();
console.log(fooNew instanceof Foo);    // true
console.log(fooNew);                   // object
console.log(fooNew.bar());             // "bar didalam Foo"
console.log(fooNoNew instanceof Foo);  // true
console.log(fooNoNew);                 // object
console.log(fooNoNew.bar());           // "bar didalam Foo"
```

Ternyata bisa, bahkan inilah yang terjadi saat kita menggunakan `new` saat memanggil suatu fungsi. Secara singkat yang terjadi adalah:

1. Buat suatu object **obj**.
2. Set prototype dari object **obj** dengan prototype dari Foo.
3. Jalankan fungsi constructor dari Foo dengan argumen yang di
   berikan dan variabel *this* diset dengan object **obj** yang barusan dibuat.
4. Kembalikan object hasil dari fungsi `constructor`, jika fungsi `contructor` tidak mengembalikan suatu object maka object **obj** yang sebelunya dhasilkan yang akan dikembalikan.

Moral dari cerita diatas? selalu gunakan keyword `new` jika kita mengharapkan suatu instance object, karena menggunakan `new` lebih cepat dan program kita akan lebih mudah dibaca dan dimengerti :)
