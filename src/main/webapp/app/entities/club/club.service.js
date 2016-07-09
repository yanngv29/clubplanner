(function() {
    'use strict';
    angular
        .module('clubplannerApp')
        .factory('Club', Club);

    Club.$inject = ['$resource'];

    function Club ($resource) {
        var resourceUrl =  'api/clubs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
