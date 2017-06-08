(function() {
  var readyEvent,
    handlers,
    responseCallbacks;

  if (window.jsBridgeWebView) {
    return;
  }

  handlers = {};
  responseCallbacks = {};
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

     registerHandler: function(name, callback) {
        handlers[name] = callback
     },

     _nativeCallbackHandler: function(data, callbackId) {
       var callback = responseCallbacks[callbackId];
       if (typeof callback === 'function') {
         callback(JSON.parse(data));
         delete(responseCallbacks[callbackId]);
       }
     },

     _callJsHandler: function(name, data) {
       var handler = handlers[name];
       if (typeof handler === 'function') {
         handler(JSON.parse(data), function(response) {
           var options = {
              name: name,
              response: response
           };
           window.jsBridgeWebViewInterface.jsCallbackHandler(JSON.stringify(options));
         });
       }
     }
  };

  readyEvent = document.createEvent('Events');
  readyEvent.initEvent('jsBridgeWebViewReady');
  document.dispatchEvent(readyEvent);
})();