(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('ClubDialogController', ClubDialogController);

    ClubDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Club', 'Team', 'ClubEvent', 'UserExtraInfo'];

    function ClubDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Club, Team, ClubEvent, UserExtraInfo) {
        var vm = this;

        vm.club = entity;
        vm.clear = clear;
        vm.save = save;
        vm.teams = Team.query();
        vm.clubevents = ClubEvent.query();
        vm.userextrainfos = UserExtraInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.club.id !== null) {
                Club.update(vm.club, onSaveSuccess, onSaveError);
            } else {
                Club.save(vm.club, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clubplannerApp:clubUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
