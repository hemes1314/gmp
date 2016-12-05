var require = {
    urlArgs: "bust=" + (new Date()).getTime(),
    paths: {
        jquery: 'jquery',
        mock: 'mock',
        validator: 'en',
        laydate: 'laydate',
        laypage: 'laypage',
        spinner: 'jquery.spinner',
        clockpicker: 'jquery-clockpicker.min',
        common: 'common',
    },
    shim: {
        'jquery.selectlist': {
            deps: ['jquery'],
            exports: 'jQuery.fn.selectlist'
        },
        'jquery.autocomplete': {
            deps: ['jquery'],
            exports: 'jQuery.fn.autocomplete'
        },
    }
};
