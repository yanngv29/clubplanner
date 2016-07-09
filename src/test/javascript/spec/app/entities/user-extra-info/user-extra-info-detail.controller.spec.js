'use strict';

describe('Controller Tests', function() {

    describe('UserExtraInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUserExtraInfo, MockUser, MockClubEvent, MockTeam;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUserExtraInfo = jasmine.createSpy('MockUserExtraInfo');
            MockUser = jasmine.createSpy('MockUser');
            MockClubEvent = jasmine.createSpy('MockClubEvent');
            MockTeam = jasmine.createSpy('MockTeam');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UserExtraInfo': MockUserExtraInfo,
                'User': MockUser,
                'ClubEvent': MockClubEvent,
                'Team': MockTeam
            };
            createController = function() {
                $injector.get('$controller')("UserExtraInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubplannerApp:userExtraInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
