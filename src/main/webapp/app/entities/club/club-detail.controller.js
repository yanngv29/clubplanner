(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('ClubDetailController', ClubDetailController);

    ClubDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Club', 'Team', 'ClubEvent'];

    function ClubDetailController($scope, $rootScope, $stateParams, entity, Club, Team, ClubEvent) {
        var vm = this;

        vm.club = entity;

        var unsubscribe = $rootScope.$on('clubplannerApp:clubUpdate', function(event, result) {
            vm.club = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
