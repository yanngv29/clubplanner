(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Team', 'UserExtraInfo', 'Club'];

    function TeamDetailController($scope, $rootScope, $stateParams, entity, Team, UserExtraInfo, Club) {
        var vm = this;

        vm.team = entity;

        var unsubscribe = $rootScope.$on('clubplannerApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
