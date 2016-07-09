(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('ClubEventDialogController', ClubEventDialogController);

    ClubEventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClubEvent', 'Site', 'UserExtraInfo', 'Club'];

    function ClubEventDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ClubEvent, Site, UserExtraInfo, Club) {
        var vm = this;

        vm.clubEvent = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sites = Site.query();
        vm.userextrainfos = UserExtraInfo.query();
        vm.clubs = Club.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.clubEvent.id !== null) {
                ClubEvent.update(vm.clubEvent, onSaveSuccess, onSaveError);
            } else {
                ClubEvent.save(vm.clubEvent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('clubplannerApp:clubEventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
