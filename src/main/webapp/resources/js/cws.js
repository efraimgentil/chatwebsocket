/**
 * 
 */
var CWS = function(options ) {
	options = options || {  };
	var cws = {
	    wsUri : options.wsUri,
		websocket : null,
		connect : function() {
			this.websocket = new WebSocket( this.wsUri );
			this.websocket.onopen = this.onOpen;
			this.websocket.onclose = this.onMessage;
			this.websocket.onmessage = this.onClose;
		},
		onOpen : function() {
			console.log("CONNECT");
		},
		onMessage : function() {
			console.log("MESSAGE ");
		},
		onClose : function() {
			console.log("CLOSE");
		}
	};
	return cws;
};