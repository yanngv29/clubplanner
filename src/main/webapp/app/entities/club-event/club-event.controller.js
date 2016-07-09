(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('ClubEventController', ClubEventController);

    ClubEventController.$inject = ['$scope', '$state', 'ClubEvent', 'ClubEventSearch'];

    function ClubEventController ($scope, $state, ClubEvent, ClubEventSearch) {
        var vm = this;
        
        vm.clubEvents = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ClubEvent.query(function(result) {
                vm.clubEvents = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ClubEventSearch.query({query: vm.searchQuery}, function(result) {
                vm.clubEvents = result;
            });
        }    }
})();
