(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('club', {
            parent: 'entity',
            url: '/club',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clubplannerApp.club.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/club/clubs.html',
                    controller: 'ClubController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('club');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('club-detail', {
            parent: 'entity',
            url: '/club/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clubplannerApp.club.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/club/club-detail.html',
                    controller: 'ClubDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('club');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Club', function($stateParams, Club) {
                    return Club.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('club.new', {
            parent: 'club',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club/club-dialog.html',
                    controller: 'ClubDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('club', null, { reload: true });
                }, function() {
                    $state.go('club');
                });
            }]
        })
        .state('club.edit', {
            parent: 'club',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club/club-dialog.html',
                    controller: 'ClubDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Club', function(Club) {
                            return Club.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('club', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('club.delete', {
            parent: 'club',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club/club-delete-dialog.html',
                    controller: 'ClubDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Club', function(Club) {
                            return Club.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('club', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
