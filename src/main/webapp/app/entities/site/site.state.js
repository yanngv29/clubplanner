(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('site', {
            parent: 'entity',
            url: '/site',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clubplannerApp.site.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/site/sites.html',
                    controller: 'SiteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('site');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('site-detail', {
            parent: 'entity',
            url: '/site/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clubplannerApp.site.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/site/site-detail.html',
                    controller: 'SiteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('site');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Site', function($stateParams, Site) {
                    return Site.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('site.new', {
            parent: 'site',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/site/site-dialog.html',
                    controller: 'SiteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                adress: null,
                                mapLink: null,
                                isGymnasium: null,
                                residentClubName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('site', null, { reload: true });
                }, function() {
                    $state.go('site');
                });
            }]
        })
        .state('site.edit', {
            parent: 'site',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/site/site-dialog.html',
                    controller: 'SiteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Site', function(Site) {
                            return Site.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('site', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('site.delete', {
            parent: 'site',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/site/site-delete-dialog.html',
                    controller: 'SiteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Site', function(Site) {
                            return Site.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('site', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
