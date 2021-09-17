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
    'js/lib/slick.min.js',
    'css/bootstrap.css',
    'css/bootstrap-dark.css',
    'css/colors-dark.css',
    'css/colors.css',
    'css/datatable.min.css',
    'css/font-awesome.all.min.css',
    'css/jquery.datatable.min.css',
    'css/select2.min.css',
    'css/slick.css'
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