(function() {
    'use strict';
    angular
        .module('clubplannerApp')
        .factory('UserExtraInfo', UserExtraInfo);

    UserExtraInfo.$inject = ['$resource'];

    function UserExtraInfo ($resource) {
        var resourceUrl =  'api/user-extra-infos/:id';

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
