(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('SiteDetailController', SiteDetailController);

    SiteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Site'];

    function SiteDetailController($scope, $rootScope, $stateParams, entity, Site) {
        var vm = this;

        vm.site = entity;

        var unsubscribe = $rootScope.$on('clubplannerApp:siteUpdate', function(event, result) {
            vm.site = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
