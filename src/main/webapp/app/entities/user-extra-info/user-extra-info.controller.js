(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('UserExtraInfoController', UserExtraInfoController);

    UserExtraInfoController.$inject = ['$scope', '$state', 'UserExtraInfo', 'UserExtraInfoSearch'];

    function UserExtraInfoController ($scope, $state, UserExtraInfo, UserExtraInfoSearch) {
        var vm = this;
        
        vm.userExtraInfos = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            UserExtraInfo.query(function(result) {
                vm.userExtraInfos = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            UserExtraInfoSearch.query({query: vm.searchQuery}, function(result) {
                vm.userExtraInfos = result;
            });
        }    }
})();
