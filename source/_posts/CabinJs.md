title: Ngeblog dengan Cabin.js
date: 2014-02-01
tags:
- Javascript
- Programming
---
<!-- more -->
[Cabin.js](//www.cabinjs.com) adalah sebuah generator situs statik yang dibuat dengan JavaScript dan dijalankan diatas [node.js](//nodejs.org).
<!-- more -->
## Instalasi
Untuk menginstall cabinjs pastikan bahwa nodejs sudah terinstall, lalu kita bisa menginstall cabinjs dengan menjalankan perintah berikut di terminal.

``` bash
npm install -g cabin grunt-cli
```

Setelah terinstall kita akan memiliki perintah `cabin` yang bisa dijalankan di terminal.

## Mulai membuat blog
Kita bisa mulai membuat blog kita dengan menjalankan perintah `cabin new` di terminal

``` bash
cabin new blogku
```

Parameter pertama setelah `new` adalan nama folder tujuan tempat semua file yang dihasilkan akan berada, jika kita ingin menggunakan tema blog yang berbeda kita bisa berikan nama repositori tema tersebut di github pada parameter kedua setelah nama folder tujuan.

``` bash
cabin new blogku CabinJS/Candy
```

Perintah tersebut akan membuat blog kita dengan menggunakan tema Candy, untuk tema lainnya silahkan cek di situs cabinjs.

Setelah perintah tadi selesai maka kita bisa menjalankan blog kita dengan perintah seperti berikut:

``` bash
cd blogku && grunt
```

Perintah diatas akan menjalankan _grunt_ yang akan mulai memproses blog kita dan menjalankan sebuah web server sederhana sehingga kita bisa melihat seperti apa blog kita itu nantinya. Perintah itu juga akan membuka perambah web yang akan membuka halaman dengan alamat `localhost:5455`, coba lihat - lihat seperti apa blog kita itu.

## Membuat artikel blog
Berbeda dengan _blog engine_ lain seperti [Wordpress](http://wordpress.org) atau [Drupal](http://drupal.com), di cabinjs kita membuat artikel blog dengan membuat atau mengedit file [Markdown](http://daringfireball.net/projects/markdown/) dalam folder _posts_.

Coba buat satu file di dalam folder _posts_ dengan nama `postPertama.md`, lalu didalamnya isikan baris - baris berikut:

``` text
{
  title: 'Post pertama',
  date: '2014-02-03',
  description: 'Post pertama dengan cabinjs'
}
Selamat datang...
## Apa kabar?
Senang bisa membuat blog dengan Cabinjs
```

Setelah kita save file itu, _grunt_ akan otomatis membangun ulang blog kita dan me-_refresh_ perambah web sehingga kita bisa melihat artikel yang baru kita buat itu.

## Membangun blog kita
Sekarang setelah kita bisa membuat dan mengedit artikel untuk blog kita, yang perlu kita lakukan adalah memproses blog kita supaya dapat kita deploy ke web server yang bisa diakses secara publik. Untuk memproses blog dengan cabinjs kita bisa menjalankan perintah `grunt build` di dalam folder blog kita pada terminal

``` bash
# didalam folder blog
grunt build
```

Perintah tersebut akan menggabungkan semua _source_ dari blog kita dan membuat versi statiknya dan menempatkannya di dalam folder _dist_. Isi dari folder _dist_ inilah yang perlu kita unggah ke web server pubik atau hosting yang kita pakai. Karena file - file didalam folder _dist_ ini merupakan file HTML statik kita tidak perlu menjalankan program apapun di web server atau hosting kita, hanya perlu web server saja seperti Apache atau Nginx, dan biasanya kalau kita memanfaatkan jasa hosting hal - hal seperti itu sudah tersedia, jadi kita hanya perlu mengunggah file - file kita saja.

Untuk mengunggah blog kita kita bisa gunakan bermacam cara, bisa dengan FTP, atau rsync, atau kalau kita hosting biasanya kita juga mendapatkan _control panel_ yang biasanya menyediakan alat untuk membantu kita mengunggah file.

## Mengunggah dengan grunt-ftpush
Jika kita mengunggah file blog kita dengan metode FTP, maka kita bisa memanfaatkan _tool_ lain yang bisa digunakan dengan _grunt_ yaitu [grunt-ftpush](https://github.com/inossidabile/grunt-ftpush). Dengan bantuan _tool_ ini kita hanya perlu menjalankan perintah `grunt deploy` dan otomatis blog kita akan diproses dan di unggah, menyingkat banyak waktu dan mengurangi kesalahan.

Untuk menggunakan _grunt-ftpush_, langkah - langkah yang harus kita lakukan adalah:

Buat file _.ftppass_ (jangan lupa titik didepan nama file) di folder yang sama dengan file _Gruntfile_. Isi dari file _.ftppass_ ini adalah informasi untuk akun ftp kita

``` json
{
  "key1": {
    "username": "username1",
    "password": "password1"
  }
}
```

Lalu edit file _Gruntfile_, tambahkan baris - baris berikut ke dalam fungsi `grunt.initConfig`

``` javascript
ftpush: {
  build: {
    auth: {
      host: 'ftp.server.com', // ganti dengan server tujuan kita
      port: 21,
      authKey: 'key1' // key1 yang ada di file .ftppass, sesuaikan jika nama
                      // key berbeda
    },
    src: 'dist',
    dest: '/',
    simple: true,
    exclusions: ['**.DS_Store']
  }
}
```

Kemudian tambahkan _task_ deploy dibawah ini pada Gruntfile

``` javascript
grunt.registerTask('deploy', [
  'build',
  'ftpush'
]);
```

Setelah langkah - langkah diatas setiap kali kita ingin mengunggah file yang ada (setelah kita membuat artikel blog baru misalnya) kita hanya perlu menjalankan perintah berikut

``` bash
cd blog && grunt deploy
```

Untuk keterangan lebih lengkap tentang grunt-ftpush bisa lihat di [repositorinya](https://github.com/inossidabile/grunt-ftpush) di [Github](https://github.com), dan untuk metode lain untuk mengunggah blog yang dibangun dengan cabinjs kita bisa lihat di [wikinya](https://github.com/CabinJS/Cabin/wiki/Deployment-Tools), mudah bukan? :)

Selamat ngeblog!
