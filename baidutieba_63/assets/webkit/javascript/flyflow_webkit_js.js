window.flyflow_webkit_js = {

	onGoBack: function(){
	    window.console.log("FLYFLOW-JSI:onGoBack:null");
	},

	onGoForward: function(){
    	window.console.log("FLYFLOW-JSI:onGoForward:null");
    },

    onGo: function(step){
        window.console.log("FLYFLOW-JSI:onGo:" + step);
    },

    onClick: function(url){
        window.console.log("FLYFLOW-JSI:onClick:" + url);
    },

    onReload: function(){
        window.console.log("FLYFLOW-JSI:onReload:null");
    },

    onWebJsClientFinished: function(){
    },
};
