(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('ClubEventDeleteController',ClubEventDeleteController);

    ClubEventDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClubEvent'];

    function ClubEventDeleteController($uibModalInstance, entity, ClubEvent) {
        var vm = this;

        vm.clubEvent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClubEvent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
