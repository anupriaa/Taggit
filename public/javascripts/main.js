
(function() {
    alert("HERE");
    var url = location.href;
    alert(url);
    var x = jsroutes.controllers.Application.enterUrlTest(url);
    x.ajax({
        crossDomain : true,
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
