(function() {
    'use strict';

    angular
        .module('clubplannerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-extra-info', {
            parent: 'entity',
            url: '/user-extra-info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clubplannerApp.userExtraInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-extra-info/user-extra-infos.html',
                    controller: 'UserExtraInfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userExtraInfo');
                    $translatePartialLoader.addPart('userType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-extra-info-detail', {
            parent: 'entity',
            url: '/user-extra-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'clubplannerApp.userExtraInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-extra-info/user-extra-info-detail.html',
                    controller: 'UserExtraInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userExtraInfo');
                    $translatePartialLoader.addPart('userType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserExtraInfo', function($stateParams, UserExtraInfo) {
                    return UserExtraInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('user-extra-info.new', {
            parent: 'user-extra-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-extra-info/user-extra-info-dialog.html',
                    controller: 'UserExtraInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nickname: null,
                                userType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-extra-info', null, { reload: true });
                }, function() {
                    $state.go('user-extra-info');
                });
            }]
        })
        .state('user-extra-info.edit', {
            parent: 'user-extra-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-extra-info/user-extra-info-dialog.html',
                    controller: 'UserExtraInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserExtraInfo', function(UserExtraInfo) {
                            return UserExtraInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-extra-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-extra-info.delete', {
            parent: 'user-extra-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-extra-info/user-extra-info-delete-dialog.html',
                    controller: 'UserExtraInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserExtraInfo', function(UserExtraInfo) {
                            return UserExtraInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-extra-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
