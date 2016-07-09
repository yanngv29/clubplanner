(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('UserExtraInfoDeleteController',UserExtraInfoDeleteController);

    UserExtraInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserExtraInfo'];

    function UserExtraInfoDeleteController($uibModalInstance, entity, UserExtraInfo) {
        var vm = this;

        vm.userExtraInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserExtraInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
