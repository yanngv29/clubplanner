(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .factory('ClubEventSearch', ClubEventSearch);

    ClubEventSearch.$inject = ['$resource'];

    function ClubEventSearch($resource) {
        var resourceUrl =  'api/_search/club-events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
