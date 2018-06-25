$(function () {

    // setup csrf for ajax json
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });


})
var refreshHome = function () {
    $('.page-content-area').ace_ajax('reload');
};
