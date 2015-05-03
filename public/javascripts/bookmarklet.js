/**
 * Created by Anupriya on 5/1/2015.
 */

/*
var jsRemoteSidebar = function() {

    // This block, until the return statement, is private

    // Conventions: _ indicates an out of scope variable
    //              UPPER_CASE indicates a constant (or flag), also maybe out of scope.

    // "constants"
    var HTML_URL = "http://localhost:9000/Bookmarklet?url='+escape(document.location.href)";
    var CSS_URL = "https://raw.githubusercontent.com/anupriaa/Taggit/milestone-3-bookmarklet-2/public/stylesheets/main.css";
    var FRAME_HEIGHT = 100;   // in pixels
    var SLIDE_TIMING = 20;    // in miliseconds

    // scratch variables, do not modify or remove please.
    var _remoteDiv = document.getElementById("jsSidebar90210");

    if (document.getElementsByTagName('frameset').length) {
        // disallow use on pages constructed with framesets.
        // it's possible to insert a new frame and work inside that
        // but that will be version 2.0 of the script (if ever)
        alert('This service will not work on framed pages');
        return undefined;
    }

    if (window.noRemoteSidebars||document.getElementById('noRemoteSidebars')) {
        // Check to see if the remote site has disallowed remote sidebars
        // This is my convention, chances are this code will never be tripped.
        alert('This site has disallowed remote services.');
        return undefined;
    }

    */
/*if (/^https/i.test(document.location.href)) {
        // If you remove this block, your script WILL work on encrypted sites
        // but it really shouldn't.  Reading secured sites is also a legal liability.
        // if you take data from an encrypted page even with angelic intentions and
        // it gets out you could get in trouble, maybe a lot.  So leave this check in.
        // Besides IE8 doesn't allow cross domain ajax requests from https sites.
        //    (the only thing they seemed to have gotten right)

        alert('This service will not work on encrypted sites.');
        return undefined;
    }
*//*

    ajaxObject = function (url, callbackFunction) {
        // see http://www.hunlock.com/blogs/The_Ultimate_Ajax_Object
        // for documentation on this ajax object
        // Modified slightly to use IE8's xdomain scripting.  !@#$ IE
        var that=this;
        that.isIE8 = false;
        this.updating = false;
        this.abort = function() {
            if (that.updating) {
                that.updating=false;
                that.AJAX.abort();
                that.AJAX=null;
            }
        }
        this.update = function(passData,postMethod) {
            if (that.updating) { return false; }
            that.AJAX = null;
            if (window.XDomainRequest) {
                that.AJAX = new XDomainRequest();
                that.isIE8 = true;
            } else {
                if (window.XMLHttpRequest) {
                    that.AJAX=new XMLHttpRequest();
                } else {
                    // This probably won't work but maybe MS will hotfix
                    // their browser to actually work.
                    that.AJAX=new ActiveXObject("Microsoft.XMLHTTP");
                }
            }
            if (that.AJAX==null) {
                return false;
            } else {
                that.AJAX.onreadystatechange = function() {
                    if (that.AJAX.readyState==4) {
                        that.updating=false;
                        that.callback(that.AJAX.responseText,that.AJAX.status,that.AJAX.responseXML);
                        that.AJAX=null;
                    }
                }
                // For IE8
                if (that.isIE8) {
                    that.AJAX.onload = function() {
                        that.updating=false;
                        that.callback(that.AJAX.responseText);
                        that.AJAX=null;
                    }
                }
                that.updating = new Date();
                if (/post/i.test(postMethod)) {
                    var uri=urlCall+'?'+that.updating.getTime();
                    that.AJAX.open("POST", uri, true);
                    that.AJAX.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                    that.AJAX.setRequestHeader("Content-Length", passData.length);
                    that.AJAX.send(passData);
                } else {
                    var uri=urlCall+'?'+passData+'&timestamp='+(that.updating.getTime());
                    that.AJAX.open("GET", uri, true);
                    that.AJAX.send(null);
                }
                return true;
            }
        }
        var urlCall = url;
        this.callback = callbackFunction || function () { };
    }

    var init = function () {

        // The constructor method

        if (!_remoteDiv) {

            //Load the style sheet

            var cssNode = document.createElement('link');
            cssNode.type = 'text/css';
            cssNode.rel = 'stylesheet';
            cssNode.href = CSS_URL;
            cssNode.media = 'screen';
            document.body.appendChild(cssNode);

            // Make our division

            var divNode=document.createElement("div");
            divNode.id="jsSidebar90210";
            divNode.style.height="0px";
            document.body.insertBefore(divNode,document.body.firstChild);
            // why do another getElementById?
            _remoteDiv = divNode;

            // Get our HTML to fill out the division
            ajaxGetData = new ajaxObject(HTML_URL);
            ajaxGetData.callback = function(remoteData) {
                _remoteDiv.innerHTML = remoteData;
                jsRemoteSidebar.scroller();
            }
            ajaxGetData.update();
        }

    }

    init();

    // Return a public interface.

    return {
        toggle : function () {

            // Show and hide the remote div.

            if (parseInt(_remoteDiv.style.height)) {
                jsRemoteSidebar.scroller('up');
            } else {
                jsRemoteSidebar.scroller('down');
            }
        },
        scroller : function (dir) {

            // The code that actually scrolls the div up and down

            currHeight =  parseInt(_remoteDiv.style.height);
            if (dir=='up') {
                if (currHeight > 0) {
                    currHeight-=SLIDE_TIMING;
                    _remoteDiv.style.height=(currHeight<0) ? "0px" : currHeight+"px";
                    setTimeout("jsRemoteSidebar.scroller('up')", 5);
                } else {
                    _remoteDiv.style.display='none';
                }
            } else {
                _remoteDiv.style.display='block';
                if (currHeight < FRAME_HEIGHT) {
                    currHeight+=SLIDE_TIMING;
                    _remoteDiv.style.height=currHeight+"px";
                    setTimeout("jsRemoteSidebar.scroller()", 5);
                } else {
                    _remoteDiv.focus();  // ensure there are no text cursors in the background
                }
            }
        }
    }

}();
*/

