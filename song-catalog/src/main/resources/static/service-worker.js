var CACHE_NAME = 'song-catalog-cache-v1';

self.addEventListener('install', function(event) {
  var urlsToCache = [
    contextpath + 'js/site/site.js',
    contextpath + 'js/lib/bootstrap.bundle.js',
    contextpath + 'js/lib/buttons.html5.min.js',
    contextpath + 'js/lib/datatable.js',
    contextpath + 'js/lib/dataTables.buttons.min.js',
    contextpath + 'js/lib/jquery-3.3.1.min.js',
    contextpath + 'js/lib/jquery-ui-min.js',
    contextpath + 'js/lib/jquery.dataTables.js',
    contextpath + 'js/lib/jquery.ui.touch-punch.min.js',
    contextpath + 'js/lib/jspdf.debug.js',
    contextpath + 'js/lib/jszip.min.js',
    contextpath + 'js/lib/moment-with-locales.min.js',
    contextpath + 'js/lib/pdfmake.min.js',
    contextpath + 'js/lib/popper.min.js',
    contextpath + 'js/lib/qrcode.min.js',
    contextpath + 'js/lib/select2.full.min.js',
    contextpath + 'js/lib/slick.min.js',
    contextpath + 'css/site.css',
    contextpath + 'css/bootstrap.css',
    contextpath + 'css/bootstrap-dark.css',
    contextpath + 'css/colors-dark.css',
    contextpath + 'css/colors.css',
    contextpath + 'css/datatable.min.css',
    contextpath + 'css/font-awesome.all.min.css',
    contextpath + 'css/jquery.datatable.min.css',
    contextpath + 'css/select2.min.css',
    contextpath + 'css/site.css',
    contextpath + 'css/slick.css'
  ];
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(function(cache) {
        console.log('Opened cache');
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