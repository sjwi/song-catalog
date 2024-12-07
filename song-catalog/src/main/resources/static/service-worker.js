var CACHE_NAME = 'song-catalog-cache-v1';

self.addEventListener('install', function(event) {
  var urlsToCache = [
    'js/lib/bootstrap.bundle.js',
    'js/lib/buttons.html5.min.js',
    'js/lib/datatable.js',
    'js/lib/dataTables.buttons.min.js',
    'js/lib/jquery-3.3.1.min.js',
    'js/lib/jquery-ui.min.js',
    'js/lib/jquery.dataTables.js',
    'js/lib/jquery.ui.touch-punch.min.js',
    'js/lib/jspdf.debug.js',
    'js/lib/jszip.min.js',
    'js/lib/moment-with-locales.min.js',
    'js/lib/pdfmake.min.js',
    'js/lib/popper.min.js',
    'js/lib/qrcode.min.js',
    'js/lib/select2.full.min.js',
    'css/bootstrap.css',
    'css/bootstrap-dark.css',
    'css/datatable.min.css',
    'css/font-awesome.all.min.css',
    'css/jquery.datatable.min.css',
    'css/select2.min.css',
    'images/add-song-to-set-inverse.png',
    'images/add-song-to-set.png',
    'images/click-here.png',
    'images/close-inverse.png',
    'images/copy-inverse.png',
    'images/copy.png',
    'images/delete-transparent.png',
    'images/delete.png',
    'images/export-inverse.png',
    'images/dropdown.png',
    'images/edit-inverse.png',
    'images/ellipse-inverse.png',
    'images/ellipse.png',
    'images/email-inverse.png',
    'images/export-ios.png',
    'images/export.png',
    'images/favicon.ico_old',
    'images/favicon.ico_square',
    'images/favicon.png',
    'images/favicon_white.png',
    'images/grip-inverse-lg.png',
    'images/grip-inverse-xl.png',
    'images/grip-inverse.png',
    'images/grip-inverse1.png',
    'images/icon_dark.png',
    'images/icon_dark.png_old',
    'images/icon_dark_192.png',
    'images/icon_dark_512.png',
    'images/loading.gif',
    'images/logo.png',
    'images/logo_preview.png',
    'images/logo_transparent.png',
    'images/logo_transparent.png_old',
    'images/logo_transparent_dark.png',
    'images/logo_transparent_dark.png_old',
    'images/logo_transparent_white.png',
    'images/logo_v2.png',
    'images/mag-glass-inverse.png',
    'images/mag-glass.png',
    'images/pdf.png',
    'images/play-inverse.png',
    'images/plus-blue.png',
    'images/plus-green.png',
    'images/plus-inverse.png',
    'images/plus-sign-circle-inverse.png',
    'images/plus-sign-green-md.png',
    'images/plus-sign-inverse-md.png',
    'images/plus-sign-inverse.png',
    'images/plus-song.png',
    'images/plus.png',
    'images/ppt.png',
    'images/qrcode.png',
    'images/refresh.png',
    'images/sort.png',
    'images/sort_asc.png',
    'images/sort_both.png',
    'images/sort_desc.png',
    'images/splashscreen_1125x2436.png',
    'images/splashscreen_1170x2532.png',
    'images/splashscreen_1242x2148.png',
    'images/splashscreen_1284x2778.png',
    'images/splashscreen_1536x2048.png',
    'images/splashscreen_1668x2224.png',
    'images/splashscreen_2048x2732.png',
    'images/splashscreen_640x1136.png',
    'images/splashscreen_750x1294.png',
    'images/splashscreen_780x1688.png',
    'images/splashscreen_828x1792.png',
    'images/sun.png',
    'images/version-control-inverse.png'
  ];
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(function(cache) {
        console.log('Opened cache');
        cache.delete('css/site.css');
        cache.delete('js/site/site.js');
        return cache.addAll(urlsToCache);
      })
  );
});

self.addEventListener('fetch', function(event) {
  event.respondWith(
    caches.match(event.request)
      .then(function(response) {
        if (response) {
          return response;
        }
        return fetch(event.request);
      }
    )
  );
});