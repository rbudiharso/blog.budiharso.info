title: Rails, Paperclip, Wkhtmltopdf, & image tag
date: 2014-02-11
tags:
- Ruby on Rails
- Programming
---
<!-- more -->
Membuat file PDF dari html dengan wkhtmltopdf memang susah - susah gampang. Gampang karena dengan gem wicked\_pdf dan wkhtmltopdf kita tinggal membuat template html untuk dijadikan file PDF. Susah karena agak sedikit _tricky_ untuk memilih binary wkhtmltopdf yang sesuai.
<!-- more -->

## Persiapan aplikasi

Untuk aplikasi rails saya menggunakn rails 4.0.x, pertama kita tambahkan gem wicked\_pdf di `Gemfile`.

``` ruby
gem 'wicked_pdf', github: 'mileszs/wicked_pdf'
```

lalu buat file initializer di `config/initializers/wicked_pdf.rb` yang isinya:

``` ruby
wicked_config = {
  development: {
    exe_path: '/usr/local/bin/wkhtmltopdf',
    layout: 'pdf.pdf',
    page_size: 'Letter'
  },
  staging: {
    exe_path: '/home/app/invoice/shared/bin/wkhtmltopdf',
    layout: 'pdf.pdf',
    page_size: 'Letter'
  }
}
WickedPdf.config = wicked_config[Rails.env.to_sym]
```

## Persiapan file binari

Gem wicked\_pdf membutuhkan file binari wkhtmltopdf yang harus diinstall terlebih dahulu. Ada beberapa cara untuk menginstall wkhtmltopdf. Di Mac OS X cara paling gampang adalah dengan menginstall menggunakan _installer_ yang bisa di unduh [disini](https://code.google.com/p/wkhtmltopdf/downloads/detail?name=wkhtmltopdf.dmg&can=1&q=) atau dengan homebrew. Di Linux bisa menggunakan _package manager_ tiap distro.

Setelah wkhtmltopdf terinstall ternyata masalah belum selesai, karena biasanya jika menggunakan _installer_, binary wkhtmltopdf yang terinstall adalah versi yang relatif baru, tapi justru disini masalahnya, karena versi yang baru memiliki beberapa masalah yang akan saya jelaskan nanti. Sekarang lebih baik kita gunakan versi yang lama, saya menggunakan versi 0.9.9 static binary.

Saya memilih binary yang statik karena berarti kita hanya perlu mengurus satu file binari itu saja, karena semua dependensinya sudah ikut dicompile menjadi satu.

Unduh file binari dari [google code](https://code.google.com/p/wkhtmltopdf/downloads/list?can=1), pastikan kita mengunduh binari yang statik, biasanya dinamanya ada kata - kata _static_ atau semacamnya, dan unduh yang versi 0.9.9. Lalu letakkan file yang diunduh tadi di direktori `/usr/local/bin` (pastikan path ini sesuai dengan path yang disebutkan di file initializer wicked\_pdf diatas). Sekarang seharusnya kita bisa menghasilkan file pdf dengan wicked\_pdf.

## Masalah wicked\_pdf dengan image tag

Berdasarkan pengalaman yang saya rasakan, walaupun wikhtmltopdf gampang untuk diinstall, ternyata tidak selalu bekerja sesuai yang saya inginkan. Salah satu masalah yang saya temui adalah jika kita menggunakan _helper_ dari wicked\_pdf untuk menghasilkan tag image dengan attribut _src_ yang menggunakan url dan bukan asset rails, maka gambar tidak muncul di file pdf yang dihasilkan.

Masalah ini muncul karena wicked\_pdf mengasumsikan file gambar yang ditabahkan ke file pdf adalah asset local yang bisa di akses dengan _file path_, sehingga di attribut _src_ dari tag image selalu di tambahkan `file:///` didepannya.

Hal ini jadi masalah terutama ketika kita menggunakan gambar yang di atur oleh gem paperclip, yang penyimpanan gambarnya bisa kita letakkan di server lain, dan kita akses menggunakan url dengan protokol `http://`. Hal inilah yang saya alami.

Setelah _googling_ tanpa hasil yang memuaskan, akhirnya saya pikir kalau gambar itu dirubah menjadi [data uri](https://developer.mozilla.org/en-US/docs/data_URIs) maka seharusnya tak ada masalah dengan perbedaan protokol lagi, karena dengan menggunakan data uri maka gambar itu di tempel langsung di template html. Untuk itu saya buat helper untuk paperclip seperti dibawah ini:

``` ruby
module Paperclip
  class Attachment
    def to_data_uri
      asset = url.start_with?('/') ? path : url
      base64 = Base64.encode64(open(asset).read)
      "data:#{content_type};base64,#{base64}"
    end
  end
end
```

dan kita gunakan helper tersebut seperti ini

```ruby
<%= image_tag model_dengan_attachment_paperclip.to_data_uri %>
```

## Masalah dengan page break

Masalah lain yang saya alami adalah penentuan page break secara manual. Seringnya kita ingin bisa mengatur page break dari file PDF yang dihasilkan secara manual. Page break bisa dibuat secara manual dengan css, dan seharusnya tak ada masalah jika kita menggunakan wkhtmltopdf versi 0.9.9 statik.

