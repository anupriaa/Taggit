/**
 * Created by Anupriya on 5/1/2015.
 */
(function() {
    /*alert("HERE");
    var httpRequest;
    url="http://localhost:9000/EnterUrl";
    //document.getElementById("ajaxButton").onclick = function() { makeRequest('test.html'); };

    function makeRequest(url) {
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
    
    jsRoutes.controllers.Application.enterUrl(location.href).ajax({
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