// note the trailing (), it tells the function to run (but you knew that already!)




(function() {

alert("HERE");
   /* var httpRequest;
    url="http://localhost:9000/EnterUrl";
    //document.getElementById("ajaxButton").onclick = function() { makeRequest('test.html'); };

    function makeRequest(url) {
        alert("INSIDE MAKE REQUEST");
        if (window.XMLHttpRequest) { // Mozilla, Safari, ...
            httpRequest = new XMLHttpRequest();
        } else if (window.ActiveXObject) { // IE
            try {
                httpRequest = new ActiveXObject("Msxml2.XMLHTTP");
            }
            catch (e) {
                try {
                    httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
                }
                catch (e) {}
            }
        }

        if (!httpRequest) {
            alert('Giving up :( Cannot create an XMLHTTP instance');
            return false;
        }
        httpRequest.onreadystatechange = alertContents;
        httpRequest.open('GET', url);
        httpRequest.send("url='+encodeURIComponent(location.href)");
        alert("REQUEST SENT");
    }

    function alertContents() {
        if (httpRequest.readyState === 4) {
            if (httpRequest.status === 200) {
                alert(httpRequest.responseText);
            } else {
                alert('There was a problem with the request.');
            }
        }
    }*/


    jsRoutes.controllers.Application.enterUrlTest(location.href).ajax({
        type: "POST",
        success: function(data) {
            alert("Success");
            console.debug("Success of Ajax Call");
            console.debug(data);
        },
        error: function(err) {
            alert("An error occurred trying to find your location. Sorry :(");
            console.debug("Error of ajax Call");
            console.debug(err);
        }
    })
})();

