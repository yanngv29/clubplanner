(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .factory('ClubSearch', ClubSearch);

    ClubSearch.$inject = ['$resource'];

    function ClubSearch($resource) {
        var resourceUrl =  'api/_search/clubs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
