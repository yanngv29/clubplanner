(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .factory('UserExtraInfoSearch', UserExtraInfoSearch);

    UserExtraInfoSearch.$inject = ['$resource'];

    function UserExtraInfoSearch($resource) {
        var resourceUrl =  'api/_search/user-extra-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
