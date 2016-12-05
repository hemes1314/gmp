var require = {
    urlArgs: "bust=" + (new Date()).getTime(),
    paths: {
        jquery: 'jquery',
        mock: 'mock-min',
        validator: 'en',
        selectlist: 'jquery.selectlist',
        autocomplete: 'jquery.autocomplete',
        laydate: 'laydate',
        laypage: 'laypage',
        common: 'common',
        browser: 'jquery.browser',
        bgiframe: 'jquery.bgiframe.min',
        valid: 'easy_validator.pack'
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
