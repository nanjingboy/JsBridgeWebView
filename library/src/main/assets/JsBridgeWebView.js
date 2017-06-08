(function() {
  if (window.jsBridgeWebView) {
    return;
  }

  var responseCallbacks = {};
  window.jsBridgeWebView = {
     callHandler: function(name, arguments, callback) {
       var data = {
         name: name,
         arguments: arguments,
         callbackId: name + '_' + new Date().getTime()
       };
       if (typeof callback === 'function') {
         responseCallbacks[data.callbackId] = callback;
       }
       window.jsBridgeWebViewInterface.callNativeHandler(JSON.stringify(data));
     },

     callbackHandler: function(data, callbackId) {
       var callback = responseCallbacks[callbackId];
       if (typeof callback === 'function') {
         if (data.length > 0) {
           callback(JSON.parse(data));
         } else {
           callback(null);
         }
         delete(responseCallbacks[callbackId]);
       }
     }
  };

  var readyEvent = document.createEvent('Events');
  readyEvent.initEvent('jsBridgeWebViewReady');
  document.dispatchEvent(readyEvent);

  window.setupJsBridgeWebView = function(callback) {
    if (window.jsBridgeWebView) {
      callback(window.jsBridgeWebView);
    } else {
      document.addEventListener(
        'jsBridgeWebViewReady',
        function() {
          callback(window.jsBridgeWebView);
        },
        false
      );
    }
  };
})();