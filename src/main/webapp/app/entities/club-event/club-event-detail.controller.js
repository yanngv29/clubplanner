(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('ClubEventDetailController', ClubEventDetailController);

    ClubEventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ClubEvent', 'Site', 'UserExtraInfo', 'Club'];

    function ClubEventDetailController($scope, $rootScope, $stateParams, entity, ClubEvent, Site, UserExtraInfo, Club) {
        var vm = this;

        vm.clubEvent = entity;

        var unsubscribe = $rootScope.$on('clubplannerApp:clubEventUpdate', function(event, result) {
            vm.clubEvent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
