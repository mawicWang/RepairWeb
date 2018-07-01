$.fn.dataTable.defaults.oLanguage = {
    "sProcessing": "处理中...",
    "sLengthMenu": "显示 _MENU_ 项结果",
    "sZeroRecords": "没有匹配结果",
    "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
    "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
    "sInfoPostFix": "",
    "sSearch": "搜索:",
    "sUrl": "",
    "sEmptyTable": "表中数据为空",
    "sLoadingRecords": "载入中...",
    "sInfoThousands": ",",
    "oPaginate": {
        "sFirst": "首页",
        "sPrevious": "上页",
        "sNext": "下页",
        "sLast": "末页"
    },
    "oAria": {
        "sSortAscending": ": 以升序排列此列",
        "sSortDescending": ": 以降序排列此列"
    }
};


$(function () {

    // setup csrf for ajax json
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });


})

var formDialog = function (options) {
    var default_options = {
        title: "",
        saveAction: "",
        method: "post",
        saveBtnLabel: "<i class='ace-icon fa fa-check'></i> 保存",
        cancelBtnLabel: "取消",

        saveBtnCallback: function () {
            startLoad();
            $.ajax({
                type: options.method,
                url: options.saveAction,
                contentType: "application/json",
                data: JSON.stringify(dialog.find('.bootbox-body').find("form").serializeJSON())
            })
                .always(options.saveAlways)
                .done(options.saveDone)
                .fail(options.saveFail);

            return options.closeDialogInTheEnd;
        },
        cancelBtnCallback: function () {
            //clicked, do something
        },

        saveAlways: stopLoad,
        saveDone: refreshHome,
        saveFail: alertError,

        detailAjaxOptions: {
            url: "",
            data: {}
        },

        closeDialogInTheEnd: true
    };

    options = $.extend(default_options, options)

    var dialog = bootbox.dialog({
        title: options.title,
        message: '<div class="text-center"><i class="fa fa-spin fa-spinner"></i> Loading...</div>',
        buttons:
            {
                "save":
                    {
                        "label": options.saveBtnLabel,
                        "className": "btn-sm btn-success",
                        "callback": options.saveBtnCallback
                    },
                "cancel":
                    {
                        "label": options.cancelBtnLabel,
                        "className": "btn-sm",
                        "callback": options.cancelBtnCallback
                    }
            }
    });

    dialog.init(function () {
        $.ajax(options.detailAjaxOptions)
            .done(function (res) {
                dialog.find('.bootbox-body').html(res);
            })
            .fail(function () {
                alertError();
                dialog.hide();
            });
    });

    return dialog;
};

var simpleConfirm = function (options) {
    var default_options = {
        title: "确认？",
        message: "确认吗？",
        confirmLabel: "<i class=\"fa fa-check\"></i> 确认",
        confirmLabelClass: "btn-danger btn-sm",
        cancelLabel: "<i class=\"fa fa-times\"></i> 取消",
        confirmAjaxOptions: {
            url: "",
            data: {},
            type: "post"
        },
        saveAlways: stopLoad,
        saveDone: alertMsg,
        saveFail: alertError
    };
    options = $.extend(default_options, options)

    var confirm = bootbox.confirm({
        title: options.title,
        size: 'small',
        message: options.message,
        buttons: {
            confirm: {
                label: options.confirmLabel,
                className: options.confirmLabelClass
            },
            cancel: {
                label: options.cancelLabel,
                className: 'btn-sm'
            }
        },
        callback: function (result) {
            if (!result) return;
            startLoad();
            $.ajax(options.confirmAjaxOptions)
                .always(options.saveAlways)
                .done(options.saveDone)
                .fail(options.saveFail);
        }
    });

    return confirm;
}

var alertMsg = function (msg) {
    bootbox.alert({
        message: msg,
        size: 'small',
        backdrop: true
    });
};

var alertMsgWithCallback = function (msg, callback) {
    bootbox.alert({
        message: msg,
        size: 'small',
        backdrop: true,
        callback: callback
    });
};

var alertError = function () {
    alertMsg("发生未知错误！");
};

var refreshHome = function () {
    // alert("refreshHome")
    $('.page-content-area').ace_ajax('reload');
};

var startLoad = function () {
    // alert("startLoad")
    $('.page-content-area').data('ace_ajax').startLoading();
};
var stopLoad = function () {
    // alert("try stopLoad")
    if ($('.page-content-area').data('ace_ajax').working()) {
        // alert("stopLoad")
        $('.page-content-area').data('ace_ajax').stopLoading(true);
    }
};

