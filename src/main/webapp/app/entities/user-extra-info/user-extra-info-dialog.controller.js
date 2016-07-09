(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('UserExtraInfoDialogController', UserExtraInfoDialogController);

    UserExtraInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UserExtraInfo', 'User', 'ClubEvent', 'Team'];

    function UserExtraInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, UserExtraInfo, User, ClubEvent, Team) {
        var vm = this;

        vm.userExtraInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.clubevents = ClubEvent.query();
        vm.teams = Team.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userExtraInfo.id !== null) {
                UserExtraInfo.update(vm.userExtraInfo, onSaveSuccess, onSaveError);
            } else {
                UserExtraInfo.save(vm.userExtraInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clubplannerApp:userExtraInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
