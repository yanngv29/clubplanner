(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('UserExtraInfoDetailController', UserExtraInfoDetailController);

    UserExtraInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'UserExtraInfo', 'User', 'ClubEvent', 'Team'];

    function UserExtraInfoDetailController($scope, $rootScope, $stateParams, entity, UserExtraInfo, User, ClubEvent, Team) {
        var vm = this;

        vm.userExtraInfo = entity;

        var unsubscribe = $rootScope.$on('clubplannerApp:userExtraInfoUpdate', function(event, result) {
            vm.userExtraInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
