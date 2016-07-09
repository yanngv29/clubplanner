(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .controller('SiteController', SiteController);

    SiteController.$inject = ['$scope', '$state', 'Site', 'SiteSearch'];

    function SiteController ($scope, $state, Site, SiteSearch) {
        var vm = this;
        
        vm.sites = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Site.query(function(result) {
                vm.sites = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SiteSearch.query({query: vm.searchQuery}, function(result) {
                vm.sites = result;
            });
        }    }
})();
