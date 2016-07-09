(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$state', 'Team', 'TeamSearch'];

    function TeamController ($scope, $state, Team, TeamSearch) {
        var vm = this;
        
        vm.teams = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Team.query(function(result) {
                vm.teams = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TeamSearch.query({query: vm.searchQuery}, function(result) {
                vm.teams = result;
            });
        }    }
})();
