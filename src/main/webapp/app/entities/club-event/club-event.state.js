(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('club-event', {
            parent: 'entity',
            url: '/club-event',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clubplannerApp.clubEvent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/club-event/club-events.html',
                    controller: 'ClubEventController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clubEvent');
                    $translatePartialLoader.addPart('eventType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('club-event-detail', {
            parent: 'entity',
            url: '/club-event/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clubplannerApp.clubEvent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/club-event/club-event-detail.html',
                    controller: 'ClubEventDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clubEvent');
                    $translatePartialLoader.addPart('eventType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClubEvent', function($stateParams, ClubEvent) {
                    return ClubEvent.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('club-event.new', {
            parent: 'club-event',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club-event/club-event-dialog.html',
                    controller: 'ClubEventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                schedule: null,
                                eventType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('club-event', null, { reload: true });
                }, function() {
                    $state.go('club-event');
                });
            }]
        })
        .state('club-event.edit', {
            parent: 'club-event',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club-event/club-event-dialog.html',
                    controller: 'ClubEventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClubEvent', function(ClubEvent) {
                            return ClubEvent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('club-event', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('club-event.delete', {
            parent: 'club-event',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club-event/club-event-delete-dialog.html',
                    controller: 'ClubEventDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClubEvent', function(ClubEvent) {
                            return ClubEvent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('club-event', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
