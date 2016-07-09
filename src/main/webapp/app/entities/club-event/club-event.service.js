(function() {
    'use strict';
    angular
        .module('clubplannerApp')
        .factory('ClubEvent', ClubEvent);

    ClubEvent.$inject = ['$resource'];

    function ClubEvent ($resource) {
        var resourceUrl =  'api/club-events/:id';

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
