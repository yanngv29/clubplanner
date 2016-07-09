(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('ClubDeleteController',ClubDeleteController);

    ClubDeleteController.$inject = ['$uibModalInstance', 'entity', 'Club'];

    function ClubDeleteController($uibModalInstance, entity, Club) {
        var vm = this;

        vm.club = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Club.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
